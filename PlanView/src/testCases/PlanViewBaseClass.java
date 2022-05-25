package testCases;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// = new ArrayList<ExcelDataObject>();
class ExcelDataObject {

	String empName, taskName, startDate, endDate, gcmRole, projectName, wbsCode;

	List<ExcelDataObject> getExcelData(String Path, List<ExcelDataObject> excelDataObjects) {

		excelDataObjects = new ArrayList<ExcelDataObject>();
		try {
			FileInputStream file = new FileInputStream(Path);
			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			// Get first desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheet("Master");

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			String wbsCode = null;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				ExcelDataObject excelDataObject = new ExcelDataObject();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext() && row.getRowNum() == 0) {
					Cell cell = cellIterator.next();
					if (cell.getColumnIndex() == 0) {
						excelDataObject.projectName = cell.toString();
					} else if (cell.getColumnIndex() == 5) {
						wbsCode = cell.toString();

					}
				}

				while (cellIterator.hasNext() && row.getRowNum() > 0) {
					Cell cell1 = cellIterator.next();

					// Check the cell type and format accordingly
					if (cell1.getColumnIndex() == 1) {
						excelDataObject.empName = cell1.toString();
					} else if (cell1.getColumnIndex() == 2) {
						excelDataObject.taskName = cell1.toString();
					} else if (cell1.getColumnIndex() == 3) {
						excelDataObject.startDate = cell1.toString();
					} else if (cell1.getColumnIndex() == 4) {
						excelDataObject.endDate = cell1.toString();
					} else if (cell1.getColumnIndex() == 5) {
						excelDataObject.gcmRole = cell1.toString();
					}

					System.out.println("");
				}
				if (excelDataObject.empName != null) {
					excelDataObject.wbsCode = wbsCode;
					excelDataObjects.add(excelDataObject);
				}
			}
			workbook.close();
			file.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return excelDataObjects;

	}

}

public class PlanViewBaseClass {
	public static List<ExcelDataObject> excelDataObjects;
	static {
		String path = System.getProperty("user.dir");
		System.out.println("Chromedriver loading" + path);
		System.setProperty("webdriver.chrome.driver", path + "\\resources\\chromedriver.exe");

	}

	public static void main(String[] args) throws InterruptedException, AWTException, IOException {

		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 10000);

		driver.get("https://worldline.pvcloud.com/");
		driver.manage().window().maximize();
		Thread.sleep(5000L);

		driver.findElement(By.xpath("//div[@class='wg-pki']//input[4]")).click(); // click on login Button

		Thread.sleep(10000L);// Wait for user to select certificate and enter PIN

		System.out.println(driver.getTitle());

		if (driver.getTitle().contains("My Overview - Planview"))
			;
		{
			System.out.println("Login Successful Dashboard Navigation Complete....");
		}

		Thread.sleep(2000L);

		Actions action1 = new Actions(driver);

		Thread.sleep(3000L);

		// workAssignmentPage.createTask(driver, wait, action1);

		ExcelDataObject excelDataObject = new ExcelDataObject();
		List<ExcelDataObject> excelDataObjects2 = excelDataObject.getExcelData(
				System.getProperty("user.dir") + "\\src\\testData\\TimesheetTasksCollection.xlsm", excelDataObjects);

		Iterator<ExcelDataObject> iterator = excelDataObjects2.iterator();
		// First time add requirement and allocate
		String currentTaskName = null;

		NewRequirement newRequirement;
		AddAllocation addAllocation;
		WorkAssignmentPage workAssignmentPage;
		if (iterator.hasNext()) {
			excelDataObject = (ExcelDataObject) iterator.next();
			if (excelDataObject.empName == null) {
				SearchProject obj1 = new SearchProject();
				obj1.searchbyprojectname(driver, wait, excelDataObject.projectName);

				workAssignmentPage = new WorkAssignmentPage();
				workAssignmentPage.navigateWorkAssignmentPage(driver, wait);
			} else
				currentTaskName = excelDataObject.taskName;
		}
		while (iterator.hasNext()) {
			// first task addition without comparison
			workAssignmentPage.createTask(driver, wait, action1, excelDataObject.taskName);
			newRequirement = new NewRequirement();
			newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

			addAllocation = new AddAllocation();
			addAllocation.addAllocation(driver, action1, excelDataObject.empName);

			if (iterator.hasNext())
				excelDataObject = (ExcelDataObject) iterator.next();

			// Compare next task in loop with earlier task if same then only add GCM and
			// Employee
			// If not then come out of for loop and add new task
			for (; iterator.hasNext() && currentTaskName.equals(excelDataObject.taskName);) {

				newRequirement = new NewRequirement();
				newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

				addAllocation = new AddAllocation();
				addAllocation.addAllocation(driver, action1, excelDataObject.empName);
				excelDataObject = (ExcelDataObject) iterator.next();

			}
			// Set current task to new task name
			currentTaskName = excelDataObject.taskName;
		}
	}
}
