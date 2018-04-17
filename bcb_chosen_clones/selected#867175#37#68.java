    public static IPlugin instantiatePlugin(ClassLoader classLoader, IHostContext hostContext, String pluginConfiguration) {
        try {
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(pluginConfiguration);
            properties.load(fis);
            Class pluginClass = classLoader.loadClass((String) properties.get("class"));
            fis.close();
            Constructor pluginConstructor = pluginClass.getConstructor(IHostContext.class, String.class);
            return (IPlugin) pluginConstructor.newInstance(hostContext, pluginConfiguration);
        } catch (ClassNotFoundException e) {
            if (_logger.isErrorEnabled()) _logger.error(e.getLocalizedMessage(), e);
            return null;
        } catch (NoSuchMethodException e) {
            if (_logger.isErrorEnabled()) _logger.error(e.getLocalizedMessage(), e);
            return null;
        } catch (IllegalAccessException e) {
            if (_logger.isErrorEnabled()) _logger.error(e.getLocalizedMessage(), e);
            return null;
        } catch (InvocationTargetException e) {
            if (_logger.isErrorEnabled()) _logger.error(e.getLocalizedMessage(), e);
            return null;
        } catch (InstantiationException e) {
            if (_logger.isErrorEnabled()) _logger.error(e.getLocalizedMessage(), e);
            return null;
        } catch (FileNotFoundException e) {
            if (_logger.isErrorEnabled()) _logger.error(e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            if (_logger.isErrorEnabled()) _logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
