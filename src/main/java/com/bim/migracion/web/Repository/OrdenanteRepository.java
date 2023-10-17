package com.bim.migracion.web.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bim.migracion.web.Entity.OrdenanteEntity;



@Repository
public interface OrdenanteRepository extends JpaRepository<OrdenanteEntity, Integer> {

}
