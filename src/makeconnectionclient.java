import java.io.IOException;
import java.util.Properties;

public class makeconnectionclient extends makeconnection {
	
	
public void deploycandlepin () throws IOException{
//	makeconnection p = new makeconnection();
	try {
		enablessh(server1, server_user,server_password);
		String dis_selinux ="ssh "+ server_user+"@"+server1 + " setenforce=0" ;
		Runtime Run = Runtime.getRuntime();
		Process dis_sel = Run.exec(dis_selinux);
		dis_sel.waitFor();
		if(dis_sel.exitValue()== 0){System.out.println("selinux sucessfully disables at " + server1);}
		else {System.out.println("Not able to disables selinux at " + server1);}
		String dis_iptables = "ssh " + server_user+"@"+server1 + " service iptables stop ";
		Process dis_ipta = Run.exec(dis_iptables);
		dis_ipta.waitFor();
		if(dis_ipta.exitValue()== 0){System.out.println("Iptables stopped at " + server1);}
		else {System.out.println("Not able to stop iptables at " + server1);}
		
		
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
public void connectclient (){
	try {
		enablessh(client1,clientuser,clientpassword);
		Runtime Run = Runtime.getRuntime();
		String copycatmp  = "scp " + server_user+"@"+server1 + ":/etc/candlepin/certs/candlepin-ca.crt" + " /tmp/candlepin-ca.pem";
		String cacopy = "scp " + " /tmp/candlepin-ca.pem " + clientuser+"@"+ client1 +":/etc/rhsm/ca/";
		System.out.println(copycatmp);
		System.out.println(cacopy);
		try {
			Process copyca = Run.exec(copycatmp);
			copyca.waitFor();
			if(copyca.exitValue() == 0){System.out.println("candlepin-ca.crt sucessfully copied to /tmp folder ");}
			else {System.out.println("Not able to copy file in tmp folder");
				System.exit(0);}
			Process copy_cacopy = Run.exec(cacopy);
			copy_cacopy.waitFor();
			if(copy_cacopy.exitValue()== 0){System.out.println("Sucessfully copied ca cert at client location");}
			else{System.out.println("Not able to copy ca cert at client location ");
				System.exit(0);}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void clientregister() throws InterruptedException{
	register(smuser,smpassword,smorg,server1,client1);
	
}
public void  clientsubscribe(){
	try {
		subscribepool(client1,clientuser,sub);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


public static void main (String args[]) throws IOException{
	makeconnectionclient p  = new makeconnectionclient();
	
//	System.out.println(p.smorg);
//	p.test();
	
//	String host = "test1";
//	String user = "test2";
//	String password = "test3";
	
//	makeconnection c = new makeconnection();
//	p.deploycandlepin();
	try {
		p.deploycandlepin();
		p.connectclient();
		p.clientregister();
		p.clientsubscribe();
		p.unregister(client1);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//	try {
		
		
//		c.enablessh(client1, clientuser, clientpassword);
		
//	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	
}
}