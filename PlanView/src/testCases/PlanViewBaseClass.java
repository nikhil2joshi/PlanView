package testCases;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.awt.AWTException;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// = new ArrayList<ExcelDataObject>();
class ExcelDataObject {

	String empName, taskName, startDate, endDate, gcmRole, projectName, wbsCode;

	String getdate(String date) {
		String dateArray1[] = date.split("-");
		String monthnum = null;

		switch (dateArray1[1]) {
		case "Jan":
			monthnum = "01";
			break;
		case "Feb":
			monthnum = "02";
			break;
		case "Mar":
			monthnum = "03";
			break;
		case "Apr":
			monthnum = "04";
			break;
		case "May":
			monthnum = "05";
			break;
		case "Jun":
			monthnum = "06";
			break;
		case "Jul":
			monthnum = "07";
			break;
		case "Aug":
			monthnum = "08";
			break;
		case "Sep":
			monthnum = "09";
			break;
		case "Oct":
			monthnum = "10";
			break;
		case "Nov":
			monthnum = "11";
			break;
		case "Dec":
			monthnum = "12";
			break;

		}

		return monthnum + "/" + dateArray1[0] + "/" + dateArray1[2];
	}

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
					}
					if (cell.getColumnIndex() == 5) {
						wbsCode = cell.toString();
						excelDataObject.wbsCode = wbsCode;
					}

				}
				if (row.getRowNum() == 0)
					excelDataObjects.add(excelDataObject);

				while (cellIterator.hasNext() && row.getRowNum() > 1) {
					Cell cell1 = cellIterator.next();

					// Check the cell type and format accordingly
					if (cell1.getColumnIndex() == 1) {
						excelDataObject.empName = cell1.toString();
					} else if (cell1.getColumnIndex() == 2) {
						excelDataObject.taskName = cell1.toString();
					} else if (cell1.getColumnIndex() == 3) {
						excelDataObject.startDate = excelDataObject.getdate(cell1.toString());
					} else if (cell1.getColumnIndex() == 4) {
						excelDataObject.endDate = excelDataObject.getdate(cell1.toString());
					} else if (cell1.getColumnIndex() == 5) {
						excelDataObject.gcmRole = cell1.toString();
					}

				}
				if (excelDataObject.empName != null) {
					excelDataObject.wbsCode = wbsCode;
					String currentTaskName = excelDataObject.taskName;
					if (excelDataObject.taskName == currentTaskName) {

					}
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
		// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://worldline.pvcloud.com/");
		driver.manage().window().maximize();

		driver.findElement(By.xpath("//div[@class='wg-pki']//input[4]")).click(); // click on login Button

		Thread.sleep(10000L);// Wait for user to select certificate and enter PIN

		System.out.println(driver.getTitle());

		if (driver.getTitle().contains("My Overview - Planview"))
			;
		{
			System.out.println("Login Successful Dashboard Navigation Complete....");
		}

		Actions action1 = new Actions(driver);

		// workAssignmentPage.createTask(driver, wait, action1);

		ExcelDataObject excelDataObject = new ExcelDataObject();
		List<ExcelDataObject> excelDataObjects2 = excelDataObject.getExcelData(
				System.getProperty("user.dir") + "\\src\\testData\\TimesheetTasksCollection.xlsm", excelDataObjects);

		Iterator<ExcelDataObject> iterator = excelDataObjects2.iterator();
		// First time add requirement and allocate
		String currentTaskName = null;

		NewRequirement newRequirement;
		AddAllocation addAllocation;
		WorkAssignmentPage workAssignmentPage = new WorkAssignmentPage();
		;
		excelDataObject = (ExcelDataObject) iterator.next();
		if (iterator.hasNext() && excelDataObject.empName != null) {

			if (excelDataObject.empName == null) {
				SearchProject obj1 = new SearchProject();
				obj1.searchbyprojectname(driver, wait, excelDataObject.projectName);

				workAssignmentPage.navigateWorkAssignmentPage(driver, wait);
				excelDataObject = (ExcelDataObject) iterator.next();
				currentTaskName = excelDataObject.taskName;
			}

		}
		while (iterator.hasNext() && excelDataObject.empName != null) {
			if (currentTaskName.equals("SK12 - TS")) {
				System.out.println("Reached SK12");
			}
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

		// Create last task
		workAssignmentPage.createTask(driver, wait, action1, excelDataObject.taskName);
		newRequirement = new NewRequirement();
		newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

		addAllocation = new AddAllocation();
		addAllocation.addAllocation(driver, action1, excelDataObject.empName);

		driver.quit();

	}
}
