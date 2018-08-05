    @SuppressWarnings("unchecked")
    private void initializeHttpPostRequestDecoder(Properties sosProps) throws OwsExceptionReport {
        String className = sosProps.getProperty(POSTREQUESTDECODER);
        try {
            if (className == null) {
                log.fatal("No postRequestDecoder Implementation is set in the configFile!");
                OwsExceptionReport se = new OwsExceptionReport();
                se.addCodedException(OwsExceptionReport.ExceptionCode.NoApplicableCode, "SosConfigurator.initializeHttpPostRequestDecoder()", "No postRequestDecoder Implementation is set in the configFile!");
                throw se;
            }
            Class httpPostRequestDecoderClass = Class.forName(className);
            Constructor<IHttpPostRequestDecoder> constructor = httpPostRequestDecoderClass.getConstructor();
            this.httpPostDecoder = constructor.newInstance();
            log.info("\n******\n" + className + " loaded successfully!\n******\n");
        } catch (ClassNotFoundException cnfe) {
            log.fatal("Error while loading postRequestDecoder, required class could not be loaded: " + cnfe.toString());
            throw new OwsExceptionReport(cnfe.getMessage(), cnfe.getCause());
        } catch (SecurityException se) {
            log.fatal("Error while loading postRequestDecoder: " + se.toString());
            throw new OwsExceptionReport(se.getMessage(), se.getCause());
        } catch (NoSuchMethodException nsme) {
            log.fatal("Error while loading postRequestDecoder, no required constructor available: " + nsme.toString());
            throw new OwsExceptionReport(nsme.getMessage(), nsme.getCause());
        } catch (IllegalArgumentException iae) {
            log.fatal("Error while loading postRequestDecoder, parameters for the constructor are illegal: " + iae.toString());
            throw new OwsExceptionReport(iae.getMessage(), iae.getCause());
        } catch (InstantiationException ie) {
            log.fatal("The instatiation of a postRequestDecoder failed: " + ie.toString());
            throw new OwsExceptionReport(ie.getMessage(), ie.getCause());
        } catch (IllegalAccessException iace) {
            log.fatal("The instatiation of an postRequestDecoder failed: " + iace.toString());
            throw new OwsExceptionReport(iace.getMessage(), iace.getCause());
        } catch (InvocationTargetException ite) {
            log.fatal("the instatiation of an postRequestDecoder failed: " + ite.toString() + ite.getLocalizedMessage() + ite.getCause());
            throw new OwsExceptionReport(ite.getMessage(), ite.getCause());
        }
    }
