package models;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name="evaluationConfig")
public class EvaluationConfig extends Model {
    @ManyToOne
    @Required(message = "validacao.requerido")
    public Event event;
    
    @ManyToOne
    public EvaluationConfig prior;
    
    @Temporal(TemporalType.DATE)
    public Date start;
    
    @Temporal(TemporalType.DATE)
    public Date end;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    public EvaluationStatus status;
    
    @NotNull
    @Min(1)
    public int minEvaluations;
    
    
    @Lob
    public String guidelines;
    
    
    
    @Transient
    public List<Paper> getPapers (){
        List<Paper> achievements = JPA.em().createQuery("select a from Achievement a where a.enrollment.id =  '"+this.id+"'").getResultList();
        return achievements;
    }
    
}
