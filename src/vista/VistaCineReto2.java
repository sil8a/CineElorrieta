package vista;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import controlador.ControladorBDcine;
import controlador.ControladorEntradaYSalida;
import modelo.Cliente;
import modelo.Entrada;
import modelo.Pelicula;
import modelo.Sesion;
/**
 * VistaCineReto2: Es la clase Directora:
 * usa:
 * ControladorBD para guardar y leer datos
 * Controlador Entrada para hablar con el usuario
 * controla el flujo de pantallas de la aplicación.
 */

public class VistaCineReto2 {

    /**
     * Variables Globales, se declaran static
     */
    static ControladorBDcine controlBD;
    static ControladorEntradaYSalida controlIO;
    static Scanner sc = new Scanner(System.in);
    
    // VARIABLES DE ESTADO
    /**
     * carritoCompra: Es una lista donde se acumulan las entradas 
     * antes de pagar.
     * clienteLogueado: aquí se guarda quién está usando la app, si es null
     * significa que nadie ha iniciado sesión.
     */
    static Cliente clienteLogueado = null;
    static ArrayList<Entrada> carritoCompra = new ArrayList<>();
    static ArrayList<Pelicula> listaPelis = null;
    static ArrayList<Sesion> listaSesiones = null;

   //MAIN:
    public static void main(String[] args) {
        /**
         *  Conectamos controladores
         */
        controlBD = new ControladorBDcine("cine_Elorrieta");
        controlIO = new ControladorEntradaYSalida();

        /**
         *  Iniciamos conexión
         */
        if (controlBD.iniciarConexion()) {
            /**
             * la variable: int pantalla Actual sirve para mostrar diferentes menus de las etapas de 
             * la App.
             */
            int pantallaActual = 0; // 0=Inicio, 1=Login, 2=SeleccionPeli, 3=Resumen, 4=Reset, 5=Salir
            
            while (pantallaActual != 5) {
                switch (pantallaActual) {
                case 0: // BIENVENIDA
                    System.out.println("\n=== BIENVENIDO AL CINE ELORRIETA ===");
                    System.out.println("1. Iniciar Sesión / Registrarse");
                    System.out.println("2. Salir");
                    int op = controlIO.esValorMenuValido(1, 2);
                    if(op == 1) pantallaActual = 1; 
                    else pantallaActual = 5;       
                    break;

                case 1: // LOGIN
                    pantallaActual = gestionarLogin(); 
                    break;

                case 2: // SELECCIÓN PELÍCULA
                    pantallaActual = seleccionarPelicula();
                    break;
                    
                case 3: // RESUMEN Y PAGO
                    pantallaActual = finalizarCompra();
                    break;
                    
                case 4: // LIMPIEZA
                    carritoCompra.clear();
                    
                    System.out.println("...Volviendo al inicio...");
                    pantallaActual = 0; 
                    break;
                }
            }
            
            controlBD.cerrarConexion();
            System.out.println("Fin del programa.");
        } else {
            System.out.println("FATAL ERROR: No se pudo conectar a la Base de Datos.");
        }
    }
 // --- LÓGICA DEL LOGIN ---
/**
 * gestionarLogin: gestiona si el usuario ya existe o es nuevo
 * pide DNI y contraseña, llama a controlBD.login(),si devuelve un
 * cliente, adentro.
 * Si es registro llama a controlBD.registrarCliente()
 * verifica si el DNI existe antes de intentar registrarlo para
 * no duplicar.
 * 
 */
    
    public static int gestionarLogin() {
        System.out.println("\n--- IDENTIFICACIÓN ---");
        System.out.println("1. Ya tengo cuenta (Login)");
        System.out.println("2. Quiero registrarme");
        System.out.println("3. Atrás");
        int op = controlIO.esValorMenuValido(1, 3);

        if (op == 3) return 0; // Volver

        String dni = controlIO.pedirDni();
        
        if (op == 1) { // LOGIN
            String pass = controlIO.pedirContrasena();
            clienteLogueado = controlBD.login(dni, pass);
            
            if (clienteLogueado != null) {
                System.out.println(">> ¡Bienvenido " + clienteLogueado.getNombre() + "!");
                return 2; // Éxito -> Cartelera
            } else {
                System.out.println(">> Error: Credenciales incorrectas.");
                return 1; // Reintentar
            }

        } else { // REGISTRO
            if (controlBD.existeCliente(dni)) {
                System.out.println(">> ERROR: El DNI ya está registrado.");
                return 1; 
            }

            System.out.println("Introduce tu Nombre:");
            String nombre = sc.nextLine();
            System.out.println("Introduce tu Apellido:");
            String apellido = sc.nextLine();
            System.out.println("Introduce tu Email:");
            String email = sc.nextLine();
            String pass = controlIO.pedirContrasena();

            Cliente nuevoCliente = new Cliente(dni, nombre, apellido, email, pass);
            
            if (controlBD.registrarCliente(nuevoCliente)) {
                System.out.println(">> Registro exitoso. Por favor, inicia sesión.");
                return 1; 
            } else {
                System.out.println(">> Error desconocido al registrar.");
                return 1;
            }
        }
    }

    // --- LÓGICA DE SELECCIÓN DE PELÍCULAS ---
    
    public static int seleccionarPelicula() {
        if (listaPelis == null) listaPelis = controlBD.obtenerPeliculas();

        System.out.println("\n--- CARTELERA ---");
        for (int i = 0; i < listaPelis.size(); i++) {
            System.out.println((i + 1) + ". " + listaPelis.get(i).getTitulo() + " (" + listaPelis.get(i).getDuracion() + " min)");
        }
        System.out.println("0. Ver Carrito y Pagar (" + carritoCompra.size() + " items)");
        System.out.println("99. Cerrar Sesión");

        int eleccion = controlIO.esValorMenuValido(0, listaPelis.size());

        if (eleccion == 99) return 4; 
        if (eleccion == 0) {
            if (carritoCompra.isEmpty()) {
                System.out.println("El carrito está vacío. Elige una película primero.");
                return 2;
            }
            return 3; // Ir a Resumen
        }

        Pelicula peliElegida = listaPelis.get(eleccion - 1);
        
        // Llamamos al método que devuelve boolean si compró algo
        boolean compraRealizada = agregarSesionAlCarrito(peliElegida);

        if (compraRealizada) {
            System.out.println("\n-------------------------------------------");
            System.out.println("¿Deseas comprar otra película o proceder con el pago?");
            System.out.println("1. Comprar otra película");
            System.out.println("2. Proceder con el pago");
            int decision = controlIO.esValorMenuValido(1, 2);
            
            if (decision == 2) {
                return 3; // Nos lleva a finalizarCompra (Resumen)
            }
            // Si elige 1, vuelve al bucle (return 2)
        }

        return 2; // Volver a Cartelera
    }
/**
 * 
 * agregarSesionAlCarrito es el método en el que validamos
 * que haya sitio disponible
 * El cálculo que hace es:
 * Libres= CapacidadTotal menos EspectadoresActuales
 * si libres es <= a 0 avisa que está agotado.
 * y si hay sitio crea el objeto Entrada y lo mete al carritoCompra
 */
    public static boolean agregarSesionAlCarrito(Pelicula peli) {
        System.out.println("\n--- SESIONES PARA: " + peli.getTitulo() + " ---");
        listaSesiones = controlBD.obtenerSesiones();
        
        ArrayList<Sesion> sesionesValidas = new ArrayList<>();
        int contador = 1;
        
        for (Sesion s : listaSesiones) {
            if (s.getIdPelicula() == peli.getIdPelicula()) {
                int capacidadSala = controlBD.obtenerCapacidadSala(s.getIdSala());
                int libres = capacidadSala - s.getEspectadores();

                System.out.println(contador + ". Fecha: " + s.getFecha() 
                        + " | Hora: " + s.getHoraInicio() 
                        + " | Sala: " + s.getIdSala()
                        + " | Libres: " + libres + " butacas"
                        + " | Precio: " + s.getPrecioSesion() + "€");
                
                sesionesValidas.add(s);
                contador++;
            }
        }
        
        if (sesionesValidas.isEmpty()) {
            System.out.println("No hay sesiones disponibles para esta película.");
            return false;
        }
        
        System.out.println("Elige sesión (número) o 0 para cancelar:");
        int sel = controlIO.esValorMenuValido(0, sesionesValidas.size());
        
        if (sel == 0) return false;

        Sesion sesionElegida = sesionesValidas.get(sel - 1);
        
        // VALIDACIÓN DE AFORO
        int capacidadSala = controlBD.obtenerCapacidadSala(sesionElegida.getIdSala());
        int libres = capacidadSala - sesionElegida.getEspectadores();
        
        if (libres <= 0) {
            System.out.println(">> ERROR: No hay sillas disponibles en esta sesión.");
            return false; 
        }

        System.out.println("¿Cuántas entradas quieres? (Máximo " + libres + "):");
        int cantidad = controlIO.esValorMenuValido(1, libres);
        
        Entrada nuevaEntrada = new Entrada(sesionElegida, peli, cantidad);
        carritoCompra.add(nuevaEntrada);
        System.out.println(">> Entrada añadida al carrito correctamente.");
        return true;
    }

    // --- LÓGICA DE PAGO ---
    /**
     * 
     * Cálculo de descuentos:
     * Recorre el carrito y guarda los títulos de las películas en una
     * lista que se llama titulosVistos (sin repetir.
     * si hay 2 titulos distintos sabe que hay que aplicar el 20%
     * de descuento y si hay 3 o mas titulos distintos calcula el 30%
     * si solo es una pelicula aunque compre 5 entradas es 0% de descuento)
     */
    public static int finalizarCompra() {
        System.out.println("\n==================================");
        System.out.println("       RESUMEN DE COMPRA");
        System.out.println("==================================");
        
        double totalBruto = 0;
        int cantidadPelisDistintas = 0; 
        ArrayList<String> titulosVistos = new ArrayList<>();

        for (Entrada e : carritoCompra) {
            System.out.println("* " + e.getPelicula().getTitulo() + 
                    " | " + e.getNumeroPersonas() + " pers. | " + e.getPrecio() + "€");
            
            totalBruto += e.getPrecio();
            
            if (!titulosVistos.contains(e.getPelicula().getTitulo())) {
                titulosVistos.add(e.getPelicula().getTitulo());
                cantidadPelisDistintas++;
            }
        }
        
        // Descuentos
        double descuento = 0;
        if (cantidadPelisDistintas >= 3) {
            descuento = totalBruto * 0.30;
            System.out.println(">> PROMOCIÓN APLICADA: 3+ Películas (30%)");
        } else if (cantidadPelisDistintas == 2) {
            descuento = totalBruto * 0.20;
            System.out.println(">> PROMOCIÓN APLICADA: 2 Películas (20%)");
        }
        System.out.println("   Descuento: -" + descuento + "€");
        
        double totalFinal = totalBruto - descuento;
        System.out.println("----------------------------------");
        System.out.println("TOTAL A PAGAR: " + totalFinal + "€");
        System.out.println("==================================");
        
        System.out.println("\n1. Confirmar Pago");
        System.out.println("2. Seguir comprando");
        System.out.println("3. Cancelar compra");
        
        int op = controlIO.esValorMenuValido(1, 3);
        
        if (op == 2) return 2; 
        if (op == 3) return 4; 
        
        // PROCESO DE PAGO
        /**
         * Las transacciones se guardas en la BD:
         * primero la compra: Se guarda el total y el DNI, la BD nos devuelve un ID generado.
         * Segundo, para las entradas  se hace un for, un recorrido al carritoCompra, vinculándola al idGenerado
         * Tercero, se actualiza el aforo en la BD , se hace UPDATE en la tabla sesiones para sumar los 
         * espectadores nuevos.
         */
        int idCompraGenerado = controlBD.guardarCompra(clienteLogueado.getDni(), totalFinal, descuento);
        
        if (idCompraGenerado > 0) {
            for(Entrada e : carritoCompra) {
                controlBD.guardarEntrada(e, idCompraGenerado);
                controlBD.actualizarAforo(e.getSesion().getIdSesion(), e.getNumeroPersonas());
            }
/**
 * EL TICKET: Al final se genera un texto resumen y le pregunta al
 * usuario si quiere imprimirlo( usando el método de ControladorIO
 */
            String ticket = "TICKET CINE ELORRIETA\n";
            ticket += "Cliente: " + clienteLogueado.getNombre() + "\n";
            ticket += "Fecha: " + new Date() + "\n";
            ticket += "ID Compra: " + idCompraGenerado + "\n----------------\n";
            for(Entrada e : carritoCompra) {
                ticket += e.getPelicula().getTitulo() + " x" + e.getNumeroPersonas() + " = " + e.getPrecio() + "e\n";
            }
            ticket += "----------------\n";
            ticket += "Total Pagado: " + totalFinal + "e\n";
            
            System.out.println("\n" + ticket);
            
            System.out.println("¿Quieres guardar el ticket en un archivo?");
            System.out.println("1. Sí");
            System.out.println("2. No");
            int guardar = controlIO.esValorMenuValido(1, 2);
            if(guardar == 1) {
                controlIO.imprimirTicket(ticket);
            }
            
            System.out.println(">> ¡Compra finalizada con éxito!");
        } else {
            System.out.println("Error crítico al guardar la compra en la BD.");
        }
        
        return 4; // Reset
    }
    /**
     * En Resumen:  Para la navegación se ha usado un switch-case
     * Para el aforo se calcula calcula Capacidad menos Ocupados 
     * leyendo la base de datos y se puede ver la actualización una vez que el cliente
     * ha pagado.
     * Para el descuento el sistema detecta automáticamente si se ha comprado 2 o mas entradas
     * diferentes.
     */
}