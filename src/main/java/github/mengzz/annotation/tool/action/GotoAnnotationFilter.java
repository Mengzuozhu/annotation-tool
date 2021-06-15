package github.mengzz.annotation.tool.action;

import com.intellij.ide.util.gotoByName.ChooseByNameFilter;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.openapi.project.Project;
import github.mengzz.annotation.tool.config.AnnotationNameFilterConfiguration;
import github.mengzz.annotation.tool.config.AnnotationToolSetting;
import github.mengzz.annotation.tool.model.AnnotationInfo;
import github.mengzz.annotation.tool.model.GotoAnnotationModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mengzz
 **/
class GotoAnnotationFilter extends ChooseByNameFilter<AnnotationInfo> {
    GotoAnnotationFilter(final ChooseByNamePopup popup, GotoAnnotationModel model, final Project project) {
        super(popup, model, AnnotationNameFilterConfiguration.getInstance(project), project);
    }

    @Override
    protected String textForFilterValue(@NotNull AnnotationInfo value) {
        return value.getUniqueName();
    }

    @Override
    protected Icon iconForFilterValue(@NotNull AnnotationInfo value) {
        return null;
    }

    @Override
    @NotNull
    protected List<AnnotationInfo> getAllFilterValues() {
        Map<String, Set<String>> annotationAndProperties =
                AnnotationToolSetting.getInstance().getAnnotationAndAttributes();
        return annotationAndProperties.keySet()
                .stream()
                .map(AnnotationInfo::instanceOf)
                .collect(Collectors.toList());
    }
}
