package github.mengzz.annotation.tool.model;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.gotoByName.CustomMatcherModel;
import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.MinusculeMatcher;
import com.intellij.psi.codeStyle.NameUtil;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GotoAnnotationModel extends FilteringGotoByModel<AnnotationInfo> implements DumbAware, CustomMatcherModel {

    public GotoAnnotationModel(@NotNull Project project, @NotNull ChooseByNameContributor[] contributors) {
        super(project, contributors);
    }

    @Override
    public String getPromptText() {
        return "Find in Annotation";
    }

    @Override
    public String getNotInMessage() {
        return IdeBundle.message("label.no.matches.found.in.project", "");
    }

    @Override
    public String getNotFoundMessage() {
        return IdeBundle.message("label.no.matches.found");
    }

    @Nullable
    @Override
    public String getCheckBoxName() {
        return null;
    }

    /**
     * Only for compatibility problem
     *
     * @return the check box mnemonic
     */
    @Override
    public char getCheckBoxMnemonic() {
        return 0;
    }

    @Override
    public boolean loadInitialCheckBoxState() {
        return false;
    }

    @Override
    public void saveInitialCheckBoxState(boolean state) {
    }

    @NotNull
    @Override
    public String[] getSeparators() {
        return ArrayUtil.EMPTY_STRING_ARRAY;
    }

    @Nullable
    @Override
    public String getFullName(Object element) {
        return getElementName(element);
    }

    @Override
    public boolean willOpenEditor() {
        return true;
    }

    @Override
    public boolean matches(@NotNull String popupItem, @NotNull String userPattern) {
        MinusculeMatcher matcher = NameUtil.buildMatcher("*" + userPattern, NameUtil.MatchingCaseSensitivity.NONE);
        return matcher.matches(popupItem);
    }

    @Nullable
    @Override
    protected AnnotationInfo filterValueFor(NavigationItem item) {
        return item instanceof AnnotationItem ? ((AnnotationItem) item).getAnnotationInfo() : null;

    }


}
