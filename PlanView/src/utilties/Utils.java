package utilties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utils {

	
	//Wait for an element before Clicking
	public static void clickOn(WebDriver driver, WebElement element)

	{

	new WebDriverWait(driver, 20).

	until(ExpectedConditions.elementToBeClickable(element));

	element.click();

	}
	
	// Perform Click Operation
	public static void clickElementByJS(WebElement element, WebDriver driver)
    {
		
     JavascriptExecutor js = ((JavascriptExecutor) driver);
     js.executeScript("arguments[0].click();", element); 
     
    }
}



