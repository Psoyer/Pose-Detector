/*
 * @Author: yyj-wiki
 * @Date:   2023-03-12
 * @Last Modified by: yyj-wiki
 * @Last Modified time: 2023-04-29
 */

package com.nxnu.sjxy.vision.demo.java;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import android.widget.CompoundButton;

import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.common.annotation.KeepName;
import com.nxnu.sjxy.vision.demo.CameraSource;
import com.nxnu.sjxy.vision.demo.CameraSourcePreview;
import com.nxnu.sjxy.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.demo.R;
import com.nxnu.sjxy.vision.demo.java.posedetector.PoseDetectorProcessor;
import com.nxnu.sjxy.vision.demo.preference.PreferenceUtils;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/** 主界面 */
@KeepName
public final class LivePreviewActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener, TextToSpeech.OnInitListener {
  private static final String POSE_DETECTION = "Pose Detection";

  private static final String TAG = "LivePreviewActivity";

  private CameraSource cameraSource = null;
  private CameraSourcePreview preview;
  private GraphicOverlay graphicOverlay;
  private String selectedModel = POSE_DETECTION;
  private TextToSpeech textToSpeech;
  public static String cueText;

  private HashMap<String, String> cueWord;

  private Thread ttsThread;
  private boolean ttsThreadFlag = true;
  private List<String> analyzeData;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    setContentView(R.layout.activity_vision_live_preview);

    preview = findViewById(R.id.preview_view);
    if (preview == null) {
      Log.d(TAG, "Preview is null");
    }
    graphicOverlay = findViewById(R.id.graphic_overlay);
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null");
    }

    // 打印当前主线程id
    System.out.println("主线程id：===="+android.os.Process.myTid());
    ttsThread =  new Thread(new Runnable() {
      @Override
      public void run() {
        analyzeData= new ArrayList();
        while (ttsThreadFlag){
          try {
            Thread.sleep(5000);
            System.out.println("当前播报内容" + cueText);
            analyzeData.add(cueText);
            textToSpeech.speak(cueWord.get(cueText),
                    TextToSpeech.QUEUE_ADD, null);
          } catch (Exception e){
            Log.d(TAG,e.toString());
          }
        }

      }
    });//启动线程
    ttsThread.start();

    // 初始化Android TTS
    initTTS();
    Spinner spinner = findViewById(R.id.spinner);
    List<String> options = new ArrayList<>();
    options.add(POSE_DETECTION);

    selectedModel = POSE_DETECTION;
    Log.d(TAG, "Selected model: " + selectedModel);
    preview.stop();
    createCameraSource(selectedModel);
    startCameraSource();

    // 前后摄像头选择器
    ToggleButton facingSwitch = findViewById(R.id.facing_switch);
    facingSwitch.setOnCheckedChangeListener(this);

    // 结束训练，跳转分析界面
    findViewById(R.id.end_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent afterTrainingActivity = new Intent(LivePreviewActivity.this,AfterTrainingActivity.class);
        afterTrainingActivity.putExtra("analyzeData", analyzeData.toArray(new String[analyzeData.size()]));
        startActivity(afterTrainingActivity);
        finish();
      }
    });


    createCameraSource(selectedModel);
  }

  public void initTTS(){
    textToSpeech = new TextToSpeech(this, this);
    cueWord = new HashMap<>();
    cueWord.put("leftElbowLarge", "左手肘角度偏大");
    cueWord.put("leftElbowSmall", "左手肘角度偏小");
    cueWord.put("rightElbowLarge", "右手肘角度偏大");
    cueWord.put("rightElbowSmall", "右手肘角度偏小");
    cueWord.put("leftShoulderLarge", "左肩膀角度偏大");
    cueWord.put("leftShoulderSmall", "左肩膀角度偏小");
    cueWord.put("rightShoulderLarge", "右肩膀角度偏大");
    cueWord.put("rightShoulderSmall", "右肩膀角度偏小");
    cueWord.put("leftKneeLarge", "左膝盖角度偏大");
    cueWord.put("leftKneeSmall", "左膝盖角度偏小");
    cueWord.put("rightKneeLarge", "右膝盖角度偏大");
    cueWord.put("rightKneeSmall", "右膝盖角度偏小");
    cueWord.put("perfect","您的姿势很完美");

  }



  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    Log.d(TAG, "Set facing");
    if (cameraSource != null) {
      if (isChecked) {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
      } else {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
      }
    }
    preview.stop();
    startCameraSource();
  }

  private void createCameraSource(String model) {
    // 如果没有现有的cameraSource，创建一个。
    if (cameraSource == null) {
      cameraSource = new CameraSource(this, graphicOverlay);
    }

    try {
      switch (model) {
        case POSE_DETECTION:
          PoseDetectorOptionsBase poseDetectorOptions =
                  PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
          Log.i(TAG, "Using Pose Detector with options " + poseDetectorOptions);
          boolean shouldShowInFrameLikelihood =
                  PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this);
          boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
          boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
          boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);
          cameraSource.setMachineLearningFrameProcessor(
                  new PoseDetectorProcessor(
                          this,
                          poseDetectorOptions,
                          shouldShowInFrameLikelihood,
                          visualizeZ,
                          rescaleZ,
                          runClassification,
                          textToSpeech,
                          /* isStreamMode = */ true));
          break;
        default:
          Log.e(TAG, "Unknown model: " + model);
      }
    } catch (RuntimeException e) {
      Log.e(TAG, "Can not create image processor: " + model, e);
      Toast.makeText(
              getApplicationContext(),
              "Can not create image processor: " + e.getMessage(),
              Toast.LENGTH_LONG)
          .show();
    }
  }

  /**
   *启动或重新启动相机源（如果存在）。如果相机来源还不存在
   *（例如，由于onResume是在创建相机源之前调用的），因此将再次调用
   */
  private void startCameraSource() {
    if (cameraSource != null) {
      try {
        if (preview == null) {
          Log.d(TAG, "resume: Preview is null");
        }
        if (graphicOverlay == null) {
          Log.d(TAG, "resume: graphOverlay is null");
        }
        preview.start(cameraSource, graphicOverlay);
      } catch (IOException e) {
        Log.e(TAG, "Unable to start camera source.", e);
        cameraSource.release();
        cameraSource = null;
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
    createCameraSource(selectedModel);
    startCameraSource();
  }

  /** Stops the camera. */
  @Override
  protected void onPause() {
    super.onPause();
    if (cameraSource != null) {
      cameraSource.release();
    }
    if(!textToSpeech.isSpeaking()) {
      textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
      textToSpeech.shutdown(); // 关闭，释放资源
    }
    ttsThreadFlag = false; //销毁tts线程
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (cameraSource != null) {
      cameraSource.release();
    }
    if(!textToSpeech.isSpeaking()) {
      textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
      textToSpeech.shutdown(); // 关闭，释放资源
    }
    ttsThreadFlag = false; //销毁tts线程
  }

  // Android TTS Init
  @Override
  public void onInit(int i) {
    if (i == TextToSpeech.SUCCESS) {
      Log.d(TAG, "init success");
      //设置语言
      int result = textToSpeech.setLanguage(Locale.CHINESE);
      if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
              && result != TextToSpeech.LANG_AVAILABLE) {
        Toast.makeText(this, "TTS暂时不支持这种语音的朗读！",
                Toast.LENGTH_SHORT).show();
      }
      //设置音调
      textToSpeech.setPitch(1.0f);
      //设置语速，1.0为正常语速
      textToSpeech.setSpeechRate(1.5f);
//      textToSpeech.speak("TTS完成初始化",
//              TextToSpeech.QUEUE_ADD, null);
    } else {
      Log.d(TAG, "init fail");
    }
  }
}
