import java.net.*; /* ソケットを使用するのでimport */
import java.io.*; /* 入出力ストリームを使用するのでimport */
import java.util.*;

public class ChatClientHandler extends Thread {
    Socket socket;
    BufferedReader in;
    BufferedWriter out; 
    List <ChatClientHandler>clients = new <ChatClientHandler>ArrayList();
    List <ChatClientHandler>blacklist = new <ChatClientHandler>ArrayList();
    String name;
    
    
    ChatClientHandler(Socket sock, List clients) {
	this.socket = sock;
	this.clients = clients;
	this.name = "underfined" + (clients.size() + 1); /* クライアントの */
  	                                                 /*デフォルト名 */
    }
    public void run() {
    }
    
}
    