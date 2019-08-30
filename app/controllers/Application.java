package controllers;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.inject.Scope;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.junit.After;

import models.Event;
import models.Permission;
import models.User;
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
		renderArgs.put("eAtivado", event);
	}
	
	public static void comingSoon() throws Throwable {
		renderTemplate("Application/comingsoon.html");
	}
	
	public static void chamadasTrabalhos() throws Throwable {
		renderTemplate("Application/chamadasTrabalhos.html");
	}
	
	public static void index() throws Throwable {
		renderTemplate("Application/index.html");
	}

	public static void cpanel() throws Throwable {
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
				} else if (user.permission == Permission.EXPERT) {
					Experts.cpanel();
				} else if (user.permission == Permission.ATTENDANT) {
					Attendants.cpanel();
				} else if (user.permission == Permission.ATTENDEE) {
					Attendees.cpanel();
				} else {
					Security.login();
				}
			}
		}
	}

	public static void save(@Valid User user) throws Throwable {
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
		user.permission = Permission.ATTENDEE;
		user.save();
		flash.success("user.success");
		redirect(Play.ctxPath+"/login");
	}

	public static void signup() {
		render();
	}


	
	public static void getEventLogo(Long eventid) {
		try {
			Event event = Event.findById(eventid);
			response.setContentTypeIfNotSet(event.logo.type());
			java.io.InputStream binaryData = event.logo.get();
			renderBinary(binaryData);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			VirtualFile vf = VirtualFile.fromRelativePath("/public/img/logo.jpg");
			File f  = vf.getRealFile();
			renderBinary(f);
		}
	}

}
