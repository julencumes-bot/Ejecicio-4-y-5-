import java.util.Locale;

/** Automovil con recargo diario cuando su transmision es automatica. */
public class Automovil extends Vehiculo {
    private int pasajeros;
    private boolean automatica;

    public Automovil(String placa, String marca, String modelo, double tarifaDiaria,
            int diasIniciales, int pasajeros, boolean automatica) {
        super(placa, marca, modelo, tarifaDiaria, diasIniciales);
        if (pasajeros <= 0 || diasIniciales >= 30) {
            throw new IllegalArgumentException("Pasajeros deben ser positivos y dias iniciales menores a 30.");
        }
        this.pasajeros = pasajeros;
        this.automatica = automatica;
    }

    @Override
    public double calcularSubtotal(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los dias deben ser positivos.");
        }
        double subtotal = getTarifaDiaria() * dias;
        if (automatica) {
            subtotal += 50.0 * dias;
        }
        return subtotal;
    }

    @Override
    public boolean aceptaLicencias(Cliente cliente) {
        return cliente.tieneLicencia("A") || cliente.tieneLicencia("B") || cliente.tieneLicencia("C");
    }

    @Override
    public int getUmbral() {
        return 30;
    }

    @Override
    public String getCategoria() {
        return "Automovil";
    }

    @Override
    public String describir() {
        String transmision = "manual";
        if (automatica) {
            transmision = "automatica";
        }
        return String.format(Locale.US,
                "%s | %s %s | Automovil | %d pasajeros | %s | Q%.2f/dia | %s | dias: %d/%d",
                getPlaca(), getMarca(), getModelo(), pasajeros, transmision,
                getTarifaDiaria(), getEstado(), getDiasAcumulados(), getUmbral());
    }
}
