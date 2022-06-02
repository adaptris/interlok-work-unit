package com.adaptris.workunit.services;

import java.io.InputStream;
import java.net.URL;

import javax.validation.constraints.NotBlank;

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
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.workunit.ChainURLStreamHandlerProvider;
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

  private static final String WORK_UNIT_URL_PREFIX = "workunit:";

  private static final String XML_EXTENSION = ".xml";
  private static final String DEFAULY_XML_NAME = "work-unit";
  private static final String SEPARATOR = "!/";

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
  @InputFieldDefault(DEFAULY_XML_NAME)
  @Getter
  @Setter
  private String xmlConfigName;

  @Getter(value = AccessLevel.PROTECTED)
  @Setter(value = AccessLevel.PROTECTED)
  private Service proxiedService;

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    getProxiedService().doService(msg);
  }

  private Service deserializeService(InputStream inputStream) throws CoreException {
    AdaptrisMarshaller marshaller = DefaultMarshaller.getDefaultMarshaller();
    return (Service) marshaller.unmarshal(inputStream);
  }

  @Override
  public void prepare() throws CoreException {
    try {
      setProxiedService(
          deserializeService(
              new URL(WORK_UNIT_URL_PREFIX + getWorkUnitName() + SEPARATOR + xmlConfigName() + XML_EXTENSION).openStream()
              )
          );
      LifecycleHelper.prepare(getProxiedService());
    } catch (Exception e) {
      throw ExceptionHelper.wrapCoreException(e);
    }
  }

  private String xmlConfigName() {
    return StringUtils.defaultIfBlank(DEFAULY_XML_NAME, getXmlConfigName());
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
