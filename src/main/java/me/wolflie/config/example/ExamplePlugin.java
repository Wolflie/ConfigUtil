package me.wolflie.config.example;

import me.wolflie.config.example.config.ExampleConfigHook;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info(ExampleConfigHook.ONE);
        getLogger().info(ExampleConfigHook.TwoSection.THREE);
    }
}
