package testData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataObject {
	public int SR;
	public String sequenceID, empName, taskName, startDate, endDate, gcmRole, projectName, wbsCode, taskType,
			flagTaskAdded, flagGCMAdded, flagResourceAdded;

	public static void setData(String adr, String sht, int rn, int cn, String val) throws IOException {

		FileInputStream fsIP = new FileInputStream(new File(adr)); // Read the spreadsheet that needs to be updated

		XSSFWorkbook wb = new XSSFWorkbook(fsIP); // Access the workbook

		XSSFSheet worksheet = wb.getSheet(sht); // Access the worksheet, so that we can update / modify it.

		Cell cell = null; // declare a Cell object
		try {
			cell = worksheet.getRow(rn).getCell(cn); // Access the second cell in second row to update the value

			if (worksheet.getRow(rn) == null) {
				worksheet.createRow(rn);
			}

			if (worksheet.getRow(rn).getCell(cn) == null) {
				worksheet.getRow(rn).createCell(cn);
			}

			cell.setCellValue(val); // Get current cell value value and overwrite the value
		} catch (NullPointerException e) {
			System.out.println("Unable to write in excel due to nullpointer exception... ");
		} finally {

			fsIP.close();// Close the InputStream
			FileOutputStream output_file = new FileOutputStream(new File(adr)); // Open FileOutputStream to write
																				// updates

			wb.write(output_file); // write changes
			wb.close(); // close the stream
			output_file.close(); // close the stream
		}

	}

	public String getdate(String date) {
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

	public List<ExcelDataObject> getExcelData(String Path, List<ExcelDataObject> excelDataObjects) {
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		FileInputStream file = null;
		excelDataObjects = new ArrayList<ExcelDataObject>();
		try {
			file = new FileInputStream(Path);
			// Create Workbook instance holding reference to .xlsx file
			try {
				workbook = new XSSFWorkbook(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(
						"Unable to access file. \n Check if TimesheetTasksCollection.xlsm Excel is already open Or It is not corrupted.");
			}
			// Get first desired sheet from the workbook
			sheet = workbook.getSheet("Master");

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
					if (cell1.getColumnIndex() == 0) {
						double d = Double.parseDouble(cell1.toString());
						excelDataObject.SR = (int) d;
					}

					else if (cell1.getColumnIndex() == 1)
						excelDataObject.empName = cell1.toString();
					else if (cell1.getColumnIndex() == 2)
						excelDataObject.taskName = cell1.toString();
					else if (cell1.getColumnIndex() == 3)
						excelDataObject.startDate = excelDataObject.getdate(cell1.toString());
					else if (cell1.getColumnIndex() == 4)
						excelDataObject.endDate = excelDataObject.getdate(cell1.toString());
					else if (cell1.getColumnIndex() == 5)
						excelDataObject.taskType = cell1.toString();
					else if (cell1.getColumnIndex() == 6)
						excelDataObject.gcmRole = cell1.toString();
					else if (cell1.getColumnIndex() == 7)
						excelDataObject.flagTaskAdded = cell1.toString();
					else if (cell1.getColumnIndex() == 8)
						excelDataObject.flagGCMAdded = cell1.toString();
					else if (cell1.getColumnIndex() == 9)
						excelDataObject.flagResourceAdded = cell1.toString();
					else if (cell1.getColumnIndex() == 10)
						excelDataObject.sequenceID = cell1.toString();

				}
				if (excelDataObject.empName != null) {
					excelDataObject.wbsCode = wbsCode;
					String currentTaskName = excelDataObject.taskName;
					if (excelDataObject.taskName == currentTaskName) {

					}
					excelDataObjects.add(excelDataObject);
				}
			}

		} catch (FileNotFoundException e1) {
			System.out.println(
					"TimesheetTasksCollection.xlsm Excel File not found. \n Make sure it is in same folder as PlanView.jar file");
		} catch (NullPointerException e2) {
			System.out.println("Unable to fetch data from TimesheetTasksCollection.xlsm Excel");
		} finally {
			try {
				workbook.close();
				file.close();
			} catch (IOException e) {
				System.out.println("File not accessible I/O Exception");
				e.printStackTrace();
			}

		}

		return excelDataObjects;

	}

}