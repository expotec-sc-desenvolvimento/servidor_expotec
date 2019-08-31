package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name="tracks")
public class Track extends Model {
	@MinSize(0)
	@Required(message = "validacao.requerido")
    public String name;
    
    @Temporal(TemporalType.DATE)
    @Required(message = "validacao.requerido")
    public Date start;
    
    @Temporal(TemporalType.DATE)
    public Date end;
    
    @NotNull
    @Min(1)
    public int minEvaluations;
    
    @Lob
    @Required(message = "validacao.requerido")
    public String guidelines;

    @NotNull
    @Enumerated(EnumType.STRING)
    public TrackStatus status;
    
    @NotNull
    public boolean privacy;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    public CalculationType calcType;
    
    @Enumerated(EnumType.STRING)
    public TrackType type;
    
    @ManyToOne
    @Required(message = "validacao.requerido")
    public Event event;
    
    @ManyToOne
    public Track prior;
    
    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Criteria> criterias = new ArrayList<Criteria>();
    
    @Transient
    public List<Paper> getPapers (){
        List<Paper> papers = JPA.em().createQuery("select p from Papers p where p.track.id =  '"+this.id+"'").getResultList();
        return papers;
    }
    
    /*
     * @Transient
     
    public List<Criteria> getCriterias (){
        List<Criteria> criterias = JPA.em().createQuery("select c from Criteria c where c.track.id =  '"+this.id+"'").getResultList();
        return criterias;
    }
    */
}
