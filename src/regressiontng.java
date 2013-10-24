
		
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

//import org.testng.annotations.Test;

//import org.testng.annotations.Test;

//@Test(groups={"regression"})
public class regressiontng extends makeconnectionclient {


private String Poolid;

	// Testcase https://tcms.engineering.redhat.com/case/148182/?from_plan=5846
	@BeforeSuite()
	public void setup(){
	try {
		deploycandlepin();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	connectclient();
	try {
		clientregister();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//	String Sub = "Awesome OS Server Basic (multi-entitlement)";
	String l_avail = "ssh " + clientuser+"@"+client1 + command + "list --avail  | grep -A10 " + "\"Awesome OS Server Basic (multi-entitlement)\"" ;
	System.out.println(l_avail);
	Runtime run = Runtime.getRuntime();
	Process l_av = null;
	try {
		l_av = run.exec(l_avail);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		l_av.waitFor();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	if (l_av.exitValue()== 0)
	{
		System.out.println("list available command sucessfully executed for client " + client1);
		logger.info("list available command sucessfully executed for client " + client1);
		}
	else
	{
		System.out.println("list available command failed for " + client1);
		logger.info("list available command failed for " + client1);
		}
	BufferedReader buf = new BufferedReader(new InputStreamReader(l_av.getInputStream()));
	List<String> aList = new ArrayList<String>();
	String line;
	try {
		while ((line=buf.readLine())!=null) {
		aList.add(line);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println(aList);
	
	String[] Pool = aList.get(2).trim().split(":");
	String Poolid = Pool[1].trim();
	System.out.println("Pool id to subscribe" + Poolid);
	logger.info("Pool id to subscribe" + Poolid);
	}	
	
	@Test()
	public void quantitysubs()throws InterruptedException, IOException {
				
		
		
// Subscribe more than available quantity Poolid ***************************************************
		int scenario1 = 0;
		String overconsume = "ssh " + clientuser+"@"+client1 + command + " subscribe --pool " + Poolid + " --quantity 20";
		Runtime run = Runtime.getRuntime();
		Process oconsume = run.exec(overconsume);
		oconsume.waitFor();
		Assert.assertNotEquals(oconsume.waitFor(), 0);
		if(oconsume.exitValue()==0)
		{
			System.out.println("Sucessfully consumed more than available quantity ");
			System.out.println("Fail:able to subscribe more than available quantity");
			logger.info("Fail:able to subscribe more than available quantity");
				}
		else 
		{
			System.out.println("Not able to consume more than available subscriptiuon");
			System.out.println("Pass: Not able to consume more than available quantity");
			logger.info("Pass: Not able to consume more than available quantity");
			scenario1 = 1;
			}
		unregister(client1);
	}
	@Test()
	public void invalidargs(){
		String invalidargs = "ssh " + clientuser+"@"+client1 + command + " subscribe --pool " + Poolid + " --quantity -2";
		Runtime run = Runtime.getRuntime();
		Process iargs = null;
		try {
			iargs = run.exec(invalidargs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			iargs.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotEquals(iargs.exitValue(), 0);
		if(iargs.exitValue()==0)
		{
			System.out.println("Sucessfully consumed invalid args ");
			System.out.println("Fail:able to subscribe invalid  quantity");
			logger.info("Fail:able to subscribe invalid  quantity");
			}
		else 
		{
			System.out.println("Not able to consume invalid quantity");
			System.out.println("Pass: Not able to consume invalid quantity");
			logger.info("Pass: Not able to consume invalid quantity");
				
				}
	}
}