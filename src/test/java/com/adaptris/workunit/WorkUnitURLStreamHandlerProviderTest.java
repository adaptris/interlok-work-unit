package com.adaptris.workunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

public class WorkUnitURLStreamHandlerProviderTest {

  @Test
  public void convertUrl() throws Exception {
    URL resultUrl = new WorkUnitURLStreamHandlerProvider().convertUrl(new URL("workunit:work-unit.xml"));

    System.out.println(resultUrl);
    assertTrue(resultUrl.getPath().endsWith("work-unit.xml"));
    assertTrue(resultUrl.getProtocol().equals("file"));
  }

  @Test
  public void convertUrlInJar() throws Exception {
    URL resultUrl = new WorkUnitURLStreamHandlerProvider().convertUrl(new URL("workunit:my-work-unit!/work-unit.xml"));

    System.out.println(resultUrl);
    assertTrue(resultUrl.getPath().endsWith("work-unit.xml"));
    assertTrue(resultUrl.getProtocol().equals("jar"));
  }

  @Test
  public void createURLStreamHandler() throws Exception {
    InputStream openStream = new URL("workunit:my-work-unit!/work-unit.xml").openStream();

    assertNotNull(openStream);
  }

  @Test
  public void url() throws Exception {
    URL url = WorkUnitURLStreamHandlerProvider.url("my-work-unit", "work-unit.xml");

    assertEquals("workunit:my-work-unit!/work-unit.xml", url.toString());
  }

}
