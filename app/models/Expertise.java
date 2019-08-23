package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import play.db.jpa.Model;

@Entity
@Table(name="expertises")
public class Expertise extends Model{
	
	@NotNull
	@ManyToOne
	public User expert;

	@NotNull
	@ManyToOne
	public Subject subject;
	
	@NotNull
	public boolean status;
}
