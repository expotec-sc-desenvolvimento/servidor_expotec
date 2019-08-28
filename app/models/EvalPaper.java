package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name="evalPaper")
public class EvalPaper extends Model{
	
	@Min(0)
	public double avg;
	
	@Temporal(TemporalType.DATE)
    public Date start;
    
    @Temporal(TemporalType.DATE)
    public Date end;

	@Enumerated(EnumType.STRING)
	@Required
	public EvaluationStatus status;
    
    @Lob
    public String comments;
    
    @Transient
    public List<EvalCriteria> getEvalCriterias (){
        List<EvalCriteria> evalsCriterias = JPA.em().createQuery("select ec from EvaluationCriteria ev where ev.evalPaper.id =  '"+this.id+"'").getResultList();
        return evalsCriterias;
    }
	
}
