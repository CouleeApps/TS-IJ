package com.torquescript.runner;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 3/13/18.
 */
public class TSRunConfigurationFactory extends ConfigurationFactory {
    private static final String FACTORY_NAME = "Torque App configuration factory";

    public TSRunConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new TSRunConfiguration(project, this, "Torque App");
    }

    @Override
    public String getName() {
        return FACTORY_NAME;
    }
}
