import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.util.logging.*;

//import org.apache.commons.io.FileUtils;

public class makeconnection {
	
	
	Properties prop = new Properties();
	 public static String server1 = "";
	 public static String server_user = "";
	 public static String server_password = "";
	 public static String client1 = "";
	 public static String clientuser = "";
	 public static String clientpassword = "";
	 public static String clientproxy = "";
	 public String  smorg = "";
	 public static String command = " subscription-manager ";
	 public String smuser = null;
	 public String smpassword = null;
	 public String sub = "Subscription Name: Awesome OS for x86_64";
	
	 public static Logger logger = Logger.getLogger(makeconnection.class.getName());
	 
	
	 public makeconnection(){
	
		try {
			prop.load(makeconnection.class.getClassLoader().getResourceAsStream("automation1"));
			}
		catch(IOException e ) {System.out.println("File not found ");}
			server1 = prop.getProperty("sm.server.hostname","");
			server_user = prop.getProperty("serveruser","");
			server_password = prop.getProperty("serverpassword","");
			client1 = prop.getProperty("sm.client.hostname","");
			clientuser = prop.getProperty("clientuser","");
			clientpassword = prop.getProperty("clientpassword","");
			smorg = prop.getProperty("smorg","");
			smuser = prop.getProperty("smuser","");
			smpassword = prop.getProperty("smpassword","");
			
	}		
	 
	public  void enablessh(String server,String user,String passwd) throws InterruptedException{
		
		Runtime run = Runtime.getRuntime();
		String server2 = user+"@"+server;
	
//		String cl4 = "sshpass -p " + server_password + " ssh " + server2 + " -o StrictHostKeyChecking=no exit ";
		String cl4 = "sshpass -p " + passwd + " ssh " + server2 + " -o StrictHostKeyChecking=no exit ";
		System.out.println(cl4);
		try {
			Process pr = run.exec(cl4);
			pr.waitFor();
			if(pr.exitValue() != 0){System.out.println("Not able to add host in knownhost file ");
			System.exit(0);
			}
			else {System.out.println("Host scucessfully added in knownhost file");}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File(System.getenv("HOME")+"/.ssh/id_rsa.pub");
		if(file.exists()){
			String cl6 = "sshpass -p " + passwd + " ssh-copy-id -i " + file +" "+ server2;
//			System.out.println(cl6);
			try {
				Process pr2 = run.exec(cl6);
				pr2.waitFor();
				if(pr2.exitValue() !=0){System.out.println("Filed to enable password less login for "+ server);}
				else{System.out.println("Sucessfully enabled paswordless login for " + server);}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Not able to find ssh key file ");
		}
	}
	
	public void register (String user,String password,String org,String serverurl,String client) throws InterruptedException{
		String c_register = ""+command + " register ";
		user = "--u "+ user;
		password = "--password " + password;
		org = " --org " + org;
		client = "root"+"@"+client;
		serverurl = " --serverurl "+ serverurl+":8443/candlepin";
		 
		
		Runtime run = Runtime.getRuntime();
		
	String registercommand = "ssh " + client + c_register + user + password + org + serverurl;
	    System.out.println(registercommand);
	    try {
			Process register = run.exec(registercommand);
			register.waitFor();
			if (register.exitValue()==0){System.out.println(client1 + " sucessfully registered to candlepin");}
			else {System.out.println(client1 + " Registration process failed" );
				System.exit(0);}

//			BufferedReader buf = new BufferedReader(new InputStreamReader(register.getInputStream()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
					 
	}
	public void unregister (String client) throws IOException, InterruptedException{
	
		String c_unregister = command + " unregister ";
		Runtime run = Runtime.getRuntime();
		String unregister = "ssh " + clientuser+"@"+client + c_unregister;
		System.out.println(unregister);
		Process u_client = run.exec(unregister);
		u_client.waitFor();
		if(u_client.exitValue()== 0){
			System.out.println("Sucessfully unregister " + client);
		}
		else{System.out.println("unable to unregister  " + client);}
		
	}
	
	public void subscribepool (String client,String clientuser,String sub1) throws IOException, InterruptedException{
		
		String list_avail = command + "list --avail ";
		String l_avail = "ssh " + clientuser+"@"+client1 + list_avail;
		Runtime run = Runtime.getRuntime();
		Process l_av = run.exec(l_avail);
		l_av.waitFor();
		if (l_av.exitValue()== 0){System.out.println("list available command sucessfully executed for client " + client1);}
		else{System.out.println("list available command failed for " + client1);}
		BufferedReader buf = new BufferedReader(new InputStreamReader(l_av.getInputStream()));
		List<String> aList = new ArrayList<String>();

		String line;
		while ((line=buf.readLine())!=null) {
		aList.add(line);
		}
		System.out.println(aList);
//		String sub1 = "Subscription Name: Awesome OS for x86_64";
		sub1.trim();
		System.out.println(sub1);
//		boolean index = aList.contains(sub1);
		int index2 = aList.indexOf(sub1);
//		index2 = index2++;
//		System.out.println(index2);
//		String[] sub2 = aList.get(index2 +2).trim().split(":");

		String[] sub2 = aList.get(index2 +2).split(":");
		String poolid = sub2[1].trim();
		String sub_pool = "ssh "+ clientuser+"@"+client1 + command + " subscribe  --pool " + poolid;
		Process subpool = run.exec(sub_pool);
		subpool.waitFor();
		if(subpool.exitValue()== 0){System.out.println("Sucessfully subscribed pool " + sub1);}
		else{System.out.println("Not able to subscribe pool " + sub1);}
		
//		System.out.println(sub2);
//		System.out.println(sub2[1]);
		
//		System.out.println(index);
		
		
	}

	
	
}
