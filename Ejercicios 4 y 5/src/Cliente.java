import java.util.ArrayList;
import java.util.Locale;

/** Datos comunes del cliente; cada subclase define su politica de alquiler. */
public abstract class Cliente {
    private String identificador;
    private String nombre;
    private ArrayList<String> licencias;

    protected Cliente(String identificador, String nombre, ArrayList<String> licencias) {
        if (identificador == null || identificador.trim().isEmpty()
                || nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificador y nombre son obligatorios.");
        }
        if (licencias == null || licencias.isEmpty()) {
            throw new IllegalArgumentException("Debe presentar al menos una licencia valida.");
        }
        this.licencias = new ArrayList<String>();
        for (String licencia : licencias) {
            if (licencia == null) {
                throw new IllegalArgumentException("Licencia invalida.");
            }
            String tipo = licencia.trim().toUpperCase(Locale.ROOT);
            if (!tipo.equals("A") && !tipo.equals("B") && !tipo.equals("C") && !tipo.equals("M")) {
                throw new IllegalArgumentException("Las licencias validas son A, B, C y M.");
            }
            if (!this.licencias.contains(tipo)) {
                this.licencias.add(tipo);
            }
        }
        this.identificador = identificador.trim().toUpperCase(Locale.ROOT);
        this.nombre = nombre.trim();
    }

    public String getIdentificador() {
        return identificador;
    }
    public String getNombre() {
        return nombre;
    }
    public ArrayList<String> getLicencias() {
        return new ArrayList<String>(licencias);
    }

    // Consulta el tipo exacto; cada vehiculo aplica la jerarquia A, B, C.
    public boolean tieneLicencia(String licencia) {
        return licencia != null && licencias.contains(licencia.trim().toUpperCase(Locale.ROOT));
    }

    public abstract double getPorcentajeDescuento(int confirmados);
    public abstract int getLimiteActivos();
    public abstract String describir();
}
