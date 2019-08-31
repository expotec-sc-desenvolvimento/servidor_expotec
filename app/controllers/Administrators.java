package controllers;

import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import javax.validation.Valid;

import org.apache.commons.collections.iterators.ArrayListIterator;
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
		renderArgs.put("ulogado", user);
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
        listEvents();
	}

	public static void saveTrack(@Valid Track track) {
		validation.future(track.end, track.start);
		if (validation.hasErrors() ) {
			System.out.println(validation.errorsMap());
            flash.error("erro.operacao");
            renderArgs.put("track", track);
            List<TrackType> trackTypes = TrackType.list();
    		renderArgs.put("tktypes", trackTypes);
    		
    		List<CalculationType> calcTypes = CalculationType.list();
    		renderArgs.put("calcTypes", calcTypes);
            renderTemplate("Administrators/editTrack.html");
        }
        
		List<Criteria> criterias = new ArrayList<Criteria>();
		for(Criteria c:track.criterias) {
			if(c != null) {
				criterias.add(Criteria.findById(c.id));
			}
		}
		
		track.criterias = criterias;
		track.save();
		flash.success("track.success");
        
		listTracks(track.event.id);
	}
	
	public static void saveUser(@Valid User user) {
		if (validation.hasErrors() ) {
			System.out.println(validation.errorsMap());
            flash.error("erro.operacao");
            renderArgs.put("u", user);
            renderTemplate("Administrators/editUser.html");
        }
		user.save();
		flash.success("event.success");
        listUsers();
	}

	
	public static void editEvent(Long id) {
		Event e = Event.findById(id);
		renderArgs.put("e", e);
		render();
	}

	public static void editActivity(Long id) {
		Activity a = Activity.findById(id);
		renderArgs.put("a", a);
		render();
	}
	
	public static void editTrack(Long id) {
		List<TrackType> trackTypes = TrackType.list();
		renderArgs.put("tktypes", trackTypes);
		
		List<CalculationType> calcTypes = CalculationType.list();
		renderArgs.put("calcTypes", calcTypes);
		
		List<Criteria> criterias = new ArrayList<Criteria>();
		renderArgs.put("criterias", criterias);
		
		Track t = Track.findById(id);
		renderArgs.put("track", t);
		render();
	}
	
	public static void editUser(Long id) {
		
		List<Permission> permissions = Permission.list();
		renderArgs.put("permissions", permissions);
		
		User user = User.findById(id);
		renderArgs.put("user", user);
		render();
	}

	public static void listEvents() {
		List<Event> events = Event.all().fetch();
		renderArgs.put("events", events);
		renderTemplate("Administrators/listEvents.html");
	}

	public static void removeCriteria(Long id) {
		Criteria c = Criteria.findById(id);
		if (c == null) {
			renderJSON("Critério inválido");
		}
		
		c.delete();
		renderJSON("Critério removido com sucesso");
	}
	
	public static void listUsers() {
		List<User> users = User.all().fetch();
		renderArgs.put("users", users);
		renderTemplate("Administrators/listUsers.html");
	}
	
	
	public static void listActivities(Long eventid) {
		List<Activity> activities = Activity.find("select a from Activity a where a.event.id = "+eventid).fetch();
		renderArgs.put("activities", activities);
		renderTemplate("Administrators/listActivities.html");
	}
	
	public static void listTracks(Long eventid) {
		List<Track> tracks = Track.find("select t from Track t where t.event.id = "+eventid).fetch();
		renderArgs.put("tracks", tracks);
		renderTemplate("Administrators/listTracks.html");
	}

}
