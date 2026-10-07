import java.util.ArrayList;

/** Empresa con descuento fijo y hasta tres alquileres simultaneos. */
public class ClienteCorporativo extends Cliente {
    private String contacto;

    public ClienteCorporativo(String identificador, String nombre,
            ArrayList<String> licencias, String contacto) {
        super(identificador, nombre, licencias);
        if (contacto == null || contacto.trim().isEmpty()) {
            throw new IllegalArgumentException("El contacto es obligatorio.");
        }
        this.contacto = contacto.trim();
    }

    @Override
    public double getPorcentajeDescuento(int confirmados) {
        return 0.10;
    }

    @Override
    public int getLimiteActivos() {
        return 3;
    }

    @Override
    public String describir() {
        return "Corporativo | NIT: " + getIdentificador() + " | Empresa: " + getNombre()
                + " | Contacto: " + contacto + " | Licencias: " + getLicencias();
    }
}
