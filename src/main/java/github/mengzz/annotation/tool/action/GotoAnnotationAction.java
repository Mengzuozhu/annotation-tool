package github.mengzz.annotation.tool.action;

import com.intellij.featureStatistics.FeatureUsageTracker;
import com.intellij.ide.actions.GotoActionBase;
import com.intellij.ide.util.gotoByName.*;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import github.mengzz.annotation.tool.model.AnnotationInfo;
import github.mengzz.annotation.tool.model.AnnotationItem;
import github.mengzz.annotation.tool.model.GotoAnnotationModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mengzz
 */
public class GotoAnnotationAction extends GotoActionBase implements DumbAware {
    private static final String GO_TO_ANNOTATION = "Go to Annotation";

    @Override
    protected void gotoActionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        FeatureUsageTracker.getInstance().triggerFeatureUsed("navigation.popup.GotoAnnotationAction");

        final GotoAnnotationModel model = getGotoAnnotationModel(project);
        GotoActionCallback<AnnotationInfo> callback = getGotoActionCallback(project, model);

        ChooseByNameItemProvider provider = new DefaultChooseByNameItemProvider(getPsiContext(e));
        showNavigationPopup(e, model, callback, GO_TO_ANNOTATION, true, true, provider);
    }

    @Override
    protected <T> void showNavigationPopup(AnActionEvent e,
                                           ChooseByNameModel model,
                                           final GotoActionCallback<T> callback,
                                           @Nullable final String findUsagesTitle,
                                           boolean useSelectionFromEditor,
                                           final boolean allowMultipleSelection,
                                           final ChooseByNameItemProvider itemProvider) {
        final Project project = e.getData(CommonDataKeys.PROJECT);
        if (project == null) {
            return;
        }

        boolean mayRequestOpenInCurrentWindow =
                model.willOpenEditor() && FileEditorManagerEx.getInstanceEx(project).hasSplitOrUndockedWindows();
        Pair<String, Integer> start = getInitialText(useSelectionFromEditor, e);
        ChooseByNamePopup namePopup = ChooseByNamePopup.createPopup(project, model, itemProvider, start.first,
                mayRequestOpenInCurrentWindow, start.second);
        showNavigationPopup(callback, findUsagesTitle, namePopup, allowMultipleSelection);
    }

    @NotNull
    private GotoActionBase.GotoActionCallback<AnnotationInfo> getGotoActionCallback(Project project,
                                                                                    GotoAnnotationModel model) {
        return new GotoActionCallback<AnnotationInfo>() {
            @Override
            protected ChooseByNameFilter<AnnotationInfo> createFilter(@NotNull ChooseByNamePopup popup) {
                return new GotoAnnotationFilter(popup, model, project);
            }

            @Override
            public void elementChosen(ChooseByNamePopup chooseByNamePopup, Object element) {
                if (element instanceof AnnotationItem) {
                    AnnotationItem navigationItem = (AnnotationItem) element;
                    if (navigationItem.canNavigate()) {
                        navigationItem.navigate(true);
                    }
                }
            }
        };
    }

    @NotNull
    private GotoAnnotationModel getGotoAnnotationModel(Project project) {
        ChooseByNameContributor[] contributors = {new GotoAnnotationContributor()};
        return new GotoAnnotationModel(project, contributors);
    }

}
