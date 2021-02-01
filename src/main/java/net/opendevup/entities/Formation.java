package net.opendevup.entities;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;


@SuppressWarnings("deprecation")
@Entity
public class Formation implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long ID;
	@Column(name="INTITULE", length = 30)
	@NotEmpty
	@Size(min=2 , max = 30)	
	private String intitule;
	@NotEmpty
	@Size(min=2 , max = 3)	
	private String smestre;
	@NotEmpty
	private String contenu;
	private String fichier;
	public Formation () { super(); }
	
	public Formation( String intitule, String smestre, String contenu, String fichier) {
		super();
		this.intitule = intitule;
		this.smestre = smestre;
		this.contenu = contenu;
		this.fichier=fichier;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public String getintitule() {
		return intitule;
	}

	public void setintitule(String intitule) {
		this.intitule = intitule;
	}

	public String getSmestre() {
		return smestre;
	}

	public void setSmestre(String smestre) {
		this.smestre = smestre;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public String getFichier() {
		return fichier;
	}

	public void setFichier(String fichier) {
		this.fichier = fichier;
	}
	
	

}
