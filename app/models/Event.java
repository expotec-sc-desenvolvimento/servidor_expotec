package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import net.sf.oval.constraint.MaxSize;
import play.data.binding.As;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name="events")
public class Event extends Model{
	@Required
    @MinSize(1)
    @Unique
    @MaxSize(150)
    public String title;
	
	@MaxSize(100)
	public String edition;
	
    @Lob
    public String about;

    public Blob logo;
    
    @Temporal(TemporalType.DATE)
    @As("yyyy-MM-dd") 
    public Date start;
    
    @Temporal(TemporalType.DATE)
    @As("yyyy-MM-dd") 
    public Date end;
    
    @MaxSize(155)
    public String address;
    
    public String promoVideo;
    
    public String blog;
    
    @MaxSize(150)
    public String organization;
    
    @MaxSize(50)
    public String contact;
    
    @Transient
    public List<Activity> getAllActivities(){
        List<Activity> activities = JPA.em().createQuery("select a from Activity a where a.event.id =  '"+this.id+"'").getResultList();
        return activities;
    }
    
    @Transient
    public List<Activity> getUndraftActivities(){
        List<Activity> activities = JPA.em().createQuery("select a from Activity a where a.status != 'DRAFT' and a.event.id =  '"+this.id+"'").getResultList();
        return activities;
    }
    
    @Transient
    public List<Track> getAllTracks(){
        List<Track> tracks = JPA.em().createQuery("select t from Track t where t.event.id =  '"+this.id+"'").getResultList();
        return tracks;
    }
    
    @Transient
    public List<Track> getOpenedTracks(){
        List<Track> tracks = JPA.em().createQuery("select t from Track t where t.status = 'OPENED' and t.event.id =  '"+this.id+"'").getResultList();
        return tracks;
    }
    
    @Transient
    public List<Track> getUndraftTracks(){
        List<Track> tracks = JPA.em().createQuery("select t from Track t where t.status != 'DRAFT' and t.event.id =  '"+this.id+"'").getResultList();
        return tracks;
    }
    
    @Transient
    public List<Track> getParcialUndraftTracks(){
        List<Track> tracks = JPA.em().createQuery("select t from Track t where t.status != 'DRAFT' and t.order = 1  and t.event.id =  '"+this.id+"'").getResultList();
        return tracks;
    }
}
