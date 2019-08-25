package controllers;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import javax.validation.Valid;

import org.junit.After;

import java.util.*;

import models.*;

@With(Secure.class)
@Check("administrador")
public class Administrators extends Attendants {
	public Administrators() {
		super();
	}

	@Before
	static void setUserAndEvent() {
		User user = User.findById(Long.parseLong(session.get("userid")));
		Event event = Event.findById(Long.parseLong(session.get("eventid")));
		renderArgs.put("user", user);
		renderArgs.put("event", event);
	}

	public static void saveEvent(@Valid Event e) {
		validation.future(e.end, e.start);
		if (validation.hasErrors() ) {
			System.out.println(validation.errorsMap());
            flash.error("erro.operacao");
            renderArgs.put("e", e);
            renderTemplate("Administrators/editEvent.html");
        }
		e.save();
		flash.success("event.success");
        viewEvent(e.id);
	}

	public static void editEvent(Long id) {
		Event e = Event.findById(id);
		renderArgs.put("e", e);
		render();
	}

	public static void listEvents() {
		List<Event> events = Event.all().fetch();
		renderArgs.put("events", events);
		renderTemplate("Administrators/listEvents.html");
	}

	public static void listActivities(Long eventid) {
		List<Activity> activities = Activity.find("select a from Activity a where a.event.id = "+eventid).fetch();
		renderArgs.put("activities", activities);
		renderTemplate("Administrators/listActivities.html");
	}

}
