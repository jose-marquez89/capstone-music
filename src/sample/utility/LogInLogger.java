package sample.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.*;

public class LogInLogger {
    private static Logger loginLogger = Logger.getLogger("sample.main");
    private static FileHandler loggingHandler;

    public static void initializeLogger() throws IOException {
        try {
            LogManager.getLogManager().readConfiguration(
                    new FileInputStream("resources/login_logger.properties")
            );
            System.out.println("Set log manager file");
        } catch (IOException e) {
            System.out.println("Exception while setting logging properties: " + e.getMessage());
        }

        loggingHandler = new FileHandler("logging_activity.txt", true);
        loggingHandler.setFormatter(new SimpleFormatter());
        loginLogger.addHandler(loggingHandler);
        loginLogger.setLevel(Level.ALL);
    }

    public static Logger getLogger() {
        return loginLogger;
    }
}
