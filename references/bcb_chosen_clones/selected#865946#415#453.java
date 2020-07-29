    @SuppressWarnings("unchecked")
    private void initializeOMEncoder(Properties sosProps) throws OwsExceptionReport {
        String className = sosProps.getProperty(OMENCODER);
        try {
            if (className == null) {
                log.fatal("No OMEncoder Implementation is set in the configFile!");
                OwsExceptionReport se = new OwsExceptionReport();
                se.addCodedException(OwsExceptionReport.ExceptionCode.NoApplicableCode, "SosConfigurator.initializeOMEncoder()", "No OMEncoder Implementation is set in the configFile!");
                throw se;
            }
            Class omEncoderClass = Class.forName(className);
            Class[] constrArgs = {};
            Object[] args = {};
            Constructor<IOMEncoder> constructor = omEncoderClass.getConstructor();
            this.omEncoder = constructor.newInstance();
            log.info("\n******\n" + className + " loaded successfully!\n******\n");
        } catch (ClassNotFoundException cnfe) {
            log.fatal("Error while loading OMEncoder, required class could not be loaded: " + cnfe.toString());
            throw new OwsExceptionReport(cnfe.getMessage(), cnfe.getCause());
        } catch (SecurityException se) {
            log.fatal("Error while loading OMEncoder: " + se.toString());
            throw new OwsExceptionReport(se.getMessage(), se.getCause());
        } catch (NoSuchMethodException nsme) {
            log.fatal("Error while loading OMEncoder, no required constructor available: " + nsme.toString());
            throw new OwsExceptionReport(nsme.getMessage(), nsme.getCause());
        } catch (IllegalArgumentException iae) {
            log.fatal("Error while loading OMEncoder, parameters for the constructor are illegal: " + iae.toString());
            throw new OwsExceptionReport(iae.getMessage(), iae.getCause());
        } catch (InstantiationException ie) {
            log.fatal("The instatiation of a OMEncoder failed: " + ie.toString());
            throw new OwsExceptionReport(ie.getMessage(), ie.getCause());
        } catch (IllegalAccessException iace) {
            log.fatal("The instatiation of an OMEncoder failed: " + iace.toString());
            throw new OwsExceptionReport(iace.getMessage(), iace.getCause());
        } catch (InvocationTargetException ite) {
            log.fatal("the instatiation of an OMEncoder failed: " + ite.toString() + ite.getLocalizedMessage() + ite.getCause());
            throw new OwsExceptionReport(ite.getMessage(), ite.getCause());
        }
    }
