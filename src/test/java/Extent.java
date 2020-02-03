import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
public class Extent {
	ExtentHtmlReporter reporter;
	ExtentReports reports;
	ExtentTest logger;
	WebDriver driver;
    @BeforeClass
    public void StartTest()
    {
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-ms");
    	String path = System.getProperty("user.dir")+"/extent-reports/"+sdf.format(new Date())+".html";
    	reporter =new ExtentHtmlReporter(path);
    	reports = new ExtentReports();
    	reports.attachReporter(reporter);
    	reports.setSystemInfo("Hostname","Localhost");
    	reports.setSystemInfo("Enviroment","Testing Enviorment");
    	reports.setSystemInfo("Username","Remi");
    	reporter.config().setDocumentTitle("Remi's HTML Report");
    	reporter.config().setReportName("Next Gen Testing report");
    	reporter.config().setTheme(Theme.DARK);
    	    	
    }
    @AfterClass
    public void endtest()
    {
    	reports.flush();
    }
    @AfterMethod
    public void captureStatus(ITestResult result)
    {
    	if(result.getStatus()==ITestResult.SUCCESS)
    	{
    		logger.log(Status.PASS, result.getMethod()+" test is passed");
    	}
    	else if(result.getStatus()==ITestResult.FAILURE)
    	{
    		String imagepath=System.getProperty("user.dir")+"/extent-reports/capture/"+result.getMethod().getMethodName()+".png";
    		
    		logger.log(Status.FAIL,result.getMethod().getMethodName()+" test is failed");
    		
    		TakesScreenshot capture=(TakesScreenshot) driver;
    		File src =capture.getScreenshotAs(OutputType.FILE);
    		//logger.addScreenCaptureFromPath(imagePath)
    		try
    		{
    			FileUtils.copyFile(src, new File(imagepath));
    			logger.addScreenCaptureFromPath(imagepath);
    		}catch (IOException e)
    		{
    		     e.printStackTrace();
    		}
    	}
    	else if(result.getStatus()==ITestResult.SKIP)
    	{
    		logger.log(Status.SKIP,result.getMethod().getMethodName()+" test is skipped");
    		
    	}
    	
    }
    @Test
    public void passTest()
    {
    	logger=reports.createTest("Pass Test");
    	Assert.assertTrue(true);
    }
    @Test
    public void demowebShopLoginTest()
    {
    	logger=reports.createTest("Fail Test");
    	System.setProperty("webdriver.chrome.driver", "C:\\selenium driver\\chromedriver_win32\\chromedriver.exe");
	       driver= new ChromeDriver();	       
	       //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	       driver.get("http://demowebshop.tricentis.com/login");
	       driver.manage().window().maximize();
	       driver.findElement(By.id("Email")).sendKeys("remi.jullian@accenture.com");
	       driver.findElement(By.id("Pass")).sendKeys("test@1234");
    }
    @Test
    public void skipTest()
    {
    	logger=reports.createTest("Skip test");
    	throw new SkipException("Some reason");
    }
   
}
