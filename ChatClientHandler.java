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
	try {
	    open();
	    while(true) {
		String message = receive();
		String[] commands = message.split(" ");
		/* helpコマンド使用 */
		if(commands[0].equalsIgnoreCase("help")) {
		    help(); 
		    System.out.println("：help, name, whoami, users, bye, post, tell, reject"); /* サーバ画面に出力 */
		}
	    }
	}
	catch(IOException e) {
	    e.printStackTrace();
	}
	finally {
	    close();
	}
    }
/************************************************************************/
    /* 処理可能な命令の一覧を表示させる */
    public void help() throws IOException {
	this.send("help, name, whoami, users, bye, post, tell, reject");
    }
    /************************************************************************/
    /* クライアントとのデータのやり取りを行うストリームを開く */
    public void open() throws IOException {
	/* socketから入力ストリームを取得し、入力ストリームからデータを読み込む */
	InputStream socketIn = socket.getInputStream(); 
	in = new BufferedReader(new InputStreamReader(socketIn));
	/* socketから出力ストリームを取得し、出力ストリームにデータを書き出す */
	OutputStream socketOut = socket.getOutputStream();
	Writer streamWriter = new OutputStreamWriter(socketOut);
	out = new BufferedWriter(streamWriter);
    }
    /************************************************************************/
    /* クライアントからデータを受け取る */
    String receive() throws IOException {
	String line = in.readLine();
	System.out.print(line); /* クライアントからのデータをサーバで出力 */
	return line;
    }
    
    /************************************************************************/
    /* クライアントにデータを送信する */
    void send(String message) throws IOException {
	out.write(message); 
	out.write("\r\n"); 
	out.flush();
    }
     /************************************************************************/
    /* クライアントとの接続を閉じる */
    void close() {
	if(in != null) {
	    try {
		in.close(); }
	    catch(IOException e) { }
	}
	if(out != null) {
	    try {
		out.close(); }
	    catch(IOException e) { }
	}
	if(socket != null) {
	    try {
		socket.close(); }
	    catch(IOException e) { }
	}	    
    }
}
    