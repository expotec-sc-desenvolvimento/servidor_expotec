/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.List;

import org.junit.After;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import models.*;
import play.db.jpa.JPA;

@With(Secure.class)
@Check("atendente")
public class Attendants extends Attendees{
	public Attendants() {
		super();
	}
	
	@After
	static void setUserAndEvent() {
		User user = User.findById(Long.parseLong(session.get("userid")));
		Event event = Event.findById(Long.parseLong(session.get("eventid")));
		renderArgs.put("user", user);
		renderArgs.put("event", event);
	}
	
}
