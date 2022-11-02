package com.adaptris.workunit.varsub;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Properties;

import org.junit.Test;

import com.adaptris.util.KeyValuePairSet;

public class KeyValuePairVariableSetTest {

  @Test
  public void variables() {

    KeyValuePairVariableSet keyValuePairVariableSet = new KeyValuePairVariableSet();

    keyValuePairVariableSet.setVariables(new KeyValuePairSet(Collections.singletonMap("key1", "value1")));

    Properties variables = keyValuePairVariableSet.variables("doesntmatter");

    assertEquals(1, variables.size());
    assertEquals("value1", variables.getProperty("key1"));
  }

}
