package github.mengzz.annotation.tool.config.ui;

import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.ui.IdeBorderFactory;
import github.mengzz.annotation.tool.config.AnnotationToolSetting;
import github.mengzz.annotation.tool.config.TableDecorator;
import github.mengzz.annotation.tool.model.AnnotationConfig;
import github.mengzz.annotation.tool.utils.ListUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * The type Config gui.
 *
 * @author mengzz
 */
public class ConfigGUI extends JPanel implements SearchableConfigurable {
    private static final String DISPLAY_NAME = "Annotation Tool";
    private final TableDecorator tableDecorator;
    private AnnotationToolSetting settings;
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel generalPanel;

    private JTextField defaultAttrValueText;

    public ConfigGUI() {
        super();
        settings = AnnotationToolSetting.getInstance();
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        setBorder();

        tableDecorator = new TableDecorator(settings.getAnnotationConfigs());
        tablePanel.add(tableDecorator.build(), BorderLayout.CENTER);
    }

    @Override
    public @Nullable JComponent createComponent() {
        return this;
    }

    @Override
    public boolean isModified() {
        return !tableDecorator.getAnnotationConfigs().equals(settings.getAnnotationConfigs())
                || !settings.getDefaultAttrValue().equals(defaultAttrValueText.getText());
    }

    @Override
    public void apply() {
        settings.setAnnotationConfigs(ListUtil.copy(tableDecorator.getAnnotationConfigs(), AnnotationConfig.class));
        settings.setDefaultAttrValue(defaultAttrValueText.getText());
    }

    @Override
    public void reset() {
        tableDecorator.reset(settings.getAnnotationConfigs());
        defaultAttrValueText.setText(settings.getDefaultAttrValue());
    }

    @Override
    public @NotNull String getId() {
        return DISPLAY_NAME;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return DISPLAY_NAME;
    }

    private void setBorder() {
        tablePanel.setBorder(IdeBorderFactory.createTitledBorder("Annotation Config", false));
        generalPanel.setBorder(IdeBorderFactory.createTitledBorder("General", false));
    }

}
