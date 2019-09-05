package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import play.data.validation.Min;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name="criterio")
public class Criteria extends Model {
	
	@Required
	@MinSize(1)
	@Column(name = "descricao")
	
	public String description;
	
	@Min(0)
	@Column(name = "peso")
	public int weight;
	
	@ManyToOne()
	@JoinColumn(name = "idmodalidade")
	public Track track;
	
	public Criteria(String description, int weight) {
		this.description = description;
		this.weight = weight;
	}
}
