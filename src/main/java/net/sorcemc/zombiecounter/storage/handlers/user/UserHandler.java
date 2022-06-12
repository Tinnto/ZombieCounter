package net.sorcemc.zombiecounter.storage.handlers.user;

import com.google.common.collect.ImmutableList;
import net.sorcemc.zombiecounter.Config;
import net.sorcemc.zombiecounter.storage.handlers.inherit.AbstractHandler;
import net.sorcemc.zombiecounter.storage.runnables.QueryRunnable;
import net.sorcemc.zombiecounter.storage.runnables.UpdateRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class UserHandler extends AbstractHandler {

    private final @NotNull List<User> users;

    public UserHandler(@NotNull Config conf) {
        super(conf);

        this.users = new LinkedList<User>();

        new UpdateRunnable(this, "CREATE TABLE IF NOT EXISTS db_users (uuid VARCHAR(36) NOT NULL, zombieCount INT, PRIMARY KEY(uuid));", false);
    }

    public void initUser(@NotNull UUID uuid) {
        new QueryRunnable(this, "SELECT * FROM db_users WHERE uuid=?", ImmutableList.of(uuid.toString()), resultSet -> {
            User user = new User(uuid);

            try {
                if (resultSet != null) {
                    if (resultSet.next()) {
                        user = new User(uuid, resultSet.getInt("zombieCount"));
                    } else {
                        new UpdateRunnable(this, "INSERT INTO db_users (uuid, zombieCount) VALUES (?, ?);", ImmutableList.of(uuid.toString(), user.getZombieCount()), true);
                    }
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

            this.users.add(user);
        }, false);
    }

    public void closeUser(@NotNull UUID uuid) {
        User cachedUser = this.getCachedUser(uuid);
        if (cachedUser != null) {
            this.users.remove(cachedUser);
        }
    }

    public void saveUser(@NotNull User user) {
        new UpdateRunnable(this, "UPDATE db_users SET zombieCount=? WHERE uuid=?", ImmutableList.of(user.getZombieCount(), user.getUniqueId().toString()), true);
    }

    public @Nullable User getCachedUser(@NotNull UUID uuid) {
        return new LinkedList<User>(this.users).stream().filter(user -> user.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }
}
