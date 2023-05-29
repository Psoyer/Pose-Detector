package com.nxnu.sjxy.vision.demo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.mlkit.vision.demo.R;
import com.nxnu.sjxy.vision.demo.java.LivePreviewActivity;

public class PoseIntroduceActivity extends AppCompatActivity {
    private String PoseType;
    private String poseRecommendations;
    private final static String hookBall = "        在羽毛球运动中，勾球也称网前勾对角线，是将对方击到自己前场区域位置的球还击到与自己成对角线位置的对方网前区域内的击球技术。它可以从自己右前场区域位置将球勾到对方右前场区域位置，也可以从自己左前场区域位置将球勾到对方左前场区域位置。网前勾对角技术动作可以在球网以上位置完成，也可以在球网以下位置完成，整个技术要求动作细腻、准确，拍面及力量控制得当，是一项技巧性非常高的击球技术。网前勾对角线有正手勾对角线和反手勾对角线两种方法。";
    private final static String smash = "        杀球是把对方击来的球在尽量高的击球点上斜压下去。这种球力量大，弧线直，落地快，给对方的威胁很大。它是进攻的主要技术。杀球分为正手杀直线和对角线球。头顶杀直线和对角线球、正手腾空突击杀直线球和反手杀直线球。\n 有四个需要注意的地方：1、步伐到位，让击球点在脸前方\n2、杀球前身体弯曲，杀完球收腹\n3、杀球前轻松握拍,”像切菜的手一样握着“，杀球一刹那握紧\n4、向后引拍，大臂带动小臂，'鞭打'";
    private final static String dink = "        正手吊球是后场正手上手主要击球技术之一。击球前，身体先半侧对球网，右脚在后，左脚在前，两脚尖均踮起，身体重心自然落在右脚掌上。右手采用正手握拍法握拍，自然将球拍举到右肩侧上方，左手自然上举，眼睛注视来球。当球下落到接近击球点高度时，右腿开始蹲伸，并以髋关节带动身体由右向左转动，做左腿后撤，右腿前迈的两腿交叉动作\n        伴随下肢蹲转动作的同时，胸部舒张，两侧肩关节外展，左手自然上举，持拍臂的前臂向后移动，保持高肘后撤球拍。在腰腹协调用力的配合下，上臂带动前臂利用伸肘关节丶前臂旋内和屈腕的力量，向前下方轻击来球。";
    private final static String serve = "1、发球站位：单打发球在中线附近，站在离前发球线约1米左右。\n2、准备姿势：身体左肩侧对球网，左脚在前，右脚在后，重心在右脚上，右手持拍向右后侧举起，肘部放松微屈，左手拇指、食指和中指夹住球，举在胸腹间。发球时，身体重心由右脚移至左脚。 用正手发球，不论是发何种弧线的球，其发球前的姿势都应该一致，这样就会给对方的接发球造成判断上的困难。";

    private TextView introduceText;
    private VideoView videoView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pose_introduce);
        introduceText =  findViewById(R.id.introductionText);
        textView = findViewById(R.id.textView);

        videoView = findViewById(R.id.video);

        double[] angles = new double[0];
        Bundle bundle = this.getIntent().getExtras();
        PoseType = (String) bundle.get("PoseType");
        switch (PoseType){
            case "hookBall":
                Uri uriHookBall = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.gouqiu);
                angles = new double[]{180.0, 160.0, 60.0, 45.0, 165.0, 95.0};
                poseRecommendations = "正手勾球的动作要领是：用并步加蹬跨步上右网前，球拍随前臂往右前斜上举，在前臂前伸时稍有外旋，手腕微后伸，握拍手将拍柄稍向外捻动，使拇指贴在拍柄的宽面上，食指的第二指关节贴在拍柄背面的宽面上，拍柄不触掌心。球拍随着向右侧前挥动，拍面朝着右侧前方。";
                textView.setText("勾球");
                videoView.setVideoURI(uriHookBall);
                introduceText.setText(hookBall);
                System.out.println(uriHookBall);
                break;
            case "smash":
                Uri uriSmash = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.shaqiu);
                angles = new double[]{100.0, 80.0, 180.0, 180.0, 180.0, 180.0};
                poseRecommendations = "手腕立腕，保持球拍直立；架拍动作必须保持球拍立起，与小臂形成一定的夹角，大臂与身体夹角小于90度，拍面朝前或朝侧面，保证下一步引拍发力的流畅。 立腕动作是手臂手腕自然伸直，握拍形成角度。 立腕动作与引拍外旋动作配合，能提高引拍的速度和稳定。 腰腹充分打开放松，左腿虚点地，微内扣；右腿要 略弯曲 ，脚尖朝右侧略向后一点，重心在右脚脚掌上，转体动作充分做到，预防髋、膝关节处扭伤。 准备击球后退移动，调整重心至持拍脚（右脚），移动到位后，充分利用蹬转发力击球； 击球后身体重心向前回中，保持击球的连贯。 架拍击球前使拍面垂直于来球方向，根据球的轨迹和高度，架拍位置根据发力习惯做出微调。";
                textView.setText("杀球");
                videoView.setVideoURI(uriSmash);
                introduceText.setText(smash);
                break;
            case "dink":
                Uri uriDink = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.diaoqiu);
                angles = new double[]{180.0, 180.0, 180.0, 180.0, 150.0, 110.0};
                poseRecommendations = "手肘不需要非常靠近头部；手肘到位或叫固定或叫锁肘后，小臂上扬，但是握拍手要向外多偏转大概2，30度（正常菜刀式是90度），让出手腕转动空间，内旋时向内有更好的发力空间。";
                textView.setText("吊球");
                videoView.setVideoURI(uriDink);
                introduceText.setText(dink);
                break;
            case "serve":
                Uri uriServe = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.faqiu);
                angles = new double[]{120.0, 85.0, 110.0, 120.0, 180.0, 180.0};
                poseRecommendations = "羽毛球正手发球时，身体左肩膀面对球网，发正手球时要求站“丁字步”，左脚在前，右脚在后，重心在右脚上。12\n" +
                        "\n" +
                        "正手发高远球要领：\n" +
                        "\n" +
                        "正手发球时的手上动作是：持拍手采用正手握拍的方式，自然举拍，持球手一般采用球腰持球。\n" +
                        "脚下的站位有一定的要求，具体如下：左脚在前，脚尖垂直于球网，右脚在后，右脚尖近似垂直于边线。2";
                textView.setText("正手发球");
                videoView.setVideoURI(uriServe);
                introduceText.setText(serve);
                break;
        }
        videoView.requestFocus();
        videoView.start();

        Intent LivePreviewActivity = new Intent(PoseIntroduceActivity.this, LivePreviewActivity.class);
        double[] finalAngles = angles;
        findViewById(R.id.goActivity).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LivePreviewActivity.putExtra("poseRecommendations", poseRecommendations);
                LivePreviewActivity.putExtra("angles", finalAngles);
                startActivity(LivePreviewActivity);
            }
        });
    }
}