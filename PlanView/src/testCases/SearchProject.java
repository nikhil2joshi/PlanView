package testCases;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchProject {	
	
	public void searchbyprojectname(WebDriver driver, WebDriverWait wait, String projectName) throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		WebElement searchClick=driver.findElement(By.id("bannerSearchBox"));
		searchClick.sendKeys(projectName);
		Thread.sleep(2000L);
			
		WebElement optionsToSelect = driver.findElement(By.xpath("//ul[@id='searchUl']/li/a[@title='"+projectName+"']"));
		wait.until(ExpectedConditions.elementToBeClickable(optionsToSelect));
		optionsToSelect.click();
		
		

	}	
}
