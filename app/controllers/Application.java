package controllers;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.inject.Scope;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.junit.After;

import models.Activity;
import models.Event;
import models.Permission;
import models.User;
import notifiers.Mails;
import play.Play;
import play.data.validation.Equals;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.results.Ok;
import play.mvc.results.Result;
import play.vfs.VirtualFile;

public class Application extends Controller {
	@Before
	static void setEvent() {
		Event event = Event.all().first();
		if (event == null) {
			event = new Event();
		}
		renderArgs.put("event", event);
	}

	public static void comingSoon() throws Throwable {
		session.put("url", "/application/commingSoon");
		renderTemplate("Application/comingsoon.html");
	}

	public static void callForPapers() throws Throwable {
		session.put("url", "/application/callForPapers");
		renderTemplate("Application/callforpapers.html");
	}

	public static void sheduleOfActivities() throws Throwable {
		session.put("url", "/application/sheduleOfActivities");
		renderTemplate("Application/schedule.html");
	}

	public static void index() throws Throwable {
		session.put("url", "/application/index");
		renderTemplate("Application/index.html");
	}

	public static void detailUserJSON(Long id) {

		User user = User.findById(id);
		renderJSON(user);
	}

	public static void listActivitiesJSON(Long id) {
		List<Activity> activities = Activity.find("select a from Activity a where a.event.id = " + id).fetch();

		renderJSON(activities);
	}

	public static void listEventsJSON() {
		List<Event> events = Event.findAll();
		renderJSON(events);
	}

	public static void cpanel() {
		try {
			if (session.get("userid") == null) {
				Security.login();
			} else {
				User user = User.findById(Long.parseLong(session.get("userid")));
				if (user == null) {
					Security.login();
				} else {
					// Redirect to the original URL (or /)
					if (user.permission == Permission.ADMIN) {
						Administrators.cpanel();
					} else if (user.permission == Permission.ATTENDANT) {
						Attendants.cpanel();
					} else if (user.permission == Permission.ATTENDEE) {
						Attendees.cpanel();
					} else {
						Security.login();
					}
				}
			}
		} catch (Throwable e) {
			flash.error("application.error");
			renderTemplate("Secure/login.html");
			e.printStackTrace();
		}

	}

	public static void save(@Valid User user) {
		validation.required(user.password);
		validation.minSize(user.password, 6);
		validation.maxSize(user.password, 16);

		validation.required(user.pwConfirmation);
		validation.equals(user.password, user.pwConfirmation);

		if (validation.hasErrors()) {
			flash.error("validacao.erro");
			user.password = "";
			user.pwConfirmation = "";
			renderTemplate("Application/signup.html", user);
		}
		user.password = Crypto.passwordHash(user.password);
		user.registration = new Date();
		user.outsider = true;
		user.status = false;
		user.permission = Permission.ATTENDEE;
		user.save();
		Mails.emailCredencials(user);
		flash.success("user.success");
		redirect(Play.ctxPath + "/login");
	}

	public static void signup() {
		flash.clear();
		User user = new User();
		render(user);
	}

	public static void activeUser(Long id) {
		User user = User.findById(id);
		if (user != null) {
			user.status = true;
			user.save();
			flash.success("user.active");
			renderTemplate("Secure/login.html");
		} else {
			flash.error("application.error");
			renderTemplate("Secure/login.html");
		}

	}

	public static void getEventLogo(Long eventid) {
		try {
			Event event = Event.findById(eventid);
			if (event.logo.get() != null) {
				response.setContentTypeIfNotSet(event.logo.type());
				InputStream binaryData = event.logo.get();
				renderBinary(binaryData);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			InputStream binaryData = null;
			renderBinary(binaryData);
		}
	}
	
	public static void resetPassword(String email) {
		System.out.println(email);
        User u = User.find("byEmail", email).first();
        if (u != null) {
            Mails.emailNewPassword(u);
            renderJSON("E-mail enviado com sucesso para " + u.email);
        } else {
            renderJSON("Erro! Usuário não encontrado");
        }
       
        //redirect(Play.ctxPath + "/login");
    }

}
