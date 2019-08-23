package util;

import java.security.NoSuchAlgorithmException;

import models.User;
import play.mvc.Mailer;

public class Mails extends Mailer {
	public static void esqueciSenha(User user) {
		setSubject("[Filhos de Sophia] Solicitação de alteração de senha!");
		addRecipient(user.email);
		setFrom("no-reply <filhosdesophia@gmail.com>");
		send(user);
	}
	
	public static void resetarSenha(User user, String novaSenha) throws NoSuchAlgorithmException {
		setSubject("[Filhos de Sophia] Sua senha foi resetada!");
		addRecipient(user.email);
		setFrom("no-reply <filhosdesophia@gmail.com>");
		send(user, novaSenha);
	}
	
	public static void enviarContato(String nome, String email, String mensagem) {
		setSubject("[Filhos de Sophia] " + nome + " entrou em contato.");
		addRecipient("no-reply <filhosdesophia@gmail.com>");
		setFrom(email);
		
		send(nome, email, mensagem);
	}
}
