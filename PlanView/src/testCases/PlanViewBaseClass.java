package testCases;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PlanViewBaseClass {

	static {
		String path = System.getProperty("user.dir");
		System.out.println("Chromedriver loading... " + path);
		System.setProperty("webdriver.chrome.driver", path + "\\resources\\chromedriver.exe");

	}

	public static void main(String[] args) throws InterruptedException, AWTException, IOException {

		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 10000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://worldline.pvcloud.com/");
		driver.manage().window().maximize();

		driver.findElement(By.xpath("//div[@class='wg-pki']//input[4]")).click(); // click on login Button

		Thread.sleep(10000L);// Wait for user to select certificate and enter PIN

		if (driver.getTitle().contains("My Overview - Planview")) {
			System.out.println("Login Successful Dashboard Navigation Complete....");
		}

		SearchProject obj1 = new SearchProject();
		obj1.searchbyprojectname(driver, wait);

		WorkAssignmentPage workAssignmentPage = new WorkAssignmentPage();
		workAssignmentPage.navigateWorkAssignmentPage(driver, wait);

		Actions action1 = new Actions(driver);
		workAssignmentPage.createTask(driver, wait, action1);

		NewRequirement newRequirement = new NewRequirement();
		newRequirement.addNewRequirement(driver, action1, "GSDRS/SDCO/GSC\\SPM03-Senior Consultant - Testing");

		AddAllocation addAllocation = new AddAllocation();
		addAllocation.addAllocation(driver, action1, "Nikhil Joshi");
		// driver.quit();
	}

}
