package github.mengzz.annotation.tool.handler;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMember;
import github.mengzz.annotation.tool.config.AnnotationToolSetting;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;

/**
 * @author mengzz
 **/
public class LocationBuilder {
    private static AnnotationToolSetting settings = AnnotationToolSetting.getInstance();

    @Nullable
    public static String buildLocationString(PsiElement psiElement) {
        if (!(psiElement instanceof PsiMember)) {
            return null;
        }
        String location = null;
        PsiMember psiMember = (PsiMember) psiElement;
        PsiClass psiClass = psiMember.getContainingClass();
        if (psiClass != null) {
            location = MessageFormat.format("{0}.{1}", getName(psiClass), psiMember.getName());
        } else if (psiElement instanceof PsiClass) {
            location = getName((PsiClass) psiElement);
        }

        return location;
    }

    @Nullable
    private static String getName(PsiClass psiClass) {
        return settings.isEnableQualifiedName() ? psiClass.getQualifiedName() : psiClass.getName();
    }

}
