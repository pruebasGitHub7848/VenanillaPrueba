package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.AgregarProductoDTO;
import co.ventanilla_gimli.dto.ItemRegistroProductoDTO;
import co.ventanilla_gimli.dto.DetalleRegistroProductoDTO;
import co.ventanilla_gimli.model.*;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.EmpleadoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpleadoServicioImpl implements EmpleadoServicio {

    private final ClienteRepo clienteRepo;
    private final ProductoRepo productoRepo;
    private final RegistroProductosRepo registroProductosRepo;
    private final EmpleadoRepo empleadoRepo;
    private final AdministradorRepo administradorRepo;

    @Override
    public int encontrarClientePorCorreo(String correo) throws Exception {
        Cliente clienteEncontrado = clienteRepo.findClienteByCorreo(correo);
        if (clienteEncontrado != null) {
            return clienteEncontrado.getCodigo();
        } else {
            throw new Exception("No se encontró ningún cliente con el correo proporcionado");
        }
    }


    @Override
    public int agregarProducto(AgregarProductoDTO agregarProductoDTO) throws Exception {

        Categoria categoria = agregarProductoDTO.categoria();
        if (categoria != null) {
            if (categoria.equals(Categoria.ALCOHOL)) {
                Producto productoEncontrado = productoRepo.findByNombresAlcohol(agregarProductoDTO.nombre());

                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                // Guardar el producto actualizado en la base de datos
                productoRepo.save(productoEncontrado);
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.codigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());

            } else if (categoria.equals(Categoria.DULCES)) {
                Producto productoEncontrado = productoRepo.findByNombresDulces(agregarProductoDTO.nombre());

                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                productoRepo.save(productoEncontrado);
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.getCodigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());

            } else {
                Producto productoEncontrado = productoRepo.findByNombresGaseosas(agregarProductoDTO.nombre());

                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                productoRepo.save(productoEncontrado);
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.codigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());
            }

        }

        return 0;
    }

        public void hacerRegistroDeAgregacion(Producto producto, int codigoEmpleado, String nombreProducto, int cantidad){

            Administrador administrador = new Administrador();
            administrador.setCodigo(100);

            Empleado empleado = new Empleado();
            empleado.setCodigo(codigoEmpleado);


            RegistroProducto r = new RegistroProducto();
            r.setProducto(producto);
            r.setEmpleado(empleado);
            r.setCategoria(producto.getCategoria());
            r.setSubcategoria(producto.getSubcategoria());
            r.setNombreProducto(nombreProducto);
            r.setCantidad(cantidad);



            // Establecer la fecha actual
            LocalDate fechaActual = LocalDate.now();
            r.setFechaRegistro(fechaActual);

            // Establecer la hora actual en formato de cadena (String)
            LocalTime horaActual = LocalTime.now();
            String horaActualString = horaActual.toString(); // Convertir a formato de cadena
            r.setHoraDeRegistro(horaActualString);


            registroProductosRepo.save(r);



    }


    @Override
    public List<ItemRegistroProductoDTO> listarRegistroProductos() {

        List<RegistroProducto> registrosEncontrados = registroProductosRepo.findAll();
        List<ItemRegistroProductoDTO> registros = new ArrayList<>();

        for(RegistroProducto r : registrosEncontrados){
            registros.add(new ItemRegistroProductoDTO(

                    r.getCodigo(),
                    r.getNombreProducto(),
                    r.getCategoria(),
                    r.getEmpleado().getCodigo(),
                    r.getFechaRegistro()
            ));
        }

        return registros;
    }

    @Override
    public DetalleRegistroProductoDTO verDetalleRegistro(int codigoRegistro) throws Exception {

        Optional<RegistroProducto> registroEncontrado = registroProductosRepo.findById(codigoRegistro);
        Optional<Empleado> empleado = empleadoRepo.findById(registroEncontrado.get().getEmpleado().getCodigo());

        if(registroEncontrado.isEmpty()){
            throw new Exception("No se pudo encontrar el registro");
        }

        RegistroProducto registro = registroEncontrado.get();

        String nombreEmpleado = empleado.get().getNombre();

        return new DetalleRegistroProductoDTO(
                registro.getProducto().getCodigo(),
                registro.getNombreProducto(),
                registro.getCantidad(),
                registro.getCategoria(),
                registro.getSubcategoria(),
                registro.getFechaRegistro(),
                registro.getHoraDeRegistro(),

                registro.getEmpleado().getCodigo(),
                nombreEmpleado
        );
    }
}
