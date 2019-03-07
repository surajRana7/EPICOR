package epicor;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class EndToEnd {
	
	public WebDriver driver;
	
	@BeforeTest
	public void OpenBrowser()
	{
		System.setProperty("webdriver.ie.driver","C:\\Users\\Sach\\Desktop\\assignment\\IEDriver_32bit\\IEDriverServer.exe");
		driver=new InternetExplorerDriver();
	}
	
	@Test
	public void demo() throws InterruptedException, IOException, AWTException
	{
		File f1 =new File("C:\\Users\\Sach\\eclipse-workspace-New\\LedgerAccount\\src\\essentials\\Details.prop");
		FileInputStream fistr=new FileInputStream(f1);
		Properties prop=new Properties();
		prop.load(fistr);
		
		driver.get(prop.getProperty("url"));
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath("//input[@id='txtPassword']")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//button[@id='btnLogin']")).click();
		
		driver.switchTo().frame("menu");
		
		driver.findElement(By.xpath("//a[@class='dynatree-title'][contains(text(),'Epicor Education')]")).click();
		Thread.sleep(2000);
		//driver.findElement(By.xpath("//a[@class='dynatree-title'][contains(text(),'Main')]")).click();	
		driver.findElement(By.xpath("//a[@class='dynatree-title'][contains(text(),'Financial Management')]")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//a[contains(text(),'General Ledger')]")).click();
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//span[@class='dynatree-node dynatree-exp-c dynatree-ico-c']//a[@class='dynatree-title'][contains(text(),'Setup')]")).click();
		Thread.sleep(2000);

		driver.findElement(By.xpath("//div[@title='General Ledger Account']")).click();
		
		Set<String> ids=driver.getWindowHandles();
		Iterator <String>it=ids.iterator();
		String parent=it.next();
		String child=it.next();
		driver.switchTo().window(child);
		System.out.println(driver.getTitle());		
		Thread.sleep(2000);
		
		File f =new File("C:\\Users\\Sach\\Desktop\\GLAccount.xls");
		FileInputStream fis=new FileInputStream(f);
		HSSFWorkbook wb=new HSSFWorkbook(fis);
		HSSFSheet ws=wb.getSheet("Sheet1");
		
		String abc=NumberToTextConverter.toText(ws.getRow(1).getCell(0).getNumericCellValue());
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='glaeGLAccount_dropText']")).sendKeys(abc);
		driver.findElement(By.xpath("//button[@title='Save']")).click();
		Thread.sleep(2000);
		
		Robot robo=new Robot();
		robo.keyPress(KeyEvent.VK_ENTER);
		Thread.sleep(3000);
		
		driver.findElement(By.xpath("//button[@title='Save']")).click();
		Thread.sleep(2000);
		String sc=driver.findElement(By.xpath("//div[@class='TreeItem active']")).getText();
		Assert.assertEquals("1238-01-20", sc);
	}
	
	@AfterTest
	public void Teardown()
	{
		driver.quit();
	}
	
	
}
