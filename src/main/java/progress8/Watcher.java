package progress8;

import lombok.Data;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

@Data
public class Watcher {

    private final PathMatcher xlsPathMatcher = FileSystems.getDefault().getPathMatcher("regex:.*\\.xls");

    private final WatchService watcher;
    private final ExcelParser excelParser;
    private final Path directoryPath;

    public Watcher(String dirName, ExcelParser excelParser) throws IOException {
        this.excelParser = excelParser;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.directoryPath = Paths.get(dirName);

        directoryPath.register(watcher, ENTRY_CREATE);
    }

    public void watch() throws InterruptedException {
        while (true) {
            WatchKey watchKey = watcher.take();

            for (WatchEvent<?> event : watchKey.pollEvents()) {
                // This key is registered only for ENTRY_CREATE events, but an OVERFLOW event can occur regardless if events are lost or discarded.
                if (event.kind() == OVERFLOW) {
                    System.err.println("Received event type of OVERFLOW, skipping.");
                    continue;
                }

                // The filename is the context of the event.
                // noinspection unchecked
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path filePath = ev.context();

                if (xlsPathMatcher.matches(filePath)) {
                    try {
                        excelParser.parse(directoryPath.resolve(filePath));
                    } catch (IOException e) {
                        System.err.println("Error parsing file: " + filePath.toString() + " - " + e.getMessage());
                    }
                } else {
                    System.out.println("Skipping file: " + filePath);
                }
            }

            // Reset the key - this step is critical if you want to receive further watch events.
            // If the key is no longer valid, the directory is inaccessible so exit the loop.
            if (!watchKey.reset()) {
                System.err.println("Unable to reset key, exiting.");
                break;
            }
        }
    }

}
