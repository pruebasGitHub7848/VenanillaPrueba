package co.ventanilla_gimli.servicios.interfaces;

import co.ventanilla_gimli.dto.ClienteDTO.ModificarClienteDTO;
import co.ventanilla_gimli.dto.FiltroBusquedaDTO;
import co.ventanilla_gimli.dto.ItemProductoDTO;
import co.ventanilla_gimli.dto.ClienteDTO.RegistroClienteDTO;
import co.ventanilla_gimli.dto.RegistroCompraClienteDTO;

import java.util.List;

public interface ClienteServicio {
    int registrarCliente(RegistroClienteDTO registroClienteDTO) throws Exception;
    int modificarCliente(ModificarClienteDTO modificarClienteDTO) throws Exception;
    boolean eliminarCuenta(int codigoCliente)throws Exception;
    List<ItemProductoDTO> listarProductos();
    FiltroBusquedaDTO filtrarProductoPorNombre(String nombreProducto);
    int realizarCompra(RegistroCompraClienteDTO registroCompraClienteDTO);
}
