import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogToFileAndConsoleExample {
    private static final Logger LOGGER = Logger.getLogger(LogToFileAndConsoleExample.class.getName());

    static {
        try {
            // Создаем FileHandler для записи логов в файл "application.log"
            FileHandler fileHandler = new FileHandler("application.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);

            // Создаем ConsoleHandler для вывода логов в консоль
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(consoleHandler);

            // Не отключаем вывод в консоль
            LOGGER.setUseParentHandlers(false);

        } catch (IOException e) {
            LOGGER.severe("Failed to setup logger handler.");
        }
    }

    public static void main(String[] args) {
        LOGGER.info("This log will be written to the file and displayed in the console.");
    }
}
