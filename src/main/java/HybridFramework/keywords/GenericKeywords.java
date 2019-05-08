package HybridFramework.keywords;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import HybridFramework.reports.ExtentManager;




public class GenericKeywords {
	public Properties envProp;
	public Properties prop;
	public String objectKey;
	public String dataKey;
	public String proceedOnFail;
	public String s = null;
	public Hashtable<String, String> data;
	public WebDriver driver;
	public ExtentTest test;
	public SoftAssert softAssert = new SoftAssert();
	
	public void openBrowser() {
		String bType = dataKey;
		System.out.println(dataKey);
		test.log(Status.INFO, "Opening browser " + bType);
		if (bType.equals("Mozilla")) {
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "null");
			System.out.println(System.getProperty("user.dir") + "\\drivers\\geckodriver.exe");
			//invoke profile
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
		}else if(bType.equals("Chrome")) {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		}else if(bType.equals("IE")) {
			driver = new InternetExplorerDriver();
		}else if (bType.equals("Edge")) {
			driver = new EdgeDriver();
		} else {
			System.out.println("Unable to open browser");
			System.exit(0);
		}
		driver.manage().window().maximize();
		
	}
	public void navigate() {
		test.log(Status.INFO, "Navigating to website " + objectKey);
		driver.get(objectKey);
	}
	public void click() {
		test.log(Status.INFO, "Clicking " + objectKey);
		getElement(objectKey).click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}
	public void type() {
		test.log(Status.INFO, "Typing in " + objectKey + " . Data - " + dataKey);
		getElement(objectKey).sendKeys(dataKey);
	}
	public void validateTitle() {
		test.log(Status.INFO, "Validating title - " + prop.getProperty(objectKey));
		String expectedTitle = prop.getProperty(objectKey);
		String actualTitle = driver.getTitle();
		if (!actualTitle.contains(expectedTitle)) {
			reportFailure("Titles did not match. Expected: " + expectedTitle + ", Actual: " + actualTitle);
		}
	}
	public void quit() {
		if (driver != null) driver.quit();
	}
	public void manageAlert() throws InterruptedException {
		try {
			Thread.sleep(2000);
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			test.log(Status.INFO, "Alert accepted successfully");
		}catch(Exception e) {
			test.log(Status.INFO, "Alert not present");	
		}
	}
	public void getTextOffScreen() {
		test.log(Status.INFO, "Attempting to grab text from " + prop.getProperty(objectKey) + " text: " + s);
		String xpath;
		s= getElement(prop.getProperty(prop.getProperty(dataKey))).getText();
		
		
	}
	public void select() {
		test.log(Status.INFO, "Selecting from " + prop.getProperty(objectKey) + " . Data - " + prop.getProperty(dataKey));
		List<WebElement> options = new Select(getElement(objectKey)).getOptions();
		for (int i = 0; i < options.size(); i++) {
			//if (options.get(i).getText().equals(data.get(dataKey)))
			if (options.get(i).getText().trim().equals(dataKey))	
				break;
			if (i == options.size() - 1) {
				reportFailure("Option not found in DropDown: " + dataKey);
			}
		}
		new Select(getElement(objectKey)).selectByVisibleText(dataKey);
	}
	public WebElement getElement(String objectKey) {
		WebElement e = null;
		try {
			e = driver.findElement(By.xpath(objectKey));
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOf(e));
		}catch(Exception ex) {
			ex.printStackTrace();
			reportFailure("WebElement not found: " + objectKey);
		}
		return e;
	}
	public void setEnvProp(Properties envProp) {
		this.envProp = envProp;
	}
	public void setProp(Properties prop) {
		this.prop = prop;
	}
	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
	public void setData(Hashtable<String, String> data) {
		this.data = data;
	}
	public void setExtentTest(ExtentTest test) {
		this.test = test;
	}
	public String getProceedOnFail() {
		return proceedOnFail;
	}
	public void setProceedOnFail(String proceedOnFail) {
		this.proceedOnFail = proceedOnFail;
	}
	public boolean pressEnter() {
		getElement(objectKey).sendKeys(Keys.ENTER);
		
		
		
		return true;
		
	}
	public boolean isElementPresent(String objectKey) {
		List<WebElement> list = null;
		if (objectKey.endsWith("_id")){
			list = driver.findElements(By.id(objectKey));
		}else if (objectKey.endsWith("_name")){
			list = driver.findElements(By.name(objectKey));
		}else if (objectKey.endsWith("_xpath")){
			list = driver.findElements(By.xpath(objectKey));
		}else if (objectKey.endsWith("_css")) {
			list = driver.findElements(By.cssSelector((objectKey)));
		}
		if (list.size() == 0) return false;
		else return true;
	}
	/**********************Reporting function*************************/
	public void reportFailure(String failureMsg) {
		test.log(Status.FAIL, failureMsg);
		takeScreenShot();
		if (proceedOnFail.equals("Y")) {
			softAssert.fail(failureMsg);
		}else {
			softAssert.fail(failureMsg);
			softAssert.assertAll();
		}
		//fail the test
		//take the screenshot and imbed in reports
	}
	public void assertAll() {
		softAssert.assertAll();
	}
	public void takeScreenShot() {
		Date t = new Date();
		String screenshotFile = t.toString().replace(":", "_").replace(" ", "_") + ".png";
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + screenshotFile));
			test.log(Status.INFO, "Screenshot -> " + test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath + screenshotFile));
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
