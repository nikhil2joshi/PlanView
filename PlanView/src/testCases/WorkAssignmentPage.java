package testCases;

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

	
	public void navigateWorkAssignmentPage(WebDriver driver,WebDriverWait wait) throws InterruptedException {
		// TODO Auto-generated method stub
	
	
	WebElement clickMenuIcon=driver.findElement(By.xpath("//button[@id='PVBannerTitleBarMenuButton']/span[@title='Actions']"));
	clickMenuIcon.click();
	Thread.sleep(3000L);
	
	WebElement selectWorkAssgmnt=driver.findElement(By.xpath("//span[@class='bannerMenuItemText'][contains(text(),'Work and Assignments')]"));
	//String titlePage= driver.getTitle();
	selectWorkAssgmnt.click();
	Thread.sleep(5000L);
	verifyManageSchedule(driver,wait);
	
	}
	
	public void verifyManageSchedule(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		
		String expectedText="1 - Manage Schedule";
		
		Thread.sleep(3000L);
		WebElement actualText= driver.findElement(By.xpath("//span/span[contains(text(),'Manage Schedule')]"));
		
		Assert.assertEquals(expectedText, actualText.getText());
			
	}

	public void createTask(WebDriver driver, WebDriverWait wait, Actions action1) throws InterruptedException {
		// TODO Auto-generated method stub
		
		action1.moveToElement(driver.findElement(By.xpath("//div[@class='vsplitter ui-draggable ui-draggable-handle']/div[1]/div[1]/div[2]/div[@class='dock-right-icon']"))).click().build().perform();
		Thread.sleep(3000L);
		
		action1.moveToElement(driver.findElement(By.xpath("//button[@title='collapse: click to hide child rows']"))).click().build().perform();
		Thread.sleep(3000L);
		
		WebElement clickProjectMenuoption= driver.findElement(By.xpath("//div[@class='ActionLinkButton']/span[1]/span[1]"));
		clickProjectMenuoption.click();
		Thread.sleep(3000L);
		
		WebElement selectInserUnder= driver.findElement(By.xpath("//span[@class='pv12FastTrackInsertUnder']/span[1]"));
		selectInserUnder.click();
		Thread.sleep(5000L);
		
		String taskName="Demo_Task_2201";
		action1.moveToElement(driver.findElement(By.xpath("//span[@class='grid-drag-handle icon icon5x13 sm-vertical-ellipses']"))).
		doubleClick().sendKeys(taskName).sendKeys(Keys.ENTER).build().perform();
		
		Thread.sleep(5000L);
		
		action1.moveToElement(driver.findElement(By.xpath("//span[contains(text(),'"+taskName+"')]"))).contextClick().build().perform();
		Thread.sleep(3000L);
		
		WebElement assignmentmenuoption= driver.findElement(By.xpath("//ul[@class='standard-wrapper banner-list-menu']/li[3]"));
		action1.moveToElement(assignmentmenuoption).perform();
		Thread.sleep(2000L);
		
	}
}
