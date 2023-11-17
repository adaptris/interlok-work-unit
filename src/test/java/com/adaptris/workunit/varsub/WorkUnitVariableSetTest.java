package com.adaptris.workunit.varsub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.adaptris.core.CoreException;
import com.adaptris.workunit.util.WorkUnitDetector;
import com.adaptris.workunit.util.WorkUnitRegistry;
import com.adaptris.workunit.util.WorkUnitUrlUtils;

public class WorkUnitVariableSetTest {

  @Test
  public void testVariables() throws CoreException, IOException {
    URL workUnitUrl = WorkUnitDetector.findWorkUnitAdaptrisVersionUrl("my-work-unit");
    WorkUnitRegistry.getInstance().register("my-work-unit", WorkUnitUrlUtils.stripJarUrlFilePath(workUnitUrl));

    try {
      WorkUnitVariableSet workUnitVariableSet = new WorkUnitVariableSet();

      workUnitVariableSet.setName("variables");

      Properties variables = workUnitVariableSet.variables("my-work-unit");

      assertEquals(2, variables.size());
      assertEquals("Value-1", variables.getProperty("value1"));
      assertEquals("Value-2", variables.getProperty("value2"));
    } finally {
      WorkUnitRegistry.getInstance().unregister("my-work-unit");
    }
  }

  @Test
  public void testVariablesInvalidWorkUnitName() throws CoreException {

    WorkUnitVariableSet workUnitVariableSet = new WorkUnitVariableSet();

    workUnitVariableSet.setName("variables");

    assertThrows(CoreException.class, () -> workUnitVariableSet.variables("work-unit"));
  }

}
