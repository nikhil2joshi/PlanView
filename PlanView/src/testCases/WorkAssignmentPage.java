package testCases;

import org.openqa.selenium.Point;

import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import testData.ExcelDataObject;
import utilties.Utils;

public class WorkAssignmentPage {

	public void navigateWorkAssignmentPage(WebDriver driver, Actions action1) throws InterruptedException {
		// TODO Auto-generated method stub

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		WebElement clickMenuIcon = driver
				.findElement(By.xpath("//button[@id='PVBannerTitleBarMenuButton']/span[@title='Actions']"));
		Utils.clickOn(driver, clickMenuIcon);
		WebElement selectWorkAssgmnt = driver
				.findElement(By.xpath("//span[@class='bannerMenuItemText'][contains(text(),'Work and Assignments')]"));

		Utils.clickOn(driver, selectWorkAssgmnt);
		Thread.sleep(1000L);
		WebElement selectSchedule = driver.findElement(By.xpath("//span[@class='button-label']"));
		Utils.clickElementByJS(selectSchedule, driver);
		Thread.sleep(1000L);
		WebElement scheduleDropdown = driver.findElement(By.xpath("//li[@id='pvSelectItem0']"));
		Utils.clickElementByJS(scheduleDropdown, driver);

	}

	public void addWBSElement(WebDriver driver, Actions action1, String WBSCode) throws InterruptedException {

		// driver.manage().window().setPosition(new Point(0, -3000));
		WebElement webElementWBSCode = driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l6 r6 hasEditor')][contains(@class,'selected')]"));

		Utils.doubleClickOnElementbyActions(driver, webElementWBSCode, action1);
		WebElement viewDataPicker = driver
				.findElement(By.xpath("//img[@class='datapickericon' and @title='View Data Picker']"));
		Utils.clickOn(driver, viewDataPicker);
		Thread.sleep(1000L);
		String mainWindowHandle = driver.getWindowHandle();
		Set<String> allWindowHandles = driver.getWindowHandles();

		Iterator<String> iterator = allWindowHandles.iterator();

		while (iterator.hasNext()) {

			String ChildWindow = iterator.next();
			// System.out.println(ChildWindow);
			if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
				// driver.switchTo().window(ChildWindow).manage().window().setPosition(new
				// Point(0, -3000));
				Thread.sleep(1000L);
				driver.switchTo().window(ChildWindow).manage().window().maximize();
				WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(30));

				// explicitWait.until(ExpectedConditions
				// .visibilityOfElementLocated(By.linkText("//ul[@id='pickerTabBar']/li[@id='current']/a[contains(text(),'Search')]")));
				WebElement search = driver.findElement(By.xpath("//a[contains(text(),'Search')]"));

				Utils.clickOn(driver, search);

				driver.switchTo().frame(driver.findElement(By.id("iframeSearchView")));
				driver.switchTo().frame(driver.findElement(By.id("frameAttributes")));

				driver.findElement(By.id("attribute_description")).sendKeys(WBSCode); // Click on Description
																						// textbox
																						// and send EmpName
				Thread.sleep(1000);
				WebElement searchButton;
				searchButton = driver.findElement(By.xpath("//input[@id='_search']"));
				searchButton.click();

				driver.switchTo().parentFrame(); // Switching back to Parent frame IframeSearchView

				driver.switchTo().frame(driver.findElement(By.id("frameSearchList")));

				// Clicking on searched GCMRole
				WebElement wbsCode = driver.findElement(By.xpath("//a[contains(text(),'" + WBSCode + "')]"));
				Utils.clickOn(driver, wbsCode);

				driver.switchTo().defaultContent();

				WebElement oKButton = driver.findElement(By.xpath("//input[@type='button' and @id = 'OK']"));
				Utils.clickOn(driver, oKButton);
				;

				driver.switchTo().window(mainWindowHandle);
				// driver.manage().window().setPosition(new Point(0, -3000));

			}
		}

	}

	/**
	 * @param driver
	 * @param wait
	 * @param action1
	 * @param excelDataobject
	 * @param currentProjectName
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void createTask(WebDriver driver, WebDriverWait wait, Actions action1, ExcelDataObject excelDataobject,
			String currentProjectName) throws InterruptedException, IOException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		// TODO Auto-generated method stub

		if (!excelDataobject.flagTaskAdded.equals("Y")) {
			try {

				WebElement dockRightIcon = driver.findElement(By.xpath(
						"//div[@class='vsplitter ui-draggable ui-draggable-handle']/div[1]/div[1]/div[2]/div[@class='dock-right-icon']"));
				Utils.singleclickOnElementbyActions(driver, dockRightIcon, action1);
			} catch (Exception e) {
				// do nothing and proceed further
			}
			Thread.sleep(1000L);

			WebElement hideChildRows = driver
					.findElement(By.xpath("//*[@id=\"grid1\"]/div[4]/div[3]/div/div[2]/div[3]/span[1]"));
			Utils.singleclickOnElementbyActions(driver, hideChildRows, action1);
			Thread.sleep(2000L);
			WebElement clickProjectMenuoption = driver
					.findElement(By.xpath("//*[@id=\"grid1\"]/div[4]/div[3]/div/div[2]/div[1]/span"));
			Thread.sleep(2000L);
			Utils.singleclickOnElementbyActions(driver, clickProjectMenuoption, action1);
			Thread.sleep(2000L);
			WebElement selectInserUnder = driver.findElement(By.xpath("//*[@id=\"prm-226553\"]/div/ul/a[2]/span/span"));
			Utils.singleclickOnElementbyActions(driver, selectInserUnder, action1);
			Thread.sleep(2000L);

			action1.moveToElement(driver.findElement(By.xpath("//span [@title='>: ' and @class='prm-work']")))
					.doubleClick().sendKeys(excelDataobject.taskName).sendKeys(Keys.ENTER).build().perform();

			Thread.sleep(2000L);

			WebElement currentSeqIdWebElement = driver.findElement(By.xpath(
					"//div[contains(@class,'slick-viewport slick-viewport-top slick-viewport-right')]//div[contains(@class,'slick-cell l3 r3 readonly')][contains(@class,'selected')]/div[contains(@style,'overflow: hidden; text-align: left;')]"));
			Utils.clickOn(driver, currentSeqIdWebElement);
			String currentSeqID = currentSeqIdWebElement.getAttribute("title");

			excelDataobject.sequenceID = currentSeqID;

			Thread.sleep(1000L);
		} else {

			WebElement taskSearch = driver.findElement(By
					.xpath("//div[@id='tester']/div/div/div/div/div/div/div/div/input[@placeholder='Type to filter']"));
			Utils.doubleClickOnElementbyActions(driver, taskSearch, action1);
			taskSearch.clear();
			taskSearch.sendKeys(excelDataobject.sequenceID);

			Thread.sleep(1000L);
			WebElement currentSeqIdWebElement = driver.findElement(By.xpath(
					"//div[contains(@class,'slick-viewport slick-viewport-top slick-viewport-right')]//div[contains(@class,'slick-cell l3 r3 readonly')]/div[contains(@style,'overflow: hidden; text-align: left;')][contains(@title,'"
							+ excelDataobject.sequenceID + "')]"));

			Utils.clickOn(driver, currentSeqIdWebElement);

			PlanViewBaseClass.globalSeqID = excelDataobject.sequenceID;
		}
		Thread.sleep(2000L);

		addWBSElement(driver, action1, excelDataobject.wbsCode);
		Thread.sleep(1000L);
		WebElement startDate = driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l7 r7 hasEditor')][contains(@class,'selected')]"));

		action1.moveToElement(startDate).doubleClick().sendKeys(excelDataobject.startDate).build().perform();
		Thread.sleep(1000L);

		Utils.doubleClickOnElementbyActions(driver, driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l6 r6 hasEditor')][contains(@class,'selected')]")),
				action1);
		Thread.sleep(1000L);
		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l8 r8 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().sendKeys(Keys.BACK_SPACE).build().perform();
		Thread.sleep(1000L);

		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l8 r8 hasEditor')][contains(@class,'selected')]")))
				.sendKeys(excelDataobject.endDate).build().perform();
		Utils.doubleClickOnElementbyActions(driver, driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l6 r6 hasEditor')][contains(@class,'selected')]")),
				action1);
		Thread.sleep(1000L);
		WebElement currentSeqIdWebElement1 = driver.findElement(By.xpath(
				"//div[contains(@class,'slick-viewport slick-viewport-top slick-viewport-right')]//div[contains(@class,'slick-cell l3 r3 readonly')]/div[contains(@style,'overflow: hidden; text-align: left;')][contains(@title,'"
						+ excelDataobject.sequenceID + "')]"));

		Utils.clickOn(driver, currentSeqIdWebElement1);

		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l9 r9 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().build().perform();
		Thread.sleep(1000L);
		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l9 r9 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().build().perform();

		WebElement li = driver.findElement(By.xpath("//li[@role='menuitem'][@title=' ']"));
		Utils.clickOn(driver, li);

		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l5 r5 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().build().perform();

		Thread.sleep(1000L);
		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l5 r5 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().build().perform();

		WebElement li2 = driver.findElement(By.xpath("//li[@role='menuitem'][@title='Yes']"));
		Utils.clickOn(driver, li2);

		ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataobject.SR + 1, 8,
				excelDataobject.sequenceID);
		ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataobject.SR + 1, 6, "Y");

	}

	public void deleteTask(WebDriver driver, Actions action1) throws InterruptedException {

		System.out.println("Enter task name to be deleted--------");

		@SuppressWarnings("resource")
		Scanner sc1 = new Scanner(System.in);
		String taskNametobeDeleted = sc1.next();
		Thread.sleep(1000L);

		action1.moveToElement(driver.findElement(By.xpath("//span[contains(text(),'" + taskNametobeDeleted + "')]")))
				.contextClick().build().perform();

		WebElement deleteOptionClick = driver.findElement(By.xpath("//a[contains(text(),'Delete')]"));

		deleteOptionClick.click();

		driver.switchTo().alert().accept();
	}

	public List<WebElement> getallCount(WebDriver driver, ExcelDataObject excelDataObject) {

		List<WebElement> allTasks = driver.findElements(By.xpath("//span[@class='prm-work']"));
		// System.out.println(allTasks);
		return allTasks;

	}
}
