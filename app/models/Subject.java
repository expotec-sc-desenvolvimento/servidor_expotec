package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
public class Subject extends Model{
	@ManyToOne
	public Subject parent;

	@Unique
	@MinSize(1)
	@NotNull
	@Required(message="validacao.requerido")
	public String description;
		
}
