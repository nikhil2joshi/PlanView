package utilties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import testCases.PlanViewBaseClass;

public class Utils {

	// Perform Click Operation
	public static void clickOn(WebDriver driver, WebElement element) throws InterruptedException {

		int i = 0;
		for (; i < PlanViewBaseClass.globalWait; i++) {
			try {
				element.click();
				System.out.println("Worked on first click " + element);
				break;
			} catch (Exception e) {
				Thread.sleep(2000);
				System.out.println("Inside normal wait for " + i + " " + element);
				if (i == 5) {
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

				JavascriptExecutor js = ((JavascriptExecutor) driver);
				js.executeScript("arguments[0].click();", element);
				System.out.println("Worked on first click " + element);
				break;

			} catch (Exception e) {
				Thread.sleep(2000);
				System.out.println("Inside JS wait for " + i + " " + element);

				if (i == 5) {
					throw e;
				}
			}
		}

	}

	// Perform Click Operation
	public static void singleclickOnElementbyActions(WebDriver driver, WebElement element, Actions action1)
			throws InterruptedException {

		int i = 0;
		for (; i < PlanViewBaseClass.globalWait; i++) {
			try {

				action1.moveToElement(element).click().build().perform();
				System.out.println("Worked on first click " + element);
				break;

			} catch (Exception e) {
				Thread.sleep(2000);
				System.out.println("Inside Single click wait for " + i + " " + element);

				if (i == 5) {
					throw e;
				}
			}
		}
	}

	public static void rightClickOnElementbyActions(WebDriver driver, WebElement element, Actions action1)
			throws InterruptedException {

		int i = 0;
		for (; i < PlanViewBaseClass.globalWait; i++) {
			try {
				action1.moveToElement(element).contextClick().build().perform();
				System.out.println("Worked on first click " + element);
				break;

			} catch (Exception e) {
				Thread.sleep(2000);
				System.out.println("Inside rightClick wait for " + i + " " + element);

				if (i == 5) {
					throw e;
				}
			}
		}
	}

	public static void doubleClickOnElementbyActions(WebDriver driver, WebElement element, Actions action1)
			throws InterruptedException {

		int i = 0;
		for (; i < PlanViewBaseClass.globalWait; i++) {
			try {
				action1.moveToElement(element).doubleClick().build().perform();
				System.out.println("Worked on first click " + element);
				break;

			} catch (Exception e) {
				Thread.sleep(2000);
				System.out.println("Inside DoubleClick wait for " + i + " " + element);

				if (i == 5) {
					throw e;
				}
			}
		}
	}
}
