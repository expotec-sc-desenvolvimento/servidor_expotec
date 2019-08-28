/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import play.data.validation.Min;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="papers")
public class Paper extends GenericModel {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UUID_SEQ")
	@SequenceGenerator(name = "UUID_SEQ", sequenceName = "UUID_SEQ", initialValue = 1, allocationSize = 1)
	public Long uuid;	
    
    @Min(0)
    public double score;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    public PaperStatus status;
   
    @NotNull
    @ManyToOne
    public User author;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<User> coauthors = new ArrayList<User>();
    
    @ManyToOne()
    public Track track;
    
    @ManyToOne()
    public Area mainArea;  
    
}
