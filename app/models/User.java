package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Password;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Blob;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name = "users")
public class User extends GenericModel {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UUID_SEQ")
	@SequenceGenerator(name = "UUID_SEQ", sequenceName = "UUID_SEQ", initialValue = 1, allocationSize = 1)
	public Long uuid;	
	
	@Required(message = "validacao.requerido")
    public String name;

    @Required(message = "validacao.requerido")
    @Unique(message = "validacao.unico.email")
    public String email;
    
    public String cel;
    
    public String password;
    
    @Transient
    public String pwConfirmation;
          
    public boolean outsider;
    
    public String cpf_suap;
    
    public Blob picture;
    
    @Temporal(TemporalType.DATE)
    public Date registration;
    
    public boolean status;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    public Permission permission;
   
    @ManyToOne
    public Event event;
    
    public String citation;
    
    @OneToMany(fetch = FetchType.LAZY)
    public List<Expertise> expertises = new ArrayList<Expertise>();
  
    public User() {
    	super();
    }
    
    public User(Long uuid, String name, Blob picture) {
    	super();
    	this.uuid = uuid;
    	this.name = name;
    	this.picture = picture;
	}

	public String toString(){
    	this.name += " "; 
    	int pos = this.name.indexOf(" ");
        return this.name.substring(0,pos);
    }
    
    @Transient
    public List<Paper> getMyPapers (){
        List<Paper> papers = JPA.em().createQuery("select p from Paper p left join p.coauthors ca where p.author.uuid =  "+this.uuid+" or ca.uuid = "+this.uuid).getResultList();
        return papers;
    }
}
