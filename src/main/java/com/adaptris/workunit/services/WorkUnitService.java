package com.adaptris.workunit.services;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.annotation.InputFieldHint;
import com.adaptris.core.AdaptrisMarshaller;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.DefaultMarshaller;
import com.adaptris.core.Service;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.management.BootstrapProperties;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.core.varsub.WorkUnitVarSubPreProcessor;
import com.adaptris.workunit.ChainURLStreamHandlerProvider;
import com.adaptris.workunit.WorkUnitURLStreamHandlerProvider;
import com.adaptris.workunit.varsub.VariableSet;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@XStreamAlias("work-unit-service")
@AdapterComponent
@ComponentProfile(summary = "Execute the named work unit.", tag = "service,work-unit")
@DisplayOrder(order = {"workUnitName"})
public class WorkUnitService extends ServiceImp {

  private static final String XML_EXTENSION = ".xml";
  private static final String DEFAULT_XML_NAME = "work-unit";

  static {
    ChainURLStreamHandlerProvider.init();
  }

  /**
   * The name of the work unit jar file without the .jar
   *
   * @param workUnitName
   * @return String workUnitName
   */
  @NotBlank
  @InputFieldHint(style = "com.adaptris.workunit.util.WorkUnitDetector#list")
  @Getter
  @Setter
  @NonNull
  private String workUnitName;

  /**
   * The name of the work unit config xml file without the .xml By default 'work-unit' is used
   *
   * @param xmlConfigName
   * @return String xmlConfigName
   */
  @AdvancedConfig
  @InputFieldDefault(DEFAULT_XML_NAME)
  @Getter
  @Setter
  private String xmlConfigName;

  @Valid
  @NotNull
  @Getter
  @Setter
  @NonNull
  private List<VariableSet> variableSets;

  @Getter(value = AccessLevel.PROTECTED)
  @Setter(value = AccessLevel.PROTECTED)
  private Service proxiedService;

  public WorkUnitService() {
    variableSets = new ArrayList<>();
  }

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    getProxiedService().doService(msg);
  }

  private Service deserializeService(InputStream inputStream) throws Exception {
    AdaptrisMarshaller marshaller = DefaultMarshaller.getDefaultMarshaller();
    String xml = varSub(IOUtils.toString(inputStream, Charset.defaultCharset()));
    return (Service) marshaller.unmarshal(xml);
  }

  private String varSub(String xml) throws CoreException {
    BootstrapProperties bootstrapProperties = new BootstrapProperties(new Properties());
    return new WorkUnitVarSubPreProcessor(bootstrapProperties).process(xml, loadSubstitutions());
  }

  private Properties loadSubstitutions() throws CoreException {
    Properties properties = new Properties();
    for (VariableSet variableSet : variableSets) {
      properties.putAll(variableSet.variables(getWorkUnitName()));
    }
    return properties;
  }

  @Override
  public void prepare() throws CoreException {
    try {
      setProxiedService(
          deserializeService(
              WorkUnitURLStreamHandlerProvider.url(workUnitName, xmlConfigName() + XML_EXTENSION).openStream()
              )
          );
      LifecycleHelper.prepare(getProxiedService());
    } catch (Exception expt) {
      throw ExceptionHelper.wrapCoreException(expt);
    }
  }

  private String xmlConfigName() {
    return StringUtils.defaultIfBlank(DEFAULT_XML_NAME, getXmlConfigName());
  }

  @Override
  public void start() throws CoreException {
    LifecycleHelper.start(getProxiedService());
  }

  @Override
  public void stop() {
    LifecycleHelper.stop(getProxiedService());
  }
  @Override
  protected void initService() throws CoreException {
    LifecycleHelper.init(getProxiedService());
  }

  @Override
  protected void closeService() {
    LifecycleHelper.close(getProxiedService());
  }

}
