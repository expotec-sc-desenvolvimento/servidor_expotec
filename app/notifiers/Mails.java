package notifiers;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.ning.http.client.Request;

import models.User;
import play.Play;
import play.libs.Crypto;
import play.mvc.Mailer;

public class Mails extends Mailer {
	public static void emailNewPassword(User u) {
		setFrom("EXPOTEC/SC <ifrn.expotecsc@gmail.com>");
        setSubject("Redefinir Senha");
        addRecipient(u.email);
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        String novaSenha = String.format("%06d", number);
        u.password = Crypto.passwordHash(novaSenha);
        u.save();
        send(u,novaSenha);
    }
	public static void emailCredencials(User u) {
		setFrom("EXPOTEC/SC <ifrn.expotecsc@gmail.com>");
		setSubject("Credenciamento de Usuário");
        addRecipient(u.email);
        String pth  = "http://localhost:9000"+Play.ctxPath+"/credenciar/"+u.uuid;
        send(u, pth);
    }
}
