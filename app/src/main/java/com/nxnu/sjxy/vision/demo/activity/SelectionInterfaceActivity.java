package com.nxnu.sjxy.vision.demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.mlkit.vision.demo.R;
import com.nxnu.sjxy.vision.demo.java.AfterTrainingActivity;
import com.nxnu.sjxy.vision.demo.java.LivePreviewActivity;

public class SelectionInterfaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_interface);

        Intent PoseIntroduceActivity = new Intent(SelectionInterfaceActivity.this, PoseIntroduceActivity.class);
        findViewById(R.id.in1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PoseIntroduceActivity.putExtra("PoseType", "serve");
                startActivity(PoseIntroduceActivity);
            }
        });
        findViewById(R.id.in2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PoseIntroduceActivity.putExtra("PoseType", "dink");
                startActivity(PoseIntroduceActivity);
            }
        });
        findViewById(R.id.in3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PoseIntroduceActivity.putExtra("PoseType", "hookBall");
                startActivity(PoseIntroduceActivity);
            }
        });
        findViewById(R.id.in4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PoseIntroduceActivity.putExtra("PoseType", "smash");
                startActivity(PoseIntroduceActivity);
            }
        });
    }

}