package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name="inscriptions")
public class Inscription extends Model {
    @ManyToOne
    @Required(message = "validacao.requerido")
    public Activity activity;
    
    @ManyToOne
    @Required(message = "validacao.requerido")
    public User user;
    
    @NotNull
    public boolean present;
    
}
