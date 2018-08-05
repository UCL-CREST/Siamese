    private static final Object copyValueFromString(Class<?> destClass, String strV) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object dest = null;
        if (!StringUtils.isBlank(strV)) {
            if (java.lang.String.class.equals(destClass)) {
                log.debug("--[copyValueFromString]-- destClass=java.lang.String");
                dest = strV;
            } else if (java.sql.Timestamp.class.equals(destClass) || java.util.Date.class.equals(destClass)) {
                log.debug("--[copyValueFromString]-- destClass=" + destClass);
                dest = new DateConverter().convert(destClass, strV);
            } else {
                try {
                    log.debug("--[copyValueFromString]-- try destClass.newInstance and BeanUtilsEx.copyProperties(dest,strV)");
                    dest = destClass.newInstance();
                    BeanUtilsEx.copyProperties(dest, strV);
                } catch (Exception e) {
                    try {
                        log.debug("--[copyValueFromString]-- try destClass.getConstructor(String.class)");
                        dest = destClass.getConstructor(String.class).newInstance(strV);
                    } catch (NoSuchMethodException ex) {
                        try {
                            log.debug("--[copyValueFromString]-- try destClass.getMethod('valueOf', String.class).invoke(null, strV)");
                            dest = destClass.getMethod("valueOf", String.class).invoke(null, strV);
                        } catch (Exception e2) {
                            log.error("--[copyValueFromString]-- " + destClass + " All copy Method FAILED.", e);
                        }
                    }
                }
            }
        }
        return dest;
    }
