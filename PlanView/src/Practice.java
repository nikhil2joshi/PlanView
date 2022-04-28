
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
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;

import org.sikuli.script.Screen;


import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

public class Practice {

	static {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\a566317\\OneDrive - Worldline\\Documents\\My Received Files\\Demo\\Browser\\chromedriver.exe");
		System.out.println("Static block");
	}

	public static void main(String[] args) throws InterruptedException, AWTException, FindFailed, IOException {

		WebDriver driver = new ChromeDriver ();
		
		driver.manage().window().maximize();
		driver.get("https://one.myworldline.com/en/home.html");
		
		Runtime.getRuntime().exec("C:\\Users\\a566317\\eclipse-workspace\\CodeMaster_sikuli.jar");
		Thread.sleep(30000);
		Thread.sleep(5000);
		System.out.println("Title of the page: " + driver.getTitle());

		//driver.findElement(By.xpath("//a[@href='javascript: document.logoutForm.submit()']")).click();

		Thread.sleep(5000);
		driver.quit();

	}

}
