package testCases;

import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;

import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import testData.ExcelDataObject;
import utilties.Utils;

public class NewRequirement {

	WebElement assignmentsubmenuoption;

	public void addNewRequirement(WebDriver driver, Actions action1, ExcelDataObject excelDataObject)
			throws InterruptedException, IOException {
		WebElement taskSearch = driver.findElement(
				By.xpath("//div[@id='tester']/div/div/div/div/div/div/div/div/input[@placeholder='Type to filter']"));
		Utils.doubleClickOnElementbyActions(driver, taskSearch, action1);
		taskSearch.clear();
		taskSearch.sendKeys(excelDataObject.sequenceID);
		
		Thread.sleep(3000L);
		WebElement currentSeqIdWebElement = driver.findElement(By.xpath(
				"//div[contains(@class,'slick-viewport slick-viewport-top slick-viewport-right')]//div[contains(@class,'slick-cell l3 r3 readonly')]/div[@style='overflow: hidden; text-align: left;'][contains(@title,'"
						+ excelDataObject.sequenceID + "')]"));

		Utils.clickOn(driver, currentSeqIdWebElement);
		String currentSeqID = currentSeqIdWebElement.getAttribute("title");
		PlanViewBaseClass.globalSeqID = currentSeqID;
		excelDataObject.sequenceID = currentSeqID;
		WebElement taskWebElement = driver.findElement(By.xpath("//div[@title='" + excelDataObject.sequenceID + "']"));
		Utils.rightClickOnElementbyActions(driver, taskWebElement, action1);

		Thread.sleep(3000L);
		WebElement taskInformation = driver.findElement(By.xpath("//a[contains(text(),'Task Information')]"));
		Utils.clickOn(driver, taskInformation);
		// Click on Requirement

		// driver.manage().window().setPosition(new Point(0, -3000));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		Thread.sleep(3000L);
		WebElement require = driver.findElement(By.xpath("//a[contains(text(),'Require')]"));
		Utils.clickOn(driver, require);

		WebElement Requirement = driver
				.findElement(By.xpath("//span[contains(text(),'Requirement')][@class='button-text']"));
		Utils.clickOn(driver, Requirement);

		Thread.sleep(3000L);
		String mainWindowHandle = driver.getWindowHandle();
		Set<String> allWindowHandles = driver.getWindowHandles();
		Iterator<String> iterator = allWindowHandles.iterator();

		while (iterator.hasNext()) {

			String ChildWindow = iterator.next();
			if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
				// driver.switchTo().window(ChildWindow).manage().window().setPosition(new
				// Point(0, -3000));
				driver.switchTo().window(ChildWindow).manage().window().maximize();
				WebElement searchOption = driver.findElement(By.xpath("//a[contains(text(),'Search')]"));
				Utils.clickOn(driver, searchOption);

				// Switching to the nested frames so as to reach to intended elements in HTML
				driver.switchTo().frame(driver.findElement(By.id("iframeSearchView")));
				driver.switchTo().frame(driver.findElement(By.id("frameAttributes")));

				WebElement searchGCMrole = driver
						.findElement(By.xpath("//label[contains(text(),'Description')]/following-sibling::input[1]"));
				searchGCMrole.sendKeys(excelDataObject.gcmRole);

				WebElement searchButton = driver.findElement(By.xpath("//input[@id='_search']"));
				Utils.clickOn(driver, searchButton);
				// click on search button
				driver.switchTo().parentFrame(); // Switching back to Parent frame IframeSearchView

				/*
				 * final List<WebElement> iframes = driver.findElements(By.tagName("frame"));
				 * for (WebElement iframe : iframes) {
				 * System.out.println(iframe.getAttribute("id")); }
				 */

				// Switch inside the frameSearchlist frame
				driver.switchTo().frame(driver.findElement(By.id("frameSearchList")));

				WebElement SearchedGCMRole = driver
						.findElement(By.xpath("//a[contains(text(),'" + excelDataObject.gcmRole + "')]"));
				Utils.clickOn(driver, SearchedGCMRole); // Clicking on searched GCMRole

				driver.switchTo().defaultContent();

				WebElement OkButton = driver.findElement(By.xpath("//input[@type='button' and @id = 'OK']"));
				Utils.clickOn(driver, OkButton);

				driver.switchTo().window(mainWindowHandle);
				// driver.manage().window().setPosition(new Point(0, -3000));
				ExcelDataObject.setData(PlanViewBaseClass.file_path, "Master", excelDataObject.SR + 1, 8, "Y");
			}
		}
	}

}
