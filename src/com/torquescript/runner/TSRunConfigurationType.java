package com.torquescript.runner;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.torquescript.TSIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created on 3/13/18.
 */
public class TSRunConfigurationType implements ConfigurationType {
    @Override
    public String getDisplayName() {
        return "Torque App";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Torque App Run Configuration Type";
    }

    @Override
    public Icon getIcon() {
        return TSIcons.FILE;
    }

    @NotNull
    @Override
    public String getId() {
        return "TS_RUN_CONFIGURATION";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new TSRunConfigurationFactory(this)};
    }
}
