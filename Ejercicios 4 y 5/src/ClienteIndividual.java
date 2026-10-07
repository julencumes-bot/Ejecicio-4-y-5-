import java.util.ArrayList;

/** Persona identificada por DPI; recibe descuento desde el cuarto alquiler. */
public class ClienteIndividual extends Cliente {
    public ClienteIndividual(String identificador, String nombre, ArrayList<String> licencias) {
        super(identificador, nombre, licencias);
        String dpi = identificador.trim();
        if (dpi.length() != 13) {
            throw new IllegalArgumentException("El DPI debe tener exactamente 13 digitos.");
        }
        for (int i = 0; i < dpi.length(); i++) {
            if (dpi.charAt(i) < '0' || dpi.charAt(i) > '9') {
                throw new IllegalArgumentException("El DPI solo puede contener digitos de 0 a 9.");
            }
        }
    }

    @Override
    public double getPorcentajeDescuento(int confirmados) {
        if (confirmados >= 3) {
            return 0.05;
        }
        return 0;
    }

    @Override
    public int getLimiteActivos() {
        return 1;
    }

    @Override
    public String describir() {
        return "Individual | DPI: " + getIdentificador() + " | " + getNombre()
                + " | Licencias: " + getLicencias();
    }
}
