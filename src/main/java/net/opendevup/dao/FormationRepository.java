package net.opendevup.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.opendevup.entities.Etudiant;
import net.opendevup.entities.Formation;

public interface FormationRepository extends JpaRepository<Formation, Long> {
	
	@Query("select f from Formation f where f.intitule like :x")
	public Page<Formation> listFormation(@Param("x") String mc, Pageable pageable);
	
	public List<Formation> findByIntitule(String n);
	public Page<Formation> findByIntitule(String n, Pageable pageable);
	

}
