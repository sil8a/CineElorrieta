package modelo;
/**
 * Se importa Timestamp para fecha compra
 * porque necesito tanto la fecha y la hora de la compra
 */
import java.sql.Timestamp;

public class Compra {
    private int idCompra;
    private Timestamp fechaCompra; // Mapea DATETIME
    private double precioTotal;
    private double descuentoAplicado;
    private String dniCliente; // FK hacia Cliente

    public Compra() {
    }

    public Compra(int idCompra, Timestamp fechaCompra, double precioTotal, double descuentoAplicado, String dniCliente) {
        this.idCompra = idCompra;
        this.fechaCompra = fechaCompra;
        this.precioTotal = precioTotal;
        this.descuentoAplicado = descuentoAplicado;
        this.dniCliente = dniCliente;
    }

    public int getIdCompra() { return idCompra; }
    public void setIdCompra(int idCompra) { this.idCompra = idCompra; }

    public Timestamp getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(Timestamp fechaCompra) { this.fechaCompra = fechaCompra; }

    public double getPrecioTotal() { return precioTotal; }
    public void setPrecioTotal(double precioTotal) { this.precioTotal = precioTotal; }

    public double getDescuentoAplicado() { return descuentoAplicado; }
    public void setDescuentoAplicado(double descuentoAplicado) { this.descuentoAplicado = descuentoAplicado; }

    public String getDniCliente() { return dniCliente; }
    public void setDniCliente(String dniCliente) { this.dniCliente = dniCliente; }
    
    @Override
    public String toString() {
        return "Compra [id=" + idCompra + ", total=" + precioTotal + "]";
    }
}