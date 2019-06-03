package me.wolflie.config.util;

import com.google.common.collect.Sets;
import me.wolflie.config.Config;
import me.wolflie.config.annotation.ConfigKey;
import me.wolflie.config.annotation.ConfigSection;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigHookUtil {

    public static boolean isValidHook(Object hook) {

        if (isNestedHook(hook)) {
            return true;
        }
        return Arrays.stream(hook.getClass().getDeclaredFields()).anyMatch(field -> field.isAnnotationPresent(ConfigKey.class));
    }

    public static boolean isNestedHook(Object hook) {
        return hook.getClass().getDeclaredClasses().length > 0 && Arrays.stream(hook.getClass().getDeclaredClasses())
                .filter(clazz -> clazz.isAnnotationPresent(ConfigSection.class))
                .anyMatch(clazz ->
                        Arrays.stream(clazz.getDeclaredFields()).anyMatch(field -> field.isAnnotationPresent(ConfigKey.class))
                );
    }

    public static void initializeHook(Object hook, Config config) {
        if (!isValidHook(hook))
            throw new IllegalArgumentException("The object you've provided is not a configuration hook.");

        Set<Field> fields = Sets.newHashSet();
        fields.addAll(getHookFields(hook.getClass().getDeclaredFields()));
        if (isNestedHook(hook)) {
            for (Class<?> clazz : hook.getClass().getDeclaredClasses()) {
                fields.addAll(getHookFields(clazz.getDeclaredFields()));
            }
        }

        for (Field field : fields) {
            initializeHookField(hook, field, field.getAnnotation(ConfigKey.class), config);
        }
        config.save();
    }

    private static void initializeHookField(Object hook, Field field, ConfigKey key, Config config) {

        String path = key.value();
        Object parent = hook;

        if (field.getDeclaringClass().isAnnotationPresent(ConfigSection.class)) {
            path = field.getDeclaringClass().getAnnotation(ConfigSection.class).value() + "." + key.value();
            parent = Reflect.on(field.getDeclaringClass()).create().get();
        }

        if (config.get(path) == null) {
            config.set(path, Reflect.on(parent).get(field.getName()));
        } else {
            if (field.getType().isAssignableFrom(Collection.class))
                Reflect.on(parent).set(field.getName(), config.getList(path));
            else Reflect.on(parent).set(field.getName(), config.get(path));
        }
    }

    public static Set<Field> getHookFields(Field[] fields) {
        return Arrays.stream(fields).filter(field -> field.isAnnotationPresent(ConfigKey.class)).collect(Collectors.toSet());
    }

}
