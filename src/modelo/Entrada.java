package modelo;

public class Entrada {
    private int idEntrada;
    /**
     * RELACIONES:
     * Se guarda el objeto sesión entero, así la Entrada sabe la hora,
     * la sala y el precio base
     * También se guarda el objeto Pelicula, para podr imprimir el título en el
     * ticket facilmente
     */
    private Sesion sesion;
    private Pelicula pelicula;

    private int numeroPersonas;
    private double precio;
    private double descuento;

    // --- CONSTRUCTOR 
    public Entrada(Sesion sesion, Pelicula pelicula, int numeroPersonas) {
        this.sesion = sesion;
        this.pelicula = pelicula;
        this.numeroPersonas = numeroPersonas;
        
        /**
         * Calculamos el precio automáticamente basándonos en la sesión
         */
        this.precio = sesion.getPrecioSesion() * numeroPersonas;
        
        /**
         *  Valores iniciales por defecto (ya que no se pasan en los argumentos)
         */
        this.descuento = 0.0; 
        this.idEntrada = 0; //  es 0 porque Se asume que la Base de Datos le dará un ID luego
    }

  
    public Entrada(int idEntrada, Sesion sesion, Pelicula pelicula, int numeroPersonas, double precio, double descuento) {
        this.idEntrada = idEntrada;
        this.sesion = sesion;
        this.pelicula = pelicula;
        this.numeroPersonas = numeroPersonas;

        this.precio = sesion.getPrecioSesion() * numeroPersonas; 
        this.descuento = descuento;
    }

    // --- GETTERS Y SETTERS ---
    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Sesion getSesion() {
        return sesion;
    }

    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public int getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(int numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
  
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
}