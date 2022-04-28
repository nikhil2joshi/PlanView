
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

public class Practice {

	static {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\a566317\\eclipse-workspace\\CodeMaster\\chromedriver.exe");
		System.out.println("Static block");
	}

	public static void main(String[] args) throws InterruptedException, AWTException, IOException {

WebDriver driver = new ChromeDriver ();
		
		
		driver.get("https://worldline.pvcloud.com/");
		
		Thread.sleep(5000L);
		
		driver.manage().window().maximize();
		Thread.sleep(2000L);

		WebElement clickLoginButton = driver.findElement(By.xpath("//div['@class=\"wg-pki\"']//input[4]"));
				clickLoginButton.click();
		
		//C:\\Users\\w112749\\OneDrive - Worldline\\Documents\\test.exe
		
		//Runtime.getRuntime().exec("C:\\Users\\w112749\\OneDrive - Worldline\\Documents\\test.exe");
		Thread.sleep(10000L);
		System.out.println(driver.getTitle());
		
		Thread.sleep(2000L);
		WebElement searchClick=driver.findElement(By.id("bannerSearchBox"));
		WebDriverWait wait= new WebDriverWait(driver, 5000);
		
		searchClick.sendKeys("marc");
		Thread.sleep(5000L);
		
		String textToSelect = "WL BE FS TCC Marco Polo";
		
		WebElement optionsToSelect = driver.findElement(By.xpath("//ul[@id='searchUl']/li/a[@title='WL BE FS TCC Marco Polo']"));
		optionsToSelect.click();
		
		driver.getTitle();
		Thread.sleep(5000L);
		
		WebElement test1=driver.findElement(By.xpath("//button[@id='PVBannerTitleBarMenuButton']/span[@title='Actions']"));
		test1.click();
		
		Thread.sleep(3000L);
		WebElement test2=driver.findElement(By.xpath("//span[@class='bannerMenuItemText'][contains(text(),'Work and Assignments')]"));
		test2.click();
		Thread.sleep(3000L);

	}

}
