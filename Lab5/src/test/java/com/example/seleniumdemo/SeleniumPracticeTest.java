package com.example.seleniumdemo;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SeleniumPracticeTest {

	private static WebDriver driver;
	WebElement element;

	public static final String url = "http://www.seleniumframework.com/PracticeForm/";
	
	@BeforeClass
	public static void driverSetup() {
		System.setProperty("webdriver.chrome.driver", "/home/studinf/ablachuciak/Java/chromedriver");
		//System.setProperty("webdriver.chrome.driver", "C:\\Users\\Erathiel\\Documents\\Java\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void checkBoxTest() {
		driver.get(url);
		driver.findElement(By.cssSelector("#item-vfb-6 > div > span:nth-child(1)")).click();
		
		WebElement result = driver.findElement(By.cssSelector("#vfb-6-0"));
		
		assertTrue(result.isSelected());
	}
	
	@Test
	public void verificationTest() {
		driver.get(url);
		String testnum = "14";
		WebElement textBox = driver.findElement(By.id("vfb-3"));
		textBox.sendKeys(testnum);
		
		driver.findElement(By.id("vfb-4")).click();
		
		String result = driver.findElement(By.xpath("//*[@id='content']/div[2]/div[1]/div/div/div")).getText();
		
		assertEquals("Your form was successfully submitted. Thank you for contacting us.", result);
	}
	
	@Test
	public void selectTest() {
		driver.get(url);
		
		Select selector = new Select(driver.findElement(By.id("vfb-12")));
		selector.selectByIndex(2);
		
		List<WebElement> options = selector.getOptions();
		assertEquals(options.get(2), selector.getFirstSelectedOption());
	}
	
	@Test
	public void dragDropTest() {
		driver.get(url);
		
		WebElement dragged = driver.findElement(By.id("draga"));
		WebElement draggedto = driver.findElement(By.id("dragb"));
		
		(new Actions(driver)).dragAndDrop(dragged, draggedto).perform();
		
		assertTrue(draggedto.isDisplayed());
	}
	
	@Test
	public void alertTest() {
		driver.get(url);
		
		String prev = driver.getWindowHandle();
		driver.findElement(By.id("alert")).click();
		Alert alertwin = driver.switchTo().alert();
		alertwin.dismiss();
		
		String result = driver.getWindowHandle();
		
		assertEquals(prev, result);
	}
	
	@Test
	public void linkTest() {
		driver.get(url);
		
		driver.findElement(By.linkText("This is a link")).click();
		
		(new WebDriverWait(driver, 15)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
            	Set<String> handles = driver.getWindowHandles();
            	WebDriver a = null;
            	for (String handle : handles) {
            		d.switchTo().window(handle);
            		if(d.getTitle().startsWith("Selenium Framework | Selenium,"))
            			a = d;
            	}
				if(a.getTitle().contains("Selenium Framework | Selenium,")) {
					return true;
				}
				else return false;
            }
        });
		
		String title = driver.getTitle();
		String expected = "Selenium Framework | Selenium, Cucumber, Ruby, Java et al.";
		
		assertEquals(expected, title);
	}
	
	@AfterClass
	public static void cleanUp() {
		driver.quit();
	}
}
