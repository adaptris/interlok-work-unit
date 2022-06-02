# interlok-work-unit

[![GitHub tag](https://img.shields.io/github/tag/adaptris/interlok-work-unit.svg)](https://github.com/adaptris/interlok-work-unit/tags) [![license](https://img.shields.io/github/license/adaptris/interlok-work-unit.svg)](https://github.com/adaptris/interlok--work-unit/blob/develop/LICENSE) [![Actions Status](https://github.com/adaptris/interlok-work-unit/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/adaptris/interlok-work-unit/actions) [![codecov](https://codecov.io/gh/adaptris/interlok-work-unit/branch/develop/graph/badge.svg)](https://codecov.io/gh/adaptris/interlok-work-unit) [![Total alerts](https://img.shields.io/lgtm/alerts/g/adaptris/interlok-work-unit.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/adaptris/interlok-work-unit/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/adaptris/interlok-work-unit.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/adaptris/interlok-work-unit/context:java)

## What's this? ##

The beginnings of using work units within your Interlok configurations.
A work unit contains a service-list.  That is to say you may have a complex and/or often used set of services that you find yourself copying into multiple installations of Interlok and you now want to make that list of services more modular.

The work unit allows you to black box that process making it easier for the next person to re-use your process (set of services).

## Testing me ##

The first thing is to build your work-unit.

Work unit's are simply a service-list configuration and any supporting files your services require bundled into a jar file. 

### Configuring your service-list ###

That's it, it's just a service list.  An example;

```xml
<service-collection class="service-list">
  <services>
    <service class="log-message-service">
      <unique-id>log-message-service</unique-id>
      <log-level>DEBUG</log-level>
    </service>
  </services>
</service-collection>
```

Sometimes your service-list will require additional files; for example if you're configuring a transform then you'll need the XSL file.  Sometimes the additional resource required will be remote, perhaps you're serving a transform over http.  But other times you want the resource locally packaged with your configuration.  In this case your work unit jar file will contain your service-list configuration and another supporting file like an XSL transform named "my-transform.xsl".  In these cases it's important that in your work unit's service list you refer to the packaged resource file like this using the  __workunit:__  url handler;

```xml
<service-collection class="service-list">
  <services>
    <xml-transform-service>
      <unique-id>transform-service</unique-id>
      <url>workunit:my-transform.xsl</url>
    </xml-transform-service>
  </services>
</service-collection>
```

If we want to specify the jar name

```xml
<service-collection class="service-list">
  <services>
    <xml-transform-service>
      <unique-id>transform-service</unique-id>
      <url>workunit:my-work-unit!/my-transform.xsl</url>
    </xml-transform-service>
  </services>
</service-collection>
```

### Work Unit Service ###

Once you have your work unit Jar file and it's dropped into your Interlok lib directory (make sure your re-start Interlok) you can then edit your main configuration adding a new service.
If you have a service-list file packed in that my-work-unit.jar jar file named __my-work-unit.xml__, then inside your main Interlok configuration you can refer to your work unit's service list simply like this;

```xml
<work-unit-service>
  <unique-id>work-unit-service</unique-id>
  <!-- Jar name without .jar -->
  <work-unit-name>my-work-unit</work-unit-name>
  <!-- Xml name without .xml. Optional, default to work-unit -->
  <xml-config-name>my-work-unit</xml-config-name>
</work-unit-service>
```

### Dynamic Execution ###

Package your work-unit as shown above. The service you'll choose is the __dynamic-service-executor (with url)__. 

Assuming your work unit jar file (name is not important) contains a service-list configuration named my-service.xml at the root of the jar, then you would configure your dynamic service executor with the following URL;

```xml
<dynamic-service-executor>
  <unique-id>dynamic-service</unique-id>
  <service-extractor class="dynamic-service-from-url">
    <url>workunit:my-work-unit.xml</url>
  </service-extractor>
</dynamic-service-executor>
```

If there is a risk of multiple jars having the same xml file name you can specify the jar name

```xml
<dynamic-service-executor>
  <unique-id>dynamic-service</unique-id>
  <service-extractor class="dynamic-service-from-url">
    <url>workunit:my-work-unit!/my-service.xml</url>
  </service-extractor>
</dynamic-service-executor>
```

Now imagine your work unit jar file (again, name of the jar file is unimportant) has a service-list configuration buried a couple of levels deep in a directory structure like this; __/some/directory/__ then you would configure your dynamic service like this;

```xml
<dynamic-service-executor>
  <unique-id>dynamic-service</unique-id>
  <service-extractor class="dynamic-service-from-url">
    <url>workunit:some/directory/my-service.xml</url>
  </service-extractor>
</dynamic-service-executor>
```
