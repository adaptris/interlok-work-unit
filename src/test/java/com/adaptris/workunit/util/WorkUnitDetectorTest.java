package com.adaptris.workunit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;

import org.junit.jupiter.api.Test;

public class WorkUnitDetectorTest {

  @Test
  public void testList() throws Exception {
    String[] workUnits = WorkUnitDetector.list();
    assertTrue(workUnits.length > 0);
  }

  @Test
  public void testJarName() throws Exception {
    String url = "jar:file:/path/to/my-work-unit.jar!/META-INF/work-unit";
    assertEquals("my-work-unit", WorkUnitDetector.jarName(url));
    String url2 = "/META-INF/work-unit";
    assertEquals("/META-INF/work-unit", WorkUnitDetector.jarName(url2));
  }

  @Test
  public void testIsCorrectJarTrue() throws Exception {
    assertTrue(WorkUnitDetector.isCorrectJar(new URL("jar:file:/path/to/my-work-unit.jar!/META-INF/work-unit"), "my-work-unit"));
  }

  @Test
  public void testIsCorrectJarFalse() throws Exception {
    assertFalse(WorkUnitDetector.isCorrectJar(new URL("jar:file:/path/to/my-work-unit.jar!/META-INF/work-unit"), "different-jar"));
  }

  @Test
  public void testIsCorrectJarNullName() throws Exception {
    assertTrue(WorkUnitDetector.isCorrectJar(new URL("jar:file:/path/to/my-work-unit.jar!/META-INF/work-unit"), null));
  }

}
