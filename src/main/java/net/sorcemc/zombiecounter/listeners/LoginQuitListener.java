package net.sorcemc.zombiecounter.listeners;

import net.sorcemc.zombiecounter.ZombieCounter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginQuitListener implements Listener {

    public LoginQuitListener() {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        assert (ZombieCounter.getInstance() != null) && (ZombieCounter.getInstance().getUserHandler() != null);

        ZombieCounter.getInstance().getUserHandler().initUser(event.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        assert (ZombieCounter.getInstance() != null) && (ZombieCounter.getInstance().getUserHandler() != null);

        ZombieCounter.getInstance().getUserHandler().closeUser(event.getPlayer().getUniqueId());
    }
}
