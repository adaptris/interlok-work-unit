package com.adaptris.workunit.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

import org.junit.Test;

public class WorkUnitDetailsUtilsTest {

  @Test
  public void testListVars() throws Exception {
    String[] variables = WorkUnitDetailsUtils.listVars("my-work-unit", "work-unit.xml");
    assertEquals(2, variables.length);
  }

  @Test
  public void testListVarsWrongJarName() throws Exception {
    assertThrows(NoSuchElementException.class, () -> WorkUnitDetailsUtils.listVars("wrong-work-unit", "work-unit.xml"));
  }

  @Test
  public void testListVarsWrongXmlName() throws Exception {
    assertThrows(FileNotFoundException.class, () -> WorkUnitDetailsUtils.listVars("my-work-unit", "wrong-work-unit.xml"));
  }

  @Test
  public void testListVarProperties() throws Exception {
    String[] variables = WorkUnitDetailsUtils.listVarProperties("my-work-unit");
    assertEquals(1, variables.length);
  }

  @Test
  public void testListVarPropertiesWrongJarName() throws Exception {
    assertThrows(NoSuchElementException.class, () -> WorkUnitDetailsUtils.listVarProperties("wrong-work-unit"));
  }

  @Test
  public void testReadme() throws Exception {
    String[] readme = WorkUnitDetailsUtils.readme("my-work-unit");
    assertEquals(1, readme.length);
    assertEquals(replaceNewLine("# My Work Unit\n\nThis is a work unit to use in work unit tests"), replaceNewLine(readme[0]));
  }

  @Test
  public void testReadmeLowercase() throws Exception {
    String[] readme = WorkUnitDetailsUtils.readme("my-work-unit-lowercase-readme");
    assertEquals(1, readme.length);
    assertEquals(replaceNewLine("# My Work Unit\n\nThis is a work unit to use in work unit tests"), replaceNewLine(readme[0]));
  }

  private String replaceNewLine(String str) {
    return str.replaceAll("\\n|\\r\\n", System.getProperty("line.separator"));
  }

}
