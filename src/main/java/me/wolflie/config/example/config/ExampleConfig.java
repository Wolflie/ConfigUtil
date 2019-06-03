package me.wolflie.config.example.config;

import me.wolflie.config.Config;
import me.wolflie.config.example.ExamplePlugin;
import me.wolflie.config.yml.YAMLConfig;

public class ExampleConfig {

    private final Config config;

    public ExampleConfig(ExamplePlugin plugin) {
        this.config = new YAMLConfig(plugin.getDataFolder(), "example");
        config.addHook(new ExampleConfigHook());
    }

}
