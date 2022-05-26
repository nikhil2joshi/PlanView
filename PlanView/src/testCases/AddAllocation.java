package testCases;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class AddAllocation {

	public void addAllocation(WebDriver driver, Actions action1, String empName) throws InterruptedException {
		Thread.sleep(2000);
		action1.moveToElement(driver.findElement(By.xpath(
				"//div[@class='slick-cell l2 r2 hasEditor selected row-selected row-selected-top row-selected-bottom']")))
				.contextClick().build().perform();
		Thread.sleep(2000);
		action1.moveToElement(driver.findElement(By.xpath("//a[normalize-space(text())='Fill Requirement']")))
				.perform();
		Thread.sleep(2000);
		action1.moveToElement(driver.findElement(By.xpath("//a[normalize-space(text())='Allocate...']"))).click()
				.perform();
		Thread.sleep(2000);
		String mainWindowHandle = driver.getWindowHandle();
		Set<String> allWindowHandles = driver.getWindowHandles();

		Iterator<String> iterator = allWindowHandles.iterator();

		while (iterator.hasNext()) {

			String ChildWindow = iterator.next();
			if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
				driver.switchTo().window(ChildWindow).manage().window().maximize();
				driver.manage().window().maximize();

				driver.switchTo().frame(driver.findElement(By.id("iframeSearchView")));
				driver.switchTo().frame(driver.findElement(By.id("frameAttributes")));

				driver.findElement(By.id("attribute_description")).sendKeys(empName); // Click on Description textbox
																						// and send EmpName
				Thread.sleep(2000);
				WebElement searchButton;
				searchButton = driver.findElement(By.xpath("//input[@id='_search']"));
				searchButton.click();

				driver.switchTo().parentFrame(); // Switching back to Parent frame IframeSearchView

				driver.switchTo().frame(driver.findElement(By.id("frameSearchList")));

				Thread.sleep(2000L);
				// Clicking on searched GCMRole
				WebElement gcmRole = driver.findElement(By.xpath("//a[contains(text(),'" + empName + "')]"));
				gcmRole.click();

				driver.switchTo().defaultContent();
				driver.findElement(By.xpath("//input[@type='button' and @id = 'OK']")).click();

				driver.switchTo().window(mainWindowHandle);

			}
		}

	}

}
