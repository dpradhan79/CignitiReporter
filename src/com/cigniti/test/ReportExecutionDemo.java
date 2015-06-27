package com.cigniti.test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.cigniti.BrowserContext;
import com.cigniti.ReporterConstants;
import com.cigniti.report.CReporter;
import com.cigniti.report.TestResult;
public class ReportExecutionDemo {
	private static final Logger LOG = Logger.getLogger(ReportExecutionDemo.class);
	
	
	@BeforeSuite
	public void beforeSuite()
	{
		
	}
	@AfterSuite
	public void afterSuite()
	{
		
	}
	@BeforeTest
	public void beforeTest(ITestContext testContext)
	{
		//get browser info
		String browserName = testContext.getCurrentXmlTest().getParameter("browser");
		String version = testContext.getCurrentXmlTest().getParameter("version");
		String platform = testContext.getCurrentXmlTest().getParameter("platform");
		CReporter reporter = CReporter.getCReporter(browserName, version, platform, true);		
		reporter.calculateSuiteStartTime();
		
		
	}
	
	@AfterTest
	public void afterTest(ITestContext testContext) throws Exception
	{
		//get browser info
		String browserName = testContext.getCurrentXmlTest().getParameter("browser");
		String version = testContext.getCurrentXmlTest().getParameter("version");
		String platform = testContext.getCurrentXmlTest().getParameter("platform");
		CReporter reporter = CReporter.getCReporter(browserName, version, platform, true);
		reporter.calculateSuiteExecutionTime();
		reporter.createHtmlSummaryReport(ReporterConstants.APP_BASE_URL,true);
		reporter.closeSummaryReport();
	}
	@BeforeMethod
	public void beforeMethod(Method method, ITestContext testContext)
	{
		//get browser info
		String browserName = testContext.getCurrentXmlTest().getParameter("browser");
		String version = testContext.getCurrentXmlTest().getParameter("version");
		String platform = testContext.getCurrentXmlTest().getParameter("platform");
		
		CReporter reporter = CReporter.getCReporter(browserName, version, platform, true);
		BrowserContext browserContext = reporter.getBrowserContext();
		
		TestResult.tc_name.put(browserContext, method.getName() + "-");
		
		String strClassName = this.getClass().getName();
		int iLastIndex_Dot = strClassName.lastIndexOf(".");
		String strPackage = strClassName.substring(0,iLastIndex_Dot);
		
		TestResult.packageName.put(browserContext, strPackage );		
		
		reporter.testHeader(TestResult.tc_name.get(browserContext), true);
		TestResult.stepNum.put(browserContext, 0);
		TestResult.PassNum.put(browserContext, 0);
		TestResult.FailNum.put(browserContext, 0);
		TestResult.testName.put(browserContext, method.getName());
		
		reporter.calculateTestCaseStartTime();
	}
	@AfterMethod
	public void afterMethod(Method method, ITestContext testContext) throws IOException
	{
		//get browser info
		String browserName = testContext.getCurrentXmlTest().getParameter("browser");
		String version = testContext.getCurrentXmlTest().getParameter("version");
		String platform = testContext.getCurrentXmlTest().getParameter("platform");
		
		CReporter reporter = CReporter.getCReporter(browserName, version, platform, true);
		BrowserContext browserContext = reporter.getBrowserContext();
		
		reporter.calculateTestCaseExecutionTime();
		
		reporter.closeDetailedReport();
		if(TestResult.FailNum.get(browserContext) != 0)
		{
			Integer failCount = TestResult.failCounter.get(browserContext) == null ? 1 : TestResult.failCounter.get(browserContext) + 1;
			TestResult.failCounter.put(browserContext, failCount);
			Map<String, String> mapResult = TestResult.testResults.get(browserContext);
			if(mapResult == null)
			{
				mapResult = new HashMap<String, String>();
			}
			mapResult.put(TestResult.tc_name.get(browserContext), ReporterConstants.TEST_CASE_STATUS_FAIL);
			TestResult.testResults.put(browserContext, mapResult);
		}
		else
		{
			Integer passCount = TestResult.passCounter.get(browserContext) == null ? 1 : TestResult.passCounter.get(browserContext) + 1;
			TestResult.passCounter.put(browserContext, passCount);
			Map<String, String> mapResult = TestResult.testResults.get(browserContext);
			if(mapResult == null)
			{
				mapResult = new HashMap<String, String>();
			}
			mapResult.put(TestResult.tc_name.get(browserContext), ReporterConstants.TEST_CASE_STATUS_PASS);
			TestResult.testResults.put(browserContext, mapResult);
		}
	}
	
	
	
	
	@Test(description = "verifying addition of 2 integers")
	public void validateSumInt(ITestContext testContext) throws Throwable
	{
		int iNum1 = 1;
		int iNum2 = 2;
		int expectedSum = 3;
		int actualResult = 0;
		String browserName = testContext.getCurrentXmlTest().getParameter("browser");
		String version = testContext.getCurrentXmlTest().getParameter("version");
		String platform = testContext.getCurrentXmlTest().getParameter("platform");
		CReporter reporter = CReporter.getCReporter(browserName, version, platform, true);
		Map<String, String> mapTestDescription = TestResult.testDescription.get(reporter.getBrowserContext());
		if(mapTestDescription == null)
		{
			mapTestDescription = new HashMap<String, String>();
			
		}
		mapTestDescription.put(TestResult.tc_name.get(reporter.getBrowserContext()), "verifying addition of 2 integers");
		TestResult.testDescription.put(reporter.getBrowserContext(), mapTestDescription);
		try
		{
			actualResult = Addition.Add(iNum1, iNum2);
			Assert.assertEquals(actualResult, expectedSum);
			reporter.SuccessReport("Addition Of 2 Numbers : " + iNum1 + " , " + iNum2 , "Expected Value ( " + expectedSum + " ) Match With Actual Value ( " + actualResult + " )");
		}
		catch(AssertionError e)
		{
			reporter.SuccessReport("Addition Of 2 Numbers : " + iNum1 + " , " + iNum2 , "Expected Value ( " + expectedSum + " ) Does Not Match With Actual Value ( " + actualResult + " )");
		}
		
	}
	@Test(description = "verifying addition of 2 doubles")
	public void validateSumDouble(ITestContext testContext) throws Throwable
	{
		double iNum1 = 1.0;
		double iNum2 = 2;
		double expectedSum = 4;
		double actualResult = 0;
		String browserName = testContext.getCurrentXmlTest().getParameter("browser");
		String version = testContext.getCurrentXmlTest().getParameter("version");
		String platform = testContext.getCurrentXmlTest().getParameter("platform");
		CReporter reporter = CReporter.getCReporter(browserName, version, platform, true);
		Map<String, String> mapTestDescription = TestResult.testDescription.get(reporter.getBrowserContext());
		if(mapTestDescription == null)
		{
			mapTestDescription = new HashMap<String, String>();
			
		}
		mapTestDescription.put(TestResult.tc_name.get(reporter.getBrowserContext()), "verifying addition of 2 doubles");
		TestResult.testDescription.put(reporter.getBrowserContext(), mapTestDescription);
		try
		{
			actualResult = Addition.Add(iNum1, iNum2);
			Assert.assertEquals(actualResult, expectedSum);
			reporter.SuccessReport("Addition Of 2 Doubles : " + iNum1 + " , " + iNum2 , "Expected Value ( " + expectedSum + " ) Match With Actual Value ( " + actualResult + " )");
		}
		catch(AssertionError e)
		{
			
			try
			{
				reporter.failureReport("Addition Of 2 Doubles : " + iNum1 + " , " + iNum2 , "Expected Value ( " + expectedSum + " ) Does Not Match With Actual Value ( " + actualResult + " )", new FirefoxDriver());
			}
			catch(Throwable t)
			{
				LOG.info("Exception Encountered : " + t.getMessage());
			}
		}
		
		
	}
	
}
