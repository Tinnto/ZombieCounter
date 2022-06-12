package net.sorcemc.zombiecounter.storage.handlers.inherit;

import net.sorcemc.zombiecounter.Config;
import net.sorcemc.zombiecounter.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractHandler {

    private final @NotNull Storage storage;

    private final @NotNull ExecutorService executor;

    protected AbstractHandler(@NotNull AbstractHandler handler) {
        this.storage = handler.getStorage();

        this.executor = handler.getExecutor();
    }

    protected AbstractHandler(@NotNull Config conf) {
        this.storage = new Storage(conf);

        this.executor = Executors.newCachedThreadPool();
    }

    public void close() {
        this.storage.disconnect();

        this.executor.shutdownNow();
    }

    protected @NotNull Storage getStorage() {
        return this.storage;
    }

    protected @NotNull ExecutorService getExecutor() {
        return this.executor;
    }
}
