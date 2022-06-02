package com.adaptris.workunit.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.CoreException;
import com.adaptris.interlok.junit.scaffolding.services.ExampleServiceCase;

public class WorkUnitServiceTest extends ExampleServiceCase {

  @Test
  public void testPrepare() throws CoreException {
    WorkUnitService service = newService();
    assertNull(service.getProxiedService());
    service.prepare();
    assertNotNull(service.getProxiedService());
  }

  @Test
  public void testDoService() throws CoreException {
    WorkUnitService service = newService();
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    execute(service, msg);

    assertFalse(msg.getMetadata().isEmpty());
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    return newService();
  }

  private WorkUnitService newService() {
    WorkUnitService service = new WorkUnitService();
    service.setWorkUnitName("my-work-unit");
    return service;
  }

}
