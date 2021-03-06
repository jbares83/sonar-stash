package org.sonar.plugins.stash.issue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;
import org.sonar.api.rule.Severity;

public class CoverageIssueTest {

  @Spy
  CoverageIssue myIssue;
  
  private final static String PATH_COVERAGE = "coverage/file";
  private final static String SONARQUBE_URL = "http://sonarqube";
  
  @Before
  public void setUp() {
    CoverageIssue issue = new CoverageIssue(Severity.INFO, PATH_COVERAGE);
    myIssue = spy(issue);
  }
  
  @Test
  public void testGetMessage() {
    myIssue.setPreviousCoverage(60.0);
    when(myIssue.getCoverage()).thenReturn(50.0);
  
    assertEquals("Code coverage of file " + PATH_COVERAGE + " lowered from 60.0% to 50.0%.", myIssue.getMessage());
  }
  
  @Test
  public void testGetCoverage() {
    myIssue.setLinesToCover(100);
    myIssue.setUncoveredLines(60);
    assertTrue(myIssue.getCoverage() == 40);
    
    myIssue.setLinesToCover(0);
    myIssue.setUncoveredLines(60);
    assertTrue(myIssue.getCoverage() == 0);
  }
  
  @Test
  public void testIsLowered() {
    myIssue.setPreviousCoverage(60.0);
    when(myIssue.getCoverage()).thenReturn(50.0);
    assertTrue(myIssue.isLowered());
    
    myIssue.setPreviousCoverage(50.0);
    when(myIssue.getCoverage()).thenReturn(60.0);
    assertFalse(myIssue.isLowered());
  }
  
  @Test
  public void testPrintIssueMarkdown() {
    myIssue.setPreviousCoverage(60.0);
    when(myIssue.getCoverage()).thenReturn(50.0);
    
    assertEquals("*INFO* - Code coverage of file " + PATH_COVERAGE + " lowered from 60.0% to 50.0%.", myIssue.printIssueMarkdown(SONARQUBE_URL));
  };
}
