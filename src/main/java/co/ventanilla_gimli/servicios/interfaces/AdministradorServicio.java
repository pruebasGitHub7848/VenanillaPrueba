package co.ventanilla_gimli.servicios.interfaces;

import co.ventanilla_gimli.dto.RegistroEmpleadoDTO;

public interface AdministradorServicio {

    int registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO)throws Exception;
}
