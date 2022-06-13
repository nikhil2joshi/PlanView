package testCases;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
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
		System.out.println("Chromedriver loading" + path);
		System.setProperty("webdriver.chrome.driver", path + "\\chromedriver.exe");
		System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

	}
	private static JFrame frame;
	private static JButton btnNewButton;
	private static JTabbedPane tabbedPane;
	private static JTextArea textArea_Console;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlanViewBaseClass pvc = new PlanViewBaseClass();
					PlanViewBaseClass.frame.setVisible(true);
					PlanViewBaseClass.frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
					textArea_Console.setText(null);

					btnNewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							options = new ChromeOptions();
							// options.addArguments("--headless");
							textArea_Console.setText(null);
							WebDriver driver = new ChromeDriver(options);
							try {
								redirectSystemStreams(textArea_Console);

								driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
								WebDriverWait wait = new WebDriverWait(driver, 10000);
								// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
								driver.get("https://worldline.pvcloud.com/");
								driver.manage().window().maximize();

								driver.findElement(By.xpath("//div[@class='wg-pki']//input[4]")).click(); // click on
																											// login
																											// Button

								Thread.sleep(10000L);// Wait for user to select certificate and enter PIN

								System.out.println(driver.getTitle());

								Actions action1 = new Actions(driver);
								driver.manage().window().maximize();
								//driver.manage().window().setPosition(new Point(0, -2000));
								
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

								// adding last entry in excel
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

							} catch (Exception e1) {
								e1.printStackTrace();
							} finally {
								driver.quit();
							}

						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}

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
		textArea_Console.setBounds(68, 10, 641, 560);
		panel_2.add(textArea_Console);

		JScrollPane scrollPane = new JScrollPane(textArea_Console);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(24, 100, 1200, 800);
		panel_2.add(scrollPane);

		btnNewButton = new JButton("Start");
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton.setBounds(400, 35, 100, 50);
		panel_2.add(btnNewButton);

		JButton startButton = new JButton("Clear");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea_Console.setText(null);
			}
		});
		startButton.setForeground(Color.BLACK);
		startButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		startButton.setBackground(new Color(255, 255, 255));
		startButton.setBounds(600, 35, 100, 50);
		panel_2.add(startButton);

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
