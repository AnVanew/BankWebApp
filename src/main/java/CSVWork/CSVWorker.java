package CSVWork;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * Класс CSVWorker служит для работы с CSV файлами.
 */

public class CSVWorker {

    /**
     * Метод реализует запись объекта в строку в CSV файл по соответсвующим заголовкам
     *
     * @param file   файл CSV для записи
     * @param obj    объект для записи
     * @param header заголовки, по которым производится запись
     */
    public static <T> void PrintInCVS(File file, T obj, String[] header) throws CSVException {
        try (FileWriter fw = new FileWriter(file, true);
             ICsvBeanWriter writer = new CsvBeanWriter(fw, CsvPreference.STANDARD_PREFERENCE)) {
            writer.write(obj, header);
        } catch (IOException e) {
            throw new CSVException();
        }
    }
}
