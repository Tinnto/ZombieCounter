package net.sorcemc.zombiecounter.listeners;

import net.sorcemc.zombiecounter.ZombieCounter;
import net.sorcemc.zombiecounter.storage.handlers.user.User;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DeathListener implements Listener {

    public DeathListener() {
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        assert (ZombieCounter.getInstance() != null) && (ZombieCounter.getInstance().getUserHandler() != null);

        if (event.getEntity() instanceof Zombie) {
            Player player = event.getEntity().getKiller();
            if (player != null) {
                User user = ZombieCounter.getInstance().getUserHandler().getCachedUser(player.getUniqueId());
                if (user != null) {
                    user.incrementZombieCount();

                    ZombieCounter.getInstance().getUserHandler().saveUser(user);
                }
            }
        }
    }
}
