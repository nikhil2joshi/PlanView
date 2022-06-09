package testCases;

import java.util.Iterator;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class NewRequirement {

	WebElement assignmentsubmenuoption;

	public void addNewRequirement(WebDriver driver, Actions action1, String gcmRole) throws InterruptedException {

		// Click on Requirement
		driver.manage().window().setPosition(new Point(0, -2000));
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//a[contains(text(),'Require')]")).click();
		driver.findElement(By.xpath("//span[normalize-space(text())='Requirement' and @class='add-line-text']"))
				.click();
		Thread.sleep(2000L);
		driver.manage().window().setPosition(new Point(0, -2000));
		String mainWindowHandle = driver.getWindowHandle();
		Set<String> allWindowHandles = driver.getWindowHandles();
		// System.out.println(allWindowHandles);
		driver.manage().window().setPosition(new Point(0, -2000));
		Iterator<String> iterator = allWindowHandles.iterator();

		while (iterator.hasNext()) {

			String ChildWindow = iterator.next();
			if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
				driver.switchTo().window(ChildWindow).manage().window().setPosition(new Point(0, -2000));

				driver.findElement(By.xpath("//a[contains(text(),'Search')]")).click();
				Thread.sleep(2000L);

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

				Thread.sleep(2000L);
				driver.findElement(By.xpath("//a[contains(text(),'" + gcmRole + "')]")).click(); // Clicking on searched
																									// GCMRole

				driver.switchTo().defaultContent();

				driver.findElement(By.xpath("//input[@type='button' and @id = 'OK']")).click();

				Thread.sleep(2000L);

				driver.switchTo().window(mainWindowHandle);
				driver.manage().window().setPosition(new Point(0, -2000));
				;
				driver.findElement(By.xpath("//input[@id='bannerSearchBox']")).click();

			}
		}

	}

}
