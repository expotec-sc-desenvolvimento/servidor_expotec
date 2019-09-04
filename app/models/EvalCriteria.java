package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name="evalCriteria")
public class EvalCriteria extends Model {
	@Min(0)
	@Required(message = "validacao.requerido")
	public double score;
	
	@ManyToOne
	@Required(message = "validacao.requerido")
	public Criteria criteria;
	
	@ManyToOne
	@Required(message = "validacao.requerido")
	public Evaluation evaluation;
	
	
}
