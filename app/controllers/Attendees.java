package controllers;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.junit.After;

import models.Activity;
import models.Event;
import models.Permission;
import models.User;
import play.data.validation.Valid;
import play.libs.Crypto;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import play.vfs.VirtualFile;

@With(Secure.class)
@Check("participante")
public class Attendees extends Controller {
	
	@Before
	static void setUserAndEvent() {
		User user = User.findById(Long.parseLong(session.get("userid")));
		Event event = Event.findById(Long.parseLong(session.get("eventid")));
		renderArgs.put("user", user);
		renderArgs.put("event", event);
	}
	
	public static void printMyBadge() {
		renderTemplate("attendees/cracha.html");
	}
	
	
	public static void cpanel() {
		renderTemplate("attendees/cpanel.html");
    }
	
	public static void viewEvent(Long id) {
		renderTemplate("attendees/viewEvent.html");
	}
	
	
	public static void viewMyProfile() {
		renderTemplate("attendees/viewMyProfile.html");
	}
	
	public static void editMyProfile() {
		renderTemplate("attendees/editMyProfile.html");
	}
	
	public static void getUserPicture(Long userid) {
        User user = User.findById(userid);
        if(user.picture.getFile() != null) {
        	renderBinary(user.picture.get());	
        }
        renderBinary(new File("public/img/default.jpg"));
    }
	
	
	public static void saveMyPassword(User user) {
		validation.required(user.password);
		validation.minSize(user.password, 6);
		validation.maxSize(user.password, 16);
		
		validation.required(user.pwConfirmation);
		validation.equals(user.password, user.pwConfirmation);
		
		if (validation.hasErrors()) {
			renderJSON("Senha e/ou confirmação inválidas.");
		}
		
		User logado = User.findById(Long.parseLong(session.get("userid").toString()));
		if(logado.uuid != user.uuid){
        	renderJSON("Você não tem permissão para realizar essa operação");
        }else {
        	logado.password = Crypto.passwordHash(user.password);
        	logado.save();
        }		
	}
	
	public static void saveMyProfile(@Valid User user) {
		if (validation.hasErrors() ) {
			System.out.println(validation.errorsMap());
            renderArgs.put("user", user);
            flash.error("erro.operacao");
            editMyProfile();
        }
		
		User logado = User.findById(Long.parseLong(session.get("userid").toString()));
		if(logado.uuid != user.uuid){
        	renderArgs.put("user", user);
            flash.error("erro.permissao");
            editMyProfile();
        }else {
        	logado.name = user.name;
        	logado.email = user.email;
        	logado.cel = user.cel;
        	logado.cpf_suap = user.cpf_suap;
        	logado.outsider = user.outsider;
        	if(user.picture != null) {
        		logado.picture = user.picture;
        	}
            logado.save();
            viewMyProfile();
        }
	}
	
	public static void listActivities(Long eventid) {
		List<Activity> activities = Activity.find("select a from Activity a where a.event.id = "+eventid).fetch();
		renderArgs.put("activities", activities);
		renderTemplate("attendees/listActivities.html");
	}
	
	public static void getEventLogo(Long eventid) {
		try {
			Event event = Event.findById(eventid);
			response.setContentTypeIfNotSet(event.logo.type());
			java.io.InputStream binaryData = event.logo.get();
			renderBinary(binaryData);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			VirtualFile vf = VirtualFile.fromRelativePath("/public/img/logo_2.png");
			File f  = vf.getRealFile();
			renderBinary(f);
		}
	}
    
}
