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

	String empName, taskName, startDate, endDate, gcmRole;

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
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				ExcelDataObject excelDataObject = new ExcelDataObject();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext() && row.getRowNum() > 0) {
					Cell cell = cellIterator.next();
					System.out.println(row.getRowNum() + " " + cell.getColumnIndex());
					// Check the cell type and format accordingly
					if (cell.getColumnIndex() == 1) {
						System.out.println(row.getRowNum() + " " + cell.getColumnIndex());

						excelDataObject.empName = cell.toString();
						System.out.println(excelDataObject.empName);

					} else if (cell.getColumnIndex() == 2) {
						System.out.println(row.getRowNum() + " " + cell.getColumnIndex());
						excelDataObject.taskName = cell.toString();
						System.out.println(excelDataObject.taskName);
					} else if (cell.getColumnIndex() == 3) {
						System.out.println(row.getRowNum() + " " + cell.getColumnIndex());
						excelDataObject.startDate = cell.toString();
						System.out.println(excelDataObject.startDate);

					} else if (cell.getColumnIndex() == 4) {
						System.out.println(row.getRowNum() + " " + cell.getColumnIndex());
						excelDataObject.endDate = cell.toString();
						System.out.println(excelDataObject.endDate);

					} else if (cell.getColumnIndex() == 5) {
						System.out.println(row.getRowNum() + " " + cell.getColumnIndex());
						excelDataObject.gcmRole = cell.toString();
						System.out.println(excelDataObject.gcmRole);

					}

					System.out.println("");
				}
				if (excelDataObject.empName != null)
					excelDataObjects.add(excelDataObject);

			}
			workbook.close();
			file.close();

		} catch (

		Exception e) {
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
		SearchProject obj1 = new SearchProject();
		obj1.searchbyprojectname(driver, wait);

		WorkAssignmentPage workAssignmentPage = new WorkAssignmentPage();
		workAssignmentPage.navigateWorkAssignmentPage(driver, wait);

		Actions action1 = new Actions(driver);

		Thread.sleep(3000L);

		// workAssignmentPage.createTask(driver, wait, action1);

		ExcelDataObject excelDataObject = new ExcelDataObject();
		List<ExcelDataObject> excelDataObjects2 = excelDataObject.getExcelData(
				System.getProperty("user.dir") + "\\src\\testData\\TimesheetTasksCollection.xlsm", excelDataObjects);

		Iterator<ExcelDataObject> iterator = excelDataObjects2.iterator();
		// First time add requirement and allocate
		String currentTaskName = null;

		NewRequirement newRequirement = new NewRequirement();
		AddAllocation addAllocation = new AddAllocation();
		if (iterator.hasNext()) {
			excelDataObject = (ExcelDataObject) iterator.next();
			currentTaskName = excelDataObject.taskName;
		}
		while (iterator.hasNext()) {

			workAssignmentPage.createTask(driver, wait, action1, excelDataObject.taskName);
			newRequirement = new NewRequirement();
			newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

			addAllocation = new AddAllocation();
			addAllocation.addAllocation(driver, action1, excelDataObject.empName);

			if (iterator.hasNext())
				excelDataObject = (ExcelDataObject) iterator.next();
			for (; iterator.hasNext() && currentTaskName.equals(excelDataObject.taskName);) {

				newRequirement = new NewRequirement();
				newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

				addAllocation = new AddAllocation();
				addAllocation.addAllocation(driver, action1, excelDataObject.empName);
				excelDataObject = (ExcelDataObject) iterator.next();

			}
			currentTaskName = excelDataObject.taskName;
		}
		
	}
}
