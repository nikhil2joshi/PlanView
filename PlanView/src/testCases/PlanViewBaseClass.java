package testCases;


import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
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
		System.out.println("Chromedriver loading"+path); 
		System.setProperty("webdriver.chrome.driver",path+"\\resources\\chromedriver.exe");
	
	}

	public static void main(String[] args) throws InterruptedException, AWTException, IOException {

		WebDriver driver = new ChromeDriver ();
		driver.get("https://worldline.pvcloud.com/");
		
		driver.manage().window().maximize();
		Thread.sleep(5000L);

		WebElement clickLoginButton = driver.findElement(By.xpath("//div[@class='wg-pki']//input[4]"));
		clickLoginButton.click();
		
		
		Thread.sleep(10000L);
		System.out.println(driver.getTitle());
		
		if(driver.getTitle().contains("My Overview - Planview"));{
			System.out.println("Login Successful Dashboard Navigation Complete....");
		}
		
		
		WebDriverWait wait=new WebDriverWait(driver, 10000);
		
		Thread.sleep(2000L);
		SearchProject obj1= new SearchProject();
		obj1.searchbyprojectname(driver, wait);
		
		WorkAssignmentPage obj2= new WorkAssignmentPage();
		obj2.navigateWorkAssignmentPage(driver, wait);
		
		Actions action1 = new Actions(driver);
		Thread.sleep(3000L);
		
		obj2.createTask(driver, wait, action1);
		
		NewRequirement obj3= new NewRequirement();
		obj3.addNewRequirement(driver, action1);
		
		//driver.quit();
	}

}
