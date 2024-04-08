package co.ventanilla_gimli.controladores;

import co.ventanilla_gimli.dto.RegistroEmpleadoDTO;
import co.ventanilla_gimli.dto.TokenDTO.MensajeDTO;
import co.ventanilla_gimli.servicios.interfaces.AdministradorServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorServicio administradorServicio;

    @PostMapping("/registrar-empleado")
    public ResponseEntity<MensajeDTO<String>> registrarEmpleado(@Valid @RequestBody RegistroEmpleadoDTO empleado) throws Exception {
        administradorServicio.registrarEmpleado(empleado);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Empleado registrado correctamente"));
    }

}
