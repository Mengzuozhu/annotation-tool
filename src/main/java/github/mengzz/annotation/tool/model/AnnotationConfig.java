package github.mengzz.annotation.tool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * @author mengzz
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationConfig {
    private String annotation;
    private String attribute;
    private String classAnnotation;
    private String classAttribute;

    @Nullable
    public Object getValue(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return getAnnotation();
            case 1:
                return getAttribute();
            case 2:
                return getClassAnnotation();
            case 3:
                return getClassAttribute();
            default:
                break;
        }
        return null;
    }

    public void setValue(Object aValue, int columnIndex) {
        String value = String.valueOf(aValue);
        switch (columnIndex) {
            case 0:
                setAnnotation(value);
                break;
            case 1:
                setAttribute(value);
                break;
            case 2:
                setClassAnnotation(value);
                break;
            case 3:
                setClassAttribute(value);
                break;
            default:
                break;
        }
    }

    public static String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Annotation";
            case 1:
                return "Attribute";
            case 2:
                return "Class Annotation(Optional)";
            case 3:
                return "Class Attribute(Optional)";
            default:
                break;
        }
        return null;
    }

}
