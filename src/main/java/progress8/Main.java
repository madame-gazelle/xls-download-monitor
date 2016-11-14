package progress8;

import java.io.File;

public class Main {

    public static void main(final String[] args) throws Exception {
        String downloadsDir = (args.length == 0) ? System.getProperty("user.home") + File.separator + "Downloads" : args[0];
        new Watcher(downloadsDir, new ExcelParser()).watch();
    }

}
