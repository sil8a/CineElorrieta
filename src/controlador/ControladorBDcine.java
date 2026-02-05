package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import modelo.Cliente;
import modelo.Entrada;
import modelo.Pelicula;
import modelo.Sala;
import modelo.Sesion;

public class ControladorBDcine {

    
    private Connection conexion;
    private String nombreBD;

    public ControladorBDcine(String nombreBD) {
        this.nombreBD = nombreBD;
    }

    // --- CONEXIÓN  ---

    /**
     * Estos métodos conectan la aplicación con el servidor MySQL
     * @return
     */
    public boolean iniciarConexion() {
        boolean conexionRealizada = false;
        try {
            /**
             * Class.forName() Carga el driver, es como un traductor para que java entienda MySQL
             */
            Class.forName("com.mysql.jdbc.Driver");
            
            /**
             * DriverManager.getConnection() Usa la direccion URL, usuario y contraseña para conectar
             */
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/" + this.nombreBD, "root", "");
            conexionRealizada = true;
            /**
             * Se usa try catch para que si xaamp está apagado el programa no explote, sino que de un error.
             */
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró la librería de mysql connector (jar)");
        } catch (SQLException e) {
            System.out.println("No se encontró la BD " + this.nombreBD);
            e.printStackTrace();
        }
        return conexionRealizada;
    }

    /**
     *  Es muy importante cerrar conexión para liberar los recursos del ordenador
 * Funciona así: 
 * 1 verifica si la conexión existe(!=null) y si está abierta (!isClosed())
 * 2 si es así ejecuta .close().
     * @return
     */
    public boolean cerrarConexion() {
        boolean Conexioncerrada = false;
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                Conexioncerrada = true;
            }
        } catch (SQLException e) {
            System.out.println("No hay conexion con la BD");
        }
        return Conexioncerrada;
    }

  
/**
 *  GESTIÓN DE CLIENTES:
     * Antes de registra a un cliente se comprueba por el DNI si ya existe en la BD para
     * evitar duplicados.
     * Lanza la consulta SELECT dni..
     * usa datos.nest(): Si este método devuelve true, significa que ha encontrado al menos una fila
     * (el cliente existe)
 * @param dni
 * @return
 */
    public boolean existeCliente(String dni) {
        boolean existe = false;
        String sql = "SELECT dni FROM cliente WHERE dni = '" + dni + "'";
        try {
            Statement orden = conexion.createStatement();
            ResultSet datos = orden.executeQuery(sql);
            if (datos.next()) {
                existe = true;
            }
            orden.close();
        } catch (SQLException e) {
            System.out.println("Error al comprobar DNI: " + e.getMessage());
        }
        return existe;
    }
/**
 * RegistrarCliente (Cliente c)
 * Este método guarda un nuevo usuario en la BD solo si no existe previamente
 * primero llama a existeCliente(). Si es true, corta la ejecución (return false).
 * Si no existe, crea la sentencia sql  INSERT INTO.
 * Usa executeUpdate() Porque estamos cambiando los datos, actualizandolos, no leyendo.
 * @param c
 * @return
 */
    public boolean registrarCliente(Cliente c) {
        if(existeCliente(c.getDni())) {
            return false; 
        }

        String query = "INSERT INTO cliente (dni, nombre, apellidos, email, contrasena) VALUES ('"
                + c.getDni() + "', '" + c.getNombre() + "', '" +c.getApellidos() + "', '" +c.getEmail()+ "', '"
                + c.getContrasena() + "')"; 
        try {
            Statement orden = conexion.createStatement();
            orden.executeUpdate(query);
            orden.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }
/**
 * Este método comprueba si el usuario y la contraseña coinciden, si es corecto
 * devuelve los datos del cliente y si no devuelve nulo.
 * Hace un SELECT * ... WHERE dni=  ... and contrasena=...
 * Si datos.next() es verdadera, rellena un objeto Cliente con la información de la BD
 * Si no hay coincidencias devuelve null.
 * @param dni
 * @param password
 * @return
 */
    public Cliente login(String dni, String password) {
        Cliente clienteEncontrado = null;
        String sql = "SELECT * FROM cliente WHERE dni = '" + dni + "' AND contrasena = '" + password + "'";
        try {
            Statement orden = conexion.createStatement();
            ResultSet datos = orden.executeQuery(sql);
            if (datos.next()) {
                clienteEncontrado = new Cliente(
                    datos.getString("dni"),
                    datos.getString("nombre"),
                    datos.getString("apellidos"),
                    datos.getString("email"),
                    datos.getString("contrasena")
                );
            }
            orden.close();
        } catch (SQLException e) {
            System.out.println("Error en el login: " + e.getMessage());
        }
        return clienteEncontrado;
    }
    
   /**
    * Aquí guardamos el ticket.
    * Lo más importante es saber que ID le ha dado la BD a  la compra
    * para luego poder vincular las entradas.
    * Hace un INSERT INTO compra...
    * Usa Statement.RETURN_GENERATED_KEYS. Esto le dice a MySQL que
    * guarde y diga que número ID le ha puesto.
    * Recupera ese número con keys.getInt(1) y lo devuelve para usarlo después
    * @param dni
    * @param total
    * @param descuento
    * @return
    */
    public int guardarCompra(String dni, double total, double descuento) {
        int idGenerado = 0;
        String query = "INSERT INTO compra (dni_cliente, fecha_compra, precio_total, descuento_aplicado) VALUES ('"
                + dni + "', NOW(), " + total + ", " + descuento + ")";
        try {
            // 1. Creamos el statement vacío
            Statement orden = conexion.createStatement();
            // 2. Ejecutamos pidiendo la clave generada
            orden.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            
            ResultSet keys = orden.getGeneratedKeys();
            if (keys.next()) {
                idGenerado = keys.getInt(1); 
            }
            orden.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar la compra: " + e.getMessage());
        }
        return idGenerado;
    }
/**
 * Una vez tenemos el ticket de la compra, aquí se guarda cada entrada
 * individual vinculada a ese ticket
 * hace un INSERT INTO entrada.
 * usa idCompra del método anterior para que la base de datos sepa a que factura pertenece 
 * esa entrada.
 * @param entrada
 * @param idCompra
 */
    public void guardarEntrada(Entrada entrada, int idCompra) {
        String query = "INSERT INTO entrada (id_sesion, id_compra, cantidad, precio) VALUES ("
                + entrada.getSesion().getIdSesion() + ", "
                + idCompra + ", "
                + entrada.getNumeroPersonas() + ", "
                + entrada.getPrecio() + ")";
        try {
            Statement orden = conexion.createStatement();
            orden.executeUpdate(query);
            orden.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar entrada: " + e.getMessage());
        }
    }
/**
 * ACTUALIZAR AFORO 
 * este método suma los espectadores nuevos a los que ya habían
 * usa la sentencia UPDATE sesion
 * la lógica es: espectadores = espectadores + nuevasPersonas.
 * @param idSesion
 * @param nuevasPersonas
 */
    public void actualizarAforo(int idSesion, int nuevasPersonas) {
        String query = "UPDATE sesion SET espectadores = espectadores + " + nuevasPersonas 
                     + " WHERE id_sesion = " + idSesion;
        try {
            Statement orden = conexion.createStatement();
            orden.executeUpdate(query);
            orden.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar aforo: " + e.getMessage());
        }
    }
/**
 * OBTENER LA CAPACIDAD DE LA SALA:
 * Se pregunta a la BD cuántas sillas tiene la sala concreta para saber cuando se llena
 * Hace un SELECT numero_sillas
 * Devuelve un número entero int
 * @param idSala
 * @return
 */
    public int obtenerCapacidadSala(int idSala) {
        int capacidad = 0;
        String query = "SELECT numero_sillas FROM sala WHERE id_sala = " + idSala;
        try {
            Statement orden = conexion.createStatement();
            ResultSet datos = orden.executeQuery(query);
            if (datos.next()) {
                capacidad = datos.getInt("numero_sillas");
            }
            orden.close();
        } catch (SQLException e) {
            System.out.println("Error al leer capacidad sala");
        }
        return capacidad;
    }

    /**
     * LISTADOS:
     * obtenerPelículas()
     * obtenerSalas()
     * obtenerSesiones()
     * los 3 métodos funcionan igual, le piden a la BD que muestre todo el listado
     * de listado, salas o sesiones y nos devuelva en forma de listas de ArrayList para que
     * podamos usarlo en el programa
     * primero crea un ArrayList vacío
     * Lanza un SELECT * FROM...
     * Usa un bucle while (datos.next()) : Mientras queden filas en la BD
     * Luego crea un objeto (Pelicula/Sala/Sesion), lo rellena con los datos de esa fila
     * (datos.getInt, datos.getString) y lo añade a la lista.
     * 
     * @return
     */

    public ArrayList<Pelicula> obtenerPeliculas() {
        ArrayList<Pelicula> listaPeliculas = new ArrayList<Pelicula>();
        String query = "SELECT * FROM pelicula"; 
        try {
            Statement orden = conexion.createStatement();
            ResultSet datos = orden.executeQuery(query);
            while (datos.next()) {
                Pelicula p = new Pelicula(
                    datos.getInt("id_pelicula"), 
                    datos.getString("titulo"),
                    datos.getInt("duracion"),
                    datos.getString("genero"),
                    datos.getDouble("precio_base")
                );
                listaPeliculas.add(p);
            }
            orden.close();
        } catch (SQLException e) {
            System.out.println("Error al leer peliculas");
        }
        return listaPeliculas;
    }

    public ArrayList<Sala> obtenerSalas() {
        ArrayList<Sala> listaSalas = new ArrayList<Sala>();
        String query = "SELECT * FROM sala";
        try {
            Statement orden = conexion.createStatement();
            ResultSet datos = orden.executeQuery(query);
            while (datos.next()) {
                Sala s = new Sala(
                    datos.getInt("id_sala"),
                    datos.getString("nombre"),
                    datos.getInt("numero_sillas")
                );
                listaSalas.add(s);
            }
            orden.close();
        } catch (SQLException e) {
            System.out.println("Error al leer salas");
        }
        return listaSalas;
    }

    public ArrayList<Sesion> obtenerSesiones() {
        ArrayList<Sesion> listaSesiones = new ArrayList<Sesion>();
        String query = "SELECT * FROM sesion";
        try {
            Statement orden = conexion.createStatement();
            ResultSet datos = orden.executeQuery(query);
            while (datos.next()) {
                Sesion s = new Sesion(
                    datos.getInt("id_sesion"),
                    datos.getDate("fecha"),
                    datos.getTime("hora_inicio"),
                    datos.getTime("hora_fin"),
                    datos.getInt("id_sala"),
                    datos.getInt("id_pelicula"),
                    datos.getInt("espectadores"),
                    datos.getDouble("precio_sesion")
                );
                listaSesiones.add(s);
            }
            orden.close();
        } catch (SQLException e) {
            System.out.println("Error al leer sesiones");
        }
        return listaSesiones;
    }
}