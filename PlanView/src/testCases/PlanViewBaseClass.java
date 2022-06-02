package testCases;


import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.AWTException;
import java.io.IOException;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PlanViewBaseClass {
	public static List<ExcelDataObject> excelDataObjects;
	static {

		String path = System.getProperty("user.dir");
		System.out.println("Chromedriver loading" + path);
		System.setProperty("webdriver.chrome.driver", path + "\\chromedriver.exe");

		System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

	}

	public static void main(String[] args) throws InterruptedException, AWTException, IOException {
		WebDriver driver = new ChromeDriver();
		try {

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, 10000);
			// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get("https://worldline.pvcloud.com/");
			driver.manage().window().maximize();

			driver.findElement(By.xpath("//div[@class='wg-pki']//input[4]")).click(); // click on login Button

			Thread.sleep(10000L);// Wait for user to select certificate and enter PIN

			System.out.println(driver.getTitle());

			Actions action1 = new Actions(driver);

			// workAssignmentPage.createTask(driver, wait, action1);

			ExcelDataObject excelDataObject = new ExcelDataObject();
			List<ExcelDataObject> excelDataObjects2 = excelDataObject.getExcelData(
					System.getProperty("user.dir") + "\\TimesheetTasksCollection.xlsm",
					excelDataObjects);

			Iterator<ExcelDataObject> iterator = excelDataObjects2.iterator();
			// First time add requirement and allocate
			String currentTaskName = null;

			NewRequirement newRequirement;
			AddAllocation addAllocation;
			WorkAssignmentPage workAssignmentPage = new WorkAssignmentPage();
			boolean flag = false;
			excelDataObject = (ExcelDataObject) iterator.next();
			if (iterator.hasNext()) {

				if (excelDataObject.empName == null) {
					SearchProject obj1 = new SearchProject();
					obj1.searchbyprojectname(driver, wait, excelDataObject.projectName);

					workAssignmentPage.navigateWorkAssignmentPage(driver, wait);
					excelDataObject = (ExcelDataObject) iterator.next();
					currentTaskName = excelDataObject.taskName;
				}

			}
			while (iterator.hasNext() && excelDataObject.empName != null) {

				// first task addition without comparison
				workAssignmentPage.createTask(driver, wait, action1, excelDataObject);

				newRequirement = new NewRequirement();
				newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

				addAllocation = new AddAllocation();
				addAllocation.addAllocation(driver, action1, excelDataObject);

				if (iterator.hasNext())
					excelDataObject = (ExcelDataObject) iterator.next();

				// Compare next task in loop with earlier task if same then only add GCM and
				// Employee
				// If not then come out of for loop and add new task
				for (; iterator.hasNext() && currentTaskName.equals(excelDataObject.taskName);) {

					newRequirement = new NewRequirement();
					newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

					addAllocation = new AddAllocation();
					addAllocation.addAllocation(driver, action1, excelDataObject);
					excelDataObject = (ExcelDataObject) iterator.next();

				}
				if (iterator.hasNext())
					currentTaskName = excelDataObject.taskName;
			}
			//adding last entry
			if (!iterator.hasNext()) {
				if (currentTaskName.equals(excelDataObject.taskName)) {
					newRequirement = new NewRequirement();
					newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);
					
					addAllocation = new AddAllocation();
					addAllocation.addAllocation(driver, action1, excelDataObject);

				} else {
					workAssignmentPage.createTask(driver, wait, action1, excelDataObject);

					newRequirement = new NewRequirement();
					newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

					addAllocation = new AddAllocation();
					addAllocation.addAllocation(driver, action1, excelDataObject);
					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();
		}
	}
}
