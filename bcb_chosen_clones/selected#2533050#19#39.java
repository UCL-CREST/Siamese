    public static Statistic getInstance(String name) {
        Statistic statistic = null;
        Constructor<?> ctor = null;
        String fullyQualifiedClassName = "oldmcdata.analyzers.utils." + name;
        try {
            Class<?> c = Class.forName(fullyQualifiedClassName);
            ctor = c.getConstructor((Class[]) null);
            statistic = (Statistic) ctor.newInstance((Object[]) null);
        } catch (ClassNotFoundException cfne) {
            handleException(cfne, "Class: " + fullyQualifiedClassName + " Does Not Exist, or not on CLASSPATH");
        } catch (NoSuchMethodException nsme) {
            handleException(nsme, "Class: " + fullyQualifiedClassName + " has no constructor with 0 arguments");
        } catch (InvocationTargetException ite) {
            handleException(ite, "Can not construct instance of Class: " + fullyQualifiedClassName);
        } catch (IllegalAccessException iae) {
            handleException(iae, "Can not access: " + fullyQualifiedClassName + " - not public");
        } catch (InstantiationException ie) {
            handleException(ie, "Class: " + fullyQualifiedClassName + " can not be constructed as it is an interface or abstract class");
        }
        return statistic;
    }
