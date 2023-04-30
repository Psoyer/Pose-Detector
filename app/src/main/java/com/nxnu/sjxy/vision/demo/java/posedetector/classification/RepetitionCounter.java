/*
 * @Author: yyj-wiki
 * @Creation Date:   2023-03-12
 */

package com.nxnu.sjxy.vision.demo.java.posedetector.classification;

/**
 * Counts reps for the give class.
 */
public class RepetitionCounter {
  // These thresholds can be tuned in conjunction with the Top K values in {@link PoseClassifier}.
  // The default Top K value is 10 so the range here is [0-10].
  private static final float DEFAULT_ENTER_THRESHOLD = 6f;
  private static final float DEFAULT_EXIT_THRESHOLD = 4f;

  private final String className;
  private final float enterThreshold;
  private final float exitThreshold;

  private int numRepeats;
  private boolean poseEntered;

  public RepetitionCounter(String className) {
    this(className, DEFAULT_ENTER_THRESHOLD, DEFAULT_EXIT_THRESHOLD);
  }

  public RepetitionCounter(String className, float enterThreshold, float exitThreshold) {
    this.className = className;
    this.enterThreshold = enterThreshold;
    this.exitThreshold = exitThreshold;
    numRepeats = 0;
    poseEntered = false;
  }

  /**
   * Adds a new Pose classification result and updates reps for given class.
   *
   * @param classificationResult {link ClassificationResult} of class to confidence values.
   * @return number of reps.
   */
  public int addClassificationResult(ClassificationResult classificationResult) {
    float poseConfidence = classificationResult.getClassConfidence(className);

    if (!poseEntered) {
      poseEntered = poseConfidence > enterThreshold;
      return numRepeats;
    }

    if (poseConfidence < exitThreshold) {
      numRepeats++;
      poseEntered = false;
    }

    return numRepeats;
  }

  public String getClassName() {
    return className;
  }

  public int getNumRepeats() {
    return numRepeats;
  }
}
