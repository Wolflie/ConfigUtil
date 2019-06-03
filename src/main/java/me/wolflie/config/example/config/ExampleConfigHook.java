package me.wolflie.config.example.config;

import me.wolflie.config.annotation.ConfigKey;
import me.wolflie.config.annotation.ConfigSection;

public class ExampleConfigHook {

    @ConfigKey("one")
    public static final String ONE;

    static {
        ONE = "HI!";
    }

    @ConfigSection("two")
    public static class TwoSection {

        @ConfigKey("three")
        public static final String THREE;


        static {
            THREE = "OH, HI THERE!";
        }
    }

}
