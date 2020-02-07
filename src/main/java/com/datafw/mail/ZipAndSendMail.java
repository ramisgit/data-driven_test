package com.datafw.mail;


import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

 /*
	The following class zips up the content of the latest report (from theReportDir) and will send it to the user (variables here do need 
	to be changed to the users) Having 2 emails may seem unnecessary but in the situation of remotely running a relatively large test set 
	(with a long run time) this can end up being very convenient
	
	
	toEmails = recipient of report email address 
	fromUser = user sending report email address (gmail)
	password = user sending report password
	
*/
public class ZipAndSendMail
{
	Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;
	static String[] toEmails = {"recipient"};  //recipient of zipped report
	static String fromUser = "fromUser";	//user sending report email (gmail)
	static String password = "password";	//user sending report password

	
    public static void main(String[] args) throws Exception
    {
    	
		//report folder - extent reports
    	Properties prop = new Properties();
    	FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//env.properties");
    	prop.load(fs);
		String reportFolder=prop.getProperty("reportPath");
    	// find latest folder

                File dir = new File(reportFolder);
        	    File[] files = dir.listFiles();
        	    File lastModified = Arrays.stream(files).filter(File::isDirectory).max(Comparator.comparing(File::lastModified)).orElse(null);
        	    System.out.println(lastModified.getName());
        	    
        	//zip
                Zip.zipDir(reportFolder+"\\"+lastModified.getName(), reportFolder+"\\"+lastModified.getName()+".zip");
                
            //mail
                Mail javaEmail = new Mail();

        		javaEmail.setMailServerProperties();
        		

        		javaEmail.createEmailMessage(
        				"Automation Test Reports", // subject
        				"Please find the reports in attachment.", // body
        				reportFolder+"\\"+lastModified.getName()+".zip", // attachment path
        				"Reports.zip", // name of attachment
        				toEmails// receivers
        				);
        		javaEmail.sendEmail(fromUser,password);
        		
        		
      }
    



 
 

}