package com.edu.ufcg.splab.tests.priorj.coverage.model;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.maven.plugins.surefire.report.ReportTestSuite;
import org.apache.maven.plugins.surefire.report.SurefireReportParser;
import org.junit.Assert;
import org.junit.Test;

public class ReadReportTest {

	@Test
	public void test() {
        List<File> reportsDirs = new ArrayList<File>();
		File reportsDir = new File("C:/PriorJ/reports" );
        Assert.assertTrue( "Reports directory is missing: " + reportsDir.getAbsolutePath(), reportsDir.exists() );
        reportsDirs.add( reportsDir );
        SurefireReportParser parser = new SurefireReportParser( reportsDirs, Locale.getDefault() );
        List<ReportTestSuite> reports;
        try
        {
            reports = parser.parseXMLReportFiles();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Couldn't parse XML reports", e );
        }
	}

}
