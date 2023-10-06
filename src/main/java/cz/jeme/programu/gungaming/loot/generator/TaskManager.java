package cz.jeme.programu.gungaming.loot.generator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum TaskManager {
    INSTANCE;
    private final List<Task> queue = new ArrayList<>();

    void check() {
        if (queue.isEmpty()) return;

        Task task = queue.get(0);
        if (task.isRunning()) return;
        if (task.isFinished()) {
            queue.remove(0);
            check();
        } else {
            task.start();
        }
    }

    void addTask(@NotNull Task task) {
        queue.add(task);
        check();
    }

    public int getQueueSize() {
        return queue.size();
    }
}