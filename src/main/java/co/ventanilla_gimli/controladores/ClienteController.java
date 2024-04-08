package co.ventanilla_gimli.controladores;

import co.ventanilla_gimli.dto.FiltroBusquedaDTO;
import co.ventanilla_gimli.dto.ItemProductoDTO;
import co.ventanilla_gimli.dto.ItemRegistroProductoDTO;
import co.ventanilla_gimli.dto.TokenDTO.MensajeDTO;
import co.ventanilla_gimli.servicios.interfaces.ClienteServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteServicio clienteServicio;

    @GetMapping("/listar-productos")
    public ResponseEntity<MensajeDTO<List<ItemProductoDTO>>> listarProductos() throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                clienteServicio.listarProductos()));
    }

    @GetMapping("/filtar-productos-nombre/{nombreProducto}")
    public ResponseEntity<MensajeDTO<FiltroBusquedaDTO>> filtrarProductoPorNombre(@PathVariable String nombreProducto) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                clienteServicio.filtrarProductoPorNombre(nombreProducto)));
    }
}
