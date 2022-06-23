package testCases;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import testData.ExcelDataObject;

public class AddAllocation {

	public void addAllocation(WebDriver driver, Actions action1, ExcelDataObject excelDataObject)
			throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(2000);
		driver.manage().window().setPosition(new Point(0, -2000));
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

		driver.manage().window().setPosition(new Point(0, -2000));
		Iterator<String> iterator = allWindowHandles.iterator();

		while (iterator.hasNext()) {

			String ChildWindow = iterator.next();
			if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
				driver.switchTo().window(ChildWindow).manage().window().setPosition(new Point(0, -2000));
				try {
					driver.switchTo().frame(driver.findElement(By.id("iframeSearchView")));
					driver.switchTo().frame(driver.findElement(By.id("frameAttributes")));

					driver.findElement(By.id("attribute_description")).sendKeys(excelDataObject.empName); // Click on
																											// Description
																											// textbox
					// and send EmpName
					Thread.sleep(2000);
					WebElement searchButton;
					searchButton = driver.findElement(By.xpath("//input[@id='_search']"));
					searchButton.click();

					driver.switchTo().parentFrame(); // Switching back to Parent frame IframeSearchView

					driver.switchTo().frame(driver.findElement(By.id("frameSearchList")));

					Thread.sleep(2000L);
					// Clicking on searched GCMRole
					WebElement gcmRole = driver
							.findElement(By.xpath("//a[contains(text(),'" + excelDataObject.empName + "')]"));
					gcmRole.click();

					driver.switchTo().defaultContent();
					driver.findElement(By.xpath("//input[@type='button' and @id = 'OK']")).click();

					driver.switchTo().window(mainWindowHandle);

					driver.manage().window().setPosition(new Point(0, -2000));
					Thread.sleep(2000);

					driver.findElement(By.xpath("//a[contains(text(),'Allocate')]")).click();
					Thread.sleep(2000);
					driver.findElement(By.xpath("//div[contains(@class,'h-btn-top')]")).click();
					Thread.sleep(2000);
					driver.findElement(
							By.xpath("//div[contains(@class,'slick-cell l1 r1 hasEditor')]/div[contains(text(),'"
									+ excelDataObject.empName + "')]"))
							.click();
					Thread.sleep(2000);
					action1.moveToElement(driver.findElement(By.xpath(
							"//div[contains(@class,'pvlp-base-content border-box-sized')]/div/div/div/div/div/div/div/div[contains(@class,'slick-cell l6 r6 hasEditor')][contains(@class,'selected')]")))
							.doubleClick().sendKeys(excelDataObject.startDate).sendKeys(Keys.ENTER).build().perform();
					Thread.sleep(2000);
					action1.moveToElement(driver.findElement(By.xpath(
							"//div[contains(@class,'pvlp-base-content border-box-sized')]/div/div/div/div/div/div/div/div[contains(@class,'slick-cell l7 r7 hasEditor')][contains(@class,'selected')]")))
							.doubleClick().sendKeys(excelDataObject.endDate).sendKeys(Keys.ENTER).build().perform();
					Thread.sleep(2000);
					driver.findElement(By.xpath("//div[contains(@class,'h-btn-bottom')]")).click();
					System.out.println(
							"Allocation done for " + excelDataObject.empName + " with " + excelDataObject.taskName);

				} catch (Exception e) {
					driver.close();
					driver.switchTo().window(mainWindowHandle);
					driver.manage().window().setPosition(new Point(0, -2000));
					System.out.println(
							"Allocation not done for " + excelDataObject.empName + " with " + excelDataObject.taskName);
					Thread.sleep(2000L);
					// driver.findElement(By.xpath("//a[contains(text(),'Require')]")).click();
					driver.findElement(By.xpath("//a[normalize-space(text())='Require']")).click();
					Thread.sleep(2000L);
					action1.moveToElement(driver.findElement(
							By.xpath("//div[contains(@class,'slick-cell l2 r2 hasEditor')]/div[contains(@title,'"
									+ excelDataObject.gcmRole + "')]")))
							.contextClick().build().perform();
					Thread.sleep(2000L);
					WebElement deleteOption = driver.findElement(By.xpath("//a[contains(text(),'Delete')]"));
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					Thread.sleep(2000L);
					executor.executeScript("arguments[0].click();", deleteOption);
					Thread.sleep(2000L);

					WebElement clickYes = driver.findElement(
							By.xpath("//div[@class='ui-dialog-buttonset']/button[contains(text(),'Yes')]"));
					Thread.sleep(2000L);
					executor.executeScript("arguments[0].click();", clickYes);
				}
			}
		}
	}
}
