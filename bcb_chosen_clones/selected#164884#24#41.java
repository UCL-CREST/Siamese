    public void initIOC(Class pluginClass, String pluginConfig) {
        try {
            MonitorContext mContext = new MonitorContext(new MonitorConfiguration(new PropertiesConfiguration("./conf/monitor.properties")), _pluginTimer);
            HostContext hContext = new HostContext(mContext, new PropertiesConfiguration("./conf/hosts/ifxsrv.depot120.dpd.de.properties"));
            Constructor constructor = pluginClass.getConstructor(IHostContext.class, String.class);
            _plugin = (IPlugin) constructor.newInstance(hContext, pluginConfig);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
