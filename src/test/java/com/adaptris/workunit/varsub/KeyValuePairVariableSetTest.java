package com.adaptris.workunit.varsub;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.adaptris.util.KeyValuePairSet;

public class KeyValuePairVariableSetTest {

  @Test
  public void testVariables() {

    KeyValuePairVariableSet keyValuePairVariableSet = new KeyValuePairVariableSet();

    keyValuePairVariableSet.setVariables(new KeyValuePairSet(Collections.singletonMap("key1", "value1")));

    Properties variables = keyValuePairVariableSet.variables("doesntmatter");

    assertEquals(1, variables.size());
    assertEquals("value1", variables.getProperty("key1"));
  }

}
