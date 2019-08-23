/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.User;
import play.mvc.With;

@With(Secure.class)
@Check("especialista")
public class Experts extends Attendees {
    public Experts() {
    	super();
    }
}
