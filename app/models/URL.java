/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
public class URL extends Model{
    @Unique
    @NotNull
    public String path;
}
