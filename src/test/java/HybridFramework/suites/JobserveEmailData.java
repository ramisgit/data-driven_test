package HybridFramework.suites;

import java.io.File;
import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import HybridFramework.base.BaseTest;
import HybridFramework.util.Constants;
import HybridFramework.util.DataUtil;
import HybridFramework.util.Xls_Reader;

public class JobserveEmailData extends BaseTest {
	@Test(dataProvider="getData")
	public void loginTest(Hashtable<String, String> data) throws Exception {
		test.log(Status.INFO, "Starting " + testName);
		if (DataUtil.isSkip(testName, xls) || data.get(Constants.RUNMODE_COL).equals("N")) {
			test.log(Status.SKIP, "Runmode set to: N");
			throw new SkipException("Runmode is set to: N");
		}
		ds.executeKeywords(testName, xls, data);
		test.log(Status.PASS, "Passed " + testName);
	}
	
	@DataProvider
	public Object[][] getData(){
		System.out.println("Inside data provider"); 
		return DataUtil.getTestData(testName, new Xls_Reader(envProp.getProperty("resultsXlsx")));
	}
}
