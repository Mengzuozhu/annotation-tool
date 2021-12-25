package github.mengzz.annotation.tool.handler;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;
import github.mengzz.annotation.tool.config.AnnotationToolSetting;
import github.mengzz.annotation.tool.model.AnnotationConfig;
import github.mengzz.annotation.tool.model.AnnotationInfo;
import github.mengzz.annotation.tool.model.AnnotationItem;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The type Annotation finder.
 * @author Mengzz
 */
public class AnnotationFinder {

    public static List<AnnotationItem> findItemsInProject(Project project) {
        if (project == null) {
            return new ArrayList<>();
        }

        GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);
        List<AnnotationItem> itemList = new ArrayList<>();
        Map<String, AnnotationConfig> annotationAndAttributes =
                AnnotationToolSetting.getInstance().getAnnotationAndAttributes();

        for (Map.Entry<String, AnnotationConfig> entry : annotationAndAttributes.entrySet()) {
            AnnotationConfig configs = entry.getValue();
            String annotation = configs.getAnnotation();
            String attribute = configs.getAttribute();
            String classAnnotation = configs.getClassAnnotation();
            String classAttr = configs.getClassAttribute();

            Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(annotation, project,
                    globalSearchScope);
            AnnotationInfo annotationInfo = AnnotationInfo.instanceOf(entry.getKey());
            for (PsiAnnotation psiAnnotation : psiAnnotations) {
                PsiElement psiElement = Optional.ofNullable(psiAnnotation.getParent())
                        .map(PsiElement::getParent)
                        .orElse(null);
                if (psiElement == null) {
                    continue;
                }

                // 获取类的注解属性值
                List<String> clazzAttributeValues = getClazzAttributeValues(classAnnotation, classAttr, psiElement);
                List<String> values = getAnnotationAttributeValues(psiAnnotation, attribute);
                for (String value : values) {
                    if (clazzAttributeValues != null) {
                        for (String clazzValue : clazzAttributeValues) {
                            value = clazzValue + value;
                            AnnotationItem item = new AnnotationItem(psiElement, value, annotationInfo, attribute);
                            itemList.add(item);
                        }
                    } else {
                        AnnotationItem item = new AnnotationItem(psiElement, value, annotationInfo, attribute);
                        itemList.add(item);
                    }
                }
            }
        }

        return itemList;
    }

    /**
     * Gets clazz values.
     *
     * @param classAnnotation the class annotation
     * @param classAttr       the class attr
     * @param psiElement      the psi element
     * @return the clazz values
     */
    @Nullable
    private static List<String> getClazzAttributeValues(String classAnnotation, String classAttr,
                                                        PsiElement psiElement) {
        List<String> clazzValues = null;
        PsiElement parent = psiElement.getParent();
        if (parent instanceof PsiClass) {
            PsiClass clazz = (PsiClass) parent;
            if (StringUtils.isNotBlank(classAnnotation) && StringUtils.isNotBlank(classAttr)) {
                PsiAnnotation[] annotations = clazz.getAnnotations();
                for (PsiAnnotation annotation : annotations) {
                    boolean match = Optional.ofNullable(annotation)
                            .map(PsiAnnotation::getNameReferenceElement)
                            .map(PsiQualifiedReference::getReferenceName)
                            .map(classAnnotation::equals)
                            .orElse(false);
                    if (match) {
                        clazzValues = getAnnotationAttributeValues(annotation, classAttr);
                    }
                }
            }
        }
        return clazzValues;
    }

    @NotNull
    private static List<String> getAnnotationAttributeValues(PsiAnnotation annotation, String attr) {
        PsiAnnotationMemberValue value = annotation.findDeclaredAttributeValue(attr);

        return getValues(value);
    }

    @NotNull
    private static List<String> getValues(PsiAnnotationMemberValue value) {
        List<String> values = new ArrayList<>();
        if (value instanceof PsiReferenceExpression) {
            PsiReferenceExpression expression = (PsiReferenceExpression) value;
            values.add(expression.getText());
        } else if (value instanceof PsiLiteralExpression) {
            values.add(String.valueOf(((PsiLiteralExpression) value).getValue()));
        } else if (value instanceof PsiArrayInitializerMemberValue) {
            PsiAnnotationMemberValue[] initializers = ((PsiArrayInitializerMemberValue) value).getInitializers();
            for (PsiAnnotationMemberValue initializer : initializers) {
                values.addAll(getValues(initializer));
            }
        }
        return values;
    }

}
