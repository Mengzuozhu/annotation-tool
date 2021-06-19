package github.mengzz.annotation.tool.action;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import github.mengzz.annotation.tool.handler.AnnotationFinder;
import github.mengzz.annotation.tool.model.AnnotationItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class GotoAnnotationContributor implements ChooseByNameContributor {

    private List<AnnotationItem> annotationItems;

    @NotNull
    @Override
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        List<AnnotationItem> itemList = AnnotationFinder.findItemsInProject(project);
        annotationItems = itemList;
        return itemList.stream()
                .map(AnnotationItem::getName)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String name, String pattern, Project project,
                                           boolean includeNonProjectItems) {
        return annotationItems.stream()
                .filter(item -> Objects.equals(item.getName(), name))
                .toArray(NavigationItem[]::new);

    }
}
