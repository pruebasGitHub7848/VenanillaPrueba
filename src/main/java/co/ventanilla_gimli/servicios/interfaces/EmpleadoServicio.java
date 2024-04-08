package co.ventanilla_gimli.servicios.interfaces;

import co.ventanilla_gimli.dto.AgregarProductoDTO;
import co.ventanilla_gimli.dto.ItemRegistroProductoDTO;
import co.ventanilla_gimli.dto.DetalleRegistroProductoDTO;

import java.util.List;

public interface EmpleadoServicio {

    int encontrarClientePorCorreo(String correo)throws Exception;
    int agregarProducto(AgregarProductoDTO agregarProductoDTO) throws Exception;
    List<ItemRegistroProductoDTO> listarRegistroProductos();
    DetalleRegistroProductoDTO verDetalleRegistro(int codigoRegistro) throws Exception;

}
