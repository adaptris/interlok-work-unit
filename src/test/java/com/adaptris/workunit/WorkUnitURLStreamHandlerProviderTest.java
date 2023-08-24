package com.adaptris.workunit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

import com.adaptris.workunit.util.WorkUnitDetector;
import com.adaptris.workunit.util.WorkUnitRegistry;
import com.adaptris.workunit.util.WorkUnitUrlUtils;

public class WorkUnitURLStreamHandlerProviderTest {

  @Test
  public void testCreateURLStreamHandler() throws Exception {
    URL workUnitUrl = WorkUnitDetector.findWorkUnitAdaptrisVersionUrl("my-work-unit");
    WorkUnitRegistry.getInstance().register("my-work-unit", WorkUnitUrlUtils.stripJarUrlFilePath(workUnitUrl));

    InputStream openStream = new URL("workunit:my-work-unit!/work-unit.xml").openStream();

    assertNotNull(openStream);
  }

  @Test
  public void testCreateURLStreamHandlerNotWorkUnit() throws Exception {
    assertThrows(MalformedURLException.class, () -> new URL("not-workunit:my-work-unit!/work-unit.xml").openStream());
  }

}
