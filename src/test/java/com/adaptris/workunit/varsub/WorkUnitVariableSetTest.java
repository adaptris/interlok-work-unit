package com.adaptris.workunit.varsub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.adaptris.core.CoreException;

public class WorkUnitVariableSetTest {

  @Test
  public void variables() throws CoreException {

    WorkUnitVariableSet workUnitVariableSet = new WorkUnitVariableSet();

    workUnitVariableSet.setName("variables");

    Properties variables = workUnitVariableSet.variables("my-work-unit");

    assertEquals(1, variables.size());
    assertEquals("Value-1", variables.getProperty("value1"));
  }

  @Test
  public void variablesInvalidWorkUnitName() throws CoreException {

    WorkUnitVariableSet workUnitVariableSet = new WorkUnitVariableSet();

    workUnitVariableSet.setName("variables");

    assertThrows(CoreException.class, () -> workUnitVariableSet.variables("work-unit"));
  }

}
