/** Comprobante: conserva los importes aceptados aunque termine el alquiler. */
public class Alquiler {
    private Cliente cliente;
    private Vehiculo vehiculo;
    private int dias;
    private double subtotal;
    private double descuento;
    private double total;
    private int numero;
    private boolean activo;
    private boolean anterior;

    public Alquiler(int numero, Cotizacion cotizacion, boolean anterior) {
        if (numero <= 0 || cotizacion == null || !cotizacion.esPosible()) {
            throw new IllegalArgumentException("El alquiler requiere numero y cotizacion validos.");
        }
        this.numero = numero;
        cliente = cotizacion.getCliente();
        vehiculo = cotizacion.getVehiculo();
        dias = cotizacion.getDias();
        subtotal = cotizacion.getSubtotal();
        descuento = cotizacion.getDescuento();
        total = cotizacion.getTotal();
        this.anterior = anterior;
        activo = !anterior;
    }

    public int getNumero() {
        return numero;
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
    public boolean isActivo() {
        return activo;
    }
    public boolean isAnterior() {
        return anterior;
    }

    public void finalizar() {
        if (!activo) {
            throw new IllegalStateException("El alquiler ya esta finalizado.");
        }
        activo = false;
    }
}
