package com.mdsol.audit.demo.audits;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DiffCalculator {

  public static List<AuditField> diff(Object obj1, Object obj2) throws IllegalAccessException, InvocationTargetException,
    NoSuchMethodException, InstantiationException {
    return diff(obj1, obj2, "");
  }

  private static List<AuditField> diff(Object obj1, Object obj2, String path) throws IllegalAccessException,
    NoSuchMethodException, InvocationTargetException, InstantiationException {
    List<AuditField> diffList = new ArrayList<>();

    Class<?> clazz = Stream.of(obj1, obj2)
      .filter(Objects::nonNull)
      .findFirst()
      .map(Object::getClass)
      .orElse(null);

    if (null == obj1 && clazz != null) {

      // Get the constructor of the class
      Constructor<?> constructor = clazz.getDeclaredConstructor();

      // Create an instance of the class
      obj1 = constructor.newInstance();
    }

    if (null == obj2 && clazz != null) {

      // Get the constructor of the class
      Constructor<?> constructor = clazz.getDeclaredConstructor();

      // Create an instance of the class
      obj2 = constructor.newInstance();
    }

    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);
      Object value1 = field.get(obj1);
      Object value2 = field.get(obj2);

      String currentPath = Stream.of(path, field.getName()).filter(value -> !value.isEmpty()).collect(Collectors.joining("."));

      if (!equals(value1, value2)) {
        if (value1 != null && value2 != null &&
          value1.getClass().equals(value2.getClass()) &&
          !value1.getClass().getName().startsWith("java.")) {
          diffList.addAll(diff(value1, value2, currentPath));
        } else {
          AuditField diffField = new AuditField(currentPath, value1 == null ? "null" : value1.toString(), value2 == null ? "null" :
            value2.toString());
          diffList.add(diffField);
        }
      }
    }

    return diffList;
  }

  private static boolean equals(Object obj1, Object obj2) {
    if (obj1 == obj2) return true;
    if (obj1 == null || obj2 == null) return false;
    return obj1.equals(obj2);
  }
}

