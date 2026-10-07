/** Punto de entrada: conecta el modelo, la vista y el controlador. */
public class Main {
    public static void main(String[] args) {
        RentaMovil empresa = new RentaMovil();
        Vista vista = new Vista();
        empresa.cargarDatosIniciales();
        Controlador controlador = new Controlador(empresa, vista);
        controlador.iniciar();
    }
}
