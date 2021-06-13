package github.mengzz.annotation.tool.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationInfo {
    private static Map<String, AnnotationInfo> records = new ConcurrentHashMap<>();
    private String uniqueName;

    private AnnotationInfo(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public static AnnotationInfo instanceOf(String uniqueKey) {
        if (uniqueKey == null) {
            return null;
        }
        AnnotationInfo annotationInfo = records.get(uniqueKey);
        if (annotationInfo == null) {
            annotationInfo = new AnnotationInfo(uniqueKey);
            records.put(uniqueKey, annotationInfo);
        }
        return annotationInfo;
    }

    public String getUniqueName() {
        return uniqueName;
    }
}

