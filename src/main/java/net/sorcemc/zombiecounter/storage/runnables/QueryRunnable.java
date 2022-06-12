package net.sorcemc.zombiecounter.storage.runnables;

import net.sorcemc.zombiecounter.storage.handlers.inherit.AbstractHandler;
import net.sorcemc.zombiecounter.storage.runnables.inherit.AbstractRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class QueryRunnable extends AbstractRunnable<ResultSet> {

    public QueryRunnable(@NotNull AbstractHandler handler, @NotNull String syntax, @NotNull Consumer<@Nullable ResultSet> callback, boolean asynchronous) {
        super(handler, syntax, callback, asynchronous);
    }

    public QueryRunnable(@NotNull AbstractHandler handler, @NotNull String syntax, @NotNull List<Object> columns, @NotNull Consumer<@Nullable ResultSet> callback, boolean asynchronous) {
        super(handler, syntax, columns, callback, asynchronous);
    }

    @Override
    public void run() {
        if (super.getCallback() != null) {
            if (super.getStorage().isConnected()) {
                try (Connection connection = super.getStorage().getConnection()) {
                    if (connection != null) {
                        try (PreparedStatement preparedStatement = connection.prepareStatement(super.getSyntax())) {
                            if (super.getColumns() != null) {
                                for (int index = 0; index < super.getColumns().size(); index++) {
                                    preparedStatement.setObject(index + 1, super.getColumns().get(index));
                                }
                            }

                            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                                super.getCallback().accept(resultSet);
                                return;
                            }
                        }
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }

            super.getCallback().accept(null);
        }
    }
}
