    public static Object createConfigSpecificClass(Map aConfig) throws SdlException {
        if (SdlUtil.isNullOrEmpty(aConfig)) {
            logger.error("Error creating manager, configuration does not exist");
            throw new SdlException("Error creating manager, configuration does not exist.");
        }
        String className = (String) aConfig.get("Class");
        if (className == null) {
            logger.error("className is null");
            throw new SdlException("Error creating the config class, no class specified in the config.");
        }
        try {
            Class clazz = Class.forName(className);
            Constructor cons = clazz.getConstructor(new Class[] { Map.class });
            Object obj = cons.newInstance(new Object[] { aConfig });
            return obj;
        } catch (InvocationTargetException ite) {
            logger.error("InvocationTargetException: " + aConfig.keySet().toString() + "\n" + ite);
            SdlException.logError(ite, "Error instantiating " + className);
            throw new SdlException("Error instantiating " + className, ite);
        } catch (Exception e) {
            logger.error("Exception: Error instantiating " + className + ": " + e);
            SdlException.logError(e, "Error instantiating " + className);
            throw new SdlException("Error instantiating " + className, e);
        }
    }
