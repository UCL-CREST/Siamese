    @SuppressWarnings("unchecked")
    private void readParams() throws MapEVOConfigurationException {
        logger.trace("Reading parameters in Species");
        String corrEvaluatorParamValue = Config.getMainConfig().getProperty(CORRESPONDENCE_EVALUATOR_PARAMETER_KEY);
        if (corrEvaluatorParamValue == null) {
            final String errMsg = "Configuration parameter \"" + CORRESPONDENCE_EVALUATOR_PARAMETER_KEY + "\" missing.";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        }
        String[] ceParamValues = corrEvaluatorParamValue.split("\\s");
        if (ceParamValues.length != 2) {
            final String errMsg = "Configuration parameter value of \"" + CORRESPONDENCE_EVALUATOR_PARAMETER_KEY + "\" must contain exactly two tokens: classname and ID";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        }
        final String corrEvaluatorClassname = ceParamValues[0];
        final String corrEvaluatorID = ceParamValues[1];
        try {
            Class<Evaluator> globalCorrespondenceEvaluatorClass = (Class<Evaluator>) Class.forName(corrEvaluatorClassname);
            final Class<?>[] paramTypes = { Properties.class, String.class, Alignment.class };
            final Object[] params = { Config.getMainConfig(), corrEvaluatorID, alignment };
            Constructor<Evaluator> constructor = globalCorrespondenceEvaluatorClass.getConstructor(paramTypes);
            corrEvaluator = constructor.newInstance(params);
        } catch (ClassNotFoundException e) {
            final String errMsg = "Cannot find specified correspondence evaluator class: " + corrEvaluatorClassname;
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        } catch (ClassCastException e) {
            final String errMsg = "Correspondence evaluator class must be of type " + corrEvaluator.getClass().getName();
            logger.error(errMsg, e);
            throw new MapEVOConfigurationException(errMsg, e);
        } catch (SecurityException e) {
            final String errMsg = "No access to constructor of the correspondence evaluator class.";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        } catch (NoSuchMethodException e) {
            final String errMsg = "Invalid correspondence evaluator. Required constructor missing.";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        } catch (IllegalArgumentException e) {
            final String errMsg = "Internal error. Constructor arguments and argument types do not match " + "when trying to instantiate correspondence evaluator.";
            logger.fatal(errMsg);
            throw new AssertionError(errMsg);
        } catch (InstantiationException e) {
            final String errMsg = "Problem instantiating correspondence evaluator. Probabily the specified class is abstrct.";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        } catch (IllegalAccessException e) {
            final String errMsg = "Invalid correspondence evaluator. Constructor inaccessible.";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        } catch (InvocationTargetException e) {
            final String errMsg = "Error instantiating correspondence evaluator.";
            logger.error(errMsg, e.getCause());
            throw new MapEVOConfigurationException(errMsg, e.getCause());
        }
        String globalAEParamValue = Config.getMainConfig().getProperty(GLOBAL_ALIGNMENT_EVALUATOR_PARAMETER_KEY);
        if (globalAEParamValue == null) {
            final String errMsg = "Configuration parameter \"" + GLOBAL_ALIGNMENT_EVALUATOR_PARAMETER_KEY + "\" missing.";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        }
        String[] gaeParamValues = globalAEParamValue.split("\\s");
        if (gaeParamValues.length != 2) {
            final String errMsg = "Configuration parameter value of \"" + GLOBAL_ALIGNMENT_EVALUATOR_PARAMETER_KEY + "\" must contain exactly two tokens: classname and ID";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        }
        final String globalAEClassname = gaeParamValues[0];
        final String globalAEID = gaeParamValues[1];
        try {
            Class<Evaluator> globalAlignmentEvaluatorClass = (Class<Evaluator>) Class.forName(globalAEClassname);
            final Class<?>[] paramTypes = { Properties.class, String.class, Alignment.class };
            final Object[] params = { Config.getMainConfig(), globalAEID, alignment };
            Constructor<Evaluator> constructor = globalAlignmentEvaluatorClass.getConstructor(paramTypes);
            alignmentEvaluator = constructor.newInstance(params);
        } catch (ClassNotFoundException e) {
            final String errMsg = "Cannot find specified global alignment evaluator class: " + globalAEClassname;
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        } catch (ClassCastException e) {
            final String errMsg = "Global alignment evaluator class must be of type " + alignmentEvaluator.getClass().getName();
            logger.error(errMsg, e);
            throw new MapEVOConfigurationException(errMsg, e);
        } catch (SecurityException e) {
            final String errMsg = "No access to constructor of the global alignment evaluator class.";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        } catch (NoSuchMethodException e) {
            final String errMsg = "Invalid global alignment evaluator. Required constructor missing.";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        } catch (IllegalArgumentException e) {
            final String errMsg = "Internal error. Constructor arguments and argument types do not match " + "when trying to instantiate global alignment evaluator.";
            logger.fatal(errMsg);
            throw new AssertionError(errMsg);
        } catch (InstantiationException e) {
            final String errMsg = "Problem instantiating global alignment evaluator. Probabily the specified class is abstrct.";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        } catch (IllegalAccessException e) {
            final String errMsg = "Invalid global alignment evaluator. Constructor inaccessible.";
            logger.error(errMsg);
            throw new MapEVOConfigurationException(errMsg);
        } catch (InvocationTargetException e) {
            final String errMsg = "Error instantiating global alignment evaluator.";
            logger.error(errMsg, e.getCause());
            throw new MapEVOConfigurationException(errMsg, e.getCause());
        }
    }
