import java.util.Locale;

/** Datos comunes y transiciones de estado de cualquier vehiculo. */
public abstract class Vehiculo {
    private String placa;
    private String marca;
    private String modelo;
    private double tarifaDiaria;
    private String estado;
    private int diasAcumulados;

    protected Vehiculo(String placa, String marca, String modelo,
            double tarifaDiaria, int diasIniciales) {
        if (placa == null || placa.trim().isEmpty()
                || marca == null || marca.trim().isEmpty()
                || modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("Placa, marca y modelo son obligatorios.");
        }
        if (!Double.isFinite(tarifaDiaria) || tarifaDiaria <= 0) {
            throw new IllegalArgumentException("La tarifa debe ser un numero positivo y finito.");
        }
        if (diasIniciales < 0) {
            throw new IllegalArgumentException("Los dias iniciales no pueden ser negativos.");
        }
        this.placa = placa.trim().toUpperCase(Locale.ROOT);
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.tarifaDiaria = tarifaDiaria;
        estado = "Disponible";
        diasAcumulados = diasIniciales;
    }

    public String getPlaca() {
        return placa;
    }
    public String getMarca() {
        return marca;
    }
    public String getModelo() {
        return modelo;
    }
    public double getTarifaDiaria() {
        return tarifaDiaria;
    }
    public String getEstado() {
        return estado;
    }
    public int getDiasAcumulados() {
        return diasAcumulados;
    }

    public abstract double calcularSubtotal(int dias);
    public abstract boolean aceptaLicencias(Cliente cliente);
    public abstract int getUmbral();
    public abstract String getCategoria();
    public abstract String describir();

    public void alquilar() {
        if (!estado.equals("Disponible")) {
            throw new IllegalStateException("El vehiculo no esta disponible.");
        }
        estado = "Alquilado";
    }

    public void devolver(int dias) {
        if (!estado.equals("Alquilado")) {
            throw new IllegalStateException("El vehiculo no esta alquilado.");
        }
        if (dias <= 0 || dias > Integer.MAX_VALUE - diasAcumulados) {
            throw new IllegalArgumentException("Cantidad de dias invalida para el acumulado.");
        }
        diasAcumulados += dias;
        // Java ejecuta el umbral de la categoria real del objeto.
        if (diasAcumulados >= getUmbral()) {
            estado = "En mantenimiento";
        } else {
            estado = "Disponible";
        }
    }

    public void finalizarMantenimiento() {
        if (!estado.equals("En mantenimiento")) {
            throw new IllegalStateException("El vehiculo no esta en mantenimiento.");
        }
        diasAcumulados = 0;
        estado = "Disponible";
    }
}
