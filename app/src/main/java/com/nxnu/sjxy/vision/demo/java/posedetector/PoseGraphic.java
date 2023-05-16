/*
 * @Author: yyj-wiki
 * @Creation Date:   2023-03-12
 */

package com.nxnu.sjxy.vision.demo.java.posedetector;

import static java.lang.Math.atan2;
import static java.lang.Math.max;
import static java.lang.Math.min;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import com.google.mlkit.vision.common.PointF3D;
import com.nxnu.sjxy.vision.demo.GraphicOverlay;
import com.nxnu.sjxy.vision.demo.GraphicOverlay.Graphic;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.nxnu.sjxy.vision.demo.java.LivePreviewActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Draw the detected pose in preview.
 */
public class PoseGraphic extends Graphic {

    private static final float DOT_RADIUS = 8.0f;
    private static final float IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f;
    private static final float STROKE_WIDTH = 10.0f;
    private static final float POSE_CLASSIFICATION_TEXT_SIZE = 60.0f;

    private final Pose pose;
    private final boolean showInFrameLikelihood;
    private final boolean visualizeZ;
    private final boolean rescaleZForVisualization;
    private float zMin = Float.MAX_VALUE;
    private float zMax = Float.MIN_VALUE;

    private final List<String> poseClassification;
    private final Paint classificationTextPaint;
    private final Paint leftPaint;
    private final Paint rightPaint;
    private final Paint whitePaint;
    private final Paint textPaint;

    private final TextToSpeech textToSpeech;

    private HashMap<String, Double> standardAngle;
    private String cueText = "";
    private double maxAngle = 0;

    PoseGraphic(
            TextToSpeech textToSpeech,
            GraphicOverlay overlay,
            Pose pose,
            boolean showInFrameLikelihood,
            boolean visualizeZ,
            boolean rescaleZForVisualization,
            List<String> poseClassification) {
        super(overlay);
        this.textToSpeech = textToSpeech;

        this.pose = pose;
        this.showInFrameLikelihood = showInFrameLikelihood;
        this.visualizeZ = visualizeZ;
        this.rescaleZForVisualization = rescaleZForVisualization;

        this.poseClassification = poseClassification;
        classificationTextPaint = new Paint();
        classificationTextPaint.setColor(Color.WHITE);
        classificationTextPaint.setTextSize(POSE_CLASSIFICATION_TEXT_SIZE);
        classificationTextPaint.setShadowLayer(5.0f, 0f, 0f, Color.BLACK);

        whitePaint = new Paint();
        whitePaint.setStrokeWidth(STROKE_WIDTH);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(IN_FRAME_LIKELIHOOD_TEXT_SIZE);
        textPaint = new Paint();
        textPaint.setStrokeWidth(STROKE_WIDTH);
        textPaint.setColor(Color.YELLOW);
        textPaint.setTextSize(80.0f);
        leftPaint = new Paint();
        leftPaint.setStrokeWidth(STROKE_WIDTH);
        leftPaint.setColor(Color.GREEN);
        rightPaint = new Paint();
        rightPaint.setStrokeWidth(STROKE_WIDTH);
        rightPaint.setColor(Color.YELLOW);

        // TTS提示词


        standardAngle = new HashMap<>();
        standardAngle.put("leftElbow", 90.0);
        standardAngle.put("rightElbow", 90.0);
        standardAngle.put("leftShoulder", 180.0);
        standardAngle.put("rightShoulder", 180.0);
        standardAngle.put("leftKnee", 20.0);
        standardAngle.put("rightKnee", 20.0);
    }


    @Override
    public void draw(Canvas canvas) {
        List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
        if (landmarks.isEmpty()) {
            return;
        }

        // 绘制姿势分类信息
//        float classificationX = POSE_CLASSIFICATION_TEXT_SIZE * 0.5f;
//        for (int i = 0; i < poseClassification.size(); i++) {
//            float classificationY =
//                    (canvas.getHeight()
//                            - POSE_CLASSIFICATION_TEXT_SIZE * 1.5f * (poseClassification.size() - i));
//            canvas.drawText(
//                    "poseClassification.get(i)", classificationX, classificationY, classificationTextPaint);
//        }

        // 画出所有关键点
//        for (PoseLandmark landmark : landmarks) {
//            drawPoint(canvas, landmark, whitePaint);
//            if (visualizeZ && rescaleZForVisualization) {
//                zMin = min(zMin, landmark.getPosition3D().getZ());
//                zMax = max(zMax, landmark.getPosition3D().getZ());
//            }
//        }

//        PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
//        PoseLandmark lefyEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER);
//        PoseLandmark lefyEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
//        PoseLandmark leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER);
//        PoseLandmark rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER);
//        PoseLandmark rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);
//        PoseLandmark rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER);
//        PoseLandmark leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR);
//        PoseLandmark rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR);
//        PoseLandmark leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH);
//        PoseLandmark rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH);

        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
        PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
        PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
        PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
        PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);

        PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
        PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
        PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
        PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
        PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
        PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
        PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
        PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
        PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
        PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);

        // Face
//        drawLine(canvas, nose, lefyEyeInner, whitePaint);
//        drawLine(canvas, lefyEyeInner, lefyEye, whitePaint);
//        drawLine(canvas, lefyEye, leftEyeOuter, whitePaint);
//        drawLine(canvas, leftEyeOuter, leftEar, whitePaint);
//        drawLine(canvas, nose, rightEyeInner, whitePaint);
//        drawLine(canvas, rightEyeInner, rightEye, whitePaint);
//        drawLine(canvas, rightEye, rightEyeOuter, whitePaint);
//        drawLine(canvas, rightEyeOuter, rightEar, whitePaint);
//        drawLine(canvas, leftMouth, rightMouth, whitePaint);
//
        drawLine(canvas, leftShoulder, rightShoulder, whitePaint);
        drawLine(canvas, leftHip, rightHip, whitePaint);

        // Left body
        drawLine(canvas, leftShoulder, leftElbow, leftPaint);
        drawLine(canvas, leftElbow, leftWrist, leftPaint);
        drawLine(canvas, leftShoulder, leftHip, leftPaint);
        drawLine(canvas, leftHip, leftKnee, leftPaint);
        drawLine(canvas, leftKnee, leftAnkle, leftPaint);
        drawLine(canvas, leftWrist, leftThumb, leftPaint);
        drawLine(canvas, leftWrist, leftPinky, leftPaint);
        drawLine(canvas, leftWrist, leftIndex, leftPaint);
        drawLine(canvas, leftIndex, leftPinky, leftPaint);
        drawLine(canvas, leftAnkle, leftHeel, leftPaint);
        drawLine(canvas, leftHeel, leftFootIndex, leftPaint);

        // Right body
        drawLine(canvas, rightShoulder, rightElbow, rightPaint);
        drawLine(canvas, rightElbow, rightWrist, rightPaint);
        drawLine(canvas, rightShoulder, rightHip, rightPaint);
        drawLine(canvas, rightHip, rightKnee, rightPaint);
        drawLine(canvas, rightKnee, rightAnkle, rightPaint);
        drawLine(canvas, rightWrist, rightThumb, rightPaint);
        drawLine(canvas, rightWrist, rightPinky, rightPaint);
        drawLine(canvas, rightWrist, rightIndex, rightPaint);
        drawLine(canvas, rightIndex, rightPinky, rightPaint);
        drawLine(canvas, rightAnkle, rightHeel, rightPaint);
        drawLine(canvas, rightHeel, rightFootIndex, rightPaint);

        //角度信息

        //手肘
        drawAngleLeftElbow(canvas, leftElbow, leftShoulder, leftWrist, textPaint);
        drawAngleRightElbow(canvas, rightElbow, rightShoulder, rightWrist, textPaint);
        //肩部
        drawAngleLeftShoulder(canvas, leftShoulder, rightShoulder, leftElbow, textPaint);
        drawAngleRightShoulder(canvas, rightShoulder, leftShoulder, rightElbow, textPaint);
        //膝盖
        drawAngleLeftKnee(canvas, leftKnee, leftHip, leftAnkle, textPaint);
        drawAngleRightKnee(canvas, rightKnee, rightHip, rightAnkle, textPaint);
        //腰部 (不常用)
//        drawAngle(canvas, leftHip, leftShoulder, leftKnee, textPaint);
//        drawAngle(canvas, rightHip, rightShoulder, rightKnee, textPaint);

        // Draw inFrameLikelihood for all points
//        if (showInFrameLikelihood) {
//            for (PoseLandmark landmark : landmarks) {
//                canvas.drawText(
//                        String.format(Locale.US, "%.2f", landmark.getInFrameLikelihood()),
//                        translateX(landmark.getPosition().x),
//                        translateY(landmark.getPosition().y),
//                        whitePaint);
//            }
//        }
//        System.out.println("子线程id：====" + android.os.Process.myTid());
        if (maxAngle > 10.0){
            LivePreviewActivity.cueText = cueText;
        } else {
            LivePreviewActivity.cueText = "perfect";
        }
    }


    void drawPoint(Canvas canvas, PoseLandmark landmark, Paint paint) {
        PointF3D point = landmark.getPosition3D();
        updatePaintColorByZValue(
                paint, canvas, visualizeZ, rescaleZForVisualization, point.getZ(), zMin, zMax);
        canvas.drawCircle(translateX(point.getX()), translateY(point.getY()), DOT_RADIUS, paint);
    }

    //    根据角度语音提示 代码群
    void drawAngleLeftElbow(Canvas canvas, PoseLandmark midPoint, PoseLandmark firstPoint, PoseLandmark lastPoint, Paint paint) {
//        PointF3D point = midPoint.getPosition3D();
        double angleDiffer = getAngle(firstPoint, midPoint, lastPoint) - standardAngle.get("leftElbow");
//        canvas.drawText(String.valueOf(angle), translateX(point.getX()), translateY(point.getY()), paint);
        if (angleDiffer > 0 && angleDiffer > maxAngle){
            cueText = "leftElbowLarge";
            maxAngle = angleDiffer;
        }
        if (angleDiffer < 0 && angleDiffer*-1 > maxAngle){
            System.out.println("小于"+angleDiffer);
            cueText = "leftElbowSmall";
            maxAngle = angleDiffer * -1.0;
        }
    }

    void drawAngleRightElbow(Canvas canvas, PoseLandmark midPoint, PoseLandmark firstPoint, PoseLandmark lastPoint, Paint paint) {
//        PointF3D point = midPoint.getPosition3D();
        double angleDiffer = getAngle(firstPoint, midPoint, lastPoint) - standardAngle.get("rightElbow");
//        canvas.drawText(String.valueOf(angle), translateX(point.getX()), translateY(point.getY()), paint);
        if (angleDiffer > 0 && angleDiffer > maxAngle){
            cueText = "rightElbowLarge";
            maxAngle = angleDiffer;
        }
        if (angleDiffer < 0 && angleDiffer*-1 > maxAngle){
            System.out.println("小于"+angleDiffer);
            cueText = "rightElbowSmall";
            maxAngle = angleDiffer * -1.0;
        }
    }

    void drawAngleLeftShoulder(Canvas canvas, PoseLandmark midPoint, PoseLandmark firstPoint, PoseLandmark lastPoint, Paint paint) {
//        PointF3D point = midPoint.getPosition3D();
//        double angleDiffer = getAngle(firstPoint, midPoint, lastPoint) - standardAngle.get("leftShoulder");
//        canvas.drawText(String.valueOf(angle), translateX(point.getX()), translateY(point.getY()), paint);
//        if (angleDiffer > 0 && angleDiffer > maxAngle){
//            cueText = "leftShoulderLarge";
//            maxAngle = angleDiffer;
//        }
//        if (angleDiffer < 0 && angleDiffer > maxAngle){
//            cueText = "leftShoulderSmall";
//            maxAngle = angleDiffer * -1.0;
//        }
    }

    void drawAngleRightShoulder(Canvas canvas, PoseLandmark midPoint, PoseLandmark firstPoint, PoseLandmark lastPoint, Paint paint) {
//        PointF3D point = midPoint.getPosition3D();
//        double angleDiffer = getAngle(firstPoint, midPoint, lastPoint) - standardAngle.get("rightShoulder");
//        canvas.drawText(String.valueOf(angle), translateX(point.getX()), translateY(point.getY()), paint);
//        if (angleDiffer > 0 && angleDiffer > maxAngle){
//            cueText = "RightShoulderLarge";
//            maxAngle = angleDiffer;
//        }
//        if (angleDiffer < 0 && angleDiffer > maxAngle){
//
//            cueText = "RightShoulderSmall";
//            maxAngle = angleDiffer * -1.0;
//        }
    }

    void drawAngleLeftKnee(Canvas canvas, PoseLandmark midPoint, PoseLandmark firstPoint, PoseLandmark lastPoint, Paint paint) {
        PointF3D point = midPoint.getPosition3D();
        double angle = getAngle(firstPoint, midPoint, lastPoint);
        canvas.drawText(String.valueOf(angle), translateX(point.getX()), translateY(point.getY()), paint);

    }

    void drawAngleRightKnee(Canvas canvas, PoseLandmark midPoint, PoseLandmark firstPoint, PoseLandmark lastPoint, Paint paint) {
        PointF3D point = midPoint.getPosition3D();
        double angle = getAngle(firstPoint, midPoint, lastPoint);
        canvas.drawText(String.valueOf(angle), translateX(point.getX()), translateY(point.getY()), paint);

    }
    // 代码群结束


    void drawLine(Canvas canvas, PoseLandmark startLandmark, PoseLandmark endLandmark, Paint paint) {
        PointF3D start = startLandmark.getPosition3D();
        PointF3D end = endLandmark.getPosition3D();

        // 获取当前的平均z
        float avgZInImagePixel = (start.getZ() + end.getZ()) / 2;
        updatePaintColorByZValue(
                paint, canvas, visualizeZ, rescaleZForVisualization, avgZInImagePixel, zMin, zMax);

        canvas.drawLine(
                translateX(start.getX()),
                translateY(start.getY()),
                translateX(end.getX()),
                translateY(end.getY()),
                paint);
    }

    static double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        double result =
                Math.toDegrees(
                        atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                lastPoint.getPosition().x - midPoint.getPosition().x)
                                - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                firstPoint.getPosition().x - midPoint.getPosition().x));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
    }
}
