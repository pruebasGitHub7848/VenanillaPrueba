package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.RegistroEmpleadoDTO;
import co.ventanilla_gimli.model.Administrador;
import co.ventanilla_gimli.model.Cliente;
import co.ventanilla_gimli.model.Empleado;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.AdministradorServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdministradorServicioImpl implements AdministradorServicio {

    private  final ClienteRepo clienteRepo;
    private  final EmpleadoRepo empleadoRepo;
    private  final AdministradorRepo administradorRepo;
    private final ProductoRepo productoRepo;
    private final VentaRepo ventaRepo;

    @Override
    public int registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO) throws Exception {

        if(correoRepetido(registroEmpleadoDTO.correo())){
            throw  new Exception("El correo digitado ya se encuentra en uso");
        }

        Empleado empleado = new Empleado();
        empleado.setEstado(true);
        empleado.setNombre(registroEmpleadoDTO.nombre());
        empleado.setTelefono(registroEmpleadoDTO.telefono());
        empleado.setCorreo(registroEmpleadoDTO.correo());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(registroEmpleadoDTO.password());

        empleado.setPassword(passwordEncriptada);

        empleadoRepo.save(empleado);

        return empleado.getCodigo();
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
