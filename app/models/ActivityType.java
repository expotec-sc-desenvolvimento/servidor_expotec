package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.sf.oval.constraint.NotNull;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
@Table(name="activityTypes")
public class ActivityType extends Model{
	
	@NotNull
	@Unique
	@Required(message="validacao.requerido")
	public String name;

	@NotNull
	@Required(message="validacao.requerido")
	public String color;
}
