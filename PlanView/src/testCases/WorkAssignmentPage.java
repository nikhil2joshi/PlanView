package testCases;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WorkAssignmentPage {

	public void navigateWorkAssignmentPage(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		// TODO Auto-generated method stub
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebElement clickMenuIcon = driver
				.findElement(By.xpath("//button[@id='PVBannerTitleBarMenuButton']/span[@title='Actions']"));
		clickMenuIcon.click();
		Thread.sleep(3000L);

		WebElement selectWorkAssgmnt = driver
				.findElement(By.xpath("//span[@class='bannerMenuItemText'][contains(text(),'Work and Assignments')]"));
		// String titlePage= driver.getTitle();
		selectWorkAssgmnt.click();
		Thread.sleep(5000L);

	}

	public void addWBSElement(WebDriver driver, Actions action1, String WBSCode) throws InterruptedException {

		// WebDriverWait wait = new WebDriverWait(driver,30);
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='ui-widget-content
		// slick-row odd active']/div[@class='slick-cell l6 r6 hasEditor selected
		// row-selected row-selected-top row-selected-bottom']")));
		WebElement webElementWBSCode = null;
		// driver.manage().window().setPosition(new Point(0, -2000));
		Thread.sleep(2000);
		webElementWBSCode = driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l6 r6 hasEditor')][contains(@class,'selected')]"));

		action1.moveToElement(webElementWBSCode).doubleClick().sendKeys(WBSCode).sendKeys(Keys.ENTER).build().perform();

		driver.findElement(By.xpath("//img[@class='datapickericon' and @title='View Data Picker']")).click();

		Thread.sleep(2000);
		//driver.manage().window().setPosition(new Point(0, -2000));
		String mainWindowHandle = driver.getWindowHandle();
		Set<String> allWindowHandles = driver.getWindowHandles();
		//driver.manage().window().setPosition(new Point(0, -2000));
		Iterator<String> iterator = allWindowHandles.iterator();

		while (iterator.hasNext()) {

			String ChildWindow = iterator.next();
			if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
				// driver.switchTo().window(ChildWindow).manage().window().setPosition(new
				// Point(0, -2000));

				driver.switchTo().window(ChildWindow).manage().window().maximize();

				Thread.sleep(2000);
				driver.findElement(By.xpath("//a[contains(text(),'Search')]")).click();

				driver.switchTo().frame(driver.findElement(By.id("iframeSearchView")));
				driver.switchTo().frame(driver.findElement(By.id("frameAttributes")));

				driver.findElement(By.id("attribute_description")).sendKeys(WBSCode); // Click on Description textbox
																						// and send EmpName
				Thread.sleep(2000);
				WebElement searchButton;
				searchButton = driver.findElement(By.xpath("//input[@id='_search']"));
				searchButton.click();

				driver.switchTo().parentFrame(); // Switching back to Parent frame IframeSearchView

				driver.switchTo().frame(driver.findElement(By.id("frameSearchList")));

				Thread.sleep(3000L);
				// Clicking on searched GCMRole
				WebElement wbsCode = driver.findElement(By.xpath("//a[contains(text(),'" + WBSCode + "')]"));
				wbsCode.click();

				driver.switchTo().defaultContent();
				driver.findElement(By.xpath("//input[@type='button' and @id = 'OK']")).click();

				driver.switchTo().window(mainWindowHandle);
				// driver.manage().window().setPosition(new Point(0, -2000));

			}
		}

	}

	public void createTask(WebDriver driver, WebDriverWait wait, Actions action1, ExcelDataObject excelDataobject)
			throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// TODO Auto-generated method stub
		try {

			action1.moveToElement(driver.findElement(By.xpath(
					"//div[@class='vsplitter ui-draggable ui-draggable-handle']/div[1]/div[1]/div[2]/div[@class='dock-right-icon']")))
					.click().build().perform();
		} catch (NoSuchElementException e) {

		}
		Thread.sleep(2000L);

		action1.moveToElement(driver.findElement(By.xpath("//button[@title='collapse: click to hide child rows']")))
				.click().build().perform();

		getTasksCount(driver);

		WebElement clickProjectMenuoption = driver
				.findElement(By.xpath("//div[@class='ActionLinkButton']/span[1]/span[1]"));
		clickProjectMenuoption.click();
		Thread.sleep(2000L);

		WebElement selectInserUnder = driver.findElement(By.xpath("//span[@class='pv12FastTrackInsertUnder']/span[1]"));
		selectInserUnder.click();
		Thread.sleep(2000L);

		action1.moveToElement(
				driver.findElement(By.xpath("//span[@class='grid-drag-handle icon icon5x13 sm-vertical-ellipses']")))
				.doubleClick().sendKeys(excelDataobject.taskName).sendKeys(Keys.ENTER).build().perform();
		WebElement currentSeqIdWebElement = driver.findElement(By.xpath(
				"//div[contains(@class,'slick-viewport slick-viewport-top slick-viewport-right')]//div[contains(@class,'slick-cell l3 r3 readonly')][contains(@class,'selected')]/div[@style='overflow: hidden; text-align: left;']"));
		String currentSeqID = currentSeqIdWebElement.getAttribute("title");

		excelDataobject.sequenceID = currentSeqID;
		Thread.sleep(2000L);

		addWBSElement(driver, action1, excelDataobject.wbsCode);
		Thread.sleep(2000L);

		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l7 r7 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().sendKeys(excelDataobject.startDate).sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(2000L);

		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l8 r8 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().sendKeys(excelDataobject.endDate).sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(2000L);

		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l9 r9 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().build().perform();

		Thread.sleep(2000L);

		Select constraintType = new Select(driver.findElement(By.xpath("//select[@class='editor-pick']")));
		constraintType.selectByIndex(0);

		Thread.sleep(2000L);

		action1.moveToElement(driver.findElement(By.xpath(
				"//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l5 r5 hasEditor')][contains(@class,'selected')]")))
				.doubleClick().build().perform();

		Thread.sleep(2000L);
		/*
		 * action1.moveToElement(driver.findElement(By.xpath(
		 * "//div[contains(@class,'gridContainer container-widget border-box-sized split-layout-first split-layout-vertical grid-driver')]//div[contains(@class,'slick-cell l5 r5 hasEditor')][contains(@class,'selected')]"
		 * ))) .doubleClick().build().perform();
		 * 
		 * // row-selected-bottom Select send2SAP = new
		 * Select(driver.findElement(By.xpath("//select[@class='editor-scode']")));
		 * Thread.sleep(2000L); send2SAP.selectByVisibleText("Yes");
		 * Thread.sleep(2000L);
		 */
		WebElement taskWebElement = driver.findElement(By.xpath("//div[@title='" + excelDataobject.sequenceID + "']"));
		action1.moveToElement(taskWebElement).contextClick().build().perform();

		Thread.sleep(2000L);
		driver.findElement(By.xpath("//a[contains(text(),'Task Information')]")).click();

		Thread.sleep(2000L);

	}

	public void deleteTask(WebDriver driver, Actions action1) throws InterruptedException {

		System.out.println("Enter task name to be deleted--------");

		@SuppressWarnings("resource")
		Scanner sc1 = new Scanner(System.in);
		String taskNametobeDeleted = sc1.next();
		Thread.sleep(2000L);

		action1.moveToElement(driver.findElement(By.xpath("//span[contains(text(),'" + taskNametobeDeleted + "')]")))
				.contextClick().build().perform();

		WebElement deleteOptionClick = driver.findElement(By.xpath("//a[contains(text(),'Delete')]"));
		Thread.sleep(3000L);
		deleteOptionClick.click();

		Thread.sleep(3000L);

		driver.switchTo().alert().accept();
		Thread.sleep(3000L);

	}

	public void getTasksCount(WebDriver driver) {

		List<WebElement> allTasks = driver.findElements(By.xpath("//span[@class='prm-work']"));

		for (int i = 0; i < allTasks.size(); i++) {

			String taskName = allTasks.get(i).getText();
			System.out.println(taskName);

		}

	}
}
