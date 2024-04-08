package co.ventanilla_gimli.repositorios;

import co.ventanilla_gimli.model.Cliente;
import co.ventanilla_gimli.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


public interface VentaRepo extends JpaRepository<Venta, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Venta v SET v.cliente.codigo = NULL WHERE v.cliente.codigo = ?1")
    void desvincularVentasDelCliente(int codigoCliente); // Cambiar Cliente por el tipo de dato correcto para el campo codigo del cliente
}
