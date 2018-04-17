    public final InputInterpreter loadInputInterpreter(Properties classes, IdealizeClassLoader loader, String separatorForNetwork) throws IdealizeConfigurationException {
        InputInterpreter interpreter = null;
        try {
            interpreter = (InputInterpreter) loader.loadClass(classes.getProperty(this.KEY_INTERPRETER)).getConstructors()[0].newInstance(separatorForNetwork);
        } catch (IllegalArgumentException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadInputInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_INTERPRETER), e);
        } catch (SecurityException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadInputInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_INTERPRETER), e);
        } catch (InstantiationException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadInputInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_INTERPRETER), e);
        } catch (IllegalAccessException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadInputInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_INTERPRETER), e);
        } catch (InvocationTargetException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadInputInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_INTERPRETER), e);
        } catch (ClassNotFoundException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadInputInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_INTERPRETER), e);
        }
        return interpreter;
    }
