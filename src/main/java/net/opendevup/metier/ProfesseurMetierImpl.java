package net.opendevup.metier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.opendevup.dao.EtudiantRepository;
import net.opendevup.dao.FormationRepository;
import net.opendevup.dao.PublicationRepository;
import net.opendevup.entities.Etudiant;
import net.opendevup.entities.Formation;
import net.opendevup.entities.Publication;

@Service
@Transactional //toute les methodes sont transactionnels
public class ProfesseurMetierImpl implements IProfesseurMetier{
	@SuppressWarnings("unused")
	@Autowired
	private EtudiantRepository etudiantRepository; 
	private FormationRepository formationRepository;
	private PublicationRepository publicationRepository;
	
	@Override
	public Etudiant consulterEtudiant(Long id) {
	
		return null;
	}

	@Override
	public Page<Formation> listFormation(String intitule, int page, int size) {
		// TODO Auto-generated method stub
		return formationRepository.listFormation(intitule, PageRequest.of(page, size));
	}

	@Override
	public Page<Publication> listPublication(int page, int size) {
		// TODO Auto-generated method stub
		return publicationRepository.listPublication(PageRequest.of(page, size));
	}

	
	

}
