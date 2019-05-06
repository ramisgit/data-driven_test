package HybridFramework.suites;

import java.util.Hashtable;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import HybridFramework.base.BaseTest;
import HybridFramework.util.Constants;
import HybridFramework.util.DataUtil;

public class EON extends BaseTest {
	Map<String, Object[]> dataMap;
	@Test(dataProvider="getData")
	public void JobserveTest(Hashtable<String, String> data) throws Exception {
		test.log(Status.INFO, "Starting " + testName);
		if (DataUtil.isSkip(testName, xls) || data.get(Constants.RUNMODE_COL).equals("N")) {
			test.log(Status.SKIP, "Runmode set to: N");
			throw new SkipException("Runmode is set to: N");
		}
		System.out.println("Running " + testName);
		ds.executeKeywords(testName, xls, data);
		test.log(Status.PASS, "Passed " + testName);
	}
	//requires a valid username before submiting password
	//and redirects to homepage when username is wrong
	@DataProvider
	public Object[][] getData(){
		System.out.println(testName);
		System.out.println("Inside data provider");
		return DataUtil.getTestData(testName, xls);
	}
}