    private synchronized void _createUsageParameterSet(SbbID sbbId, String name, boolean failIfSbbHasNoUsageParamSet) throws NullPointerException, UnrecognizedSbbException, InvalidArgumentException, UsageParameterSetNameAlreadyExistsException, ManagementException {
        if (sbbId == null) throw new NullPointerException("Sbb ID is null!");
        SbbComponent sbbComponent = sleeContainer.getComponentRepository().getComponentByID(sbbId);
        if (sbbComponent == null) {
            throw new UnrecognizedSbbException(sbbId.toString());
        }
        ServiceComponent serviceComponent = sleeContainer.getComponentRepository().getComponentByID(getService());
        if (!serviceComponent.getSbbIDs(sleeContainer.getComponentRepository()).contains(sbbId)) {
            throw new UnrecognizedSbbException(sbbId.toString() + " is not part of " + getService());
        }
        Class<?> usageParameterClass = sbbComponent.getUsageParametersConcreteClass();
        if (usageParameterClass == null) {
            if (failIfSbbHasNoUsageParamSet) {
                throw new InvalidArgumentException(sbbId.toString() + " does not define a usage parameters interface");
            } else {
                return;
            }
        }
        SbbUsageMBeanMapKey mapKey = new SbbUsageMBeanMapKey(sbbId, name);
        if (this.usageMBeans.containsKey(mapKey)) {
            throw new UsageParameterSetNameAlreadyExistsException("name " + name + " already exists for service " + serviceComponent + " and sbb " + sbbComponent);
        }
        UsageMBeanImpl usageMbean = null;
        UsageNotificationManagerMBeanImpl usageNotificationManagerMBean = null;
        Thread currentThread = Thread.currentThread();
        ClassLoader currentThreadClassLoader = currentThread.getContextClassLoader();
        try {
            currentThread.setContextClassLoader(sbbComponent.getClassLoader());
            SbbNotification sbbNotification = new SbbNotification(serviceID, sbbId);
            AbstractUsageParameterSet installedUsageParameterSet = (AbstractUsageParameterSet) AbstractUsageParameterSet.newInstance(usageParameterClass, sbbNotification, name, sleeContainer);
            Class<?> usageParameterMBeanClass = sbbComponent.getUsageParametersMBeanImplConcreteClass();
            Constructor<?> constructor = null;
            if (sbbComponent.isSlee11()) {
                constructor = usageParameterMBeanClass.getConstructor(new Class[] { Class.class, NotificationSource.class });
            } else {
                constructor = usageParameterMBeanClass.getConstructor(new Class[] { Class.class, SbbNotification.class });
            }
            ObjectName usageParameterMBeanObjectName = generateUsageParametersMBeanObjectName(name, sbbId, sbbComponent.isSlee11());
            usageMbean = (UsageMBeanImpl) constructor.newInstance(new Object[] { sbbComponent.getUsageParametersMBeanConcreteInterface(), sbbNotification });
            usageMbean.setObjectName(usageParameterMBeanObjectName);
            usageMbean.setParent(this);
            sleeContainer.getMBeanServer().registerMBean(usageMbean, usageParameterMBeanObjectName);
            installedUsageParameterSet.setUsageMBean(usageMbean);
            usageMbean.setUsageParameter(installedUsageParameterSet);
            this.usageMBeans.put(mapKey, usageMbean);
            if (sbbComponent.isSlee11() && name == null) {
                Class<?> usageNotificationManagerMBeanClass = sbbComponent.getUsageNotificationManagerMBeanImplConcreteClass();
                constructor = usageNotificationManagerMBeanClass.getConstructor(new Class[] { Class.class, NotificationSource.class, SleeComponentWithUsageParametersInterface.class });
                usageNotificationManagerMBean = (UsageNotificationManagerMBeanImpl) constructor.newInstance(new Object[] { sbbComponent.getUsageNotificationManagerMBeanConcreteInterface(), sbbNotification, sbbComponent });
                ObjectName usageNotificationManagerMBeanObjectName = generateUsageNotificationManagerMBeanObjectName(sbbId);
                usageNotificationManagerMBean.setObjectName(usageNotificationManagerMBeanObjectName);
                sleeContainer.getMBeanServer().registerMBean(usageNotificationManagerMBean, usageNotificationManagerMBeanObjectName);
                this.notificationManagers.put(sbbId, usageNotificationManagerMBean);
            }
        } catch (Throwable e) {
            if (mapKey != null && usageMbean != null) {
                this.usageMBeans.remove(mapKey);
                try {
                    sleeContainer.getMBeanServer().unregisterMBean(usageMbean.getObjectName());
                } catch (Throwable f) {
                    logger.error("failed to unregister usage parameter mbean " + usageMbean.getObjectName());
                }
            }
            if (usageNotificationManagerMBean != null) {
                this.notificationManagers.remove(sbbId);
                try {
                    sleeContainer.getMBeanServer().unregisterMBean(usageNotificationManagerMBean.getObjectName());
                } catch (Throwable f) {
                    logger.error("failed to unregister usage notification manager mbean " + usageNotificationManagerMBean.getObjectName());
                }
            }
            throw new ManagementException(e.getMessage(), e);
        } finally {
            currentThread.setContextClassLoader(currentThreadClassLoader);
        }
    }
