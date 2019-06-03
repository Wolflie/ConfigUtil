package me.wolflie.config.yml;

import com.google.common.collect.Sets;
import me.wolflie.config.Config;
import me.wolflie.config.util.ConfigHookUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class YAMLConfig extends YamlConfiguration implements Config {

    private File file;
    private File directory;
    private Set<Object> hooks = Sets.newHashSet();

    public YAMLConfig(File directory, String name) {
        this.directory = directory;
        if (!directory.exists()) directory.mkdirs();

        file = new File(directory, name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        reloadConfig();
    }

    @Override
    public Set<Object> getHooks() {
        return hooks;
    }

    @Override
    public void addHook(Object object) {
        if (hooks.contains(object)) return;
        updateHook(object);
        hooks.add(object);
    }

    @Override
    public void updateHook(Object hook) {
        ConfigHookUtil.initializeHook(hook, this);
    }


    @Override
    public void reloadConfig() {
        try {
            load(file);
            hooks.forEach(this::updateHook);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
