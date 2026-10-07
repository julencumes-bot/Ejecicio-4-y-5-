import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;

/** Enlaza el menu con el modelo, sin decidir reglas por tipo de objeto. */
public class Controlador {
    private RentaMovil empresa;
    private Vista vista;

    public Controlador(RentaMovil empresa, Vista vista) {
        this.empresa = empresa;
        this.vista = vista;
    }

    public void iniciar() {
        boolean continuar = true;
        vista.mostrar("RentaMovil - Herencia y polimorfismo");
        while (continuar) {
            try {
                vista.mostrar("\n1. Registrar vehiculo\n2. Registrar cliente\n3. Consultar flota y clientes"
                        + "\n4. Cotizar\n5. Solicitar alquiler (confirmar o cancelar)"
                        + "\n6. Registrar devolucion\n7. Finalizar mantenimiento\n8. Reportes\n0. Salir");
                int opcion = vista.leerEntero("Opcion: ");
                switch (opcion) {
                    case 1:
                        registrarVehiculo();
                        break;
                    case 2:
                        registrarCliente();
                        break;
                    case 3:
                        mostrarConsultas();
                        break;
                    case 4:
                        String placa = vista.leerTexto("Placa: ");
                        String identificador = vista.leerTexto("DPI o NIT: ");
                        int dias = vista.leerEntero("Dias: ");
                        vista.mostrarCotizacion(empresa.cotizar(placa, identificador, dias));
                        break;
                    case 5:
                        gestionarAlquiler();
                        break;
                    case 6:
                        String placaDevuelta = vista.leerTexto("Placa a devolver: ");
                        empresa.devolver(placaDevuelta);
                        vista.mostrar("Devolucion registrada. " + empresa.buscarVehiculo(placaDevuelta).describir());
                        break;
                    case 7:
                        empresa.finalizarMantenimiento(vista.leerTexto("Placa en mantenimiento: "));
                        vista.mostrar("Mantenimiento finalizado. Vehiculo disponible con cero dias acumulados.");
                        break;
                    case 8:
                        mostrarReportes();
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        vista.mostrar("Opcion invalida.");
                }
            } catch (IllegalArgumentException error) {
                vista.mostrar("No se completo la operacion: " + error.getMessage());
            } catch (IllegalStateException error) {
                vista.mostrar("No se completo la operacion: " + error.getMessage());
            } catch (NoSuchElementException error) {
                vista.mostrar("\nSe termino la entrada. Cualquier solicitud sin confirmar se descarta.");
                continuar = false;
            }
        }
        vista.mostrar("Sesion finalizada.");
    }

    private void registrarVehiculo() {
        vista.mostrar("1. Automovil\n2. Motocicleta\n3. Camioneta de carga\n4. Microbus");
        int categoria = vista.leerEntero("Categoria: ");
        if (categoria < 1 || categoria > 4) {
            throw new IllegalArgumentException("Categoria invalida.");
        }
        String placa = vista.leerTexto("Placa: ");
        String marca = vista.leerTexto("Marca: ");
        String modelo = vista.leerTexto("Modelo: ");
        double tarifa = vista.leerDecimal("Tarifa diaria: Q");
        Vehiculo vehiculo;
        // El enunciado permite seleccionar el constructor durante el registro.
        switch (categoria) {
            case 1:
                vehiculo = new Automovil(placa, marca, modelo, tarifa, 0,
                        vista.leerEntero("Pasajeros: "), vista.leerSiNo("Transmision automatica (s/n): "));
                break;
            case 2:
                vehiculo = new Motocicleta(placa, marca, modelo, tarifa, 0,
                        vista.leerEntero("Cilindraje en cc: "));
                break;
            case 3:
                vehiculo = new CamionetaCarga(placa, marca, modelo, tarifa, 0,
                        vista.leerDecimal("Capacidad maxima en toneladas: "));
                break;
            default:
                vehiculo = new Microbus(placa, marca, modelo, tarifa, 0,
                        vista.leerEntero("Pasajeros: "), vista.leerSiNo("Incluye piloto (s/n): "));
        }
        empresa.registrarVehiculo(vehiculo);
        vista.mostrar("Vehiculo registrado. " + vehiculo.describir());
    }

    private void registrarCliente() {
        vista.mostrar("1. Individual\n2. Corporativo");
        int tipo = vista.leerEntero("Tipo: ");
        if (tipo != 1 && tipo != 2) {
            throw new IllegalArgumentException("Tipo de cliente invalido.");
        }
        String identificador = vista.leerTexto("DPI o NIT: ");
        String nombre = vista.leerTexto("Nombre de persona o empresa: ");
        ArrayList<String> licencias = leerLicencias();
        Cliente cliente;
        if (tipo == 1) {
            cliente = new ClienteIndividual(identificador, nombre, licencias);
        }
        else {
            cliente = new ClienteCorporativo(identificador, nombre, licencias, vista.leerTexto("Contacto: "));
        }
        empresa.registrarCliente(cliente);
        vista.mostrar("Cliente registrado. " + cliente.describir());
    }

    private ArrayList<String> leerLicencias() {
        ArrayList<String> licencias = new ArrayList<String>();
        boolean continuar = true;
        while (continuar) {
            String licencia = vista.leerTexto("Licencia (A, B, C o M): ").toUpperCase(Locale.ROOT);
            if (!licencia.equals("A") && !licencia.equals("B") && !licencia.equals("C") && !licencia.equals("M")) {
                vista.mostrar("Licencia invalida. Intente nuevamente.");
            } else if (licencias.contains(licencia)) {
                vista.mostrar("Esa licencia ya esta incluida.");
            } else {
                licencias.add(licencia);
                continuar = vista.leerSiNo("Agregar otra licencia (s/n): ");
            }
        }
        return licencias;
    }

    private void gestionarAlquiler() {
        String placa = vista.leerTexto("Placa: ");
        String identificador = vista.leerTexto("DPI o NIT: ");
        int dias = vista.leerEntero("Dias: ");
        Cotizacion cotizacion = empresa.cotizar(placa, identificador, dias);
        vista.mostrarCotizacion(cotizacion);
        if (!cotizacion.esPosible()) {
            return;
        }
        if (vista.leerSiNo("Confirmar y cobrar el total mostrado (s/n): ")) {
            Alquiler alquiler = empresa.confirmar(cotizacion);
            vista.mostrar(String.format(Locale.US, "Alquiler #%d confirmado. Cobrado: Q%.2f",
                    alquiler.getNumero(), alquiler.getTotal()));
        } else {
            // No llamar al modelo: descartar una propuesta no altera ningun registro.
            vista.mostrar("Solicitud cancelada. No se registro ningun cobro ni alquiler.");
        }
    }

    private void mostrarConsultas() {
        vista.mostrar("FLOTA");
        for (Vehiculo vehiculo : empresa.getFlota()) {
            vista.mostrar(vehiculo.describir());
        }
        vista.mostrar("CLIENTES");
        for (Cliente cliente : empresa.getClientes()) {
            vista.mostrar(cliente.describir());
        }
    }

    private void mostrarReportes() {
        vista.mostrar("1. Flota e ingresos\n2. Alquileres activos\n3. Historial de un cliente");
        int opcion = vista.leerEntero("Reporte: ");
        switch (opcion) {
            case 1:
                vista.mostrar(empresa.generarReporte());
                break;
            case 2:
                ArrayList<Alquiler> activos = empresa.obtenerActivos();
                if (activos.isEmpty()) {
                    vista.mostrar("No hay alquileres activos.");
                }
                for (Alquiler alquiler : activos) {
                    vista.mostrar(String.format(Locale.US,
                            "#%d | %s | Cliente: %s (%s) | %d dias | Subtotal Q%.2f | Descuento Q%.2f | Total Q%.2f",
                            alquiler.getNumero(), alquiler.getVehiculo().getPlaca(),
                            alquiler.getCliente().getNombre(), alquiler.getCliente().getIdentificador(),
                            alquiler.getDias(), alquiler.getSubtotal(), alquiler.getDescuento(), alquiler.getTotal()));
                }
                break;
            case 3:
                vista.mostrar(empresa.generarHistorial(vista.leerTexto("DPI o NIT: ")));
                break;
            default:
                vista.mostrar("Reporte invalido.");
        }
    }
}
