package testCases;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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

class ExcelDataObject {

	String empName, taskName, startDate, endDate, gcmRole;

	List<ExcelDataObject> excelDataObjects = new ArrayList<ExcelDataObject>();

	List<ExcelDataObject> getExcelData(String Path) {
		ExcelDataObject excelDataObject = new ExcelDataObject();
		try {
			FileInputStream file = new FileInputStream(Path);

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheet("Master");

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					// Check the cell type and format accordingly
					if (cell.getColumnIndex() == 1) {

						excelDataObject.empName = cell.toString();
						System.out.println(excelDataObject.empName);

					} else if (cell.getColumnIndex() == 2) {
						excelDataObject.taskName = cell.toString();
						System.out.println(excelDataObject.taskName);
					} else if (cell.getColumnIndex() == 3) {
						excelDataObject.startDate = cell.toString();
						System.out.println(excelDataObject.startDate);

					} else if (cell.getColumnIndex() == 4) {
						excelDataObject.endDate = cell.toString();
						System.out.println(excelDataObject.endDate);

					} else if (cell.getColumnIndex() == 5) {
						excelDataObject.gcmRole = cell.toString();
						System.out.println(excelDataObject.gcmRole);

					}
				}
				excelDataObjects.add(excelDataObject);

				System.out.println("");
			}
			file.close();
		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return excelDataObjects;

	}
}

public class PlanViewBaseClass {

	static {
		String path = System.getProperty("user.dir");
		System.out.println("Chromedriver loading... " + path);
		System.setProperty("webdriver.chrome.driver", path + "\\resources\\chromedriver.exe");

	}

	public static void main(String[] args) throws InterruptedException, AWTException, IOException {

		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 10000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://worldline.pvcloud.com/");
		driver.manage().window().maximize();

		driver.findElement(By.xpath("//div[@class='wg-pki']//input[4]")).click(); // click on login Button

		Thread.sleep(10000L);// Wait for user to select certificate and enter PIN

		if (driver.getTitle().contains("My Overview - Planview")) {
			System.out.println("Login Successful Dashboard Navigation Complete....");
		}

		SearchProject obj1 = new SearchProject();
		obj1.searchbyprojectname(driver, wait);

		WorkAssignmentPage workAssignmentPage = new WorkAssignmentPage();
		workAssignmentPage.navigateWorkAssignmentPage(driver, wait);

		Actions action1 = new Actions(driver);
		workAssignmentPage.createTask(driver, wait, action1);

		// NewRequirement newRequirement = new NewRequirement();
		// newRequirement.addNewRequirement(driver, action1,
		// "GSDRS/SDCO/GSC\\SPM03-Senior Consultant - Testing");

		// AddAllocation addAllocation = new AddAllocation();
		// addAllocation.addAllocation(driver, action1, "Nikhil Joshi");
		// driver.quit();

		ExcelDataObject excelDataObject = new ExcelDataObject();
		List<ExcelDataObject> excelDataObjects = excelDataObject
				.getExcelData("C:\\Users\\a566317\\OneDrive - Worldline\\Documents\\TimesheetTasksCollection.xlsm");

		Iterator<ExcelDataObject> iterator = excelDataObjects.iterator();
		//First time add requirement and allocate
		String currentTaskName = null;
		NewRequirement newRequirement = new NewRequirement();
		AddAllocation addAllocation = new AddAllocation();
		if (iterator.hasNext()) {
			excelDataObject = (ExcelDataObject) iterator.next();
			currentTaskName = excelDataObject.taskName;
			newRequirement = new NewRequirement();
			newRequirement.addNewRequirement(driver, action1, excelDataObject.taskName);

			addAllocation = new AddAllocation();
			addAllocation.addAllocation(driver, action1, excelDataObject.empName);
		}
		
		for (; iterator.hasNext();) {
			excelDataObject = (ExcelDataObject) iterator.next();

			for (; iterator.hasNext() && currentTaskName.equals(excelDataObject.taskName);) {
				addAllocation = new AddAllocation();
				addAllocation.addAllocation(driver, action1, excelDataObject.empName);
				excelDataObject = (ExcelDataObject) iterator.next();
				
			}
			
			newRequirement = new NewRequirement();
			newRequirement.addNewRequirement(driver, action1, excelDataObject.taskName);
			addAllocation = new AddAllocation();
			addAllocation.addAllocation(driver, action1, excelDataObject.empName);

		}
	}
}
