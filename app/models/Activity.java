package models;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

import play.db.jpa.Model;

@Entity
@Table(name="activities")
public class Activity extends GenericModel{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UUID_SEQ")
	@SequenceGenerator(name = "UUID_SEQ", sequenceName = "UUID_SEQ", initialValue = 1, allocationSize = 1)
	public Long uuid;	
	
	@Unique
	@NotNull
	@Required(message="validacao.requerido")
    public String title;
	
	@Lob
    @MaxSize(255)
    public String description;
    
    @Min(1)
    public int days;
    
    @Min(0)
    public double totalCH;
   
    
    @NotNull
    public boolean limited;
    
    @Min(0)
    public boolean maxAttendees;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    public ActivityStatus status;
    
    @NotNull
    @ManyToOne
    public ActivityType type;
    
    @NotNull
    @ManyToOne
    public Event event;
    
    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    protected List<Schedule> schedules = new ArrayList<Schedule>();
       
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<User> facilitators = new ArrayList<User>();
    
    @Transient
    public List<Inscription> getInscriptions(){
        List<Inscription> inscriptions = Inscription.find("byActivity", this).fetch();
        return inscriptions;
    }
    
}
