package net.sorcemc.zombiecounter.storage.runnables;

import net.sorcemc.zombiecounter.storage.handlers.inherit.AbstractHandler;
import net.sorcemc.zombiecounter.storage.runnables.inherit.AbstractRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateRunnable extends AbstractRunnable<Void> {

    public UpdateRunnable(@NotNull AbstractHandler handler, @NotNull String syntax, boolean asynchronous) {
        super(handler, syntax, asynchronous);
    }

    public UpdateRunnable(@NotNull AbstractHandler handler, @NotNull String syntax, @NotNull List<Object> columns, boolean asynchronous) {
        super(handler, syntax, columns, asynchronous);
    }

    @Override
    public void run() {
        if (super.getStorage().isConnected()) {
            try (Connection connection = super.getStorage().getConnection()) {
                if (connection != null) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(super.getSyntax())) {
                        if (super.getColumns() != null) {
                            for (int index = 0; index < super.getColumns().size(); index++) {
                                preparedStatement.setObject(index + 1, super.getColumns().get(index));
                            }
                        }

                        preparedStatement.executeUpdate();
                    }
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
}
