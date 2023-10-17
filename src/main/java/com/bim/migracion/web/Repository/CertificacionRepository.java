package com.bim.migracion.web.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bim.migracion.web.Entity.CertificacionEntity;



@Repository
public interface CertificacionRepository extends JpaRepository<CertificacionEntity, Integer>{

	List<CertificacionEntity> findAllById(int i);
	
	
	@Query(value="SELECT * FROM certificacion\r\n"
			+ "INNER JOIN certificadoact ON certificacion.id= certificadoact.id\r\n"
			+ "INNER JOIN actividad ON actividad.Id_Escenario=certificadoact.id_escenario \r\n"
			+ "WHERE certificadoact.id=11 AND actividad.status=1",nativeQuery = true)
	List<CertificacionEntity> buscarCertificacionStatus();
	
	@Query(value="SELECT MAX(id) FROM certificacion",nativeQuery = true)
	public int buscarCertificacion();
	
	@Transactional
	@Modifying 
	@Query(value="UPDATE certificadoact SET status=1 WHERE id_escenario=:PruID",nativeQuery = true)
	public void updateAct(@Param("PruID") int id);
	
	@Query(value= "SELECT * FROM certificacion WHERE id!=:id", nativeQuery = true)
	public List<CertificacionEntity> listaCert(@Param("id") int id);
	

	
	
	//List<CertificacionEntity> findAllById(long l);

}
