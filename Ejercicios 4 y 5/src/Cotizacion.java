import java.util.ArrayList;

/** Resultado temporal: construirlo no modifica vehiculos, clientes ni ingresos. */
public class Cotizacion {
    private Cliente cliente;
    private Vehiculo vehiculo;
    private int dias;
    private double subtotal;
    private double descuento;
    private double total;
    private ArrayList<String> motivos;

    public Cotizacion(Cliente cliente, Vehiculo vehiculo, int dias, double subtotal,
            double descuento, double total, ArrayList<String> motivos) {
        if (cliente == null || vehiculo == null || motivos == null || dias <= 0) {
            throw new IllegalArgumentException("Datos incompletos en la cotizacion.");
        }
        if (!Double.isFinite(subtotal) || !Double.isFinite(descuento) || !Double.isFinite(total)
                || subtotal < 0 || descuento < 0 || descuento > subtotal || total < 0
                || Math.abs(subtotal - descuento - total) > 0.011) {
            throw new IllegalArgumentException("Importes invalidos en la cotizacion.");
        }
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.dias = dias;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.motivos = new ArrayList<String>(motivos);
    }

    public Cliente getCliente() {
        return cliente;
    }
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    public int getDias() {
        return dias;
    }
    public double getSubtotal() {
        return subtotal;
    }
    public double getDescuento() {
        return descuento;
    }
    public double getTotal() {
        return total;
    }
    public ArrayList<String> getMotivos() {
        return new ArrayList<String>(motivos);
    }
    public boolean esPosible() {
        return motivos.isEmpty();
    }
}
