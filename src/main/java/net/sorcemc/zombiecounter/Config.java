package net.sorcemc.zombiecounter;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class Config {

    private final @NotNull String host;
    private final @NotNull String database;
    private final @NotNull String username;
    private final @NotNull String password;
    private final int port;

    protected Config() throws IOException {
        assert ZombieCounter.getInstance() != null;

        File file = new File(ZombieCounter.getInstance().getDataFolder(), "config.yml");
        file.getParentFile().mkdir();
        if (!(file.exists())) {
            file.createNewFile();
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.options().copyDefaults(true);
        configuration.addDefault("data.mysql.host", "localhost");
        configuration.addDefault("data.mysql.database", "test");
        configuration.addDefault("data.mysql.username", "root");
        configuration.addDefault("data.mysql.password", "");
        configuration.addDefault("data.mysql.port", 3306);
        configuration.save(file);

        this.host = configuration.getString("data.mysql.host", "host");
        this.database = configuration.getString("data.mysql.database", "test");
        this.username = configuration.getString("data.mysql.username", "root");
        this.password = configuration.getString("data.mysql.password", "");
        this.port = configuration.getInt("data.mysql.port", 3306);
    }

    public @NotNull String getHost() {
        return this.host;
    }

    public @NotNull String getDatabase() {
        return this.database;
    }

    public @NotNull String getUsername() {
        return this.username;
    }

    public @NotNull String getPassword() {
        return this.password;
    }

    public int getPort() {
        return this.port;
    }
}
