package sample.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.*;

/**
 * Provides methods to record log-in successes and failures
 * to a local text file.
 *
 * @author Jose Marquez
 */
public class LogInLogger {
    private static Logger loginLogger = Logger.getLogger("sample.main");
    private static FileHandler loggingHandler;

    /**
     * Puts together the elements necessary to log login attempts.
     *
     * Sets the log file properties (string format) from a properties file
     * to include the required log string components.
     *
     * @throws IOException
     */
    public static void initializeLogger() throws IOException {
        try {
            LogManager.getLogManager().readConfiguration(
                    new FileInputStream("resources/login_logger.properties")
            );
        } catch (IOException e) {
            System.out.println("Exception while setting logging properties: " + e.getMessage());
        }

        loggingHandler = new FileHandler("login_activity.txt", true);
        loggingHandler.setFormatter(new SimpleFormatter());
        loginLogger.addHandler(loggingHandler);
        loginLogger.setLevel(Level.ALL);
    }

    /**
     * Used to retrieve the <code>Logger</code> object
     * for use across the application.
     *
     * @return a fully prepared <code>Logger</code> object
     */
    public static Logger getLogger() {
        return loginLogger;
    }
}
