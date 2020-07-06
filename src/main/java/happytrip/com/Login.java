package happytrip.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class Login
{
	WebDriver driver;
	WebDriverWait wait;
	XSSFWorkbook workbook;
	XSSFSheet sheet;
	
	
	XSSFCell cell;
	XSSFCell cell1;
	XSSFCell user;
	XSSFCell pass;

	@BeforeMethod
	public void TestSetup()
	{
		// Set the path of the chrome driver.
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\chromedriver.exe");
		//    System.setProperty("webdriver.chrome.driver", "C:\\Users\\Vishal\\Documents\\Selenium\\happytrip\\Chrome\\chromedriver.exe");
		driver = new ChromeDriver();

		// Enter url.
		driver.get("http://43.254.161.195:8085/happytriphotel1/");
		driver.manage().window().maximize();

		wait = new WebDriverWait(driver,30);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@AfterMethod
	public void teardown() {
		driver.close();
	}
	@Test(dataProvider="empLogin")
	public void loginAsAdmin(String username, String password, String Flight, String Route, String Distance, 
		
		 String DepartureTime,String ArrivalTime,String BuisnessClass,String EconomyClass ) {
//		System.out.println(username);
//		System.out.println(password);
//		System.out.println(flight);
//		System.out.println(route);
//		System.out.println("******************");
		clickLoginAsAdminLink();
		waitForPresenceOfElement("username");
		clearAndEnter("username", username);
		clearAndEnter("password", password);
		clickSignIn();
//		verifySuccessfulLogin(username);
//		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		clickScheduleFlightlink();



		driver.manage().timeouts().pageLoadTimeout(100,TimeUnit.SECONDS);

		Airline(Flight);
		Way(Route);
		
		DIST("distance",Distance);
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	
		datee();
		departure(DepartureTime);
		Arivaldatee();
		Arrival(ArrivalTime);
		Buisness("classBusiness", BuisnessClass);
		Economy("classEconomy",EconomyClass);
		AddSchedule();
	
	
	}


	private void clickScheduleFlightlink() {
		driver.findElement(By.xpath("//*[@id=\"MainTabs\"]/li[5]/a")).click();
		
	}
	

	
	private void verifySuccessfulLogin(String username) {
		Assert.assertTrue(driver.findElements(By.xpath("//*[contains(text(),'Welcome "+username+"')]")).size()>0,"Login is Successfull with username:"+username);
	}

	private void waitForPresenceOfElement(String id) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
	}

	private void clickLoginAsAdminLink() {
		driver.findElement(By.linkText("Log in as admin")).click();
	}

	private void clickSignIn() {
		driver.findElement(By.id("signInButton")).click();
	}
	private void clearAndEnter(String id , String dataToEnter) {
		driver.findElement(By.id(id)).clear();
		driver.findElement(By.id(id)).sendKeys(dataToEnter);
	}
	

	private void Airline(String flight ) {

		 Select select = new Select(driver.findElement(By.id("flight")));
    	select.selectByVisibleText(flight);	

	}
	private void Way(String router) {
	Select select=new Select(driver.findElement(By.id("route")));
	select.selectByValue(router);}

	private void DIST(String id , String enter) {
		driver.findElement(By.id(id)).clear();
		driver.findElement(By.id(id)).sendKeys(enter);
	}


private void datee() {
	  
	driver.findElement(By.xpath("//*[@id=\"AddSchedule\"]/dl/dd[6]/img")).click();
	  driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/table/tbody/tr[5]/td[5]/a")).click();
}
	
private void departure(String depart) {
	Select select=new Select(driver.findElement(By.id("departureTime")));
	select.selectByValue(depart);
	}


	private void Arivaldatee() {
		
		
		driver.findElement(By.xpath("//*[@id=\"AddSchedule\"]/dl/dd[8]/img")).click();
		
		driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/table/tbody/tr[5]/td[5]/a")).click();
	}


	
private void Arrival(String arrive) {
	Select select=new Select(driver.findElement(By.id("arrivalTime")));
	select.selectByValue(arrive);
	}


private void Buisness(String id, String buisness) {

	
		driver.findElement(By.id(id)).clear();
		driver.findElement(By.id(id)).sendKeys(buisness);
	
	}

private void Economy(String id, String economy) {

	
	driver.findElement(By.id(id)).clear();
	driver.findElement(By.id(id)).sendKeys(economy);

}

private void AddSchedule() {
 driver.findElement(By.id("signInButton")).click();
}



	

	
	@DataProvider(name="empLogin")
	public Object[][] loginData() {
		Object[][] arrayObject = getExcelData(System.getProperty("user.dir")+"\\test.xlsx","LoginData");
		return arrayObject;
	}
	
	public String[][]  getExcelData(String filePath, String sheetName)
	{
		String [] [] arrayExcelData = null;
		try  
		{  
			// Import excel sheet.
			File file=new File(filePath);
			// Load the file.
			FileInputStream fis = new FileInputStream(file);
			//creating Workbook instance that refers to .xlsx file  
			XSSFWorkbook wb = new XSSFWorkbook(fis);  
			XSSFSheet sheet = wb.getSheet(sheetName);     //creating a Sheet object to retrieve object  

			int totalNoOfRows = sheet.getPhysicalNumberOfRows();
			int totalNoOfCols = sheet.getRow(0).getPhysicalNumberOfCells();

			arrayExcelData = new String[totalNoOfRows-1][totalNoOfCols];
			//row =1, since we want to skip the first header row (username,password strings)
			for (int row= 1 ; row < totalNoOfRows; row++) {

				for (int col= 0; col < totalNoOfCols; col++) {
					XSSFCell cell = sheet.getRow(row).getCell(col);
					//to format data to string content
					DataFormatter df = new DataFormatter();
					arrayExcelData[row-1][col] = df.formatCellValue(cell);
//					System.out.println(arrayExcelData[row-1][col]);
				}

			}
		}  
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			e.printStackTrace();
		}
		return arrayExcelData;
	} 

}