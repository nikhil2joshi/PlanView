
import java.awt.AWTException;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Practice {

	// Setting ChromeDriver path
	static {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
	}

	public static void main(String[] args) throws InterruptedException, AWTException, IOException {

		WebDriver driver = new ChromeDriver();

		driver.get("https://worldline.pvcloud.com/");

		driver.manage().window().maximize();
		Thread.sleep(2000L);

		WebElement clickLoginButton = driver.findElement(By.xpath("//div['@class=\"wg-pki\"']//input[4]"));
		clickLoginButton.click();

		Thread.sleep(10000L);
		System.out.println(driver.getTitle());

		Thread.sleep(2000L);

		WebElement searchClick = driver.findElement(By.id("bannerSearchBox"));
		WebDriverWait wait = new WebDriverWait(driver, 5000);

		String textToSelect = "WL BE FS TCC Marco Polo";
		searchClick.sendKeys(textToSelect);
		Thread.sleep(5000L);

		driver.findElement(By.xpath("//ul[@id='searchUl']/li/a[@title='" + textToSelect + "']")).click();

		driver.getTitle();
		Thread.sleep(5000L);

		driver.findElement(By.xpath("//button[@id='PVBannerTitleBarMenuButton']/span[@title='Actions']")).click();
		Thread.sleep(3000L);
		driver.findElement(By.xpath("//span[@class='bannerMenuItemText'][contains(text(),'Work and Assignments')]"))
				.click();
		Thread.sleep(3000L);

	}

}
