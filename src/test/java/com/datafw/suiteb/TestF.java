package com.datafw.suiteb;

import java.util.Hashtable;
import java.util.Map;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import com.datafw.base.BaseTest;
import com.datafw.util.Constants;
import com.datafw.util.DataUtil;

public class TestF extends BaseTest {
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
	@DataProvider
	public Object[][] getData(){
		System.out.println(testName);
		System.out.println("Inside data provider");
		return DataUtil.getTestData(testName, xls);
	}
}