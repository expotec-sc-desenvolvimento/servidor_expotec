package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import play.data.binding.As;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
@Table(name="modalidade")
public class Track extends Model {
	@NotNull
	@MinSize(0)
	@MaxSize(150)
	@Required(message = "validacao.requerido")
    public String name;
    
    @Temporal(TemporalType.DATE)
    @As("yyyy-MM-dd") 
    public Date start;
    
    @Temporal(TemporalType.DATE)
    @As("yyyy-MM-dd") 
    public Date end;
    
    @Lob
    @Required(message = "validacao.requerido")
    public String guidelines;
    
    @Min(1)
    public int minEvaluations;
    
    @NotNull
    @Min(1)
    @Column(name = "ordem")
    public int order;
    
    @Enumerated(EnumType.STRING)
    public TrackStatus status;

    @Enumerated(EnumType.STRING)
    public CalculationType calcType;
    
    @Enumerated(EnumType.STRING)
    public TrackType type;

    @OneToMany(mappedBy = "track", fetch = FetchType.LAZY)
    public List<Criteria> criterias = new ArrayList<Criteria>();
    
    @ManyToOne
    @Required(message = "validacao.requerido")
    public Event event;
    
    @ManyToOne
    public Track prior;
    
    @Transient
    public List<Paper> getPapers (){
        List<Paper> papers = JPA.em().createQuery("select p from Paper p where p.track.id =  '"+this.id+"'").getResultList();
        return papers;
    }
    
    @Transient
    public List<Criteria> getCriterias (){
        List<Criteria> criterias = JPA.em().createQuery("select c from Criteria c where c.track.id =  '"+this.id+"'").getResultList();
        return criterias;
    }
    
  
    
  
}
