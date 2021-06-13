package github.mengzz.annotation.tool.config;

import com.intellij.ide.util.gotoByName.ChooseByNameFilterConfiguration;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import github.mengzz.annotation.tool.model.AnnotationInfo;

@State(name = "AnnotationNameFilterConfiguration", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class AnnotationNameFilterConfiguration extends ChooseByNameFilterConfiguration<AnnotationInfo> {

    public static AnnotationNameFilterConfiguration getInstance(Project project) {
        return ServiceManager.getService(project, AnnotationNameFilterConfiguration.class);
    }

    @Override
    protected String nameForElement(AnnotationInfo type) {
        return type.getUniqueName();
    }
}
