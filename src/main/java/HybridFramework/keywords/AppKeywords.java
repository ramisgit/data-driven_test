package HybridFramework.keywords;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;

import HybridFramework.util.DataUtil;
import HybridFramework.util.Xls_Reader;
import sun.util.logging.resources.logging;


public class AppKeywords extends GenericKeywords{
	Map<String, Object[]> dataMapGen = new HashMap<String, Object[]>();
	Xls_Reader xls;
	public void validateLogin() {
		test.log(Status.INFO, "Validating login");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		String expectedResult = data.get(dataKey);
		System.out.println(expectedResult);
		String actualResult;
		boolean result = isElementPresent("crmLink_xpath");
		if (result)
			actualResult = "LoginSuccess";
		else
			actualResult = "LoginFailure";
		
		if (!expectedResult.equals(actualResult)) {
			reportFailure("Got result as " + actualResult);
		}
		System.out.println("xxx" + result);
	}
	public void defaultLogin() {
		String username = envProp.getProperty("default_username");
		String password = envProp.getProperty("default_password");
		System.out.println("Default username is: " + username);
		System.out.println("Default password is: " + password);
	}
	//below is a function specific for jobserve
	public void establishList() {
		List<WebElement> jobElementlist = driver.findElements(By.xpath(prop.getProperty(objectKey)));
		if (jobElementlist.size() != 0) {
			test.log(Status.INFO, jobElementlist.size() + " jobs match your search criteria");
			harvestJobData(jobElementlist.size());
		}else {
			reportFailure("Either there were no available jobs for you or no elements could be selected.");
		}
	}
	public String scrollChecks(String xpathExp, int conf) {
		if (conf == 1) {
			List<WebElement> list = driver.findElements(By.xpath(xpathExp));
			if (list.size() > 0) {
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", list.get(0));
				return list.get(0).getText();
			}else {
				//System.out.println("Element doesnt exist");
				return null;
			}
		}else if (conf == 2) {
			List<WebElement> list = driver.findElements(By.xpath(xpathExp));
			if (list.size() > 0) {
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", list.get(0));
				list.get(0).click();
			}else {
				//System.out.println("Element doesnt exist");
				return null;
			}
		}else if (conf == 3) {
			List<WebElement> list = driver.findElements(By.xpath(xpathExp));
			if (list.size() > 0) {
				((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", list.get(0));
				return list.get(0).getAttribute("href");
			}else {
				//System.out.println("Element doesnt exist");
				return null;
			}
		}
		return null;
	}
	//////////////////////////////JOBSERVE SPECIFIC///////////////////////
	public void doLoginJobserve() throws InterruptedException {
		test.log(Status.INFO, "Attempting to navigate to login page");
		WebDriverWait wait = new WebDriverWait(driver, 20);
		getElement("jobserveLoginMenu_xpath").click();
		Thread.sleep(2000);
		getElement("jobserveDropDownOptionOne_xpath").click();
		Thread.sleep(2000);
		List <WebElement> list =  driver.findElements(By.xpath(prop.getProperty("jobserveAllowCookiesButton_xpath")));
		if (list.size() > 0) list.get(0).click();
		//validate login page
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty("jobserveLoginUserField_xpath"))));
		
		getElement("jobserveLoginUserField_xpath").sendKeys(data.get("Username"));
		getElement("jobserveLoginPassField_xpath").sendKeys(data.get("Password"));
		Thread.sleep(1000);
		getElement("jobserveSubmitLogin_xpath").click();
		Thread.sleep(3000);
		test.log(Status.INFO, "Successfully logged in");
	}
	public void deleteAndUpdateCV() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		test.log(Status.INFO, "Navigating to profile.");
		getElement("jobserveMyAccount_xpath").click();
		Thread.sleep(2000);
		getElement("jobserveProfile_xpath").click();
		Thread.sleep(3000);
		test.log(Status.INFO, "Detecting CV...");
		List<WebElement> listOfValidation = driver.findElements(By.xpath(prop.getProperty("jobserveElementOnPage_xpath")));
		if (listOfValidation.size() > 0) {
			test.log(Status.INFO, "Uploading CV");
			Thread.sleep(2000);
			getElement("uploadCV_xpath").sendKeys(prop.getProperty("path_to_cv"));
			Thread.sleep(1000);
			getElement("jobserveCreateProfileButton_xpath").click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty("jobserveValidateProfile_xpath"))));
			getElement("jobserveValidateProfile_xpath").click();
			Thread.sleep(1000);
			driver.get(envProp.getProperty("urlJobserve"));
			test.log(Status.INFO, "CV successfully uploaded");
		}else {
			test.log(Status.INFO, "Deleting old CV");
			getElement("jobserveDeleteCV_xpath").click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty("jobserveDeleteCVAccept_xpath"))));
			getElement("jobserveDeleteCVAccept_xpath").click();
			test.log(Status.INFO, "Successfully deteted old CV");
			Thread.sleep(2000);
			//getElement("jobserveDeleteCVAcceptINFO_xpath").click();
			Thread.sleep(2000);
			test.log(Status.INFO, "Uploading latest CV");
			getElement("jobserveMyAccount_xpath").click();
			Thread.sleep(2000);
			getElement("jobserveUpload_xpath").click();
			getElement("uploadCV_xpath").sendKeys(prop.getProperty("path_to_cv"));
			Thread.sleep(2000);
			getElement("jobserveBtnUpload_xpath").click();
			test.log(Status.INFO, "Successfully uploaded CV");
		}
		Thread.sleep(3000);
		
	}
	public void harvestJobData(int sizeOfList) {
		dataMapGen.put("1", new Object[] {"JobserveEmailData", "totalJobs = " + sizeOfList, "", "", "", "", "", "", "", ""});
		dataMapGen.put("2", new Object[] {"Runmode", "jobTitle", "jobBody", "jobLocation", "jobStartDate", "jobDuration", "jobserveSalary", "jobserveAgency", "jobserveContact", "jobservePostDate", "jobserveJobLink", "jobserveJobEmail"});
		System.out.println("Execution will look like this...............");
		int count = 1;
		for (int i = 1; i <= sizeOfList; i++) {
			System.out.println("List size " + sizeOfList);
			if (!driver.findElement(By.xpath(prop.getProperty("jobserveJobItem_xpath"))).getClass().equals("jobSelected")) {
				System.out.println(count);	
				scrollChecks("//div[@id='jsJobResContent']/div[" + count + "]", 2);
				
				String jobLocation = null; String jobStartDate = null; String jobSalary = null; String jobBody = null;
				String jobAgency = null; String jobContact = null; String jobPostDate = null; String jobTitle = null;
				String jobLink = null; String Runmode = "N"; String jobEmail = null; String jobDuration = null;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				///improved version
				WebDriverWait wait = new WebDriverWait(driver, 20);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty("applyButton_xpath"))));
					//	.elementToBeClickable(getElement("applyButton_xpath")));		//.visibilityOf(e));
				
				
				jobTitle = scrollChecks(prop.getProperty("jobserveJobTitle_xpath"), 1);
				jobBody = scrollChecks(prop.getProperty("jobserveJobBody_xpath"), 1);
				jobLocation = scrollChecks(prop.getProperty("jobserveLocation_xpath"), 1);
				jobStartDate = scrollChecks(prop.getProperty("jobserveStartDate_xpath"), 1);
				jobSalary = scrollChecks(prop.getProperty("jobserveSalary_xpath"), 1);
				jobAgency = scrollChecks(prop.getProperty("jobserveAgency_xpath"), 1);
				jobContact = scrollChecks(prop.getProperty("jobserveContact_xpath"), 1);
				jobPostDate = scrollChecks(prop.getProperty("jobservePostDate_xpath"), 1);
				jobLink = scrollChecks(prop.getProperty("jobserveLink_xpath"), 1);
				jobDuration = scrollChecks(prop.getProperty("jobDuration_xpath"), 1);
				jobEmail = scrollChecks(prop.getProperty("jobserveEmail_xpath"), 3);
				if (jobEmail != null) {
					String[] jobEmailMod = jobEmail.split(":");
					jobEmail = jobEmailMod[1].split("\\?")[0];
				}
				////////EXPERIMENTAL///////////////
				dataMapGen.put(Integer.toString(count+2), new Object[] { Runmode , jobTitle, jobBody, jobLocation, jobStartDate, jobDuration, jobSalary, jobAgency, jobContact, jobPostDate, jobLink, jobEmail });
				System.out.println("i: " + jobLocation + " | " + jobStartDate + " | " + jobSalary + " | " + jobAgency + " | " + jobContact + " | " + jobPostDate + " | " + jobLink);
				//System.out.println(dataMap.get(Integer.toString(i+1))[0]);
				if (i % 20 == 0) i = 0; 
				List<WebElement> nextElement = driver.findElements(By.xpath("//div[@id='jsJobResContent']/div[" + (count + 1) + "]"));
				if (nextElement.size() == 0 || count == 20) { //remove the second or statement
					break;
				}
				count++;
			}
		}	
		try {
			DataUtil.writeToXls(dataMapGen, envProp.getProperty("resultsXlsx"));
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		//} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}