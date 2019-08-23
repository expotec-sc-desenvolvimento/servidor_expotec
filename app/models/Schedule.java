/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name="schedules")
public class Schedule extends Model{
    @Required(message = "validacao.requerido")
    @Temporal(TemporalType.TIME)
    public Date start;
    
    @Required(message = "validacao.requerido")
    @Temporal(TemporalType.TIME)
    public Date end;
    
    @Required(message = "validacao.requerido")
    @Temporal(TemporalType.DATE)
    public Date day;
    
    @ManyToOne
    @Required(message = "validacao.requerido")
    public Location place;
}
