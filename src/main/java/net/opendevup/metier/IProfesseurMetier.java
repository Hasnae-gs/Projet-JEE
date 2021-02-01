package net.opendevup.metier;

import org.springframework.data.domain.Page;

import net.opendevup.entities.Etudiant;
import net.opendevup.entities.Formation;
import net.opendevup.entities.Publication;

public interface IProfesseurMetier {
	public Etudiant consulterEtudiant(Long id);
	
	public Page<Formation> listFormation(String intitule, int page, int size);
	
	public Page<Publication> listPublication(int page, int size);


}
