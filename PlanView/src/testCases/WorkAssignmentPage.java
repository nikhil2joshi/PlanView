package testCases;

import org.openqa.selenium.NoSuchElementException;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WorkAssignmentPage {

	public void navigateWorkAssignmentPage(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		// TODO Auto-generated method stub

		WebElement clickMenuIcon = driver
				.findElement(By.xpath("//button[@id='PVBannerTitleBarMenuButton']/span[@title='Actions']"));
		clickMenuIcon.click();
		Thread.sleep(3000L);

		WebElement selectWorkAssgmnt = driver
				.findElement(By.xpath("//span[@class='bannerMenuItemText'][contains(text(),'Work and Assignments')]"));
		// String titlePage= driver.getTitle();
		selectWorkAssgmnt.click();
		Thread.sleep(5000L);
		verifyManageSchedule(driver, wait);

	}

	public void verifyManageSchedule(WebDriver driver, WebDriverWait wait) throws InterruptedException {

		String expectedText = "1 - Manage Schedule";

		Thread.sleep(3000L);
		WebElement actualText = driver.findElement(By.xpath("//span/span[contains(text(),'Manage Schedule')]"));

		Assert.assertEquals(expectedText, actualText.getText());

	}

	public void createTask(WebDriver driver, WebDriverWait wait, Actions action1, String taskName)
			throws InterruptedException {
		// TODO Auto-generated method stub
		try {
		
			action1.moveToElement(driver.findElement(By.xpath(
					"//div[@class='vsplitter ui-draggable ui-draggable-handle']/div[1]/div[1]/div[2]/div[@class='dock-right-icon']")))
					.click().build().perform();
		}
		catch(NoSuchElementException e) {
			System.out.println("dock-right-icon Element not found");
		}
		Thread.sleep(3000L);

		action1.moveToElement(driver.findElement(By.xpath("//button[@title='collapse: click to hide child rows']")))
				.click().build().perform();
		Thread.sleep(3000L);
		
		getTasksCount(driver);

		WebElement clickProjectMenuoption = driver
				.findElement(By.xpath("//div[@class='ActionLinkButton']/span[1]/span[1]"));
		clickProjectMenuoption.click();
		Thread.sleep(3000L);

		WebElement selectInserUnder = driver.findElement(By.xpath("//span[@class='pv12FastTrackInsertUnder']/span[1]"));
		selectInserUnder.click();
		Thread.sleep(5000L);

		action1.moveToElement(
				driver.findElement(By.xpath("//span[@class='grid-drag-handle icon icon5x13 sm-vertical-ellipses']")))
				.doubleClick().sendKeys(taskName).sendKeys(Keys.ENTER).build().perform();

		Thread.sleep(5000L);

		action1.moveToElement(driver.findElement(By.xpath("//span[contains(text(),'" + taskName + "')]")))
				.contextClick().build().perform();
		Thread.sleep(3000L);

		WebElement assignmentmenuoption = driver.findElement(By.xpath("//a[contains(text(),'Task Information')]"));
		action1.moveToElement(assignmentmenuoption).perform();
		assignmentmenuoption.click();
		Thread.sleep(2000L);
		
	}
	
	public void deleteTask(WebDriver driver,Actions action1) throws InterruptedException {
		
		System.out.println("Enter task name to be deleted--------");
		
		Scanner sc1= new Scanner(System.in);
		String taskNametobeDeleted =sc1.next();
		Thread.sleep(3000L);
		
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
		
		List<WebElement> allTasks= driver.findElements(By.xpath("//span[@class='prm-work']"));
		
		for(int i=0;i<allTasks.size();i++) {
			
			String taskName=allTasks.get(i).getText();
			System.out.println(taskName);
			
		}
		
	}
}
