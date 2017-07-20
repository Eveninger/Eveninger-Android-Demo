package com.evening.bezier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.evening.bezier.controlbezier.ControlCubicActivity;
import com.evening.bezier.controlbezier.ControlQuadActivity;
import com.evening.bezier.qqbubble.QQBubbleActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        BezierAdapter adapter = new BezierAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        List<String> data = Arrays.asList("控制二阶贝塞尔曲线", "控制三阶贝塞尔曲线", "QQ拖拽气泡");
        adapter.setData(data);
        adapter.setOnItemClickListener(new BezierAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        ControlQuadActivity.start(MainActivity.this);
                        break;
                    case 1:
                        ControlCubicActivity.start(MainActivity.this);
                        break;
                    case 2:
                        QQBubbleActivity.start(MainActivity.this);
                    default:
                        break;
                }
            }
        });
    }

}
