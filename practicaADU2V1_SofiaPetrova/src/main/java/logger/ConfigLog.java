package logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")

public class ConfigLog {
    private String rutaLog;
    private String nivel;
    private int maximoTamano;
    private Rotacion rotacion;
    private Destinos destinos;

    @XmlElement
    public String getRutaLog() {
        return rutaLog;
    }

    public void setRutaLog(String rutaLog) {
        this.rutaLog = rutaLog;
    }

    @XmlElement
    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    @XmlElement
    public int getMaximoTamano() {
        return maximoTamano;
    }

    public void setMaximoTamano(int maximoTamano) {
        this.maximoTamano = maximoTamano;
    }

    @XmlElement
    public Rotacion getRotacion() {
        return rotacion;
    }

    public void setRotacion(Rotacion rotacion) {
        this.rotacion = rotacion;
    }

    @XmlElement
    public Destinos getDestinos() {
        return destinos;
    }

    public void setDestinos(Destinos destinos) {
        this.destinos = destinos;
    }

    public static class Rotacion {
        private String prefijo;

        @XmlElement
        public String getPrefijo() { return prefijo; }
        public void setPrefijo(String prefijo) { this.prefijo = prefijo; }
    }

    public static class Destinos {
        private boolean consola;
        private boolean fichero;

        @XmlElement
        public boolean isConsola() { return consola; }
        public void setConsola(boolean consola) { this.consola = consola; }

        @XmlElement
        public boolean isFichero() { return fichero; }
        public void setFichero(boolean fichero) { this.fichero = fichero; }
    }


}

