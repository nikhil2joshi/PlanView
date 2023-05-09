package utilties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import testCases.PlanViewBaseClass;

public class Utils {

	// Perform Click Operation
	public static void clickOn(WebDriver driver, WebElement element) throws InterruptedException {

		int i = 0;
		for (; i < PlanViewBaseClass.globalWait; i++) {
			try {
				Thread.sleep(1000);
				element.click();
				break;
			} catch (Exception e) {
				if (i == PlanViewBaseClass.globalWait) {
					throw e;
				}
			}
		}
	}

	// Perform Click Operation using JavaScriptExecutor
	public static void clickElementByJS(WebElement element, WebDriver driver) throws InterruptedException {

		int i = 0;
		for (; i < PlanViewBaseClass.globalWait; i++) {
			try {
				Thread.sleep(1000);
				JavascriptExecutor js = ((JavascriptExecutor) driver);
				js.executeScript("arguments[0].scrollIntoView(true);", element);
				Thread.sleep(1000);
				js.executeScript("arguments[0].click();", element);
				break;

			} catch (Exception e) {
				if (i == PlanViewBaseClass.globalWait) {
					throw e;
				}
			}
		}

	}

	// Perform Click Operation using Actions class
	public static void singleclickOnElementbyActions(WebDriver driver, WebElement element, Actions action1)
			throws InterruptedException {

		int i = 0;
		for (; i < PlanViewBaseClass.globalWait; i++) {
			try {
				Thread.sleep(1000);
				action1.moveToElement(element).click().build().perform();
				break;

			} catch (Exception e) {
				if (i == PlanViewBaseClass.globalWait) {
					throw e;
				}
			}
		}
	}

	// Perform right click using Actions class
	public static void rightClickOnElementbyActions(WebDriver driver, WebElement element, Actions action1)
			throws InterruptedException {

		int i = 0;
		for (; i < PlanViewBaseClass.globalWait; i++) {
			try {
				Thread.sleep(1000);
				action1.moveToElement(element).contextClick().build().perform();
				break;

			} catch (Exception e) {
				if (i == PlanViewBaseClass.globalWait) {
					throw e;
				}
			}
		}
	}

	// Perform double click using Actions class
	public static void doubleClickOnElementbyActions(WebDriver driver, WebElement element, Actions action1)
			throws InterruptedException {

		int i = 0;
		for (; i < PlanViewBaseClass.globalWait; i++) {
			try {
				Thread.sleep(1000);
				action1.moveToElement(element).doubleClick().build().perform();
				break;

			} catch (Exception e) {
				if (i == PlanViewBaseClass.globalWait) {
					throw e;
				}
			}
		}
	}
}
