package net.sorcemc.zombiecounter.storage.handlers.user;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.sorcemc.zombiecounter.ZombieCounter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class User {

    private final @NotNull UUID uuid;

    private long lastKilled;
    private int killedBetween;

    private int zombieCount;

    protected User(@NotNull UUID uuid) {
        this.uuid = uuid;

        this.lastKilled = 0L;
        this.killedBetween = 0;

        this.zombieCount = 0;
    }

    protected User(@NotNull UUID uuid, int zombieCount) {
        this.uuid = uuid;

        this.lastKilled = 0L;
        this.killedBetween = 0;

        this.zombieCount = zombieCount;
    }

    public void incrementZombieCount() {
        this.zombieCount++;

        this.lastKilled = System.currentTimeMillis();
        this.killedBetween++;
    }

    public void display(@NotNull Player player) {
        assert ZombieCounter.getInstance() != null;

        if (this.lastKilled + 1000L > System.currentTimeMillis()) {
            this.sendActionbar(player, "§6§l+" + this.killedBetween + " §7" + ((this.killedBetween == 1) ? "Zombie" : "Zombies"));
            return;
        }
        this.killedBetween = 0;

        this.sendActionbar(player, "§7Getötete Zombies §8» §6" + this.zombieCount);
    }

    private void sendActionbar(@NotNull Player player, @NotNull String message) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2));
    }

    public @NotNull UUID getUniqueId() {
        return this.uuid;
    }

    public int getZombieCount() {
        return this.zombieCount;
    }
}
