package com.bim.migracion.web.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.bim.migracion.web.Entity.EscenariosEntity;

@Repository
public interface EscenariosRepository extends JpaRepository<EscenariosEntity, Integer>{
	@Query(value="SELECT * FROM envios INNER JOIN enviosbd  ON envios.id = enviosbd.id_env WHERE envios.id = :PruID",nativeQuery = true)
	public EscenariosEntity datosEnvio(@Param("PruID") int id);
	
	@Query(value="SELECT id_Firma FROM envios INNER JOIN enviosbd  ON envios.id = enviosbd.id_env WHERE enviosbd.id_base = :PruID",nativeQuery = true)
	public int idFirmaDest(@Param("PruID") int id);
}
