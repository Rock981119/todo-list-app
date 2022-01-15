package com.nice.todolist.util;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class PropertyUtil {
	/**
     * Gets the properties which have null values from the given object.
     * 
     * @param - source object
     * 
     * @return - String array of property names.
     */
    public static String[] getNullPropertiesString(Object source) {
        Set<String> emptyNames = getNullProperties(source);
        String[] result = new String[emptyNames.size()];

        return emptyNames.toArray(result);
    }


    /**
     * Gets the properties which have null values from the given object.
     * 
     * @param - source object
     * 
     * @return - Set<String> of property names.
     */
    public static Set<String> getNullProperties(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        return emptyNames;
    }

    /**
     * Gets the properties which are not null from the given object.
     * 
     * @param - source object
     * 
     * @return - Set<String> array of property names.
     */
    public static Set<String> getNotNullProperties(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> names = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue != null)
                names.add(pd.getName());
        }

        return names;
    }
}
