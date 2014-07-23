//package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class HelloArkGoodbye {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  public static void main(String[] args){
	  setUp();
	  testHelloArkGoodbye();
	  tearDown();
  }
  
  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://prod.the-ark.org.au/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testHelloArkGoodbye() throws Exception {
    driver.get(baseUrl + "/ark/login?1");
    // ERROR: Caught exception [Error: locator strategy either id or name must be specified explicitly.]
    driver.findElement(By.name("userName")).clear();
    driver.findElement(By.name("userName")).sendKeys("arksuperuser@ark.org.au");
    //###insertUltraSecurePassword###
    driver.findElement(By.id("signInButton")).click();
    driver.findElement(By.id("ida")).click();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
