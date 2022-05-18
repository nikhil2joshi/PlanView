package testCases;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class NewRequirement {

	public void addNewRequirement(WebDriver driver, Actions action1) throws InterruptedException {
		WebElement assignmentsubmenuoption = driver.findElement(By.xpath("//a[contains(text(),'New Requirement')]"));
		action1.moveToElement(assignmentsubmenuoption).click().build().perform();
		
		Thread.sleep(2000L);
		String mainWindowHandle = driver.getWindowHandle();
		Set<String> allWindowHandles = driver.getWindowHandles();
		System.out.println(allWindowHandles);
		Iterator<String> iterator = allWindowHandles.iterator();

		String gcmRole = "GSDRS/SDCO/GSC\\SPM03-Senior Consultant - Testing";
		while (iterator.hasNext()) {
			String ChildWindow = iterator.next();
			if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
				driver.switchTo().window(ChildWindow).manage().window().maximize();
				driver.manage().window().maximize();
				
				driver.findElement(By.xpath("//a[contains(text(),'Search')]")).click();
				Thread.sleep(2000L);
				
				driver.switchTo().frame(driver.findElement(By.id("iframeSearchView")));
				driver.switchTo().frame(driver.findElement(By.id("frameAttributes")));
				WebElement searchGCMrole=driver.findElement(By.xpath("//label[contains(text(),'Description')]/following-sibling::input[1]"));
				searchGCMrole.sendKeys(gcmRole);
				// jse.executeScript("arguments[0].value='GSDRS/SDCO/GSC\\\\SPM03-Senior
				// Consultant - Testing';", searchGCMrole);
				driver.findElement(By.xpath("//input[@id='_search']")).click(); // click on search
				driver.switchTo().parentFrame();
				driver.switchTo().frame(driver.findElement(By.id("frameSearchList")));
				driver.switchTo().frame(driver.findElement(By.id("frameServerCall")));
				Thread.sleep(2000L);
				driver.findElement(By.xpath("/td/a[contains(text(),'"+gcmRole+"']")).click();
				
				
			}
		}

	}

}
