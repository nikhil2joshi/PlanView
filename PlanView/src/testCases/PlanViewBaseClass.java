package testCases;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import testData.ExcelDataObject;
import java.awt.Font;
import java.awt.Color;

//import javax.swing.JRadioButton;

public class PlanViewBaseClass {
	public static List<ExcelDataObject> excelDataObjects;
	public static ChromeOptions options;
	static {

		String path = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", path + "\\chromedriver.exe");
		System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

	}
	private static JFrame frame;
	private static JButton btnstartButton, btnSelectFile, btnclearButton;
	private static JTabbedPane tabbedPane;
	private static JTextArea textArea_Console;
	private static JFileChooser fileChooser;
	private static JLabel filePath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				PlanViewBaseClass pvc = new PlanViewBaseClass();
				PlanViewBaseClass.frame.setVisible(true);
				PlanViewBaseClass.frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
				textArea_Console.setText(null);
				btnstartButton.setEnabled(false);
				btnclearButton.setEnabled(false);

				btnSelectFile.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						String flag = e.getActionCommand();
						if (flag.equals("Select File")) {
							fileChooser = new JFileChooser();
							int dialogVal = fileChooser.showOpenDialog(null);
							if (dialogVal == JFileChooser.APPROVE_OPTION) {
								filePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
								btnstartButton.setEnabled(true);
								btnclearButton.setEnabled(true);

							} else {
								filePath.setText(" Selection of the file cancelled... !");
							}
						}
					}

				});

				btnstartButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String currentProjectName = null;
						options = new ChromeOptions();
						// options.addArguments("--headless");
						textArea_Console.setText(null);
						WebDriver driver = new ChromeDriver(options);
						try {
							redirectSystemStreams(textArea_Console);

							driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
							WebDriverWait wait = new WebDriverWait(driver, 10);
							// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
							driver.get("https://worldline.pvcloud.com/");
							driver.manage().window().maximize();
							driver.findElement(By.xpath("//div[@class='wg-pki']//input[4]")).click(); // click on login
																										// Button
							Thread.sleep(10000L);// Wait for user to select certificate and enter PIN

							Actions action1 = new Actions(driver);

							driver.manage().window().setPosition(new Point(0, -3000));

							ExcelDataObject excelDataObject = new ExcelDataObject();
							List<ExcelDataObject> excelDataObjects2 = null;
							String file_path = filePath.getText();
							file_path = file_path.replace("\\", "\\\\");
							excelDataObjects2 = excelDataObject.getExcelData(file_path, excelDataObjects);

							Iterator<ExcelDataObject> iterator = excelDataObjects2.iterator();
							// First time add requirement and allocate
							String currentTaskName = null;

							NewRequirement newRequirement = null;
							AddAllocation addAllocation = null;
							WorkAssignmentPage workAssignmentPage = new WorkAssignmentPage();

							excelDataObject = (ExcelDataObject) iterator.next();
							if (iterator.hasNext()) {

								if (excelDataObject.empName == null) {
									SearchProject obj1 = new SearchProject();
									obj1.searchbyprojectname(driver, wait, excelDataObject.projectName);
									currentProjectName = excelDataObject.projectName;
									workAssignmentPage.navigateWorkAssignmentPage(driver, wait);
									excelDataObject = (ExcelDataObject) iterator.next();
									currentTaskName = excelDataObject.taskName;

									try {

										action1.moveToElement(driver.findElement(By.xpath(
												"//div[@class='vsplitter ui-draggable ui-draggable-handle']/div[1]/div[1]/div[2]/div[@class='dock-right-icon']")))
												.click().build().perform();
									} catch (NoSuchElementException e1) {

									}
									Thread.sleep(3000L);

								}

							}
							while (iterator.hasNext() && excelDataObject.empName != null) {
								if (excelDataObject.taskType.equals("New")) {
									// first task addition without comparison
									workAssignmentPage.createTask(driver, wait, action1, excelDataObject,
											currentProjectName);

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
								} else if (excelDataObject.taskType.equals("Extension")) {
									// driver.findElement(By.xpath("//div[@class='tray-content-widget__content
									// tray-content-widget__right-content']/div[@class='pvFilter
									// form-field']/input[@type='text']")).sendKeys(excelDataObject.taskName);

									List<WebElement> alltasks = workAssignmentPage.getallCount(driver, excelDataObject);

									boolean flagTaskFound = false;
									for (Iterator<WebElement> iterator2 = alltasks.iterator(); iterator2.hasNext();) {
										WebElement tasksToBeExtended = (WebElement) iterator2.next();
										// System.out.println(alltasks);
										// wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementSelectionStateToBe(tasksToBeExtended,true)));
										action1.moveToElement(tasksToBeExtended).click().build().perform();
										if (tasksToBeExtended.getText().contains(excelDataObject.taskName)) {
											tasksToBeExtended.click();
											addAllocation = new AddAllocation();
											addAllocation.extendAllocation(driver, action1, excelDataObject,
													tasksToBeExtended);
											flagTaskFound = true;
											if (iterator.hasNext()) {
												excelDataObject = (ExcelDataObject) iterator.next();
												currentTaskName = excelDataObject.taskName;

											}
											break;
										}
									}
									if (flagTaskFound == false) {

										workAssignmentPage.createTask(driver, wait, action1, excelDataObject,
												currentProjectName);

										newRequirement = new NewRequirement();
										newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

										addAllocation = new AddAllocation();
										addAllocation.addAllocation(driver, action1, excelDataObject);

										// excelDataObject = (ExcelDataObject) iterator.next();
										if (iterator.hasNext()) {
											excelDataObject = (ExcelDataObject) iterator.next();
											currentTaskName = excelDataObject.taskName;

										}

									}

								}

							}

							// adding task for last entry in excel
							if (!iterator.hasNext()) {

								if (currentTaskName.equals(excelDataObject.taskName)
										&& excelDataObject.taskType.equals("New")) {
									newRequirement = new NewRequirement();
									newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

									addAllocation = new AddAllocation();
									addAllocation.addAllocation(driver, action1, excelDataObject);

								} else if (excelDataObject.taskType.equals("New")) {
									workAssignmentPage.createTask(driver, wait, action1, excelDataObject,
											currentProjectName);

									newRequirement = new NewRequirement();
									newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

									addAllocation = new AddAllocation();
									addAllocation.addAllocation(driver, action1, excelDataObject);

								} else if (excelDataObject.taskType.equals("Extension")) {

									List<WebElement> alltasks = workAssignmentPage.getallCount(driver, excelDataObject);

									boolean flagTaskFound = false;
									for (Iterator<WebElement> iterator2 = alltasks.iterator(); iterator2.hasNext();) {
										WebElement tasksToBeExtended = (WebElement) iterator2.next();
										// System.out.println(alltasks);
										// wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementSelectionStateToBe(tasksToBeExtended,true)));
										for (int k = 0; k <= 5; k++) {
											try {
												Thread.sleep(3000);
												action1.moveToElement(tasksToBeExtended).click().build().perform();

												if (tasksToBeExtended.getText().contains(excelDataObject.taskName)) {
													tasksToBeExtended.click();
													addAllocation = new AddAllocation();
													addAllocation.extendAllocation(driver, action1, excelDataObject,
															tasksToBeExtended);
													flagTaskFound = true;
													break;
												}
												break;
											} catch (Exception ex) {

											}
										}
									}
									if (flagTaskFound == false) {

										workAssignmentPage.createTask(driver, wait, action1, excelDataObject,
												currentProjectName);

										newRequirement = new NewRequirement();
										newRequirement.addNewRequirement(driver, action1, excelDataObject.gcmRole);

										addAllocation = new AddAllocation();
										addAllocation.addAllocation(driver, action1, excelDataObject);

									}

								}

							}

						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							System.out.println("");
						} finally {
							driver.quit();
						}

					}
				});

			}
		});
	}

	/**
	 * Create the application.
	 */
	public PlanViewBaseClass() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBackground(new Color(255, 255, 255));
		frame.setBounds(100, 100, 536, 468);
		frame.setState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(102, 255, 204));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 30, 30, 30, 1000, 30, 30, 30, 300, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 4;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		panel.add(tabbedPane, gbc_tabbedPane);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(51, 204, 255));
		tabbedPane.addTab("PlanView", null, panel_2, null);
		panel_2.setLayout(null);

		textArea_Console = new JTextArea();
		textArea_Console.setBounds(68, 10, 441, 560);
		panel_2.add(textArea_Console);

		JScrollPane scrollPane = new JScrollPane(textArea_Console);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(24, 200, 1200, 600);
		panel_2.add(scrollPane);

		btnstartButton = new JButton("Start");
		btnstartButton.setBackground(new Color(255, 255, 255));
		btnstartButton.setForeground(new Color(0, 0, 0));
		btnstartButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnstartButton.setBounds(550, 35, 100, 50);
		panel_2.add(btnstartButton);

		btnSelectFile = new JButton("Select File");
		btnSelectFile.setBackground(new Color(255, 255, 255));
		btnSelectFile.setForeground(new Color(0, 0, 0));
		btnSelectFile.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnSelectFile.setBounds(200, 35, 150, 50);
		panel_2.add(btnSelectFile);

		filePath = new JLabel("Please select file path ....");
		filePath.setBackground(new Color(255, 255, 255));
		filePath.setForeground(new Color(0, 0, 0));
		filePath.setFont(new Font("Tahoma", Font.BOLD, 15));
		filePath.setBounds(200, 100, 850, 50);
		panel_2.add(filePath);

		btnclearButton = new JButton("Clear");
		btnclearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea_Console.setText(null);
				filePath.setText("Please select file path ....");
				btnstartButton.setEnabled(false);
				btnclearButton.setEnabled(false);
			}
		});
		btnclearButton.setForeground(Color.BLACK);
		btnclearButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnclearButton.setBackground(new Color(255, 255, 255));
		btnclearButton.setBounds(750, 35, 100, 50);
		panel_2.add(btnclearButton);

	}

	private static void updateTextArea(final String text, final JTextArea Console) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Console.append(text);
			}
		});
	}

	private static void redirectSystemStreams(final JTextArea Console) {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf((char) b), Console);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextArea(new String(b, off, len), Console);
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};

		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}
}
