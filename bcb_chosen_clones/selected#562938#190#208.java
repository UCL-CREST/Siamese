    public final InputInterpreter loadFeedbackInterpreter(Properties classes, IdealizeClassLoader loader, String separatorForNetwork) throws IdealizeConfigurationException {
        InputInterpreter feedbackInterpreter;
        try {
            feedbackInterpreter = (InputInterpreter) loader.loadClass(classes.getProperty(this.KEY_FEEDBACK_INTERPRETER)).getConstructors()[0].newInstance(separatorForNetwork);
        } catch (IllegalArgumentException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadFeedbackInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_FEEDBACK_INTERPRETER), e);
        } catch (SecurityException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadFeedbackInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_FEEDBACK_INTERPRETER), e);
        } catch (InstantiationException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadFeedbackInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_FEEDBACK_INTERPRETER), e);
        } catch (IllegalAccessException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadFeedbackInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_FEEDBACK_INTERPRETER), e);
        } catch (InvocationTargetException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadFeedbackInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_FEEDBACK_INTERPRETER), e);
        } catch (ClassNotFoundException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadFeedbackInterpreter: Could not load input interpreter: " + classes.getProperty(this.KEY_FEEDBACK_INTERPRETER), e);
        }
        return feedbackInterpreter;
    }
