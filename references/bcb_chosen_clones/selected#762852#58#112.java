    private void init(String persistFile) {
        InputStream thefile = null;
        try {
            thefile = gov.lanl.Utility.IOHelper.getInputStream(persistFile);
        } catch (Exception fe) {
            try {
                thefile = gov.lanl.Utility.IOHelper.getInputStream(System.getProperty("telemed.defaults"));
                props.load(thefile);
                thefile = gov.lanl.Utility.IOHelper.getInputStream(props.getProperty("persist.properties"));
                props = new Properties();
            } catch (Exception fe2) {
                cat.error("No property file found! " + fe2);
                System.exit(1);
            }
        }
        try {
            props.load(thefile);
        } catch (IOException ie) {
            cat.error("Failed to load " + thefile + " ", ie);
            System.exit(1);
        }
        for (Enumeration e = props.propertyNames(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            String persistName = props.getProperty(name);
            if (cat.isDebugEnabled()) {
                cat.debug("Mapping '" + name + "' to '" + persistName + "'");
            }
            try {
                Class CorbaClass = Class.forName(name);
                Class dbClass = Class.forName(persistName);
                Class[] params = new Class[] { CorbaClass };
                Constructor construct = dbClass.getConstructor(params);
                Object obj = CorbaClass.newInstance();
                Object[] conParams = new Object[] { obj };
                construct.newInstance(conParams);
                hash.put(name, construct);
            } catch (ClassNotFoundException ex) {
                cat.error("Can't find class for name '" + persistName, ex);
                System.exit(1);
            } catch (NoSuchMethodException ex) {
            } catch (InstantiationException ex) {
                cat.error("InstantiationException for class '" + persistName, ex);
                System.exit(1);
            } catch (IllegalAccessException ex) {
                cat.error("IllegalAccessException for class '" + persistName, ex);
                System.exit(1);
            } catch (java.lang.reflect.InvocationTargetException ex) {
                cat.error("InvocationTargetException for class '" + persistName, ex);
                System.exit(1);
            } catch (Exception ex) {
                cat.error("Exception for class '" + persistName, ex);
                System.exit(1);
            }
        }
    }
