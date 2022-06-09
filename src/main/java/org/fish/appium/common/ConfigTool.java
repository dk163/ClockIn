package org.fish.appium.common;

import lombok.Cleanup;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigTool {
    public ConfigEntity loadConfig() {
        Constructor constructor = new Constructor(ConfigEntity.class);
        TypeDescription customTypeDescription = new TypeDescription(ConfigEntity.class);
        customTypeDescription.addPropertyParameters("account", AccountEntity.class);
        constructor.addTypeDescription(customTypeDescription);
        Yaml yaml = new Yaml(constructor);
        try {
            @Cleanup InputStream inputStream = this.getClass()
                    .getClassLoader()
                    .getResourceAsStream("config.yaml");
            return yaml.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(ConfigEntity config) {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource("config.yaml")).getPath();
        Yaml yaml = new Yaml();
        try {
            FileWriter writer = new FileWriter(path, false);
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.newLine();
            yaml.dump(config, buffer);
            buffer.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConfigTool configTool = new ConfigTool();
        ConfigEntity config = configTool.loadConfig();
        System.out.println(config);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setName("Fish");
        accountEntity.setUsername("15322929549");
        accountEntity.setPassword("dove@24680");
        config.setAccount(Stream.of(accountEntity).collect(Collectors.toList()));
        System.out.println(config);

        configTool.update(config);
    }
}
