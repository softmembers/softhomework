import java.net.*; /* ソケットを使うのでimport */
import java.io.*; /* 入出力ストリームを使うのでimport */
import java.util.*;

public class ChatServer { 
    private ServerSocket server;
    private List clients = new ArrayList();

    public void listen() { 
	try{ 
	    server = new ServerSocket(18080); /* ポート番号指定 */
	    /* サーバが開いたときの出力 */
	    System.out.println("Echoサーバをポート18080で起動しました.");
	    while(true) { 
		Socket socket = server.accept(); /* クライアントからの接続を待つ */
		ChatClientHandler handler = new ChatClientHandler(socket, clients);
		clients.add(handler);
		/* クライアント接続時のサーバでの出力 */
		System.out.println("クライアントが接続してきました."); 
		handler.start(); 
	    } 
	} 
	catch(IOException e) { 
	    e.printStackTrace();
	} 
    } 
    
    public static void main(String[] args) { 
	ChatServer chat = new ChatServer();
	chat.listen();
    } 
} 
