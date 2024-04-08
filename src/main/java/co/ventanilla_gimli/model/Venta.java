package co.ventanilla_gimli.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Venta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    @Column(nullable = false)
    private LocalDate fechaVenta;
    @Column(nullable = false)
    private String horaDeVenta;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private double precioUnitario;
    //______________________________________________________________________________________

    //__________________________________ FK ________________________________________________

    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private Empleado empleado;
    @OneToOne
    private  Producto producto;

    //private DetalleVenta detalleVenta;
}
