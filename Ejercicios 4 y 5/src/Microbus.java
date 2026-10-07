import java.util.Locale;

/** El piloto es una caracteristica fija del microbus. */
public class Microbus extends Vehiculo {
    private int pasajeros;
    private boolean conPiloto;

    public Microbus(String placa, String marca, String modelo, double tarifaDiaria,
            int diasIniciales, int pasajeros, boolean conPiloto) {
        super(placa, marca, modelo, tarifaDiaria, diasIniciales);
        if (pasajeros <= 0 || diasIniciales >= 25) {
            throw new IllegalArgumentException("Pasajeros deben ser positivos y dias iniciales menores a 25.");
        }
        this.pasajeros = pasajeros;
        this.conPiloto = conPiloto;
    }

    @Override
    public double calcularSubtotal(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los dias deben ser positivos.");
        }
        double subtotal = getTarifaDiaria() * dias;
        if (conPiloto) {
            subtotal += 250.0 * dias;
        }
        return subtotal;
    }

    @Override
    public boolean aceptaLicencias(Cliente cliente) {
        return conPiloto || cliente.tieneLicencia("A") || cliente.tieneLicencia("B");
    }

    @Override
    public int getUmbral() {
        return 25;
    }

    @Override
    public String getCategoria() {
        return "Microbus";
    }

    @Override
    public String describir() {
        String piloto = "sin piloto";
        if (conPiloto) {
            piloto = "con piloto";
        }
        return String.format(Locale.US,
                "%s | %s %s | Microbus | %d pasajeros | %s | Q%.2f/dia | %s | dias: %d/%d",
                getPlaca(), getMarca(), getModelo(), pasajeros, piloto,
                getTarifaDiaria(), getEstado(), getDiasAcumulados(), getUmbral());
    }
}
