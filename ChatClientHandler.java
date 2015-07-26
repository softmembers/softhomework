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
		}
		/* nameコマンド使用 */
		    else if(commands[0].equalsIgnoreCase("name")) {
			name(commands[1]);
		    }
		/* whoamiコマンド使用 */
		    else if(commands[0].equalsIgnoreCase("whoami")) {
			whoami();
			/* サーバ画面に現在のクライアント名を出力 */
			System.out.println("：" + name);
		    }
		/* usersコマンド使用 */
		    else if(commands[0].equalsIgnoreCase("users")) {
			users();
			System.out.println(""); /* 改行 */
		    }
            /* byeコマンド使用 */
            else if(commands[0].equalsIgnoreCase("bye")) {
                bye();
                /* サーバ画面にチャット終了したクライアント名を出力 */
                System.out.println("：bye " + name);
                break;
            }
	/* postコマンド使用 */
		else if(commands[0].equalsIgnoreCase("post")) {
		    post(commands[1]);
		    System.out.println(""); /* 改行 */
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
    public String getClientName() {
	return name;
    }
/************************************************************************/
    /* 処理可能な命令の一覧を表示させる */
    public void help() throws IOException {
	this.send("help, name, whoami, users, bye, post, tell, reject");
    }
  /************************************************************************/
    /* 名前の変更 */
    public void name(String user) throws IOException { 
	for(int i = 0; i < clients.size(); i++) {
	    ChatClientHandler handler = (ChatClientHandler)clients.get(i);
	    /* 変更したい名前が既に他のクライアントが使用していたら */
	    if(user.equals(handler.name)) { 
		this.send("既に使われている名前です。"); /* エラーメッセージ表示 */
		return; /* 名前の変更は行われない */
	    }
	}
	this.name = user; /* 名前を変更 */
    }
    /************************************************************************/
    /* 現在設定されている名前を表示 */
    public void whoami() throws IOException {
        this.send(this.name);
    }
 /************************************************************************/
    /* 現在チャットに参加しているメンバの名前をクライアントに表示 */
    public void users() throws IOException {
	List names = new ArrayList(); /* 配列names作成 */
	for(int i = 0; i < clients.size(); i++) { 
	    /* ハンドラーにi番目に入ってるクライアント情報を代入 */
	    ChatClientHandler handler = (ChatClientHandler)clients.get(i);
	    names.add(handler.getClientName());	/* namesに情報を記入 */	
	}
	Collections.sort(names); /* 名簿順に並び替え */
	/* メッセージを受け取ったクライアント名を表示 */
	String returnUsers = "";
	for(int i = 0; i < names.size(); i++) {
	    returnUsers = returnUsers + names.get(i) + ",";
	}
	this.send(returnUsers);
    }
    /************************************************************************/
    /* チャットを終了する */
    public void bye() throws IOException {
        this.send("Bye " + this.name); /* 終了のメッセージ表示 */
    }
 /************************************************************************/
    /* 接続しているクライアント全員にメッセージを送る */
    public void post(String message) throws IOException {
	List names = new ArrayList(); /* 配列names作成 */
	for(int i = 0; i < clients.size(); i++) {
	    /* ハンドラーにi番目に入ってるクライアント情報を代入 */
	    ChatClientHandler handler = (ChatClientHandler)clients.get(i);
		if(!name.equals(handler.name)){
		    if(handler != this) { /* 自分以外のクライアントに */
			names.add(handler.getClientName()); /* namesに情報を記入 */
			/* メッセージを受け取ったクライアントに送ったクライアント名と */
			/*メッセージを表示 */
			handler.send("[" + this.getClientName() + "]" + message);
		    }
	    }
	}
	Collections.sort(names); /* 名簿順に並び替え */
	/* メッセージを受け取ったクライアント名を表示 */
	String returnMessage = "";
	for(int i = 0; i < names.size(); i++) {
	    returnMessage = returnMessage + names.get(i) + ",";
	}
	this.send(returnMessage);
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
    