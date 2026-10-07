import java.util.Locale;

/** Motocicleta: el recargo por cilindraje se cobra una sola vez. */
public class Motocicleta extends Vehiculo {
    private int cilindraje;

    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria,
            int diasIniciales, int cilindraje) {
        super(placa, marca, modelo, tarifaDiaria, diasIniciales);
        if (cilindraje <= 0 || diasIniciales >= 20) {
            throw new IllegalArgumentException("Cilindraje debe ser positivo y dias iniciales menores a 20.");
        }
        this.cilindraje = cilindraje;
    }

    @Override
    public double calcularSubtotal(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los dias deben ser positivos.");
        }
        double subtotal = getTarifaDiaria() * dias;
        if (cilindraje > 250) {
            subtotal += 75;
        }
        return subtotal;
    }

    @Override
    public boolean aceptaLicencias(Cliente cliente) {
        return cliente.tieneLicencia("M");
    }

    @Override
    public int getUmbral() {
        return 20;
    }

    @Override
    public String getCategoria() {
        return "Motocicleta";
    }

    @Override
    public String describir() {
        return String.format(Locale.US,
                "%s | %s %s | Motocicleta | %d cc | Q%.2f/dia | %s | dias: %d/%d",
                getPlaca(), getMarca(), getModelo(), cilindraje,
                getTarifaDiaria(), getEstado(), getDiasAcumulados(), getUmbral());
    }
}
