package com.adaptris.workunit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

public class WorkUnitUrlUtilsTest {

  @Test
  public void testConvertToJarUrl() throws MalformedURLException, IOException {
    URL workUnitUrl = WorkUnitDetector.findWorkUnitAdaptrisVersionUrl("my-work-unit");
    WorkUnitRegistry.getInstance().register("my-work-unit", WorkUnitUrlUtils.stripJarUrlFilePath(workUnitUrl));

    try {

      URL jarUrl = WorkUnitUrlUtils
          .toJarUrl(new URL(WorkUnitUrlUtils.WORK_UNIT_PROTOCOL + ":my-work-unit" + WorkUnitUrlUtils.SEPARATOR + "work-unit.xml"));

      assertTrue(jarUrl.toString().startsWith("jar:file:"));
      assertTrue(jarUrl.toString().endsWith("my-work-unit.jar!/work-unit.xml"));
    } finally {
      WorkUnitRegistry.getInstance().unregister("my-work-unit");
    }
  }

  @Test
  public void testConvertToJarUrlNoWorkUnitName() throws MalformedURLException, IOException {
    URL workUnitUrl = WorkUnitDetector.findWorkUnitAdaptrisVersionUrl("my-work-unit");
    WorkUnitRegistry.getInstance().register("my-work-unit", WorkUnitUrlUtils.stripJarUrlFilePath(workUnitUrl));

    try {

      assertThrows(IllegalArgumentException.class, () -> WorkUnitUrlUtils
          .toJarUrl(new URL(WorkUnitUrlUtils.WORK_UNIT_PROTOCOL + ":" + WorkUnitUrlUtils.SEPARATOR + "work-unit.xml")));
    } finally {
      WorkUnitRegistry.getInstance().unregister("my-work-unit");
    }
  }

  @Test
  public void testWorkUnitUrl() throws MalformedURLException, IOException {
    URL expectedUrl = new URL(WorkUnitUrlUtils.WORK_UNIT_PROTOCOL + ":" + "my-work-unit" + WorkUnitUrlUtils.SEPARATOR + "work-unit.xml");
    assertEquals(expectedUrl, WorkUnitUrlUtils.workUnitUrl("my-work-unit", "work-unit.xml"));
  }

  @Test
  public void testToJarUrl() throws MalformedURLException, IOException {
    URL workUnitUrl = WorkUnitDetector.findWorkUnitAdaptrisVersionUrl("my-work-unit");
    WorkUnitRegistry.getInstance().register("my-work-unit", WorkUnitUrlUtils.stripJarUrlFilePath(workUnitUrl));

    try {
      URL jarUrl = WorkUnitUrlUtils.toJarUrl("my-work-unit", "work-unit.xml");

      assertTrue(jarUrl.toString().startsWith("jar:file:"));
      assertTrue(jarUrl.toString().endsWith("my-work-unit.jar!/work-unit.xml"));
    } finally {
      WorkUnitRegistry.getInstance().unregister("my-work-unit");
    }
  }

  @Test
  public void testToJarUrlDummyWorkUnit() throws MalformedURLException, IOException {
    String urlString = "jar:file:/some/path/dummy-work-unit.jar!/";
    WorkUnitRegistry.getInstance().register("dummy-work-unit", new URL(urlString));

    try {
      URL jarUrl = WorkUnitUrlUtils.toJarUrl("dummy-work-unit", "work-unit.xml");

      assertEquals(urlString + "work-unit.xml", jarUrl.toString());
      assertTrue(jarUrl.toString().endsWith("my-work-unit.jar!/work-unit.xml"));
    } finally {
      WorkUnitRegistry.getInstance().unregister("dummy-work-unit");
    }
  }

  @Test
  public void testToJarUrlNotInRegistry() throws MalformedURLException, IOException {
    URL jarUrl = WorkUnitUrlUtils.toJarUrl("dummy-work-unit", "work-unit.xml");

    assertNull(jarUrl);
  }

  @Test
  public void testStripJarUrlFilePath() throws MalformedURLException, IOException {
    URL jarUrl = WorkUnitUrlUtils.stripJarUrlFilePath(new URL("jar:file:/path/to/my-work-unit.jar!/META-INF/work-unit"));

    assertEquals("jar:file:/path/to/my-work-unit.jar!/", jarUrl.toString());
  }

  @Test
  public void testJarName() throws Exception {
    String url = "jar:file:/path/to/my-work-unit.jar!/META-INF/work-unit";
    assertEquals("my-work-unit", WorkUnitUrlUtils.jarName(url));
    String url2 = "/META-INF/work-unit";
    assertEquals("/META-INF/work-unit", WorkUnitUrlUtils.jarName(url2));
  }

  @Test
  public void testIsCorrectJarTrue() throws Exception {
    assertTrue(WorkUnitUrlUtils.isCorrectJar(new URL("jar:file:/path/to/my-work-unit.jar!/META-INF/work-unit"), "my-work-unit"));
  }

  @Test
  public void testIsCorrectJarFalse() throws Exception {
    assertFalse(WorkUnitUrlUtils.isCorrectJar(new URL("jar:file:/path/to/my-work-unit.jar!/META-INF/work-unit"), "different-jar"));
  }

  @Test
  public void testIsCorrectJarNullName() throws Exception {
    assertTrue(WorkUnitUrlUtils.isCorrectJar(new URL("jar:file:/path/to/my-work-unit.jar!/META-INF/work-unit"), null));
  }

}
