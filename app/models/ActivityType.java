package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.sf.oval.constraint.NotNull;
import play.data.binding.As;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
@Table(name="activityTypes")
public class ActivityType extends Model{
	
	@NotNull
	@Unique
	@Required(message="validacao.requerido")
	@MaxSize(50)
	public String name;
	
	public String color;
}
