package com.nthzz.tatsuya.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author tatsuya
 */
public class ConfigUtils {
    private static Config config = ConfigFactory.parseFile(new File("conf.properties"));
    private static ConfigUtils ourInstance = new ConfigUtils();
    public static ConfigUtils getInstance() {
        return ourInstance;
    }

    private ConfigUtils() {
    }

    public String getPhoenixDatasource() throws Exception {
        return config.getString("phoenix.data.source.url");
    }
}
