package com.bim.migracion.web.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bim.migracion.web.Entity.DatasourceEntity;

public interface DatasourceRepository extends JpaRepository<DatasourceEntity, Integer>{

}
