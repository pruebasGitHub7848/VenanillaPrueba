package co.ventanilla_gimli.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Empleado extends Usuario implements Serializable {

    @OneToMany(mappedBy = "empleado")
    private List<Venta> ventas;
    @OneToMany(mappedBy = "empleado")
    private List<RegistroProducto> registroProductos;
}
