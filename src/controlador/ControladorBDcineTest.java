package controlador;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import modelo.Cliente;
import modelo.Pelicula;
import modelo.Sala;
import modelo.Sesion;
/**
 * Esta Clase voy a probar todos los métodos de
 * la clase ControladorBDcine.
 * para probar cada método voy a usar el patrón AAA(Arrange,Act,Assert)
 * 0 crear los objetos, conectar a la base de datos,
 * definir las variables.
 * 1 ejecutar el método
 * 2 comprobar si el resultado es el que esperaba,
 * 
 */
/**
 * CHULETA PARA MI:
 * assertEquals(esperado, actual): El mejor y más usado. Compara dos valores.

Ejemplo: assertEquals(10, lista.size()); (Si falla, te dice: "Esperaba 10 pero llegó 8").

assertTrue(condicion) / assertFalse(condicion): Para verificar booleanos.

Ejemplo: assertTrue(controlador.conectar());

assertNotNull(objeto): Verifica que algo no sea nulo.

fail("mensaje"): Hace fallar el test manualmente. Se usa poco si usas bien los anteriores, pero es útil en bloques try catch o if else complejos.
 */
/**
 * ESTRATEGIA: Para recordarme a mi misma los casos de prueba y cobertura
 * 1 probar que todo va bien.
 * 2 probar los limites del sistema
 * 3 buscar casos de error las excepciones
 */
public class ControladorBDcineTest {
	private ControladorBDcine controlador;
/**
 * Before se ejecuta antes de cada test
 * sirve para inicializar objetos y evita repetir código
 * @throws Exception
 */
	@Before
	public void setUp() throws Exception {
		controlador = new ControladorBDcine("cine_elorrieta");	
	}
/**
 * After de ejecuta después de cada test sirve para 
 * cerrar conexiones, limpia la basura que dejó el test
 * @throws Exception
 */
	@After
	public void tearDown() throws Exception {
	}
/**
 * PRUEBAS DE CONEXIÓN:
 */

	@Test
	public void testIniciarConexionValida() {
		//Act
		controlador = new ControladorBDcine("cine_elorrieta");

		boolean resultado= controlador.iniciarConexion();
				//Assert
				assertTrue("Debería conectar si la Base de Datos existe y si XAAMP está encendido", resultado);
	}
	@Test
    public void testIniciarConexionNoValida() {
        // Arrange: Creamos un controlador apuntando a una BD inventada
        ControladorBDcine controladorFalso = new ControladorBDcine("BaseDeDatosInventada_12345");
        
        // Act
        boolean resultado = controladorFalso.iniciarConexion();
        //Assert
        assertFalse("No debería conectar a una Base de Datos inexistente",resultado);
        
	}
	
	
	@Test
    public void testCerrarConexion() {
        // Arrange
		controlador = new ControladorBDcine("cine_elorrieta");

        controlador.iniciarConexion();
        // Act & Assert
        assertTrue("Debería devolver true al cerrar una conexión abierta", controlador.cerrarConexion());
    }

	@Test
	public void testExisteCliente() {
	    // Arrange
		controlador = new ControladorBDcine("cine_elorrieta");
		controlador.iniciarConexion();
	    Cliente cliente = new Cliente("00000000A", "Silvia", "Ochoa", "silvia@example.com", "1234");
	    controlador.registrarCliente(cliente);

	    // Act
	    boolean resultado = controlador.existeCliente("00000000A");

	    // Assert
	    assertTrue("El cliente debería existir", resultado);
	}


//	@Test
//	public void testRegistrarClienteNuevo() {
//		controlador = new ControladorBDcine("cine_elorrieta");
//
//	controlador.iniciarConexion();
//
//	   Cliente c = new Cliente("00000000A", "TestNombre", "TestApellido", "test@email.com", "1234");
//
//	    boolean resultado = controlador.registrarCliente(c);
//
//	    assertTrue("Debería dejar registrar un cliente nuevo", resultado);
//	}


//	@Test
//	public void testLogin() {
//		controlador.iniciarConexion();
//		controlador = new ControladorBDcine("cine_elorrieta");
//
//
//	//Arrange: me aseguro de que el usuario existe en la BD
//	String dni = "00000000A";
//	String pass = "secreto";
//	Cliente c = new Cliente(dni ,"Silvia","Login","silvia@gmail.com" ,pass);
//	controlador.registrarCliente(c);
//	controlador.registrarCliente(c);
//	//Act
//	Cliente logueado = controlador.login(dni, pass);
//	//Assert
//	assertNotNull("El login correcto debe devolver",logueado);
//	assertEquals("El DNI del objeto devuelto debe coincidir",dni, logueado.getDni());
//	
//	}
	@Test
	public void loguinIncorrecto() {
		controlador = new ControladorBDcine("cine_elorrieta");

	controlador.iniciarConexion();
	//Act
	Cliente logueado = controlador.login("00000000A"," ContraseñaIncorrecta");
	//Assert
	assertNull("El login incorrecto debe devolver null", logueado);
	
	}
/**
 * PRUEBAS DE COMPRAS DE ENTRADAS:
 */
	@Test
	public void testGuardarCompra() {
		controlador = new ControladorBDcine("cine_elorrieta");

	controlador.iniciarConexion();
	//arrange Voy a usar un DNI que existe
	String dni= "00000000A";
	//Act
			int idCompra = controlador.guardarCompra(dni, 0, 0);
	//Asert  como mi BD tiene autoincrement el ID será mayor que 0
			assertTrue("Se esperaba un ID generado > 0", idCompra>0);
	
	}

//	@Test
//	public void testGuardarEntrada() {
//		fail("Not yet implemented");
//	}

	@Test
    public void testActualizarAforo() {
		controlador = new ControladorBDcine("cine_elorrieta");

        controlador.iniciarConexion();
        // que no explote (Exception)
        try {
            controlador.actualizarAforo(1, 2); // Sala 1, sumar 2 personas
        } catch (Exception e) {
            fail("El método actualizarAforo lanzó una excepción inesperada: " + e.getMessage());
}

	}

	@Test
	public void testObtenerCapacidadSala() {
		controlador = new ControladorBDcine("cine_elorrieta");

	controlador.iniciarConexion();
	//Act
	int capacidad= controlador.obtenerCapacidadSala(1) ;//Asumo que la sala 1 existe
	//Assert
	assertTrue("La sala 1 debería tener capacidad para mas de una persona" ,capacidad >0);
	
	}

	@Test
	public void testObtenerPeliculas() {
		controlador = new ControladorBDcine("cine_elorrieta");

		controlador.iniciarConexion();
		ArrayList<Pelicula>lista = controlador.obtenerPeliculas();
		assertNotNull("la lista de películas nunca debe ser null", lista);
		
		}

	@Test
	public void testObtenerSalas() {
		controlador = new ControladorBDcine("cine_elorrieta");

		controlador.iniciarConexion();
		ArrayList<Sala>lista= controlador.obtenerSalas();
		assertNotNull(lista);
		
	}

	@Test
	public void testObtenerSesiones() {
		controlador = new ControladorBDcine("cine_elorrieta");

	controlador.iniciarConexion();
	ArrayList<Sesion>lista = controlador.obtenerSesiones();
	assertNotNull("La lista de sesiones no debe dar null",lista);
	}



}
