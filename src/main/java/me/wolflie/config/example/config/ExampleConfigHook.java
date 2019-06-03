package me.wolflie.config.example.config;

import me.wolflie.config.annotation.ConfigKey;
import me.wolflie.config.annotation.ConfigSection;


public class ExampleConfigHook {

    @ConfigKey("one")
    public static String one = "one";

    @ConfigSection("two")
    public static class TwoSection {

        @ConfigKey("three")
        public static int three = 3;

    }
}
