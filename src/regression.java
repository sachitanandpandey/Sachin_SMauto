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
import org.testng.annotations.Test;

//import org.testng.annotations.Test;

//import org.testng.annotations.Test;

//@Test(groups={"regression"})
public class regression extends makeconnectionclient {

	/**
	 * @param args
	 * @return 
	 * @throws InterruptedException 
	 */
//	@Test()
/*	
	public void showowner() throws InterruptedException{
		try {
			deploycandlepin();
			connectclient();
			clientregister();
			unregister(client1);
			Runtime Run = Runtime.getRuntime();
			String sow = "ssh " + clientuser+"@"+client1 + command +" orgs " +  "--u " + smuser + " --password " + smpassword  ;
			Process sowner = Run.exec(sow);
			sowner.waitFor();
			if (sowner.exitValue()==0)
			{
				System.out.println("Owner list command sucessfully executed");
				logger.info("Owner list command sucessfully executed");
				}
			else
			{
				System.out.println("Owner list not displayed");
				logger.info("Owner list not displayed");
				}
			BufferedReader buf = new BufferedReader(new InputStreamReader(sowner.getInputStream()));
			List<String> n_org_list = new ArrayList<String>();
			String line;
			while ((line=buf.readLine())!=null) {
				n_org_list.add(line);
			}
			
			// command to check older owner list 
			String oldowner = "cat /home/spandey/workspace/candlepintest/src/data/orglist";
			Process o_owner = Run.exec(oldowner);
			o_owner.waitFor();
			if(o_owner.exitValue()== 0)
			{
				System.out.println("Sucessfully recovered owner list from data");
				logger.info("Sucessfully recovered owner list from data");
				}
			else
			{
				System.out.println("Not able to recover list from data");
				logger.info("Not able to recover list from data");
				}
			
			BufferedReader bu2 = new BufferedReader(new InputStreamReader(o_owner.getInputStream()));
			List<String> o_org_list = new ArrayList<String>();
			String line2;
			while ((line2=bu2.readLine())!=null) {
				o_org_list.add(line2);
				
			}

			System.out.println(n_org_list);
			System.out.println(o_org_list);
			if ((n_org_list).equals(o_org_list))
			{
				System.out.println("Cli : Display user owner list : Pass");
				logger.info("Cli : Display user owner list : Pass");
				}
			else
			{
				System.out.println("Cli : Display user owner list : Fail ");
				logger.info("Cli : Display user owner list : Fail ");
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/
// Testcase https://tcms.engineering.redhat.com/case/148182/?from_plan=5846
	@Test()
	public void quantitysubs()throws InterruptedException, IOException {
		deploycandlepin();
		connectclient();
		
		clientregister();
//		String Sub = "Awesome OS Server Basic (multi-entitlement)";
		String l_avail = "ssh " + clientuser+"@"+client1 + command + "list --avail  | grep -A10 " + "\"Awesome OS Server Basic (multi-entitlement)\"" ;
		System.out.println(l_avail);
		Runtime run = Runtime.getRuntime();
		Process l_av = run.exec(l_avail);
		l_av.waitFor();
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
		while ((line=buf.readLine())!=null) {
		aList.add(line);
		}
		System.out.println(aList);
		String[] Pool = aList.get(2).trim().split(":");
		String Poolid = Pool[1].trim();
		System.out.println("Pool id to subscribe" + Poolid);
		logger.info("Pool id to subscribe" + Poolid);
		
// Subscribe more than available quantity Poolid ***************************************************
		int scenario1 = 0;
		String overconsume = "ssh " + clientuser+"@"+client1 + command + " subscribe --pool " + Poolid + " --quantity 20";

		Process oconsume = run.exec(overconsume);
		oconsume.waitFor();
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
// Subscribe with invalid argument *************************************************************
		int scenario2 = 0;
		String invalidargs = "ssh " + clientuser+"@"+client1 + command + " subscribe --pool " + Poolid + " --quantity -2";

		Process iargs = run.exec(invalidargs);
		iargs.waitFor();
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
				scenario2 = 1;
				}

// Subscribe more than 1 quantity **************************************************************** 
		int scenario3 = 0;
		String validargs = "ssh " + clientuser+"@"+client1 + command + " subscribe --pool " + Poolid + " --quantity 3";

		Process vargs = run.exec(validargs);
		vargs.waitFor();
		if(vargs.exitValue()==0)
		{
			System.out.println("Command Sucessfully consumed more than 1 quantity ");
				System.out.println("able to subscribe more than 1 quantity ");
				logger.info("able to subscribe more than 1 quantity ");
				
//Verify at client side 
				String consumelist = "ssh " + clientuser+"@"+client1 + command + " list --consumed ";
				System.out.println(consumelist);
				Process clist = run.exec(consumelist);
				clist.waitFor();
				if(clist.exitValue()==0)
				{
					System.out.println("Sucessfully collected consumed list ");
					logger.info("Sucessfully collected consumed list ");
					BufferedReader buf1 = new BufferedReader(new InputStreamReader(clist.getInputStream()));
					List<String> cList = new ArrayList<String>();
					String cline;
					while ((cline=buf1.readLine())!=null)
					{
					cList.add(cline);
					}
					System.out.println(cList);
					String [] conlist = cList.get(11).trim().split(":");
					int cquantity = Integer.parseInt(conlist[1].trim());
					System.out.println(cquantity);
					if (cquantity == 3 )
					{
						System.out.println("Subscribe quantity matched");
						logger.info("Subscribe quantity matched");
						scenario3 = 1;
					}
					else 
					{
						System.out.println("subscribe quantity not matched");
						logger.info("subscribe quantity not matched");
						}
				}
				else
				{
					System.out.println("Not working");
					}
		}
		else 
		{
			System.out.println("Not able to consume more than 1  quantity");
			System.out.println("Fail: Not able to consume more than 1 quantity");
			logger.info("Fail: Not able to consume more than 1 quantity");
			}
		
		if (scenario1 == scenario2 && scenario1 == scenario3)
		{
			System.out.println("Tescase : subscribe subscription quantity : Pass" );
			logger.info("Tescase : subscribe subscription quantity : Pass");
		}
		else
		{
			System.out.println("Tescase : subscribe subscription quantity : Fail" );
			logger.info("Tescase : subscribe subscription quantity : Fail");
			
			}
		
// Cleaning setup 
		unregister(client1);
		
	}

	public static void main(String[] args) throws SecurityException, IOException {
		// TODO Auto-generated method stub
		regression cli = new regression();
		Date d = new Date();
		Handler fh = new FileHandler("regressionlog/"+d+"Regression.log",true);
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		try {
//			cli.showowner();
			cli.quantitysubs();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
