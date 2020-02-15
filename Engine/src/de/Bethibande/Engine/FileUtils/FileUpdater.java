package de.Bethibande.Engine.FileUtils;

import de.Bethibande.Engine.EngineConfig;
import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.utils.Log;

import java.io.File;
import java.nio.file.*;

public class FileUpdater extends Thread {

    public FileUpdater() {
        super("File-updater");
    }
    @Override
    public void run() {
        try {
            Path dir = EngineCore.project_root.toPath();
            WatchService watcher = dir.getFileSystem().newWatchService();
            dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
            for(;;) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    try {
                        Path child = dir.resolve(filename);
                        if (!EngineCore.handleUpdate.contains(new File(child.toString()))) {
                            EngineCore.handleUpdate.add(new File(child.toString()));
                            Log.log("Registered a file change: '" + child + "'!");
                        }
                    } catch (Exception x) {
                        System.err.println(x);
                        continue;
                    }
                    key.reset();
                    break;
                }
            }
        } catch (InterruptedException e) {
            return;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}