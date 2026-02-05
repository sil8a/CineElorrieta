package modelo;

public class Sala {
    private int idSala;
    private String nombre;
    private int numeroSillas;

    // Constructor vacío
    public Sala() {
    }

    // Constructor completo
    public Sala(int idSala, String nombre, int numeroSillas) {
        this.idSala = idSala;
        this.nombre = nombre;
        this.numeroSillas = numeroSillas;
    }

    // Getters y Setters
    public int getIdSala() { return idSala; }
    public void setIdSala(int idSala) { this.idSala = idSala; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getNumeroSillas() { return numeroSillas; }
    public void setNumeroSillas(int numeroSillas) { this.numeroSillas = numeroSillas; }

    @Override
    public String toString() {
        return "Sala [id=" + idSala + ", nombre=" + nombre + ", capacidad=" + numeroSillas + "]";
    }
}