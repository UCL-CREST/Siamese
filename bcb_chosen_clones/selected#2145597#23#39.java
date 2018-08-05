    public static IEscalation instantiateEscalation(ClassLoader classLoader, IPluginContext pluginContext, Configuration escalationConfiguration) {
        try {
            Class clasz = classLoader.loadClass(escalationConfiguration.getString("class"));
            Constructor constructor = clasz.getConstructor(IPluginContext.class, Configuration.class);
            return (IEscalation) constructor.newInstance(pluginContext, escalationConfiguration);
        } catch (ClassNotFoundException e) {
            throw new EscalationNotFoundException(e);
        } catch (IllegalAccessException e) {
            throw new EscalationNotFoundException(e);
        } catch (InstantiationException e) {
            throw new EscalationNotFoundException(e);
        } catch (NoSuchMethodException e) {
            throw new EscalationNotFoundException(e);
        } catch (InvocationTargetException e) {
            throw new EscalationNotFoundException(e);
        }
    }
