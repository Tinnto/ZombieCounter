package net.sorcemc.zombiecounter;

import com.zaxxer.hikari.pool.HikariPool;
import net.sorcemc.zombiecounter.listeners.DeathListener;
import net.sorcemc.zombiecounter.listeners.LoginQuitListener;
import net.sorcemc.zombiecounter.storage.handlers.user.User;
import net.sorcemc.zombiecounter.storage.handlers.user.UserHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class ZombieCounter extends JavaPlugin {

    private static @Nullable ZombieCounter instance;

    static {
        instance = null;
    }

    private @Nullable Config conf;

    private @Nullable UserHandler userHandler;

    public ZombieCounter() {
        this.conf = null;

        this.userHandler = null;
    }

    public static @Nullable ZombieCounter getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        try {
            this.conf = new Config();

            this.userHandler = new UserHandler(this.conf);

            this.init();
        } catch (IOException | HikariPool.PoolInitializationException exception) {
            exception.printStackTrace();

            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        instance = null;

        this.conf = null;

        if (this.userHandler != null) {
            this.userHandler.close();
            this.userHandler = null;
        }
    }

    private void init() {
        assert this.userHandler != null;

        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new LoginQuitListener(), this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> Bukkit.getOnlinePlayers().forEach(player -> {
            User user = this.userHandler.getCachedUser(player.getUniqueId());
            if (user != null) {
                user.display(player);
            }
        }), 0L, 10L);
    }

    public @Nullable UserHandler getUserHandler() {
        return this.userHandler;
    }
}
