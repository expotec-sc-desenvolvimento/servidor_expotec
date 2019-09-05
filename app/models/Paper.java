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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import play.db.jpa.Blob;
import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="papers")
public class Paper extends GenericModel {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UUID_SEQ")
	@SequenceGenerator(name = "UUID_SEQ", sequenceName = "UUID_SEQ", initialValue = 1, allocationSize = 1)
	public Long uuid;	
	
	@ManyToOne()
    public Track track;
    
	@NotNull
	@MinSize(1)
	@MaxSize(150)
	@Required(message = "validacao.requerido")
    public String title;
	
	@Lob
	public String abs;
	
    public Blob file;
    
	@ManyToOne
    public Area mainArea;
    
	@NotNull
    @ManyToOne
    public User author;
	
	public String keywords;
	
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<User> coauthors = new ArrayList<User>();
	
    
    @NotNull
    @Enumerated(EnumType.STRING)
    public PaperStatus status;
    

    @OneToMany(mappedBy = "paper", fetch = FetchType.LAZY)
    public List<Evaluation> evaluations = new ArrayList<Evaluation>();
    
    @Transient
    public double finalScore;    
}
