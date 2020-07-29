    private void registerMBean(String fqnName, boolean isTask) {
        try {
            String domain = fqnName.substring(0, fqnName.lastIndexOf('.'));
            String type = fqnName.substring(fqnName.lastIndexOf('.') + 1);
            ObjectName objectName = new ObjectName(domain + ":type=" + type);
            Class clazz = Class.forName(domain + "." + type);
            Constructor constructor = clazz.getConstructor((Class[]) null);
            if (!ManagementFactory.getPlatformMBeanServer().isRegistered(objectName)) {
                ManagementFactory.getPlatformMBeanServer().registerMBean(constructor.newInstance((Object[]) null), objectName);
                if (isTask) this.taskMBeans.add(objectName);
                ParallelJLogger.info(MessageCode.SRV07I, domain, type);
            } else {
                ParallelJLogger.info(MessageCode.SRV04I, domain, type);
            }
        } catch (Exception e) {
            ParallelJLogger.error(MessageCode.SRV11E, e, e.getClass().getCanonicalName());
        }
    }
