// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0
// license that can be found in the LICENSE file.

package github.mengzz.annotation.tool.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import github.mengzz.annotation.tool.model.AnnotationConfig;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mengzz
 */
@State(name = "github.mengzz.annotation.tool.config.AnnotationToolSetting",
        storages = {@Storage("annotation-tool.xml")}
)
@Setter
public class AnnotationToolSetting implements PersistentStateComponent<AnnotationToolSetting> {
    private List<AnnotationConfig> annotationConfigs;
    @Getter
    private String defaultAttrValue = "value";

    public static AnnotationToolSetting getInstance() {
        return ServiceManager.getService(AnnotationToolSetting.class);
    }

    public Map<String, List<String>> getAnnotationAndAttributes() {
        return getAnnotationConfigs().stream()
                .collect(Collectors.groupingBy(AnnotationConfig::getAnnotation,
                        Collectors.mapping(AnnotationConfig::getAttribute, Collectors.toList())));
    }

    public List<AnnotationConfig> getAnnotationConfigs() {
        if (annotationConfigs == null) {
            annotationConfigs = new ArrayList<>();
        }
        return annotationConfigs;
    }

    @Nullable
    @Override
    public AnnotationToolSetting getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AnnotationToolSetting state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
