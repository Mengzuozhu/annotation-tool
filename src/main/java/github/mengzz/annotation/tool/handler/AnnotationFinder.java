package github.mengzz.annotation.tool.handler;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;
import github.mengzz.annotation.tool.config.AnnotationToolSetting;
import github.mengzz.annotation.tool.model.AnnotationInfo;
import github.mengzz.annotation.tool.model.AnnotationItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The type Annotation finder.
 */
public class AnnotationFinder {

    public static List<AnnotationItem> findItemsInProject(Project project) {
        if (project == null) {
            return new ArrayList<>();
        }

        GlobalSearchScope globalSearchScope = GlobalSearchScope.projectScope(project);
        List<AnnotationItem> itemList = new ArrayList<>();
        Map<String, Set<String>> annotationAndAttributes =
                AnnotationToolSetting.getInstance().getAnnotationAndAttributes();

        for (Map.Entry<String, Set<String>> entry : annotationAndAttributes.entrySet()) {
            String annotationName = entry.getKey();
            Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(annotationName, project,
                    globalSearchScope);
            AnnotationInfo annotationInfo = AnnotationInfo.instanceOf(annotationName);
            for (PsiAnnotation psiAnnotation : psiAnnotations) {
                PsiElement psiElement = psiAnnotation.getParent().getParent();
                for (String attribute : entry.getValue()) {
                    List<String> values = getAnnotationAttributeValues(psiAnnotation, attribute);
                    for (String value : values) {
                        AnnotationItem item = new AnnotationItem(psiElement, value, annotationInfo, attribute);
                        itemList.add(item);
                    }
                }
            }
        }

        return itemList;
    }

    @NotNull
    private static List<String> getAnnotationAttributeValues(PsiAnnotation annotation, String attr) {
        PsiAnnotationMemberValue value = annotation.findDeclaredAttributeValue(attr);

        List<String> values = new ArrayList<>();
        if (value instanceof PsiReferenceExpression) {
            PsiReferenceExpression expression = (PsiReferenceExpression) value;
            values.add(expression.getText());
        } else if (value instanceof PsiLiteralExpression) {
            values.add(String.valueOf(((PsiLiteralExpression) value).getValue()));
        } else if (value instanceof PsiArrayInitializerMemberValue) {
            PsiAnnotationMemberValue[] initializers = ((PsiArrayInitializerMemberValue) value).getInitializers();
            for (PsiAnnotationMemberValue initializer : initializers) {
                values.add(initializer.getText());
            }
        }

        return values;
    }

}
