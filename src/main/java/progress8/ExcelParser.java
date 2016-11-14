package progress8;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class ExcelParser {

    public void parse(Path path) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(path.toFile()));
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);


    }

}
