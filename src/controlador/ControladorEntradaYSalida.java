package controlador;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter; // Necesario para el ticket
import java.io.IOException; // Necesario para controlar errores de fichero

import modelo.Pelicula;
import modelo.Sala;
/*
 * Esta calse sirve para escuchar lo que el usuario escribe por teclado
 * primero inicializa new Scanner (System.in)
 * Prepara un contador de ventas(que aunque este trozo de código no se use mucho
 * sirve para llevar la cuenta interna).
 */
public class ControladorEntradaYSalida {

    private Scanner sc;
    private int contadorVenta;
    
    public ControladorEntradaYSalida() {
        sc = new Scanner(System.in);
        contadorVenta = 1;
    }

    // --- MENÚS ---
    /**
     * Este metodo es importante para que el programa sea robusto
     * evita que el programa falle si el usuario se equivoca.
     * Si le pedimos un número del 1 al 3 y escribe una letra o un 8, éste método
     * lo detecta, limpia el error y le obliga a escribir bien.
     * Usa un bucle do while, que no termina hasta que el dato sea correcto.
     * sc.hasNextInt(). Es un sensor que mira si lo siguiente que viene es un número
     * si sí es un número lo guarda (sc.nextInt())
     * si no en número (son letras) lo elimina con sc.next() para que no atasque el programa.
     * al final comprueba si el número está dentro del rango mínimo o máximo.
     * @param minimo
     * @param maximo
     * @return
     */
    public int esValorMenuValido(int minimo, int maximo) {
        int opcionElegida = -1;
        do {
            System.out.print("Elige una opción (" + minimo + "-" + maximo + "): ");
            if (sc.hasNextInt()) {
                opcionElegida = sc.nextInt();
            } else {
                sc.next(); // Limpiar basura si meten letras
                opcionElegida = -1;
            }
        } while (opcionElegida < minimo || opcionElegida > maximo);
        
        sc.nextLine(); // Limpiar buffer del enter
        return opcionElegida;
    }

  

    
    // --- DATOS DE USUARIO ---
    /**
     * métodos sencillos para leer texto
     * @return
     */
    public String pedirDni() {
        System.out.println("Introduce tu DNI:");
        return sc.nextLine();
    }
    
    public String pedirContrasena() {
        System.out.println("Introduce tu contraseña:");
        return sc.nextLine();
    }
    

    /**
     * CONTRASEÑA ENCRIPTADA
     * Usa el método .hashCode()
convierte cualquier texto ej "1234" en un número raro y único (ej"1509442), no se usa en aplicaciones reales
     * @param texto
     * @return
     */
    public String encriptar(String texto) {
        return String.valueOf(texto.hashCode());
    }

    /**
     * IMPRIMIR TICKET
     * Este método es como una imprasora virtual coge todo el texto del resumen de compra
     * y crea un archivo de texto real en el ordenador para que el cliente tenga su factura.
     * Usa la clase FileWriter. Crea o sobrescribe un archivo llamado ticket_compra.txt dentro de la
     * carpeta ticket.
     * Escribe el contenido con .write().
     * Usa .close() para cerrar el archivo y asegurar que se guardan los datos
     * Está rodeado de un try catch por si falla el disco duro o no existe en la carpeta.
     * @param contenidoTicket
     */
    public void imprimirTicket(String contenidoTicket) {
        try {
            FileWriter escritor = new FileWriter("ticket/ticket_compra.txt");
            escritor.write(contenidoTicket);
            escritor.close();
            System.out.println(">> TICKET GUARDADO EN 'ticket_compra.txt'");
        } catch (IOException e) {
            System.out.println("Error al crear el fichero del ticket: " + e.getMessage());
        }
    }
}