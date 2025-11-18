package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LoggerPropio {

    private ConfigLog config;
    private File logFile;
    private BufferedWriter bw;

    public LoggerPropio(ConfigLog config) {
        this.config = config;
        logFile = new File(config.getRutaLog());


        File carpeta = logFile.getParentFile();
        if (carpeta != null && !carpeta.exists()) {
            carpeta.mkdirs();
        }


        try {
            bw = new BufferedWriter(new FileWriter(logFile, true));
        } catch (IOException ioe) {
            System.err.println("ERROR: " + ioe.getMessage());
        }
    }

    public void log(String nivel, String clase, String mensaje) {
        if (!nivelPermitido(nivel)) {
            return;
        }

        String fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS").format(new Date());
        String linea = fecha + " [" + clase + "] " + nivel + " – " + mensaje;

        if (config.getDestinos().isConsola()) {
            System.out.println(linea);
        }

        if (config.getDestinos().isFichero()) {
            try {

                bw.write(linea + "\n");
            } catch (IOException ioe) {
                System.err.println("ERROR: " + ioe.getMessage());
            }
        }

        if (logFile.length() > config.getMaximoTamano()) {
            File nuevo = new File(config.getRotacion().getPrefijo() + logFile.getName());
            logFile.renameTo(nuevo);
            logFile = new File(config.getRutaLog());
            try {
                bw = new BufferedWriter(new FileWriter(logFile, true));
            } catch (IOException ioe) {
                System.err.println("ERROR: " + ioe.getMessage());
            }

        }


    }

    private boolean nivelPermitido (String nivel) {
        String nivelConfig = config.getNivel();
        List<String> niveles = List.of("ERROR", "WARN", "INFO", "DEBUG", "TRACE");
        int nivelActual = niveles.indexOf(nivel.toUpperCase());
        int nivelConfigurado = niveles.indexOf(nivelConfig.toUpperCase());
        return nivelActual <= nivelConfigurado;

    }

    public void cerrar() {
        try {
            bw.close();
        } catch (IOException ioe) {
            System.err.println("ERROR al cerrar el log: " + ioe.getMessage());
        }
    }


}
