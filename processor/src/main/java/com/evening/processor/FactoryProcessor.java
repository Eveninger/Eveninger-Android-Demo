package com.evening.processor;

import com.evening.annotation.Factory;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by Nighter on 17/6/14.
 */

@AutoService(Processor.class)
public class FactoryProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, FactoryGroupedClasses> factoryClasses = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        typeUtils = processingEnvironment.getTypeUtils();
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    /**
     * 必须指定，这个注解处理器是注册给哪个注解的。
     * 注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称。
     * 即在这里定义注解处理器注册到哪些注解上。
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Factory.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //1.遍历所有被注解了@Factory的元素
        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Factory.class)) {
            //2.检查被注解了@Factory的元素是否是一个类
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                //如果是非类元素，则抛出错误信息
                error(annotatedElement, "Only classes can be annotated with @%s", Factory.class.getSimpleName());
                //退出处理
                return true;
            }

            TypeElement typeElement = (TypeElement) annotatedElement;
            FactoryAnnotatedClass annotatedClass = new FactoryAnnotatedClass(typeElement);
            if (!isValidClass(annotatedClass)) {
                return true;
            }

            //类符合条件
            FactoryGroupedClasses factoryClass = factoryClasses.get(annotatedClass.getQualifiedSuperClassName());
            if (factoryClass == null) {
                String qualifiedGroupName = annotatedClass.getQualifiedSuperClassName();
                factoryClass = new FactoryGroupedClasses(qualifiedGroupName);
                factoryClasses.put(qualifiedGroupName, factoryClass);
            }

            //将符合条件的类放入聚集类中
            factoryClass.add(annotatedClass);
        }
        //为注解类生成工厂代码
        try {
            for (FactoryGroupedClasses fgc : factoryClasses.values()) {
                System.out.println(fgc.toString());
                fgc.generateCode(elementUtils, filer);
            }

            factoryClasses.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void error(Element element, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                element
        );
    }

    //判断一个类是否符合标准
    //1. 公开类
    //2. 非抽象类
    //3. 必须是@Factory.type指定类型的子类或者实现类
    //4. 必须有公开的默认构造函数
    private boolean isValidClass(FactoryAnnotatedClass item) {
        TypeElement classElement = item.getAnnotatedClassElement();
        //检查类是否是public
        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            error(classElement, "The class %s is not public.", classElement.getQualifiedName().toString());
            return false;
        }

        //检查类是否抽象
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            error(classElement, "The class %s is abstract. You can't annotate abstract classes with @%s",
                    classElement.getQualifiedName().toString(), Factory.class.getSimpleName());
            return false;
        }

        //检查继承关系：必须是@Factory.type()指定类型的子类或实现类

        //获取被注解的类的@Factory.type指定类的TypeElement
        TypeElement superClassElement = elementUtils.getTypeElement(item.getQualifiedSuperClassName());

        //如果Factory.type指定的是接口
        if (superClassElement.getKind() == ElementKind.INTERFACE) {
            //检查被@Factory注解的类是否实现了Factory.type指定的接口
            if (!classElement.getInterfaces().contains(superClassElement.asType())) {
                error(classElement, "The class %s annotated with @%s must implement the interface %s",
                        classElement.getQualifiedName().toString(), Factory.class.getSimpleName(),
                        item.getQualifiedSuperClassName());
                return false;
            }
        }
        //如果Factory.type指定的是类
        else {
            TypeElement currentClass = classElement;
            while (true) {
                TypeMirror superClassType = currentClass.getSuperclass();

                if (superClassType.getKind() == TypeKind.NONE) {
                    //已经向上到达顶部，即(java.lang.Object)
                    error(classElement, "The class %s annotated with @%s must inherit from %s",
                            classElement.getQualifiedName().toString(), Factory.class.getSimpleName(),
                            item.getQualifiedSuperClassName());
                    return false;
                }


                if (superClassType.toString().equals(item.getQualifiedSuperClassName())) {
                    //找到了符合要求的父类，即Factory.type指定的类
                    break;
                }

                //沿着继承树继续向上搜索
                currentClass = (TypeElement) typeUtils.asElement(superClassType);
            }
        }

        // 检查是否提供了默认公开构造函数
        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructorElement = (ExecutableElement) enclosed;
                if (constructorElement.getParameters().size() == 0 && constructorElement.getModifiers()
                        .contains(Modifier.PUBLIC)) {
                    // 找到了默认构造函数
                    return true;
                }
            }
        }

        // 没有找到默认构造函数
        error(classElement, "The class %s must provide an public empty default constructor",
                classElement.getQualifiedName().toString());
        return false;
    }
}
