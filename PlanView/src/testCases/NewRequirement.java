package testCases;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class NewRequirement {
	
	public void addNewRequirement(WebDriver driver,Actions action1) throws InterruptedException {
		WebElement assignmentsubmenuoption= driver.findElement(By.xpath("//a[contains(text(),'New Requirement')]"));
		action1.moveToElement(assignmentsubmenuoption).click().build().perform();
		
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		
		Thread.sleep(5000L);
		String mainWindowHandle = driver.getWindowHandle();
		Set<String> allWindowHandles = driver.getWindowHandles();
		System.out.println(allWindowHandles);
        Iterator<String> iterator = allWindowHandles.iterator();
        
        String gcmRole="GSDRS/SDCO/GSC\\SPM03-Senior Consultant - Testing";
		while (iterator.hasNext()) {
            String ChildWindow = iterator.next();
                if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
                driver.switchTo().window(ChildWindow).manage().window().maximize();
                driver.manage().window().maximize();
                Thread.sleep(2000L);
                System.out.println("Heading of child window is " + driver.getTitle());
                Thread.sleep(5000L);
                driver.switchTo().frame(driver.findElement(By.id("iframeSearchView")));
                Thread.sleep(5000L);
                
                System.out.println(driver.getPageSource());
                WebElement searchGCMrole=driver.findElement(By.xpath("//label[contains(text(),'Description')]/following-sibling::input[1]"));
                jse.executeScript("arguments[0].value='GSDRS/SDCO/GSC\\\\SPM03-Senior Consultant - Testing';", searchGCMrole);

                Thread.sleep(2000L);
            }
        } 

	}

}
