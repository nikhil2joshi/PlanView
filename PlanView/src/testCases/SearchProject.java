package testCases;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilties.Utils;

public class SearchProject {

	public void searchbyprojectname(WebDriver driver, WebDriverWait wait, String projectName)
			throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		WebElement searchClick = driver.findElement(By.id("bannerSearchBox"));

		Utils.clickOn(driver, searchClick);

		searchClick.sendKeys(projectName);
		WebElement optionsToSelect = driver
				.findElement(By.xpath("//ul[@id='searchUl']/li/a[@title='" + projectName + "']"));
		wait.until(ExpectedConditions.elementToBeClickable(optionsToSelect));
		Utils.clickOn(driver, optionsToSelect);

	}
}
