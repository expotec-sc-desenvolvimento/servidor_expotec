package controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import com.thoughtworks.xstream.io.path.Path;

import models.Event;
import models.Permission;
import models.User;
import play.Play;
import play.data.validation.Required;
import play.libs.Crypto;
import play.libs.Time;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Http;
import play.utils.Java;

public class Security extends Secure.Security {

	public static void logoutRemove() throws Throwable {
		Security.invoke("onDisconnect");
		session.clear();
		response.removeCookie("rememberme");
		Security.invoke("onDisconnected");
		flash.success("secure.logoutremove");
		Application.index();
	}

	private static Object invoke(String m, Object... args) throws Throwable {
		try {
			return Java.invokeChildOrStatic(Security.class, m, args);
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	public static void login() throws Throwable {
		flash.clear();
    	Http.Cookie remember = request.cookies.get("rememberme");
        if (remember != null && remember.value.indexOf("-") > 0) {
            String sign = remember.value.substring(0, remember.value.indexOf("-"));
            String email = remember.value.substring(remember.value.indexOf("-") + 1);
            if (Crypto.sign(email).equals(sign)) {
                User user = User.find("byEmail", email).first();
                if(user != null){
                    session.put("userid", user.uuid);
                    session.put("permission", user.permission);
                }
            }
        }
        flash.keep("url");
        Secure.login();
    }

	public static void authenticate(@Required String email, String password, boolean remember) throws Throwable {
		// Check tokens
		Boolean allowed = false;
		try {
			// This is the deprecated method name
			allowed = (Boolean) Security.invoke("authentify", email, password);
		} catch (UnsupportedOperationException e) {
			// This is the official method name
			allowed = (Boolean) Security.invoke("authenticate", email, password);
		}
		if (validation.hasErrors() || !allowed) {
			flash.keep("url");
			flash.error("secure.error");
			params.flash();
			login();
		}
		// Mark user as connected
		session.put("email", email);
		User user = User.find("byEmail", email).first();
		if (user != null) {
			session.put("userid", user.uuid);
			session.put("permission", user.permission);
		}
		// Remember if needed
		if (remember) {
			Date expiration = new Date();
			String duration = Play.configuration.getProperty("secure.rememberme.duration", "30d");
			expiration.setTime(expiration.getTime() + ((long) Time.parseDuration(duration)) * 1000L);
			response.setCookie("rememberme",
					Crypto.sign(email + "-" + expiration.getTime()) + "-" + expiration.getTime(), duration);
		}
		
		Secure.redirectToOriginalURL();
	}

	public static void logout() throws Throwable {
		Security.invoke("onDisconnect");
		session.clear();
		response.removeCookie("rememberme");
		Security.invoke("onDisconnected");
		flash.success("secure.logout");
		redirect(Play.ctxPath+"/login");
	}

	static boolean authenticate(String username, String password) {
		User user = User.find("byEmail", username).first();
		if (user != null) {
			
			session.put("userid", user.uuid);
			session.put("permission", user.permission);
			
			Event event = Event.all().first();
            session.put("eventid", event.id);
			return user.password.equals(Crypto.passwordHash(password));
		} else {
			return false;
		}
	}

	static boolean check(String profile) {

		User user = User.findById(Long.parseLong(session.get("userid")));
		if (user != null) {
			if ("administrador".equalsIgnoreCase(profile)) {
				return user.permission == Permission.ADMIN;
			}  else if ("Atendente".equalsIgnoreCase(profile)) {
				return user.permission == Permission.ATTENDANT || user.permission == Permission.ADMIN;
			} else if ("Participante".equalsIgnoreCase(profile)) {
				return user.permission == Permission.ATTENDEE || user.permission == Permission.ADMIN
						 || user.permission == Permission.ATTENDANT;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
