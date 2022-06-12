package net.sorcemc.zombiecounter.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.sorcemc.zombiecounter.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

public class Storage {

    private final @NotNull HikariDataSource dataSource;

    public Storage(@NotNull Config conf) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + conf.getHost() + ":" + conf.getPort() + "/" + conf.getDatabase());
        config.setUsername(conf.getUsername());
        config.setPassword(conf.getPassword());
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("useLocalSessionState", true);
        config.addDataSourceProperty("rewriteBatchedStatements", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);
        config.addDataSourceProperty("cacheServerConfiguration", true);
        config.addDataSourceProperty("elideSetAutoCommits", true);
        config.addDataSourceProperty("maintainTimeStats", false);
        config.setMinimumIdle(0);
        config.setIdleTimeout(60000L);
        this.dataSource = new HikariDataSource(config);
    }

    public void disconnect() {
        this.dataSource.close();
    }

    public boolean isConnected() {
        return (this.dataSource.isRunning()) && !(this.dataSource.isClosed());
    }

    public @Nullable Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
