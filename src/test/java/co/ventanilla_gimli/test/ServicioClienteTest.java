package co.ventanilla_gimli.test;

import co.ventanilla_gimli.dto.ClienteDTO.ModificarClienteDTO;
import co.ventanilla_gimli.dto.FiltroBusquedaDTO;
import co.ventanilla_gimli.dto.ItemProductoDTO;
import co.ventanilla_gimli.dto.RegistroCompraClienteDTO;
import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.servicios.interfaces.ClienteServicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ServicioClienteTest {

    @Autowired
    private ClienteServicio clienteServicio;

    @Test
    public void listarNombresAlcoholes() throws Exception {

        List<ItemProductoDTO> nombresAlcoholes = clienteServicio.listarProductos();

        nombresAlcoholes.forEach(System.out::println);

        Assertions.assertEquals(1, +nombresAlcoholes.size());
    }

    @Test
    public void listarNombresPorNombre() throws Exception {

        FiltroBusquedaDTO producto = clienteServicio.filtrarProductoPorNombre("Tecate");

        System.out.println("\n" + "\n" + producto.toString());
        Assertions.assertNotEquals(0, producto);
    }

    @Test
    public  void registrarCompra(){

        RegistroCompraClienteDTO registroCompra = new RegistroCompraClienteDTO(
             5,
             1,
             102
        );

        int nuevo =  clienteServicio.realizarCompra(registroCompra);

        Assertions.assertNotEquals(0, nuevo);
    }

    @Test
    public  void modificarDatos() throws Exception {

        ModificarClienteDTO clienteDTO = new ModificarClienteDTO(
                12,
                "Mari",
                "23232333",
                "Calle 15",
                "marianauv@gmail.com",
                "1234"
        );

        int nuevo =  clienteServicio.modificarCliente(clienteDTO);

        Assertions.assertNotEquals(0, nuevo);
    }

    @Test
    public void eliminarTest() throws Exception {

        //clienteServicio.eliminarCuenta(16);

        Assertions.assertTrue(clienteServicio.eliminarCuenta(12));

    }
}
