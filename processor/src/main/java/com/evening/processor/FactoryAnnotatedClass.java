package com.evening.processor;

import com.evening.annotation.Factory;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * Created by Nighter on 17/6/14.
 */

/**
 * 被@Factory注解的类
 */
public class FactoryAnnotatedClass {
    //被@Factory注解的类Element
    private TypeElement annotatedClassElement;
    //@Factory注解的成员type所指定的类名全称 superclass指的是接口
    private String qualifiedSuperClassName;
    //@Factory注解的成员type所指定的类名全称
    private String simpleTypeName;
    //@Factory的成员id
    private String id;

    public FactoryAnnotatedClass(TypeElement classElement) {
        this.annotatedClassElement = classElement;
        Factory annotation = classElement.getAnnotation(Factory.class);
        id = annotation.id();
        if (id.length() <= 0) {
            throw new IllegalArgumentException(String.format("id() in @%s for class %s is null or empty! that's not allowed",
                    Factory.class.getSimpleName(), classElement.getQualifiedName().toString()));
        }

        try {
            //由于@Factory的type一般是接口的class，因此这里的clazz一般是接口的class
            //例如 Meal.class
            //实际上从type这里获取class对象可能会抛出MirroredTypeException异常
            Class<?> clazz = annotation.type();
            //获取@Factory.type指定类的全称
            qualifiedSuperClassName = clazz.getCanonicalName();
            //获取@Factory.type指定类的名称
            simpleTypeName = clazz.getSimpleName();
        } catch (MirroredTypeException mte) {
            //DeclaredType是TypeMirror的子类, TypeMirror表示任何类型, 而DeclaredType只表示类或者接口
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            //TypeElement仅表示类或接口的元素
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            qualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
            simpleTypeName = classTypeElement.getSimpleName().toString();
        }

    }

    public TypeElement getAnnotatedClassElement() {
        return annotatedClassElement;
    }


    /**
     * 被注解的类所使用的Factory.type指定的类或接口的全称
     * @return
     */
    public String getQualifiedSuperClassName() {
        return qualifiedSuperClassName;
    }

    public String getSimpleTypeName() {
        return simpleTypeName;
    }

    public String getId() {
        return id;
    }
}
