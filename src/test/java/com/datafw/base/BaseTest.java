package com.datafw.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.datafw.drivers.DriverScript;
import com.datafw.reports.ExtentManager;
import com.datafw.util.Xls_Reader;

public class BaseTest {
	public DriverScript ds;
	public String testName;
	public Properties envProp;
	public Properties prop;
	public Xls_Reader xls;
	public String suiteName; 
	public ExtentReports rep;
	public ExtentTest test;
	
	@BeforeTest
	public void init() throws IOException {
		testName = this.getClass().getSimpleName();
		System.out.println("test name = " + testName + " -- delete this");
		String arr[] = this.getClass().getPackage().getName().split("\\.");
		suiteName = arr[arr.length-1];
		
		prop = new Properties();
		envProp = new Properties();
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "//sheets//test-data//env.properties");
		prop.load(fs);
		String env = prop.getProperty("env");
		fs = new FileInputStream(System.getProperty("user.dir") + "//sheets//test-data//" + env + ".properties");
		envProp.load(fs);
		System.out.println("Suite name is " + suiteName);
		xls = new Xls_Reader(System.getProperty("user.dir") + "//" + envProp.getProperty(suiteName+"_xls"));
		
		//init
		ds = new DriverScript();
		ds.setEnvProp(envProp);
		ds.setProp(prop);
		
	}
	@BeforeMethod
	public void initTest() {
		rep = ExtentManager.getInstance(prop.getProperty("reportPath"));
		test = rep.createTest(testName);
		ds.setExtentTest(test);
	}
	@AfterMethod
	public void quit() throws IOException, EncryptedDocumentException, InvalidFormatException {
		if (ds != null) ds.quit();
		if (rep != null) rep.flush();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
