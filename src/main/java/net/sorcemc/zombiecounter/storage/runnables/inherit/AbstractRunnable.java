package net.sorcemc.zombiecounter.storage.runnables.inherit;

import net.sorcemc.zombiecounter.storage.handlers.inherit.AbstractHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractRunnable<R> extends AbstractHandler implements Runnable {

    private final @NotNull String syntax;
    private final @Nullable List<Object> columns;

    private final @Nullable Consumer<R> callback;

    protected AbstractRunnable(@NotNull AbstractHandler handler, @NotNull String syntax, boolean asynchronous) {
        this(handler, syntax, null, null, asynchronous);
    }

    protected AbstractRunnable(@NotNull AbstractHandler handler, @NotNull String syntax, @NotNull List<Object> columns, boolean asynchronous) {
        this(handler, syntax, columns, null, asynchronous);
    }

    protected AbstractRunnable(@NotNull AbstractHandler handler, @NotNull String syntax, @NotNull Consumer<R> callback, boolean asynchronous) {
        this(handler, syntax, null, callback, asynchronous);
    }

    protected AbstractRunnable(@NotNull AbstractHandler handler, @NotNull String syntax, @Nullable List<Object> columns, @Nullable Consumer<R> callback, boolean asynchronous) {
        super(handler);

        this.syntax = syntax;
        this.columns = columns;

        this.callback = callback;

        if (asynchronous) {
            super.getExecutor().execute(this);
        } else {
            this.run();
        }
    }

    protected @NotNull String getSyntax() {
        return this.syntax;
    }

    protected @Nullable List<Object> getColumns() {
        return this.columns;
    }

    protected @Nullable Consumer<R> getCallback() {
        return this.callback;
    }
}
