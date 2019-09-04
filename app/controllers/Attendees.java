package controllers;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Activity;
import models.Area;
import models.Event;
import models.Paper;
import models.PaperStatus;
import models.Permission;
import models.User;
import play.Play;
import play.data.validation.Valid;
import play.libs.Crypto;
import play.mvc.After;
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
		renderArgs.put("ulogado", user);
		renderArgs.put("event", event);
	}

	public static void printMyBadge() {
		renderTemplate("Attendees/cracha.html");
	}

	public static void cpanel() {
		renderTemplate("Attendees/cpanel.html");
	}

	public static void viewMyProfile() {
		renderTemplate("Attendees/viewMyProfile.html");
	}

	public static void listMyPapers() {
		renderTemplate("Attendees/listMyPapers.html");
	}

	public static void editMyProfile() {
		User user = User.findById(Long.parseLong(session.get("userid")));
		renderArgs.put("user", user);
		renderTemplate("Attendees/editMyProfile.html");
	}

	public static void editMyPaper(Long idPaper) {
		Paper paper = Paper.findById(idPaper);
		User user = User.findById(Long.parseLong(session.get("userid")));

		if ((paper.author == user || paper.coauthors.contains(user))) {
			renderArgs.put("paper", paper);
			Event event = Event.findById(Long.parseLong(session.get("eventid")));

			if (paper.status == PaperStatus.DRAFT) {
				renderArgs.put("tracks", event.getOpenedTracks());
				renderTemplate("Attendees/editMyPaper.html");
			} else {
				detailMyPaper(idPaper);
			}
		} else {
			renderText("Acesso negado!");
		}
	}

	public static void detailMyPaper(Long idPaper) {
		Paper paper = Paper.findById(idPaper);
		User user = User.findById(Long.parseLong(session.get("userid")));

		if ((paper.author == user || paper.coauthors.contains(user))) {
			renderArgs.put("paper", paper);
			renderTemplate("Attendees/detailMyPaper.html");
		} else {
			renderText("Acesso negado!");
		}
	}

	public static void myBadge() {
		renderTemplate("Attendees/myBadge.html");
	}

	public static void getUserPicture(Long userid) {
		try {
			final User user = User.findById(userid);
			System.out.println();
			if(user.picture.get() != null) {
				response.setContentTypeIfNotSet(user.picture.type());
				InputStream binaryData = user.picture.get();
				renderBinary(binaryData);
			}
			InputStream is = null;
			renderBinary(is);
		}catch (NullPointerException e) {
			
		}
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
		if (logado.uuid != user.uuid) {
			renderJSON("Você não tem permissão para realizar essa operação");
		} else {
			logado.password = Crypto.passwordHash(user.password);
			logado.save();
			renderJSON("Senha alterada com sucesso!");
		}
	}

	public static void saveMyProfile(@Valid User user) {
		if (validation.hasErrors()) {
			System.out.println(validation.errorsMap());
			renderArgs.put("user", user);
			flash.error("erro.operacao");
			editMyProfile();
		}

		User logado = User.findById(Long.parseLong(session.get("userid").toString()));
		if (logado.uuid != user.uuid) {
			renderArgs.put("user", user);
			flash.error("erro.permissao");
			editMyProfile();
		} else {
			logado.name = user.name;
			logado.email = user.email;
			logado.cel = user.cel;
			logado.cpf_suap = user.cpf_suap;
			logado.outsider = user.outsider;
			if (user.picture != null) {
				logado.picture = user.picture;
			}
			logado.save();
			viewMyProfile();
		}
	}

	public static void savePaper(Paper paper) {
		listMyPapers();
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
			File f = vf.getRealFile();
			renderBinary(f);
		}
	}

	public static void carregarAreas() {
		List<Area> areas = Area.findAll();
		renderJSON(areas);

	}

	public static void carregarUser(Long id) {
		User user = User.findById(id);
		renderJSON(user);
	}

	public static void carregarActiveUsers(String name) {
		User logado = User.findById(Long.parseLong(session.get("userid").toString()));

		List<User> _users = User.find("select distinct u from User u where lower(u.name) like '%" + name.toLowerCase()
				+ "%' and u.status = true and u.uuid != " + logado.uuid).fetch();
		List<User> users = new ArrayList<User>();
		for (User u : _users) {
			User novo = new User(u.uuid, u.name, u.picture);
			users.add(novo);
		}
		renderJSON(users);

	}

}
