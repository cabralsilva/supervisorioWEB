package service;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.ObjectWriter; 
@ServerEndpoint("/primeiro")
public class TesteWSocket extends Thread{
//	public static int valor = 0;
	public static Session sessao;
	@Override
	public void run() {
		while(true){
			try {
				if (Start.lstAGVS.get(0).getPosicao() < (Start.lstPercurso.size() - 1) )
					Start.lstAGVS.get(0).setPosicao(Start.lstAGVS.get(0).getPosicao()+1);
				else {
					Start.lstAGVS.get(0).setPosicao(0);
					return;
				}
				ObjectMapper ParseJson = new ObjectMapper(); 
				String str = ParseJson.writeValueAsString(Start.lstPercurso.get(Start.lstAGVS.get(0).getPosicao()));
				sessao.getBasicRemote().sendText(str);
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}




	@OnOpen
	public void OnOpen(Session session) {
		System.out.println("OPEN CONNECTION");
		System.out.println("SESSION: " + session);
		this.sessao = session;
		this.start();
	}

	@OnMessage
	public String OnMessage(String string) {
		System.out.println("MESSAGE");
		System.out.println(string);
		return "Mensagem Recebida - retornando ";
	}
	
		
	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("CLOSE CONNECTION");
	}
}
