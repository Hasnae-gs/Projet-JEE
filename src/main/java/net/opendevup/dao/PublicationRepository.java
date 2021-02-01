package net.opendevup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.opendevup.entities.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long>{

	@Query("select f from Publication f")
	public Page<Publication> listPublication(Pageable pageable);
	

}
