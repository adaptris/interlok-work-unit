package com.adaptris.workunit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import org.junit.Test;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.CoreException;
import com.adaptris.core.MetadataElement;
import com.adaptris.core.ServiceList;
import com.adaptris.core.services.metadata.AddMetadataService;
import com.adaptris.interlok.junit.scaffolding.services.ExampleServiceCase;
import com.adaptris.util.KeyValuePairSet;
import com.adaptris.workunit.varsub.KeyValuePairVariableSet;
import com.adaptris.workunit.varsub.WorkUnitVariableSet;

public class WorkUnitServiceTest extends ExampleServiceCase {

  @Test
  public void testPrepare() throws CoreException {
    WorkUnitService service = newService();
    assertService(service, "${value1}");
  }

  @Test
  public void testPrepareWithWorkUnitVarSet() throws CoreException {
    WorkUnitService service = newService();
    WorkUnitVariableSet variableSet = new WorkUnitVariableSet();
    variableSet.setName("variables");
    service.getVariableSets().add(variableSet);
    assertService(service, "Value-1");
  }

  @Test
  public void testPrepareWithKeyValuePairVarSet() throws CoreException {
    WorkUnitService service = newService();
    KeyValuePairVariableSet variableSet = new KeyValuePairVariableSet();
    variableSet.setVariables(new KeyValuePairSet(Collections.singletonMap("value1", "KeyValuePairVarSet-Value-1")));
    service.getVariableSets().add(variableSet);
    assertService(service, "KeyValuePairVarSet-Value-1");
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

  private void assertService(WorkUnitService service, String expectedMetadataValue) throws CoreException {
    assertNull(service.getProxiedService());
    service.prepare();
    assertNotNull(service.getProxiedService());
    AddMetadataService addMetadataService = (AddMetadataService) ((ServiceList) service.getProxiedService()).get(1);
    assertFalse(addMetadataService.getMetadataElements().isEmpty());
    MetadataElement metadataElement = addMetadataService.getMetadataElements().iterator().next();
    assertEquals(expectedMetadataValue, metadataElement.getValue());
  }

}
