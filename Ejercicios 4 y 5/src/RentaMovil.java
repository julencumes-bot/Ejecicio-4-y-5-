import java.util.ArrayList;
import java.util.Locale;

/** Coordina el negocio; las reglas variables se consultan por polimorfismo. */
public class RentaMovil {
    private ArrayList<Vehiculo> flota;
    private ArrayList<Cliente> clientes;
    private ArrayList<Alquiler> alquileres;
    private int siguienteNumero;

    public RentaMovil() {
        flota = new ArrayList<Vehiculo>();
        clientes = new ArrayList<Cliente>();
        alquileres = new ArrayList<Alquiler>();
        siguienteNumero = 1;
    }

    public void cargarDatosIniciales() {
        if (!flota.isEmpty() || !clientes.isEmpty() || !alquileres.isEmpty()) {
            throw new IllegalStateException("Los datos iniciales solo se cargan sobre listas vacias.");
        }
        registrarVehiculo(new Automovil("A001", "Toyota", "Corolla", 150, 28, 5, false));
        registrarVehiculo(new Automovil("A002", "Honda", "Civic", 180, 0, 5, true));
        registrarVehiculo(new Motocicleta("M001", "Honda", "CB", 80, 0, 250));
        registrarVehiculo(new Motocicleta("M002", "Yamaha", "MT", 100, 19, 300));
        registrarVehiculo(new CamionetaCarga("C001", "Isuzu", "NLR", 200, 14, 1.5));
        registrarVehiculo(new CamionetaCarga("C002", "Hyundai", "HD", 250, 0, 3));
        registrarVehiculo(new Microbus("B001", "Toyota", "Hiace", 450, 24, 15, true));
        registrarVehiculo(new Microbus("B002", "Hyundai", "H1", 350, 0, 12, false));

        ArrayList<String> licenciaC = new ArrayList<String>();
        licenciaC.add("C");
        ArrayList<String> licenciasAM = new ArrayList<String>();
        licenciasAM.add("A");
        licenciasAM.add("M");
        registrarCliente(new ClienteIndividual("1000000000001", "Ana Lopez", licenciaC));
        registrarCliente(new ClienteIndividual("1000000000002", "Luis Perez", licenciasAM));
        registrarCliente(new ClienteCorporativo("EMP001", "Logistica Norte", licenciasAM, "Marta Ruiz"));
        registrarCliente(new ClienteCorporativo("EMP002", "Servicios Sur", licenciaC, "Pedro Diaz"));

        // Antecedentes finalizados: cuentan para fidelidad, pero no como cobros de esta sesion.
        int[] diasAnteriores = {10, 10, 8};
        for (int dias : diasAnteriores) {
            Cotizacion cotizacion = cotizar("A001", "1000000000001", dias);
            alquileres.add(new Alquiler(siguienteNumero, cotizacion, true));
            siguienteNumero++;
        }
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo es obligatorio.");
        }
        for (Vehiculo registrado : flota) {
            if (registrado.getPlaca().equals(vehiculo.getPlaca())) {
                throw new IllegalArgumentException("Ya existe la placa " + vehiculo.getPlaca() + ".");
            }
        }
        if (!vehiculo.getEstado().equals("Disponible")) {
            throw new IllegalArgumentException("Un vehiculo nuevo debe estar disponible.");
        }
        flota.add(vehiculo);
    }

    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio.");
        }
        for (Cliente registrado : clientes) {
            if (registrado.getIdentificador().equals(cliente.getIdentificador())) {
                throw new IllegalArgumentException("Ya existe el identificador " + cliente.getIdentificador() + ".");
            }
        }
        clientes.add(cliente);
    }

    public Vehiculo buscarVehiculo(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria.");
        }
        String buscada = placa.trim().toUpperCase(Locale.ROOT);
        for (Vehiculo vehiculo : flota) {
            if (vehiculo.getPlaca().equals(buscada)) {
                return vehiculo;
            }
        }
        throw new IllegalArgumentException("No existe un vehiculo con placa " + buscada + ".");
    }

    public Cliente buscarCliente(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador es obligatorio.");
        }
        String buscado = identificador.trim().toUpperCase(Locale.ROOT);
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificador().equals(buscado)) {
                return cliente;
            }
        }
        throw new IllegalArgumentException("No existe un cliente con identificador " + buscado + ".");
    }

    public ArrayList<Vehiculo> getFlota() {
        return new ArrayList<Vehiculo>(flota);
    }
    public ArrayList<Cliente> getClientes() {
        return new ArrayList<Cliente>(clientes);
    }

    private int contarActivos(Cliente cliente) {
        int cantidad = 0;
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getCliente() == cliente && alquiler.isActivo()) {
                cantidad++;
            }
        }
        return cantidad;
    }

    private int contarConfirmados(Cliente cliente) {
        int cantidad = 0;
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getCliente() == cliente) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public Cotizacion cotizar(String placa, String identificador, int dias) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        Cliente cliente = buscarCliente(identificador);
        if (dias <= 0 || dias > Integer.MAX_VALUE - vehiculo.getDiasAcumulados()) {
            throw new IllegalArgumentException("Los dias deben ser enteros positivos dentro del rango permitido.");
        }
        double subtotal = redondear(vehiculo.calcularSubtotal(dias));
        double descuento = redondear(subtotal * cliente.getPorcentajeDescuento(contarConfirmados(cliente)));
        double total = redondear(subtotal - descuento);

        // Comprobaciones independientes para informar TODOS los impedimentos.
        ArrayList<String> motivos = new ArrayList<String>();
        if (!vehiculo.getEstado().equals("Disponible")) {
            motivos.add("Vehiculo no disponible: " + vehiculo.getEstado() + ".");
        }
        if (!vehiculo.aceptaLicencias(cliente)) {
            motivos.add("Licencia inadecuada para este vehiculo.");
        }
        if (contarActivos(cliente) >= cliente.getLimiteActivos()) {
            motivos.add("Limite de alquileres activos alcanzado (" + cliente.getLimiteActivos() + ").");
        }
        return new Cotizacion(cliente, vehiculo, dias, subtotal, descuento, total, motivos);
    }

    public Alquiler confirmar(Cotizacion cotizacion) {
        if (cotizacion == null) {
            throw new IllegalArgumentException("Debe proporcionar una cotizacion.");
        }
        Cotizacion actual = cotizar(cotizacion.getVehiculo().getPlaca(),
                cotizacion.getCliente().getIdentificador(), cotizacion.getDias());
        if (actual.getVehiculo() != cotizacion.getVehiculo()
                || actual.getCliente() != cotizacion.getCliente()) {
            throw new IllegalArgumentException("La cotizacion no pertenece a los objetos registrados.");
        }
        if (!actual.esPosible()) {
            throw new IllegalStateException("No se puede confirmar: " + actual.getMotivos());
        }
        if (!cotizacion.esPosible() || actual.getSubtotal() != cotizacion.getSubtotal()
                || actual.getDescuento() != cotizacion.getDescuento()
                || actual.getTotal() != cotizacion.getTotal()) {
            throw new IllegalStateException("La cotizacion cambio. Consulte y acepte una nueva cotizacion.");
        }
        // Primero se valida todo. Solo a partir de aqui se modifica el negocio.
        Alquiler alquiler = new Alquiler(siguienteNumero, actual, false);
        actual.getVehiculo().alquilar();
        alquileres.add(alquiler);
        siguienteNumero++;
        return alquiler;
    }

    public void devolver(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (!vehiculo.getEstado().equals("Alquilado")) {
            throw new IllegalStateException("El vehiculo no esta alquilado.");
        }
        Alquiler activo = null;
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getVehiculo() == vehiculo && alquiler.isActivo()) {
                if (activo != null) {
                    throw new IllegalStateException("Hay mas de un alquiler activo para esa placa.");
                }
                activo = alquiler;
            }
        }
        if (activo == null) {
            throw new IllegalStateException("No hay un alquiler activo para esa placa.");
        }
        vehiculo.devolver(activo.getDias());
        activo.finalizar();
    }

    public void finalizarMantenimiento(String placa) {
        buscarVehiculo(placa).finalizarMantenimiento();
    }

    public ArrayList<Alquiler> obtenerActivos() {
        ArrayList<Alquiler> activos = new ArrayList<Alquiler>();
        for (Alquiler alquiler : alquileres) {
            if (alquiler.isActivo()) {
                activos.add(alquiler);
            }
        }
        return activos;
    }

    public ArrayList<Alquiler> obtenerHistorial(String identificador) {
        Cliente cliente = buscarCliente(identificador);
        ArrayList<Alquiler> historial = new ArrayList<Alquiler>();
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getCliente() == cliente) {
                historial.add(alquiler);
            }
        }
        return historial;
    }

    public String generarReporte() {
        ArrayList<String> categorias = new ArrayList<String>();
        for (Vehiculo vehiculo : flota) {
            if (!categorias.contains(vehiculo.getCategoria())) {
                categorias.add(vehiculo.getCategoria());
            }
        }
        String reporte = "REPORTE DE FLOTA E INGRESOS DE LA SESION\n";
        int disponiblesTotal = 0;
        int alquiladosTotal = 0;
        int mantenimientoTotal = 0;
        double ingresosTotal = 0;
        double descuentosTotal = 0;
        for (String categoria : categorias) {
            int disponibles = 0;
            int alquilados = 0;
            int mantenimiento = 0;
            double ingresos = 0;
            // La etiqueta solo agrupa; nunca selecciona formulas o permisos.
            for (Vehiculo vehiculo : flota) {
                if (vehiculo.getCategoria().equals(categoria)) {
                    if (vehiculo.getEstado().equals("Disponible")) {
                        disponibles++;
                    }
                    else if (vehiculo.getEstado().equals("Alquilado")) {
                        alquilados++;
                    }
                    else if (vehiculo.getEstado().equals("En mantenimiento")) {
                        mantenimiento++;
                    }
                }
            }
            for (Alquiler alquiler : alquileres) {
                if (!alquiler.isAnterior() && alquiler.getVehiculo().getCategoria().equals(categoria)) {
                    ingresos += alquiler.getTotal();
                    descuentosTotal += alquiler.getDescuento();
                }
            }
            reporte += String.format(Locale.US,
                    "%s: %d registrados | %d disponibles | %d alquilados | %d en mantenimiento | Ingresos: Q%.2f%n",
                    categoria, disponibles + alquilados + mantenimiento, disponibles, alquilados, mantenimiento, ingresos);
            disponiblesTotal += disponibles;
            alquiladosTotal += alquilados;
            mantenimientoTotal += mantenimiento;
            ingresosTotal += ingresos;
        }
        reporte += String.format(Locale.US,
                "TOTAL: %d vehiculos | %d disponibles | %d alquilados | %d en mantenimiento%n"
                + "Ingresos de la sesion: Q%.2f%nDescuentos de la sesion: Q%.2f%n",
                flota.size(), disponiblesTotal, alquiladosTotal, mantenimientoTotal, ingresosTotal, descuentosTotal);
        return reporte + "Los antecedentes iniciales se consultan en el historial y no son ingresos de esta sesion.";
    }

    public String generarHistorial(String identificador) {
        Cliente cliente = buscarCliente(identificador);
        String reporte = "HISTORIAL\n" + cliente.describir() + "\n";
        double anteriores = 0;
        double sesion = 0;
        ArrayList<Alquiler> historial = obtenerHistorial(identificador);
        if (historial.isEmpty()) {
            reporte += "Sin alquileres registrados.\n";
        }
        for (Alquiler alquiler : historial) {
            String estado = "Finalizado";
            if (alquiler.isActivo()) {
                estado = "Activo";
            }
            String periodo = "Sesion actual";
            if (alquiler.isAnterior()) {
                anteriores += alquiler.getTotal();
                periodo = "Antecedente";
            } else {
                sesion += alquiler.getTotal();
            }
            reporte += String.format(Locale.US,
                    "#%d | %s | %d dias | Subtotal Q%.2f | Descuento Q%.2f | Total Q%.2f | %s | %s%n",
                    alquiler.getNumero(), alquiler.getVehiculo().getPlaca(), alquiler.getDias(),
                    alquiler.getSubtotal(), alquiler.getDescuento(), alquiler.getTotal(), estado, periodo);
        }
        return reporte + String.format(Locale.US,
                "Confirmados: %d | Activos: %d%nPagado anteriormente: Q%.2f%n"
                + "Pagado en la sesion: Q%.2f%nTotal historico pagado: Q%.2f",
                contarConfirmados(cliente), contarActivos(cliente), anteriores, sesion, anteriores + sesion);
    }

    private double redondear(double monto) {
        // Evita que Math.round sature silenciosamente ante numeros fuera de rango.
        if (!Double.isFinite(monto) || monto < 0 || monto >= Long.MAX_VALUE / 100.0) {
            throw new IllegalArgumentException("El monto calculado esta fuera del rango permitido.");
        }
        return Math.round(monto * 100.0) / 100.0;
    }
}
