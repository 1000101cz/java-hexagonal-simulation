package logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/** Format logger output for html file */
class SimFormatter extends Formatter {
    public String format(LogRecord rec) {
        StringBuilder buf = new StringBuilder(1000);
        buf.append("<tr>\n");

        // red color for important logs
        if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
            buf.append(String.format("\t<td style=\"color:red\"><b>"+rec.getLevel()+"</b>"));
            System.out.println(rec.getLevel() + ":   " + formatMessage(rec)); // print important logs to commands line
        } else {
            buf.append(String.format("\t<td>"+rec.getLevel()));
        }
        buf.append(String.format("</td>\n\t<td>" + CurrentDate(rec.getMillis()) + "</td>\n\t<td>" + formatMessage(rec) + "</td>\n</tr>\n"));

        return buf.toString();
    }

    // get formatted time
    private String CurrentDate(long ms) {
        SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        return date_format.format(new Date(ms));
    }

    // head of html file
    public String getHead(Handler h) {
        return """
                <!DOCTYPE html>
                <head>
                <style>
                table { width: 100% }
                th { font:bold 10pt Verdana; }
                td { font:normal 10pt Verdana; }
                h1 {font:normal 11pt Verdana;}
                </style>
                </head>
                <body>
                <h1>Simulator 2077 - log file</h1>
                <table border="0" cellpadding="5" cellspacing="3">
                <tr align="left">
                \t<th style="width:10%">Level</th>
                \t<th style="width:15%">Time</th>
                \t<th style="width:75%">Message</th>
                </tr>
                """;
    }

    // end of html file
    public String getTail(Handler h) {
        return "</table>\n</body>\n</html>";
    }
}

/** Logging all app events to log.html file */
public class SimLogger {
    public final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    static public void setup() throws IOException {

        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.INFO);
        FileHandler fileHTML = new FileHandler("log.html");

        Formatter formatterHTML = new SimFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);
    }
}
