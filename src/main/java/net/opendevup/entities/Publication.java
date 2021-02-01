package net.opendevup.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;


@SuppressWarnings("deprecation")
@Entity
public class Publication implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Long id;
	@Column(name="intitule", length = 30)
	@NotEmpty
	private String intitule;
	@Column(name="description", length = 200)
	@NotEmpty
	private String description;
	
	
	
	public Publication() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Publication(Long id, @NotEmpty String intitule, @NotEmpty String description) {
		super();
		this.id = id;
		this.intitule = intitule;
		this.description = description;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getIntitule() {
		return intitule;
	}



	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}

	
	
	
}
