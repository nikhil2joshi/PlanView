package testCases;

import java.util.Iterator;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import utilties.Utils;

public class NewRequirement {

	WebElement assignmentsubmenuoption;

	public void addNewRequirement(WebDriver driver, Actions action1, String gcmRole) throws InterruptedException {

		// Click on Requirement
		
			driver.manage().window().setPosition(new Point(0, -3000));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Thread.sleep(3000L);
			WebElement require = driver.findElement(By.xpath("//a[contains(text(),'Require')]"));
			Utils.clickOn(driver, require);
			
			WebElement Requirement = driver.findElement(By.xpath("//span[contains(text(),'Requirement')][@class='add-line-text']"));
			Utils.clickOn(driver, Requirement);

			Thread.sleep(3000L);
			String mainWindowHandle = driver.getWindowHandle();
			Set<String> allWindowHandles = driver.getWindowHandles();
			Iterator<String> iterator = allWindowHandles.iterator();

			while (iterator.hasNext()) {

				String ChildWindow = iterator.next();
				if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
					driver.switchTo().window(ChildWindow).manage().window().setPosition(new Point(0, -3000));
					
					WebElement searchButton = driver.findElement(By.xpath("//a[contains(text(),'Search')]"));
					Utils.clickOn(driver, Requirement);
					
					// Switching to the nested frames so as to reach to intended elements in HTML
					driver.switchTo().frame(driver.findElement(By.id("iframeSearchView")));
					driver.switchTo().frame(driver.findElement(By.id("frameAttributes")));

					WebElement searchGCMrole = driver
							.findElement(By.xpath("//label[contains(text(),'Description')]/following-sibling::input[1]"));
					searchGCMrole.sendKeys(gcmRole);

					driver.findElement(By.xpath("//input[@id='_search']")).click(); // click on search button
					driver.switchTo().parentFrame(); // Switching back to Parent frame IframeSearchView

					/*
					 * final List<WebElement> iframes = driver.findElements(By.tagName("frame"));
					 * for (WebElement iframe : iframes) {
					 * System.out.println(iframe.getAttribute("id")); }
					 */

					// Switch inside the frameSearchlist frame
					driver.switchTo().frame(driver.findElement(By.id("frameSearchList")));

					
					WebElement SearchedGCMRole = driver.findElement(By.xpath("//a[contains(text(),'" + gcmRole + "')]"));
							Utils.clickOn(driver, SearchedGCMRole); // Clicking on searched GCMRole

					driver.switchTo().defaultContent();

					WebElement OkButton = driver.findElement(By.xpath("//input[@type='button' and @id = 'OK']"));
					Utils.clickOn(driver,OkButton);

					driver.switchTo().window(mainWindowHandle);
					driver.manage().window().setPosition(new Point(0, -3000));
				}
			}		
	}

}
