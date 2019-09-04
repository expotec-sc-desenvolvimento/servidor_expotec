package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name="evaluations")
public class Evaluation extends Model{
	@ManyToOne
	@JoinColumn(name = "export_id")
	public User expert;
	
	@ManyToOne
	@JoinColumn(name = "paper_id")
	public Paper paper;
	
	@Min(0)
	public double avg;
	
	@Temporal(TemporalType.DATE)
    public Date start;
    
    @Temporal(TemporalType.DATE)
    public Date end;

	@Enumerated(EnumType.STRING)
	@Required
	public EvalStatus status;
    
    @Lob
    public String comments;
    
    @Transient
    public List<EvalCriteria> getCriterias (){
        List<EvalCriteria> criterias = JPA.em().createQuery("select c from Criteria c where c.track.id =  '"+this.paper.track.id+"'").getResultList();
        return criterias;
    }
	
}
