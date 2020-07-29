    private static void registerJMXBean(String mbeanName, Object mbean) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        Class mgmtFactoryClass = Class.forName("java.lang.management.ManagementFactory");
        Class objectNameClass = Class.forName("javax.management.ObjectName");
        Class mbsClass = Class.forName("javax.management.MBeanServer");
        Constructor objectNameConstructor = objectNameClass.getConstructor(new Class[] { String.class });
        Object mbeanObjectName = objectNameConstructor.newInstance(new Object[] { mbeanName });
        Object mbs = mgmtFactoryClass.getMethod("getPlatformMBeanServer", null).invoke(null, null);
        Method isMBeanRegisteredMethod = mbsClass.getMethod("isRegistered", new Class[] { objectNameClass });
        Boolean isMBeanRegistered = (Boolean) isMBeanRegisteredMethod.invoke(mbs, new Object[] { mbeanObjectName });
        if (isMBeanRegistered.booleanValue()) {
            InitLogger.log(log, "Dozer JMX MBean [" + mbeanName + "] already registered.  Unregistering the existing MBean.");
            Method unregisterMBeanMethod = mbsClass.getMethod("unregisterMBean", new Class[] { objectNameClass });
            unregisterMBeanMethod.invoke(mbs, new Object[] { mbeanObjectName });
        }
        Method registerMBeanMethod = mbsClass.getMethod("registerMBean", new Class[] { Object.class, objectNameClass });
        registerMBeanMethod.invoke(mbs, new Object[] { mbean, mbeanObjectName });
        InitLogger.log(log, "Dozer JMX MBean [" + mbeanName + "] auto registered with the Platform MBean Server");
    }
