package controllers;

import play.db.jpa.JPA;
import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import javax.validation.Valid;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.h2.tools.Console;
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

	public static void saveTrack(@Valid Track track, Criteria[] oCriterias, Criteria[] nCriterias) {

		validation.future(track.end, track.start);
		if (validation.hasErrors() ) {
			System.out.println(validation.errorsMap());
            flash.error("erro.operacao");
            renderArgs.put("track", track);
            List<TrackType> trackTypes = TrackType.list();
    		renderArgs.put("tktypes", trackTypes);
    		
    		List<CalculationType> calcTypes = CalculationType.list();
    		renderArgs.put("calcTypes", calcTypes);
            
    		List<TrackStatus> trackStatus = TrackStatus.list();
    		renderArgs.put("tkStatus", trackStatus);
            
    		renderTemplate("Administrators/editTrack.html");
        }
        
		List<Criteria> criterias = Criteria.find("select c from Criteria c where c.track.id = "+track.id).fetch();
		
		//atual 
		for (Criteria c : criterias) { 
    	    c.track = null;
    	    c.save();
    	} 
		
		track.save();
		
		//antigas
		if(oCriterias != null) {
			for (Criteria c : oCriterias) {
				Criteria update = Criteria.findById(c.getId());
				update.track = track;
				update.save();
			} 
		}
		
		
		//novas
		if(nCriterias != null) {
			for (Criteria c : nCriterias) {
				Criteria nova = new Criteria(c.description, c.weight);
				nova.track = track;
				nova.save();
			} 
		}
		
		JPA.em().createQuery("delete from Criteria c where c.track is null").executeUpdate();
	
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
 		
		List<TrackStatus> trackStatus = TrackStatus.list();
		renderArgs.put("tkStatus", trackStatus);
        
		Track t = Track.findById(id);
		renderArgs.put("track", t);
		render();
	}
	
	public static void newTrack() {
		List<TrackType> trackTypes = TrackType.list();
		renderArgs.put("tktypes", trackTypes);
		
		List<CalculationType> calcTypes = CalculationType.list();
		renderArgs.put("calcTypes", calcTypes);
		
		List<Criteria> criterias = new ArrayList<Criteria>();
		renderArgs.put("criterias", criterias);
		
 		List<TrackStatus> trackStatus = TrackStatus.list();
		renderArgs.put("tkStatus", trackStatus);
        
		Track t = new Track();
		Event event = Event.findById(Long.parseLong(session.get("eventid")));
		t.event = event;
		t.status = TrackStatus.DRAFT;
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
	
	public static void listPapers(Long eventid) {
		List<Paper> papers = Paper.find("select p from Paper p where p.track.event.id = "+eventid).fetch();
		renderArgs.put("papers", papers);
		renderTemplate("Administrators/listPapers.html");
	}

}
