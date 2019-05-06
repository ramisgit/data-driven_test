package HybridFramework.suites;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import HybridFramework.base.BaseTest;
import HybridFramework.util.Constants;
import HybridFramework.util.DataUtil;

public class TestB extends BaseTest {
	@Test(dataProvider="getData")
	public void testB(Hashtable<String, String> data) throws Exception {
		test.log(Status.INFO, "Starting " + testName);
		test.log(Status.INFO, data.toString());
		
		if (DataUtil.isSkip(testName, xls) || data.get(Constants.RUNMODE_COL).equals("N")) {
			test.log(Status.SKIP, "Runmode set to: N");
			throw new SkipException("Runmode is set to: N");
		}
		
		test.log(Status.INFO, "Executing keywords");
		ds.executeKeywords(testName, xls, data);
		test.log(Status.PASS, "TestB passed");
	}
	
	@DataProvider
	public Object[][] getData(){
		System.out.println("Inside data provider");
		return DataUtil.getTestData(testName, xls);
	}
	
	
	
	
	
	
}
