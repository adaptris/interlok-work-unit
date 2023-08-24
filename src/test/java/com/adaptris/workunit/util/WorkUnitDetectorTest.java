package com.adaptris.workunit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class WorkUnitDetectorTest {

  @Test
  public void testListVars() throws Exception {
    String[] workUnits = WorkUnitDetector.list();
    assertEquals(3, workUnits.length);
  }

  @Test
  public void testListUrls() throws Exception {
    Collection<URL> listUrls = WorkUnitDetector.listUrls("META-INF/adaptris-version");
    assertTrue(listUrls.size() > 0);
  }

  @Test
  public void testFindWorkUnitAdaptrisVersionUrl() throws Exception {
    URL jarUrl = WorkUnitDetector.findWorkUnitAdaptrisVersionUrl("my-work-unit");

    assertTrue(jarUrl.toString().startsWith("jar:file:"));
    assertTrue(jarUrl.toString().endsWith("my-work-unit.jar!/META-INF/adaptris-version"));
  }

  @Test
  public void testFindWorkUnitAdaptrisVersionUrlFails() throws Exception {
    assertThrows(NoSuchElementException.class, () -> WorkUnitDetector.findWorkUnitAdaptrisVersionUrl("dummy-work-unit"));
  }

}
