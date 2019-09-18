package controllers;

import play.Play;
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
	private static String _url;
	
	public Administrators() {
		super();
	}

	@Before
	static void setUserAndEvent() {
		User user = User.findById(Long.parseLong(session.get("userid")));
		Event event = Event.findById(Long.parseLong(session.get("eventid")));
		renderArgs.put("ulogado", user);
		renderArgs.put("event", event);
		_url = session.get("url");
	}

	
	public static void listAreas() {
		session.put("url", Play.ctxPath+"/administrators/listAreas");
		List<Area> areas = Area.findAll();
		renderArgs.put("areas", areas);
		renderTemplate("Administrators/listAreas.html");
	}
	
	public static void saveAreaJSON(String description, Long parentId) {
		Area parent = null;
		if (parentId != 0) {
			parent = Area.findById(parentId);
		}
		
		Area nova = new Area();
		nova.description = description;
		nova.parent = parent;
		nova.save();   
		renderJSON(true);
	}
	
	public static void removeAreaJSON(Long id) {
		Area area = Area.findById(id);
		List<Area> children = Area.find("select a from Area a where a.parent.id = "+area.id).fetch();
		for(Area a: children) {
			a.delete();
		}
		area.delete();
		renderJSON(true);
	}
	
	
	public static void listLocations() {
		session.put("url", Play.ctxPath+"/administrators/listLocations");
		List<Location> locations = Location.findAll();
		renderArgs.put("locations", locations);
		renderTemplate("Administrators/listLocation.html");
	}
	
	
	
	public static void listActivityTypes() {
		session.put("url", Play.ctxPath+"/administrators/listActivityTypes");
		List<ActivityType> types = ActivityType.findAll();
		renderArgs.put("types", types);
		renderTemplate("Administrators/listActivityTypes.html");
	}
	
	
	public static void newActivityType() {
		flash.clear();
		ActivityType activitytype = new ActivityType();
		renderArgs.put("activitytype", activitytype);
        renderTemplate("Administrators/newActivityType.html");
	}
	
	public static void editActivityType(Long id) {
		flash.clear();
		ActivityType activitytype = ActivityType.findById(id);
		renderArgs.put("activitytype", activitytype);
        renderTemplate("Administrators/editActivityType.html");
	}

	public static void saveActivityType(@Valid ActivityType activitytype) {
		boolean isNew = activitytype.id == null || activitytype.id  == 0;
		if (validation.hasErrors() ) {
			System.out.println(validation.errorsMap());
            flash.error("erro.operacao");
            
            renderArgs.put("activitytype", activitytype);
            if(isNew) {
            	renderTemplate("Administrators/newActivityType.html");
            }else {
            	renderTemplate("Administrators/editActivityType.html");	
            }
            
            
        }
		activitytype.save();
		flash.success("applicaiton.success");
		redirect(_url);
	}
	
	public static void saveActivityTypeJSON(String name, String color) {
		ActivityType at = new ActivityType();
		at.name = name;
		at.color = color;
		at.save();
		flash.success("event.success");
        renderJSON(at);
	}
	
	public static void removeActivityType(Long id) {
		Long count = (Long) JPA.em().createQuery("select count (a) from Activity a where a.type.id = "+id).getSingleResult();
		if(count > 0) {
			List<ActivityType> types = ActivityType.findAll();
			renderArgs.put("types", types);
			flash.error("erro.dependencia");
			renderTemplate("Administrators/listActivityTypes.html");
		}
		ActivityType activitytype = ActivityType.findById(id);
		activitytype.delete();
		listActivityTypes();
	}

	
	
	public static void listEvents() {
		flash.clear();
		session.put("url", Play.ctxPath+"/administrators/listEvents");
		List<Event> events = Event.all().fetch();
		renderArgs.put("events", events);
		renderTemplate("Administrators/listEvents.html");
	}
	
	public static void editEvent(Long id) {
		flash.clear();
		Event e = Event.findById(id);
		renderArgs.put("e", e);
		render();
	}
	
	public static void saveEvent(@Valid Event e) {
		flash.clear();
		validation.future(e.end, e.start);
		if (validation.hasErrors() ) {
			System.out.println(validation.errorsMap());
            flash.error("erro.operacao");
            renderArgs.put("e", e);
            renderTemplate("Administrators/editEvent.html");
        }
		e.save();
		flash.success("event.success");
		redirect(_url);
	}
	
	public static void listActivities(Long id) {
		flash.clear();
		session.put("url", Play.ctxPath+"/administrators/listActivities?id="+id);
		List<Activity> activities = Activity.find("select a from Activity a where a.event.id = "+id).fetch();
		renderArgs.put("activities", activities);
		renderTemplate("Administrators/listActivities.html");
	}
	
	public static void newActivity() {
		flash.clear();
		Activity a = new Activity();
		a.status = ActivityStatus.DRAFT;
		
		Event event = Event.findById(Long.parseLong(session.get("eventid")));
		a.event =event; 
		
		a.startInscription = new Date();
		renderArgs.put("activity", a);
		
		List<ActivityType> activityTypes = ActivityType.findAll();
		renderArgs.put("types", activityTypes);
		
		List<ActivityStatus> status = ActivityStatus.list();
		renderArgs.put("status", status);
		
		render();
	}
	
	public static void editActivity(Long id) {
		flash.clear();
		Activity a = Activity.findById(id);
		renderArgs.put("activity", a);
		
		List<ActivityType> activityTypes = ActivityType.findAll();
		renderArgs.put("types", activityTypes);
		
		List<ActivityStatus> status = ActivityStatus.list();
		renderArgs.put("status", status);
		
		render();
	}
	
	
	public static void saveActivity(@Valid Activity activity, String facilitators) {
		flash.clear();
		boolean isNew = activity.uuid == null || activity.uuid == 0;
		if (validation.hasErrors() ) {
			System.out.println(validation.errorsMap());
            flash.error("application.error");
            renderArgs.put("activity", activity);
            
            List<ActivityType> activityTypes = ActivityType.findAll();
    		renderArgs.put("types", activityTypes);
    		
    		List<ActivityStatus> status = ActivityStatus.list();
    		renderArgs.put("status", status);
    		
            if(isNew) {
				renderTemplate("Attendees/editActivity.html");
			}else {
				renderTemplate("Administrators/newActivity.html");
			}
        }
		String uuidArray[] = facilitators.split(",");
		List<User> fas = new ArrayList<User>();
		for(String uuid: uuidArray) {
			if(!uuid.trim().equals("")) {
				User fa = User.findById(Long.parseLong(uuid));
				fas.add(fa);
			}
		}
		activity.facilitators = fas;
		
		activity.save();
		flash.success("application.success");
		redirect(_url);
	}
	
	public static void removeActivity(Long id) {
		Activity a = Activity.findById(id);
		Long eventid = a.event.id;
		if(a.getInscriptions().size() > 0) {
			List<Activity> activities = Activity.find("select a from Activity a where a.event.id = "+eventid).fetch();
			renderArgs.put("activities", activities);
			flash.error("erro.dependencia");
			renderTemplate("Administrators/listActivities.html");
		}	
		a.delete();
		listActivities(eventid);
	}
	
	public static void listTracks(Long id) {
		flash.clear();
		session.put("url", Play.ctxPath+"/administrators/listTracks?id="+id);
		List<Track> tracks = Track.find("select t from Track t where t.event.id = "+id).fetch();
		renderArgs.put("tracks", tracks);
		renderTemplate("Administrators/listTracks.html");
	}

	public static void newTrack() {
		flash.clear();
		List<TrackType> trackTypes = TrackType.list();
		renderArgs.put("types", trackTypes);
		
		List<CalculationType> calcTypes = CalculationType.list();
		renderArgs.put("calcs", calcTypes);
		
		List<Criteria> criterias = new ArrayList<Criteria>();
		renderArgs.put("criterias", criterias);
		
 		List<TrackStatus> trackStatus = TrackStatus.list();
		renderArgs.put("status", trackStatus);
		
		Event event = Event.findById(Long.parseLong(session.get("eventid")));
		renderArgs.put("tracks", event.getAllTracks());
        
		Track t = new Track();
		t.event = event;
		t.status = TrackStatus.DRAFT;
		renderArgs.put("track", t);
		render();
	}
	
	public static void editTrack(Long id) {
		flash.clear();
		List<TrackType> trackTypes = TrackType.list();
		renderArgs.put("types", trackTypes);
		
		List<CalculationType> calcTypes = CalculationType.list();
		renderArgs.put("calcs", calcTypes);
		
		List<Criteria> criterias = new ArrayList<Criteria>();
		renderArgs.put("criterias", criterias);
		
 		List<TrackStatus> trackStatus = TrackStatus.list();
		renderArgs.put("status", trackStatus);
		
		Event event = Event.findById(Long.parseLong(session.get("eventid")));
		renderArgs.put("tracks", event.getAllTracks());
		
		Track t = Track.findById(id);
		renderArgs.put("track", t);
		render();
	}
	
	public static void saveTrack(@Valid Track track, Criteria[] oCriterias, Criteria[] nCriterias) {
		boolean isNew = track.id == null || track.id == 0;
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
            if(isNew) {
            	renderTemplate("Administrators/newTrack.html");
            }else {
            	renderTemplate("Administrators/editTrack.html");
            }
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
	
		flash.success("application.success");
		redirect(_url);
	}
	
	public static void removeTrack(Long id) {
		Long count = (Long) JPA.em().createQuery("select count (p) from Paper p where p.track.id = "+id).getSingleResult();
		Track track = Track.findById(id);
		Long eventId = track.event.id; 
		if(count > 0) {
			List<Track> tracks = Track.find("select t from Track t where t.event.id = "+eventId).fetch();
			renderArgs.put("tracks", tracks);
			flash.error("erro.dependencia");
			renderTemplate("Administrators/listTracks.html");
		}
		
		JPA.em().createQuery("delete from Criteria c where c.track.id = "+track.id).executeUpdate();
		track.delete();
		listTracks(eventId);
	}
	
	
	public static void listUsers() {
		session.put("url", Play.ctxPath+"/administrators/listUsers");
		List<User> users = User.all().fetch();
		renderArgs.put("users", users);
		renderTemplate("Administrators/listUsers.html");
	}
	
	public static void editUser(Long id) {
		List<Permission> permissions = Permission.list();
		renderArgs.put("permissions", permissions);
		
		User user = User.findById(id);
		renderArgs.put("user", user);
		render();
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
	
	public static void carregarActiveUsers(String name) {
		List<User> _users = User.find("select distinct u from User u where lower(u.name) like '%" + name.toLowerCase()
				+ "%' and u.status = true").fetch();
		List<User> users = new ArrayList<User>();
		for (User u : _users) {
			User novo = new User(u.uuid, u.name, u.picture);
			users.add(novo);
		}
		renderJSON(users);
	}
	
	public static void removeUser(Long id) {
		User u = User.findById(id);
		if(u.getMyInscriptions().size() > 0 || u.getMyPapers().size() > 0 || u.getMyActivities().size() >0) {
			List<User> users = User.all().fetch();
			renderArgs.put("users", users);
			flash.error("erro.dependencia");
			renderTemplate("Administrators/listUsers.html");
		}
		u.delete();
		listUsers();
	}
	
	
	public static void listPapers(Long id) {
		session.put("url", Play.ctxPath+"/administrators/listPapers?id="+id);
		List<Paper> papers = Paper.find("select p from Paper p where p.track.event.id = "+id).fetch();
		renderArgs.put("papers", papers);
		renderTemplate("Administrators/listPapers.html");
	}


}
