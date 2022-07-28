package testCases;

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
			throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(3000);
		// driver.manage().window().setPosition(new Point(0, -3000));
		WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(30));
		;
		explicitWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[@class='slick-cell l2 r2 hasEditor selected row-selected row-selected-top row-selected-bottom']")));
		WebElement bottomRow = driver.findElement(By.xpath(
				"//div[@class='slick-cell l2 r2 hasEditor selected row-selected row-selected-top row-selected-bottom']"));

		Utils.rightClickOnElementbyActions(driver, bottomRow, action1);

		Thread.sleep(3000);

		WebElement fillReq = driver.findElement(By.xpath("//a[normalize-space(text())='Fill Requirement']"));
		action1.moveToElement(fillReq).perform();
		Thread.sleep(2000L);
		
		WebElement allocate = driver.findElement(By.xpath("//a[normalize-space(text())='Allocate...']"));
		Thread.sleep(2000L);
		Utils.singleclickOnElementbyActions(driver, allocate, action1);
		

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
				try {
					driver.switchTo().frame(driver.findElement(By.id("iframeSearchView")));
					driver.switchTo().frame(driver.findElement(By.id("frameAttributes")));

					driver.findElement(By.id("attribute_description")).sendKeys(excelDataObject.empName); // Click on
																											// Description
																											// textbox
					// and send EmpName
					Thread.sleep(3000);
					explicitWait.until(ExpectedConditions
							.invisibilityOfElementLocated(By.xpath("By.xpath(\"//input[@id='_search']\")")));
					WebElement searchButton = driver.findElement(By.xpath("//input[@id='_search']"));
					Utils.clickOn(driver, searchButton);
					driver.switchTo().parentFrame(); // Switching back to Parent frame IframeSearchView

					driver.switchTo().frame(driver.findElement(By.id("frameSearchList")));

					Thread.sleep(3000L);
					// Clicking on searched GCMRole
					explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("")));
					WebElement gcmRole = driver
							.findElement(By.xpath("//a[contains(text(),'" + excelDataObject.empName + "')]"));
					Utils.clickOn(driver, gcmRole);

					driver.switchTo().defaultContent();
					WebElement OkButton = driver.findElement(By.xpath("//input[@type='button' and @id = 'OK']"));
					Utils.clickOn(driver, OkButton);

					driver.switchTo().window(mainWindowHandle);
					Thread.sleep(3000L);
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

				} catch (Exception e) {
					driver.close();
					driver.switchTo().window(mainWindowHandle);
					// driver.manage().window().setPosition(new Point(0, -3000));
					System.out.println(
							"Allocation not done for " + excelDataObject.empName + " with " + excelDataObject.taskName);
					WebElement require = driver.findElement(By.xpath("//a[normalize-space(text())='Require']"));
					Utils.clickOn(driver, require);

					WebElement clickOnGCMRole = driver.findElement(
							By.xpath("//div[contains(@class,'slick-cell l2 r2 hasEditor')]/div[contains(@title,'"
									+ excelDataObject.gcmRole + "')]"));
					Utils.rightClickOnElementbyActions(driver, clickOnGCMRole, action1);

					WebElement deleteOption = driver.findElement(By.xpath("//a[contains(text(),'Delete')]"));
					Utils.clickElementByJS(deleteOption, driver);

					WebElement clickYes = driver.findElement(
							By.xpath("//div[@class='ui-dialog-buttonset']/button[contains(text(),'Yes')]"));
					Utils.clickElementByJS(clickYes, driver);
					e.printStackTrace();
				}
			}
		}
	}

	public void extendAllocation(WebDriver driver, Actions action1, ExcelDataObject excelDataObject,
			WebElement webElement) throws InterruptedException {

		action1.moveToElement(webElement).contextClick().build().perform();
		Thread.sleep(3000L);
		WebElement taskInfo = driver.findElement(By.xpath("//a[contains(text(),'Task Information')]"));
		Utils.clickOn(driver, taskInfo);
		Thread.sleep(3000L);

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
		Thread.sleep(3000);
		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'pvlp-base-content border-box-sized')]/div/div/div/div/div/div/div/div[contains(@class,'slick-cell l7 r7 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().sendKeys(excelDataObject.endDate).sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(3000);

		WebElement hBtnBottom = driver.findElement(By.xpath("//div[contains(@class,'h-btn-bottom')]"));
		Utils.clickOn(driver, hBtnBottom);
		System.out.println(
				"Extension done for allocation of " + excelDataObject.empName + " with " + excelDataObject.taskName);

	}

}
