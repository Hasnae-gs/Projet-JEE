package net.opendevup.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.opendevup.dao.EtudiantRepository;
import net.opendevup.dao.FormationRepository;
import net.opendevup.dao.PublicationRepository;
import net.opendevup.entities.Etudiant;
import net.opendevup.entities.Formation;
import net.opendevup.entities.Publication;
import net.opendevup.metier.IProfesseurMetier;

@Controller
public class ProfesseurController {
	@SuppressWarnings("unused")
	private IProfesseurMetier professeurMetier;
	@Autowired
	private EtudiantRepository etudiantRepository;
	@Autowired
	private FormationRepository formationRepository;
	@Autowired
	private PublicationRepository publicationRepository;
	@Value("${dir.images}") // pour injecter une propriete qui se trouve dans le fichier
							// application.propertie
	private String imageDir;

	// @Value("$(dir.dossiers)")
	// private String dossierDir;

	@RequestMapping(value = "/home")
	public String home() {
		return "home";
	}
	
	
	
	
	
	@RequestMapping("/Index")
	public String Index(Model model, @RequestParam(name = "page", defaultValue = "0") int p,
			@RequestParam(name = "motCle", defaultValue = "") String mc) {
		Pageable paging = PageRequest.of(p, 5);
		Page<Etudiant> pageEtudiants = etudiantRepository.chercherEtudiants("%" + mc + "%", paging);

		int pagesCount = pageEtudiants.getTotalPages();
		int[] pages = new int[pagesCount];
		for (int i = 0; i < pagesCount; i++)
			pages[i] = i;

		model.addAttribute("motCle", mc);
		model.addAttribute("pageCourant", p);
		model.addAttribute("pages", pages);
		// Stocker la liste des etudiants dans le model
		model.addAttribute("pageEtudiants", pageEtudiants.getContent());
		return "etudiants"; // return le nom de la vue
	}

	/* On a utilise ici la methode get pour recuperer le formulaire */
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String formEtudiant(Model model) {
		model.addAttribute("etudiant", new Etudiant());

		return "formEtudiant";
	}

	/*
	 * @valid pour dire qu'une fois que les donnees sont stockes dans l'objet
	 * etudiants tu dois valider => validation de l'entite
	 */
	/* Bnding result pour stocker les messages d'erreur */
	/* multipartfile pour uploder des fichier */
	@RequestMapping(value = "/SaveEtudiant", method = RequestMethod.POST)
	public String saveEtudiant(@Valid Etudiant etudiant, BindingResult bindingResult,
			@RequestParam(name = "picture") MultipartFile file, RedirectAttributes ra) throws Exception {

		/* test pour s'assurer de la validation de l'etudiant : */
		if (bindingResult.hasErrors()) {
			return "formEtudiant";
		}
		if (!(file.isEmpty())) {
			etudiant.setPhoto(file.getOriginalFilename());
		}
		etudiantRepository.save(etudiant);

		/* Pour uploader un fichier */
		if (!(file.isEmpty())) {
			etudiant.setPhoto(file
					.getOriginalFilename()); /* getOriginalfilename permet de retourner le nom original de la photo */
			file.transferTo(new File(imageDir + etudiant.getId()));
		}
		ra.addFlashAttribute("messageAjouter", "Etudiant ajouté avec succés.") ;
		return "redirect:Index";
	}

	/* Methode pour retourner la photo */
	@RequestMapping(value = "/getPhoto", produces = MediaType.ALL_VALUE)
	@ResponseBody
	public byte[] getPhoto(Long id) throws Exception {
		File f = new File(imageDir + id);
		/* IOUtils de apache.commans.io */
		return IOUtils.toByteArray(new FileInputStream(f));

	}

	@RequestMapping(value = "/supprimer")
	public String supprimer(Long id) {
		etudiantRepository.deleteById(id);
		return "redirect:Index";
	}

	@RequestMapping(value="/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Etudiant etudiant = etudiantRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		model.addAttribute("etudiant", etudiant);
		return "EditEtudiant";
	}

	
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)

	public String Update( @Valid Etudiant etudiant, BindingResult bindingResult,
			@RequestParam(name = "picture") MultipartFile file, RedirectAttributes ra) throws Exception {

		/* test pour s'assurer de la validation de l'etudiant : */
		if (bindingResult.hasErrors()) {
		
			return "EditFormation";
		}
		if (!(file.isEmpty())) {
			etudiant.setPhoto(file.getOriginalFilename());
		}
		etudiantRepository.save(etudiant);

		/* Pour uploader un fichier */
		if (!(file.isEmpty())) {
			etudiant.setPhoto(file
					.getOriginalFilename()); /* getOriginalfilename permet de retourner le nom original de la photo */
			file.transferTo(new File(imageDir + etudiant.getId()));
		}
		ra.addFlashAttribute("messageUpdate", "Etudiant modifié avec succés.") ;
		return "redirect:Index";
	}
	
	
	
	
	

	/************************* FORMATION *****************************************/
	/*
	 * @RequestMapping(value="/Cours") public String ListCours(Model model) {
	 * List<Formation> formation = formationRepository.findAll();
	 * model.addAttribute("formation", formation); return "formations"; // return le
	 * nom de la vue }
	 */

	@RequestMapping("/Cours")
	public String ListCours(Model model, @RequestParam(name = "page", defaultValue = "0") int p,
			@RequestParam(name = "motCle", defaultValue = "") String mc) {
		Pageable paging = PageRequest.of(p, 5);
		Page<Formation> pageFormation = formationRepository.listFormation("%" + mc + "%", paging);

		int pagesCount = pageFormation.getTotalPages();
		int[] pages = new int[pagesCount];
		for (int i = 0; i < pagesCount; i++)
			pages[i] = i;

		model.addAttribute("motCle", mc);
		model.addAttribute("pageCourant", p);
		model.addAttribute("pages", pages);
		// Stocker la liste des etudiants dans le model
		model.addAttribute("pageFormation", pageFormation.getContent());
		return "formations"; // return le nom de la vue
	}

	
	
	
	
	
	@RequestMapping(value = "/formationform", method = RequestMethod.GET)
	public String formFormation(Model model) {
		model.addAttribute("formation", new Formation());
		return "formFormation";
	}

	/* @valid va faire la validation de l'entite */
	@RequestMapping(value = "/saveFormation", method = RequestMethod.POST)
	public String saveF(@Valid Formation formation, BindingResult bindingResult,
			@RequestParam(name = "dossier") MultipartFile file, RedirectAttributes ra) throws Exception {

		/* test pour s'assurer de la validation de l'etudiant : */
		if (bindingResult.hasErrors()) {
			return "formFormation";
		}

		/* Pour uploader un fichier */
		if (!(file.isEmpty())) {
			formation.setFichier(file
					.getOriginalFilename()); /* getOriginalfilename permet de retourner le nom original du fichier */
			file.transferTo(new File(imageDir + file.getOriginalFilename()));
		}

		formationRepository.save(formation);

		ra.addFlashAttribute("message", "La formation a été ajouté avec succés");

		return "redirect:Cours";
	}

	@RequestMapping("/download")
	public void downloadFile(@Param("ID") Long ID, HttpServletResponse response) throws Exception {
		Optional<Formation> result = formationRepository.findById(ID);
		if (!result.isPresent()) {
			throw new Exception("Could not find document with ID : " + ID);
		}

		Formation formation = result.get();

		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachement; filename=" + formation.getFichier();

		response.setHeader(headerKey, headerValue);

		ServletOutputStream servletOutputStream = response.getOutputStream();

		// servletOutputStream.write(formation.getContenu());
		servletOutputStream.close();

	}

	@RequestMapping("/supprimerF")
	public String supprimerF(Long ID) {
		formationRepository.deleteById(ID);
		return "redirect:Cours";
	}

	/*
	 * @RequestMapping(value = "/editF/{ID}") public String editF(Model
	 * model,@PathVariable("ID") Long ID) { Formation formation =
	 * formationRepository.getOne(ID); model.addAttribute("formation", formation);
	 * return "EditFormation"; }
	 */
	
	  @RequestMapping(value="/editF/{ID}") public String
	  showUpdateFormF(@PathVariable("ID") long ID, Model model) { 
		  Formation formation= formationRepository.findById(ID) .orElseThrow(() -> new
	  IllegalArgumentException("Invalid user Id:" + ID));
	  
	  model.addAttribute("formation", formation); return "EditFormation"; }
	 

	@RequestMapping(value = "/updateF", method = RequestMethod.POST)

	public String UpdateF( @Valid Formation formation, BindingResult bindingResult,
			@RequestParam(name = "dossier") MultipartFile file) throws Exception {

		/* test pour s'assurer de la validation de l'etudiant : */
		if (bindingResult.hasErrors()) {
		
			return "EditFormation";
		}
		if (!(file.isEmpty())) {
			formation.setFichier(file.getOriginalFilename());
		}
		formationRepository.save(formation);

		/* Pour uploader un fichier */
		if (!(file.isEmpty())) {
			formation.setFichier(file
					.getOriginalFilename()); /* getOriginalfilename permet de retourner le nom original de la photo */
			file.transferTo(new File(imageDir + formation.getID()));
		}
		return "redirect:Cours";
	}

	/*
	 * @RequestMapping(value="/updateF/{id}", method=RequestMethod.POST) public
	 * String UpdateF(@PathVariable("ID") long id, @Valid Formation formation
	 * ,BindingResult bindingResult,@RequestParam(name="dossier") MultipartFile
	 * file) throws Exception {
	 * 
	 * test pour s'assurer de la validation de l'etudiant :
	 * if(bindingResult.hasErrors()) { formation.setID(id); return "EditFormation";
	 * } if(!(file.isEmpty())) { formation.setFichier(file.getOriginalFilename()); }
	 * formationRepository.save(formation);
	 * 
	 * 
	 * 
	 * Pour uploader un fichier if(!(file.isEmpty())) {
	 * formation.setFichier(file.getOriginalFilename()); getOriginalfilename permet
	 * de retourner le nom original de la photo file.transferTo(new
	 * File(imageDir+formation.getID())); }
	 * 
	 * 
	 * return "redirect:Cours"; }
	 */

	
	
	/******************************************PUBLICATION************************************************/
	
	
	@RequestMapping("/pub")
	public String ListPub(Model model, @RequestParam(name = "pagee", defaultValue = "0") int p) {
		Pageable paging = PageRequest.of(p, 5);
		Page<Publication> pagePublication = publicationRepository.listPublication(paging);

		int CountPages = pagePublication.getTotalPages();
		int[] page = new int[CountPages];
		for (int i = 0; i < CountPages; i++)
			page[i] = i;

		
		model.addAttribute("CourantPage", p);
		model.addAttribute("pages", page);
	
		model.addAttribute("pagePublication", pagePublication.getContent());
		return "publication"; // return le nom de la vue
	}
	
	
	

	@RequestMapping(value = "/formPub", method = RequestMethod.GET)
	public String formPublication(Model model) {
		model.addAttribute("publication", new Publication());
		return "FormPublication";
	}
	
	
	
	@RequestMapping(value = "/savePublication", method = RequestMethod.POST)
	public String saveF(@Valid Publication publication , BindingResult bindingResult,
			 RedirectAttributes ra) throws Exception {

		/* test pour s'assurer de la validation de l'etudiant : */
		if (bindingResult.hasErrors()) {
			return "FormPublication";
		}

		
		publicationRepository.save(publication);

		ra.addFlashAttribute("messagePA", "La Publication a été ajouté avec succés");

		return "redirect:pub";
	}
	
	
	
	@RequestMapping("/supprimerP")
	public String supprimerP(Long id, RedirectAttributes ra) {
		publicationRepository.deleteById(id);
		ra.addFlashAttribute("messageSup", "Publication bien supprimer");
		return "redirect:pub";
	}
	
	
	
	@RequestMapping(value="/editP/{id}") 
	public String  showUpdateFormP(@PathVariable("id") long id, Model model) { 
		  Publication publication= publicationRepository.findById(id) .orElseThrow(() -> new
	  IllegalArgumentException("Invalid pub Id:" + id));
	  
	  model.addAttribute("publication", publication);
	  return "EditPublication"; 
	  }
	
	
	@RequestMapping(value = "/updateP", method = RequestMethod.POST)

	public String UpdateP( @Valid Publication publication, BindingResult bindingResult, RedirectAttributes ra) throws Exception {

		/* test pour s'assurer de la validation de l'etudiant : */
		if (bindingResult.hasErrors()) {
		
			return "EditPublication";
		}
		
		publicationRepository.save(publication);
		
		ra.addFlashAttribute("messageAP", "Publication modifié avec succes");
		
		return "redirect:pub";
	}
	
	/***************************************ESPACE ETUDIANT*************************************************/


	@RequestMapping(value = "/espaceEtudiant")
	public String espaceEtudiant(Model model, @RequestParam(name = "page", defaultValue = "0") int p,
			@RequestParam(name = "motCle", defaultValue = "") String mc) {
		
		
		Pageable paging = PageRequest.of(p, 5);
		Page<Formation> pageFormation = formationRepository.listFormation("%" + mc + "%", paging);

		int pagesCount = pageFormation.getTotalPages();
		int[] pages = new int[pagesCount];
		for (int i = 0; i < pagesCount; i++)
			pages[i] = i;

		model.addAttribute("motCle", mc);
		model.addAttribute("pageCourant", p);
		model.addAttribute("pages", pages);
		// Stocker la liste des etudiants dans le model
		model.addAttribute("pageFormation", pageFormation.getContent());
		return "espaceEtudiant";
	}
	


	@RequestMapping("/public")
	public String ListPublic(Model model, @RequestParam(name = "pagee", defaultValue = "0") int p) {
		Pageable paging = PageRequest.of(p, 5);
		Page<Publication> pagePublication = publicationRepository.listPublication(paging);

		int CountPages = pagePublication.getTotalPages();
		int[] page = new int[CountPages];
		for (int i = 0; i < CountPages; i++)
			page[i] = i;

		
		model.addAttribute("CourantPage", p);
		model.addAttribute("pages", page);
	
		model.addAttribute("pagePublication", pagePublication.getContent());
		return "listerPublication"; // return le nom de la vue
	}

/*********************************************EVENT CALENDAR*******************************************/
	
	
/*

	@Autowired
	private EventJpaRepository eventRepository; 
	
	
	
	@RequestMapping(value="/staticcalendar", method=RequestMethod.GET) 
	public ModelAndView staticcalendar() {
		return new ModelAndView("staticcalendar");
	}
	
	@RequestMapping(value="/calendar", method=RequestMethod.GET) 
	public String calendar() {
		return "calendar";
	}
	
	@RequestMapping(value="/jsoncalendar", method=RequestMethod.GET) 
	public ModelAndView jsoncalendar() {
		return new ModelAndView("jsoncalendar");
	}
	
	@RequestMapping(value="/eventlist", method=RequestMethod.GET) 
	public String events(HttpServletRequest request, Model model) {
		model.addAttribute("events", eventRepository.findAll());
		return "events";
	}

	
	@RequestMapping(value="/event", method=RequestMethod.POST)
	public Event addEvent(@RequestBody Event event) {
		Event created = eventRepository.save(event);
		return created; 
	}

	@RequestMapping(value="/event", method=RequestMethod.PATCH)
	public Event updateEvent(@RequestBody Event event) {
		return eventRepository.save(event);
	}

	@RequestMapping(value="/event", method=RequestMethod.DELETE)
	public void removeEvent(@RequestBody Event event) {
		eventRepository.delete(event);
	}
*/



}