import java.util.Locale;

/** Calcula el recargo con la capacidad maxima, aunque se transporte menos. */
public class CamionetaCarga extends Vehiculo {
    private double capacidadToneladas;

    public CamionetaCarga(String placa, String marca, String modelo, double tarifaDiaria,
            int diasIniciales, double capacidadToneladas) {
        super(placa, marca, modelo, tarifaDiaria, diasIniciales);
        if (!Double.isFinite(capacidadToneladas) || capacidadToneladas <= 0
                || diasIniciales >= 15) {
            throw new IllegalArgumentException("Capacidad debe ser positiva y finita; dias iniciales menores a 15.");
        }
        this.capacidadToneladas = capacidadToneladas;
    }

    @Override
    public double calcularSubtotal(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los dias deben ser positivos.");
        }
        return getTarifaDiaria() * dias + 100.0 * capacidadToneladas * dias;
    }

    @Override
    public boolean aceptaLicencias(Cliente cliente) {
        return cliente.tieneLicencia("A") || cliente.tieneLicencia("B");
    }

    @Override
    public int getUmbral() {
        return 15;
    }

    @Override
    public String getCategoria() {
        return "Camioneta de carga";
    }

    @Override
    public String describir() {
        return String.format(Locale.US,
                "%s | %s %s | Camioneta de carga | %s toneladas | Q%.2f/dia | %s | dias: %d/%d",
                getPlaca(), getMarca(), getModelo(), capacidadToneladas,
                getTarifaDiaria(), getEstado(), getDiasAcumulados(), getUmbral());
    }
}
