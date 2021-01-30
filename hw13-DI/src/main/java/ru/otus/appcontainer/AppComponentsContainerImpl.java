package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

public class AppComponentsContainerImpl implements AppComponentsContainer {

  private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

  private final Map<String, Object> appComponentsByName = new HashMap<>();
  private final Map<Class<?>, Object> configClassByInstance = new HashMap<>();

  public AppComponentsContainerImpl(String packageNameScan) {

    try {
      Reflections reflectionPaths = new Reflections(packageNameScan, new TypeAnnotationsScanner());
      Set<Class<?>> rootResourceClasses = reflectionPaths
          .getTypesAnnotatedWith(AppComponentsContainerConfig.class, true);

      Class<?>[] initialConfigClassses = new Class<?>[rootResourceClasses.size()];
      rootResourceClasses.toArray(initialConfigClassses);

      processConfig(initialConfigClassses);
    }catch (Exception e){
      logger.error("error scan package", e);
      throw new RuntimeException(e);
    }
  }

  public AppComponentsContainerImpl(Class<?>... initialConfigClassses) {
    processConfig(initialConfigClassses);
  }

  private void processConfig(Class<?>... configClasses) {

    checkConfigClass(configClasses);

    buildConfigClassByInstance(configClasses);

    buildAppComponentsByName(configClasses);
  }


  private void buildAppComponentsByName(Class<?>... configClasses) {
    var orderMethodBeans = getOrderMethodBeans(configClasses);
    orderMethodBeans.forEach(method -> {
      Object newBean = getNewBean(method, getBeanArgs(method));

      appComponentsByName.put(newBean.getClass().getName(), newBean);
      appComponentsByName.put(method.getReturnType().getName(), newBean);
      appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), newBean);
    });

  }

  private Object getNewBean(Method method, Object[] args) {
    try {
      return method.invoke(configClassByInstance.get(method.getDeclaringClass()), args);
    } catch (Exception e) {
      logger.error("new instance bean", e);
      throw new RuntimeException(e);
    }
  }

  private Object[] getBeanArgs(Method method) {
    return Arrays.stream(method.getParameterTypes())
        .map(this::getAppComponent)
        .toArray();
  }

  private List<Method> getOrderMethodBeans(Class<?>... configClasses) {
    return Arrays.stream(configClasses)
        .flatMap(configClass -> Arrays.stream(configClass.getDeclaredMethods()))
        .filter(method -> method.isAnnotationPresent(AppComponent.class))
        .sorted(Comparator.comparing(o ->
            "Container"
                + o.getDeclaringClass().getAnnotation(AppComponentsContainerConfig.class).order()
                + "Component"
                + o.getAnnotation(AppComponent.class).order()
        ))
        .collect(Collectors.toList());
  }


  private void buildConfigClassByInstance(Class<?>... configClasses) {
    for (Class<?> configClass : configClasses) {
      configClassByInstance.put(configClass, getInstanceConfigClass(configClass));
    }
  }

  private Object getInstanceConfigClass(Class<?> configClass) {
    try {
      return configClass.getConstructor().newInstance();
    } catch (Exception e) {
      logger.error("new instance configClass", e);
      throw new RuntimeException(e);
    }
  }

  private void checkConfigClass(Class<?>... configClasses) {
    for (Class<?> configClass : configClasses) {
      if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
        throw new IllegalArgumentException(
            String.format("Given class is not config %s", configClass.getName()));
      }
    }
  }

  @Override
  public <C> C getAppComponent(Class<C> componentClass) {
    return getAppComponent(componentClass.getName());
  }

  @Override
  public <C> C getAppComponent(String componentName) {
    return (C) appComponentsByName.get(componentName);
  }
}
