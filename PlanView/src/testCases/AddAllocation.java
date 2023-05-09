package testCases;

import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import testData.ExcelDataObject;
import utilties.Utils;

public class AddAllocation {

	@SuppressWarnings("deprecation")
	public void addAllocation(WebDriver driver, Actions action1, ExcelDataObject excelDataObject)
			throws InterruptedException, IOException {
		WebElement taskSearch = driver.findElement(
				By.xpath("//div[@id='tester']/div/div/div/div/div/div/div/div/input[@placeholder='Type to filter']"));
		Utils.doubleClickOnElementbyActions(driver, taskSearch, action1);
		taskSearch.clear();
		taskSearch.sendKeys(excelDataObject.sequenceID);

		Thread.sleep(1000L);
		WebElement currentSeqIdWebElement = driver.findElement(By.xpath(
				"//div[contains(@class,'slick-viewport slick-viewport-top slick-viewport-right')]//div[contains(@class,'slick-cell l3 r3 readonly')]/div[contains(@style,'overflow: hidden; text-align: left;')][contains(@title,'"
						+ excelDataObject.sequenceID + "')]"));

		Utils.clickOn(driver, currentSeqIdWebElement);
		String currentSeqID = currentSeqIdWebElement.getAttribute("title");
		PlanViewBaseClass.globalSeqID = currentSeqID;
		excelDataObject.sequenceID = currentSeqID;
		WebElement taskWebElement = driver.findElement(By.xpath("//div[@title='" + excelDataObject.sequenceID + "']"));
		Utils.rightClickOnElementbyActions(driver, taskWebElement, action1);

		Thread.sleep(1000L);
		WebElement taskInformation = driver.findElement(By.xpath("//a[contains(text(),'Task Information')]"));
		Utils.clickOn(driver, taskInformation);
	
		Thread.sleep(1000);
		// driver.manage().window().setPosition(new Point(0, -3000));

		WebElement allocate = driver.findElement(By.xpath("//a[contains(text(),'Allocate')]"));
		Thread.sleep(2000L);
		Utils.singleclickOnElementbyActions(driver, allocate, action1);
		Thread.sleep(2000L);

		WebElement allocationButton = driver.findElement(By.xpath("//span[text()='Allocation']"));
		Utils.singleclickOnElementbyActions(driver, allocationButton, action1);
		Thread.sleep(2000L);

		String mainWindowHandle = driver.getWindowHandle();
		Set<String> allWindowHandles = driver.getWindowHandles();

		// driver.manage().window().setPosition(new Point(0, -3000));
		Iterator<String> iterator = allWindowHandles.iterator();
		Thread.sleep(2000L);
		while (iterator.hasNext()) {

			String ChildWindow = iterator.next();
			if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
				// driver.switchTo().window(ChildWindow).manage().window().setPosition(new
				// Point(0, -3000));

				driver.switchTo().window(ChildWindow).manage().window().maximize();

				WebElement search = driver.findElement(By.xpath("//a[contains(text(),'Search')]"));
				Utils.clickOn(driver, search);
				try {
					driver.switchTo().frame(driver.findElement(By.id("iframeSearchView")));
					driver.switchTo().frame(driver.findElement(By.id("frameAttributes")));
					Thread.sleep(2000);
					driver.findElement(By.id("attribute_description")).sendKeys(excelDataObject.empName); // Click
																											// on
																											// Description
																											// textbox
																											// and send
																											// EmpName
					Thread.sleep(1000);

					WebElement searchButton = driver.findElement(By.xpath("//input[@id='_search']"));
					Utils.clickOn(driver, searchButton);
					driver.switchTo().parentFrame(); // Switching back to Parent frame IframeSearchView

					driver.switchTo().frame(driver.findElement(By.id("frameSearchList")));

					Thread.sleep(1000L);
					// Clicking on searched GCMRole
					// explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("")));
					WebElement gcmRole = driver
							.findElement(By.xpath("//a[contains(text(),'" + excelDataObject.empName + "')]"));
					Utils.clickOn(driver, gcmRole);

					driver.switchTo().defaultContent();
					WebElement OkButton = driver.findElement(By.xpath("//input[@type='button' and @id = 'OK']"));
					Utils.clickOn(driver, OkButton);

					driver.switchTo().window(mainWindowHandle);
					// Thread.sleep(1000L);
					// driver.manage().window().setPosition(new Point(0, -3000));

					WebElement allocate1 = driver.findElement(By.xpath("//a[contains(text(),'Allocate')]"));
					Utils.clickOn(driver, allocate1);

					WebElement hBtnTop = driver.findElement(By.xpath("//div[contains(@class,'h-btn-top')]"));
					Utils.clickOn(driver, hBtnTop);

					WebElement allocateEmployee = driver.findElement(
							By.xpath("//div[contains(@class,'slick-cell l1 r1 hasEditor')]/div[contains(text(),'"
									+ excelDataObject.empName + "')]"));
					Utils.clickOn(driver, allocateEmployee);

					action1.moveToElement(driver.findElement(By.xpath(
							"//div[contains(@class,'pvlp-base-content border-box-sized')]/div/div/div/div/div/div/div/div[contains(@class,'slick-cell l7 r7 hasEditor')][contains(@class,'selected')]")))
							.doubleClick().sendKeys(excelDataObject.endDate).sendKeys(Keys.ENTER).build().perform();
					Thread.sleep(3000);
					action1.moveToElement(driver.findElement(By.xpath(
							"//div[contains(@class,'pvlp-base-content border-box-sized')]/div/div/div/div/div/div/div/div[contains(@class,'slick-cell l6 r6 hasEditor')][contains(@class,'selected')]")))
							.doubleClick().sendKeys(excelDataObject.startDate).sendKeys(Keys.ENTER).build().perform();
					Thread.sleep(3000);
					action1.moveToElement(driver.findElement(By.xpath(
							"//div[contains(@class,'pvlp-base-content border-box-sized')]/div/div/div/div/div/div/div/div[contains(@class,'slick-cell l7 r7 hasEditor')][contains(@class,'selected')]")))
							.doubleClick().sendKeys(excelDataObject.endDate).sendKeys(Keys.ENTER).build().perform();
					Thread.sleep(3000);

					

					WebElement hBtnBottom = driver.findElement(By.xpath("//div[contains(@class,'h-btn-bottom')]"));
					Utils.clickOn(driver, hBtnBottom);
					System.out.println(
							"Allocation done for " + excelDataObject.empName + " with " + excelDataObject.taskName);
					ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataObject.SR + 1, 6, "Y"); // task
																													// addition

					ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataObject.SR + 1, 7, "Y"); // Resource
																													// addition
					ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataObject.SR + 1, 8,
							excelDataObject.sequenceID); // sequence ID addition

				} catch (Exception e) {
					e.printStackTrace();
					driver.close();
					driver.switchTo().window(mainWindowHandle);
					// driver.manage().window().setPosition(new Point(0, -3000));

					ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataObject.SR + 1, 6, "Y"); // task
																													// addition
					ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataObject.SR + 1, 7, "N"); // Resource
																													// addition
					ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataObject.SR + 1, 8,
							excelDataObject.sequenceID); // sequence ID addition

				}
			}
		}
	}

	public void extendAllocation(WebDriver driver, Actions action1, ExcelDataObject excelDataObject,
			WebElement webElement) throws InterruptedException, IOException {

		action1.moveToElement(webElement).contextClick().build().perform();
		Thread.sleep(1000L);
		WebElement taskInfo = driver.findElement(By.xpath("//a[contains(text(),'Task Information')]"));
		Utils.clickOn(driver, taskInfo);
		Thread.sleep(1000L);

		WebElement allocate = driver.findElement(By.xpath("//a[contains(text(),'Allocate')]"));
		Utils.clickOn(driver, allocate);
		WebElement hBtnTop = driver.findElement(By.xpath("//div[contains(@class,'h-btn-top')]"));
		Utils.clickOn(driver, hBtnTop);

		WebElement allocateEmployee = driver
				.findElement(By.xpath("//div[contains(@class,'slick-cell l1 r1 hasEditor')]/div[contains(text(),'"
						+ excelDataObject.empName + "')]"));
		Utils.clickOn(driver, allocateEmployee);

		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'pvlp-base-content border-box-sized')]/div/div/div/div/div/div/div/div[contains(@class,'slick-cell l6 r6 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().sendKeys(excelDataObject.startDate).sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(1000);
		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'pvlp-base-content border-box-sized')]/div/div/div/div/div/div/div/div[contains(@class,'slick-cell l7 r7 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().sendKeys(excelDataObject.endDate).sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(1000);
		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'pvlp-base-content border-box-sized')]/div/div/div/div/div/div/div/div[contains(@class,'slick-cell l7 r7 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().sendKeys(excelDataObject.endDate).sendKeys(Keys.ENTER).build().perform();
		WebElement hBtnBottom = driver.findElement(By.xpath("//div[contains(@class,'h-btn-bottom')]"));
		Utils.clickOn(driver, hBtnBottom);
		System.out.println(
				"Extension done for allocation of " + excelDataObject.empName + " with " + excelDataObject.taskName);

		ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataObject.SR + 1, 7, "Y");
		ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataObject.SR + 1, 8,
				excelDataObject.sequenceID);

	}

}
