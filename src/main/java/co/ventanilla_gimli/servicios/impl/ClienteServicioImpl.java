package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.ClienteDTO.ModificarClienteDTO;
import co.ventanilla_gimli.dto.FiltroBusquedaDTO;
import co.ventanilla_gimli.dto.ItemProductoDTO;
import co.ventanilla_gimli.dto.ClienteDTO.RegistroClienteDTO;
import co.ventanilla_gimli.dto.RegistroCompraClienteDTO;
import co.ventanilla_gimli.model.*;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.ClienteServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServicioImpl implements ClienteServicio{
    private  final ClienteRepo clienteRepo;
    private  final EmpleadoRepo empleadoRepo;
    private  final AdministradorRepo administradorRepo;
    private final ProductoRepo productoRepo;
    private final VentaRepo ventaRepo;

    @Override
    public int registrarCliente(RegistroClienteDTO registroClienteDTO) throws Exception {

        if(correoRepetido(registroClienteDTO.correo())){
            throw  new Exception("El correo digitado ya se encuentra en uso");
        }

        Cliente cliente = new Cliente();
        cliente.setEstado(true);
        cliente.setNombre(registroClienteDTO.nombre());
        cliente.setTelefono(registroClienteDTO.telefono());
        cliente.setDireccion(registroClienteDTO.direccion());
        cliente.setCorreo(registroClienteDTO.correo());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(registroClienteDTO.password());

        cliente.setPassword(passwordEncriptada);

        clienteRepo.save(cliente);

        return cliente.getCodigo();
    }

    @Override
    public int modificarCliente(ModificarClienteDTO modificarClienteDTO) throws Exception {

        Optional<Cliente> clienteEncontrado = clienteRepo.findById(modificarClienteDTO.codigoCliente());

        if(clienteEncontrado.isEmpty()){
            throw new Exception("El cliente no existe");
        }

        Cliente cliente = clienteEncontrado.get();

        cliente.setNombre(modificarClienteDTO.nombre());
        cliente.setTelefono(modificarClienteDTO.telefono());
        cliente.setDireccion(modificarClienteDTO.direccion());
        cliente.setCorreo(modificarClienteDTO.correo());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(modificarClienteDTO.password());

        cliente.setPassword(passwordEncriptada);

        clienteRepo.save(cliente);

        return cliente.getCodigo();
    }

    @Override
    public boolean eliminarCuenta(int codigoCliente) throws Exception {

       /* Optional<Cliente> clienteEncontrado = clienteRepo.findById(codigoCliente);

        Cliente cliente = clienteEncontrado.get();
       // cliente.setEstado(false);
        clienteRepo.delete(cliente);

       // clienteRepo.save(cliente);

        return true;*/

        Optional<Cliente> clienteEncontrado = clienteRepo.findById(codigoCliente);
        Cliente cliente = clienteEncontrado.orElseThrow(() -> new Exception("Cliente no encontrado"));

        // Desvincular todas las ventas asociadas al cliente estableciendo cliente_codigo en NULL
        ventaRepo.desvincularVentasDelCliente(cliente.getCodigo());

        // Finalmente, eliminar el cliente
        clienteRepo.delete(cliente);

        return true;
    }

    @Override
    @Transactional
    public List<ItemProductoDTO> listarProductos() {

        List<Producto> productos = productoRepo.findAll();
        List<ItemProductoDTO> productoAREtornar = new ArrayList<>();

        for (Producto producto : productos) {
            for (String nombre : producto.getNombresAlcohol()) {
                if(producto.getCantidad() >= 1) {
                    productoAREtornar.add(new ItemProductoDTO(
                            producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor()
                    ));
                }
            }
            for (String nombre : producto.getNombresDulces()) {
                if (producto.getCantidad() >= 1){
                    productoAREtornar.add(new ItemProductoDTO(
                            producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor()
                    ));
                }
            }
            for (String nombre : producto.getNombresGaseosas()) {
                if(producto.getCantidad() >= 1) {
                    productoAREtornar.add(new ItemProductoDTO(
                            producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor()
                    ));
                }
            }
        }

        return productoAREtornar;
    }

    @Override
    @Transactional
    public FiltroBusquedaDTO filtrarProductoPorNombre(String nombreProducto) {

       List<Producto> productos = productoRepo.findAll();

        for (Producto producto : productos) {
            for (String nombre : producto.getNombresAlcohol()) {
                if (nombre.equals(nombreProducto)) {
                    // Si encontramos una coincidencia, retornamos el producto
                    return new FiltroBusquedaDTO(producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor());
                }
            }
        }
        for (Producto producto : productos) {
            for (String nombre : producto.getNombresDulces()) {
                if (nombre.equals(nombreProducto)) {
                    // Si encontramos una coincidencia, retornamos el producto
                    return new FiltroBusquedaDTO(producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor());
                }
            }
        }
        for (Producto producto : productos) {
            for (String nombre : producto.getNombresGaseosas()) {
                if (nombre.equals(nombreProducto)) {
                    // Si encontramos una coincidencia, retornamos el producto
                    return new FiltroBusquedaDTO(producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor());
                }
            }
        }

        return null;
    }

    @Override
    public int realizarCompra(RegistroCompraClienteDTO registroCompraClienteDTO) {

        Optional<Cliente> clienteEncontrado = clienteRepo.findById(registroCompraClienteDTO.codigoCliente());
        //Optional<Empleado> empleadoEncontado = empleadoRepo.findById(registroCompraClienteDTO.codigoEmpleado());
        Optional<Producto> productoEncontrado = productoRepo.findById(registroCompraClienteDTO.codigoProducto());



        Administrador admin =  new Administrador();
        admin.setCodigo(100);

        Venta venta = new Venta();
        venta.setProducto(productoEncontrado.get());
        venta.setCliente(clienteEncontrado.get());
        venta.setEmpleado(null);
        venta.setCantidad(registroCompraClienteDTO.cantidad());
        venta.setPrecioUnitario(productoEncontrado.get().getPrecio());

        // Establecer la fecha actual
        LocalDate fechaActual = LocalDate.now();
        venta.setFechaVenta(fechaActual);

        // Establecer la hora actual en formato de cadena (String)
        LocalTime horaActual = LocalTime.now();
        String horaActualString = horaActual.toString(); // Convertir a formato de cadena
        venta.setHoraDeVenta(horaActualString);

        ventaRepo.save(venta);


        return venta.getCodigo();
    }

    private boolean correoRepetido(String correo) {

        Cliente correoCliente = clienteRepo.findClienteByCorreo(correo);
        Empleado correoEmpleado = empleadoRepo.findByCorreo(correo);
        Administrador correoAdministrador = administradorRepo.findByCorreo(correo);

        if(correoCliente != null){
            return true;
        }
        if(correoEmpleado != null){
            return true;
        }
        if(correoAdministrador != null){
            return true;
        }

        return false;
    }
}
