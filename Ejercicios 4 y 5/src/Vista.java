import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Se encarga solamente de la entrada y la salida de consola. */
public class Vista {
    private Scanner entrada;

    public Vista() {
        entrada = new Scanner(System.in);
    }

    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!entrada.hasNextLine()) {
                throw new NoSuchElementException("Fin de la entrada.");
            }
            String texto = entrada.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            mostrar("El dato no puede quedar vacio.");
        }
    }

    public int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException error) {
                mostrar("Ingrese un numero entero valido, sin decimales.");
            }
        }
    }

    public double leerDecimal(String mensaje) {
        while (true) {
            try {
                double numero = Double.parseDouble(leerTexto(mensaje));
                if (Double.isFinite(numero)) {
                    return numero;
                }
                mostrar("Ingrese un numero finito.");
            } catch (NumberFormatException error) {
                mostrar("Ingrese un numero valido; use punto para los decimales.");
            }
        }
    }

    public boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje).toLowerCase(Locale.ROOT);
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            mostrar("Responda s o n.");
        }
    }

    public void mostrar(String texto) {
        System.out.println(texto);
    }

    public void mostrarCotizacion(Cotizacion cotizacion) {
        mostrar("\nCOTIZACION");
        mostrar(cotizacion.getVehiculo().describir());
        mostrar(cotizacion.getCliente().describir());
        mostrar(String.format(Locale.US, "Dias: %d%nSubtotal: Q%.2f%nDescuento: Q%.2f%nTotal: Q%.2f",
                cotizacion.getDias(), cotizacion.getSubtotal(), cotizacion.getDescuento(), cotizacion.getTotal()));
        if (cotizacion.esPosible()) {
            mostrar("El cliente puede alquilar este vehiculo en este momento.");
        } else {
            mostrar("No se puede alquilar por los siguientes motivos:");
            for (String motivo : cotizacion.getMotivos()) {
                mostrar("- " + motivo);
            }
        }
    }
}
