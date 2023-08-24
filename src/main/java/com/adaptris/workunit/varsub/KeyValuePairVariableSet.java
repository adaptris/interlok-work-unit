package com.adaptris.workunit.varsub;

import java.util.Properties;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.InputFieldHint;
import com.adaptris.util.KeyValuePairSet;
import com.adaptris.workunit.services.WorkUnitService;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@XStreamAlias("key-value-pair-variable-set")
@AdapterComponent
@ComponentProfile(summary = "Set of key / value variables to use in the work unit variables substitution.", tag = "work-unit")
public class KeyValuePairVariableSet implements VariableSet {

  /**
   * Set of key / value variables to use in the work unit variables substitution. The 'key' should match the ${key} to replace in the work
   * unit XML with the 'value'. <br>
   * The {@link WorkUnitService#getWorkUnitName()} and {@link WorkUnitService#getXmlConfigName()} will be used to find the list of variable
   * in the work unit xml.
   */
  @Valid
  @NotNull
  @InputFieldHint(style = "com.adaptris.workunit.util.WorkUnitDetailsUtils#listVars(workUnitName,xmlConfigName)")
  @Getter
  @Setter
  @NonNull
  private KeyValuePairSet variables = new KeyValuePairSet();

  @Override
  public Properties variables(String workUnitName) {
    return KeyValuePairSet.asProperties(variables);
  }

}
