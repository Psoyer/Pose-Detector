/*
 * @Author: yyj-wiki
 * @Creation Date:   2023-03-12
 */
package com.nxnu.sjxy.vision.demo.java.posedetector.classification;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.max;

/**
 * Represents Pose classification result as outputted by {@link PoseClassifier}. Can be manipulated.
 */
public class ClassificationResult {
  // For an entry in this map, the key is the class name, and the value is how many times this class
  // appears in the top K nearest neighbors. The value is in range [0, K] and could be a float after
  // EMA smoothing. We use this number to represent the confidence of a pose being in this class.
  private final Map<String, Float> classConfidences;

  public ClassificationResult() {
    classConfidences = new HashMap<>();
  }

  public Set<String> getAllClasses() {
    return classConfidences.keySet();
  }

  public float getClassConfidence(String className) {
    return classConfidences.containsKey(className) ? classConfidences.get(className) : 0;
  }

  public String getMaxConfidenceClass() {
    return max(
        classConfidences.entrySet(),
        (entry1, entry2) -> (int) (entry1.getValue() - entry2.getValue()))
        .getKey();
  }

  public void incrementClassConfidence(String className) {
    classConfidences.put(className,
        classConfidences.containsKey(className) ? classConfidences.get(className) + 1 : 1);
  }

  public void putClassConfidence(String className, float confidence) {
    classConfidences.put(className, confidence);
  }
}
