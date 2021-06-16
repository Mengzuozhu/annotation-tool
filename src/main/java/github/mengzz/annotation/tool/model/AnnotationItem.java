package github.mengzz.annotation.tool.model;

import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.ItemPresentationWithSeparator;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.util.Iconable;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.text.MessageFormat;

public class AnnotationItem implements NavigationItem {
    private PsiElement psiElement;
    private String attrValue;
    private String attributeName;
    private AnnotationInfo annotationInfo;
    private Navigatable navigationElement;

    public AnnotationItem(PsiElement psiElement, String attrValue, AnnotationInfo annotationInfo,
                          String attributeName) {
        this.psiElement = psiElement;
        this.annotationInfo = annotationInfo;
        this.attrValue = attrValue;
        this.attributeName = attributeName;
        if (psiElement instanceof Navigatable) {
            navigationElement = (Navigatable) psiElement;
        }
    }

    @Nullable
    @Override
    public String getName() {
        return this.attrValue;
    }

    @Nullable
    @Override
    public ItemPresentation getPresentation() {
        return new RestServiceItemPresentation();
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (navigationElement != null) {
            navigationElement.navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return navigationElement.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return navigationElement.canNavigateToSource();
    }

    public AnnotationInfo getAnnotationInfo() {
        return annotationInfo;
    }

    private class RestServiceItemPresentation implements ColoredItemPresentation, ItemPresentationWithSeparator {
        @Nullable
        @Override
        public String getPresentableText() {
            return attrValue;
        }

        @Nullable
        @Override
        public String getLocationString() {
            PsiClass psiClass;
            String refer = null;
            if (psiElement instanceof PsiMember) {
                PsiMember psiMember = (PsiMember) psiElement;
                psiClass = psiMember.getContainingClass();
                if (psiClass != null) {
                    refer = MessageFormat.format("{0}.{1}", psiClass.getName(),
                            psiMember.getName());
                }
            } else if (psiElement instanceof PsiClass) {
                psiClass = (PsiClass) psiElement;
                refer = psiClass.getName();
            }

            String location = null;
            if (refer != null) {
                location = MessageFormat.format("{0}::@{1}.{2}", refer, annotationInfo.getUniqueName(),
                        attributeName);
            }

            return location;
        }

        @Nullable
        @Override
        public Icon getIcon(boolean unused) {
            return psiElement.getIcon(Iconable.ICON_FLAG_VISIBILITY | Iconable.ICON_FLAG_READ_STATUS);
        }

        @Override
        public @Nullable TextAttributesKey getTextAttributesKey() {
            try {
                PsiDocCommentOwner commentOwner = psiElement instanceof PsiDocCommentOwner ?
                        ((PsiDocCommentOwner) psiElement) : null;
                if (commentOwner != null && commentOwner.isDeprecated()) {
                    return CodeInsightColors.DEPRECATED_ATTRIBUTES;
                }
            } catch (IndexNotReadyException ignore) {
            }
            return null;
        }
    }
}
