    @SuppressWarnings("unchecked")
    private void initializeDAOFactory(Properties daoProps) throws OwsExceptionReport {
        try {
            String daoName = props.getProperty(DAO_FACTORY);
            if (daoName == null) {
                log.fatal("No DAOFactory Implementation is set in the configFile!");
                OwsExceptionReport se = new OwsExceptionReport();
                se.addCodedException(OwsExceptionReport.ExceptionCode.NoApplicableCode, "SosConfigurator.initializeDAOFactory()", "No DAOFactory Implementation is set in the configFile!");
                throw se;
            }
            Class daoFactoryClass = Class.forName(daoName);
            Class[] constrArgs = { Properties.class };
            Object[] args = { daoProps };
            Constructor<IDAOFactory> constructor = daoFactoryClass.getConstructor(constrArgs);
            this.factory = constructor.newInstance(args);
            log.info("\n******\n" + daoName + " loaded successfully!\n******\n");
        } catch (ClassNotFoundException cnfe) {
            log.fatal("Error while loading DAOFactory, required class could not be loaded: " + cnfe.toString());
            throw new OwsExceptionReport(cnfe.getMessage(), cnfe.getCause());
        } catch (SecurityException se) {
            log.fatal("Error while loading DAOFactory: " + se.toString());
            throw new OwsExceptionReport(se.getMessage(), se.getCause());
        } catch (NoSuchMethodException nsme) {
            log.fatal("Error while loading DAOFactory, no required constructor available: " + nsme.toString());
            throw new OwsExceptionReport(nsme.getMessage(), nsme.getCause());
        } catch (IllegalArgumentException iae) {
            log.fatal("Error while loading DAOFactory, parameters for the constructor are illegal: " + iae.toString());
            throw new OwsExceptionReport(iae.getMessage(), iae.getCause());
        } catch (InstantiationException ie) {
            log.fatal("The instatiation of a DAOFactory failed: " + ie.toString());
            throw new OwsExceptionReport(ie.getMessage(), ie.getCause());
        } catch (IllegalAccessException iace) {
            log.fatal("The instatiation of a DAOFactory failed: " + iace.toString());
            throw new OwsExceptionReport(iace.getMessage(), iace.getCause());
        } catch (InvocationTargetException ite) {
            log.fatal("the instatiation of a DAOFactory failed: " + ite.toString() + ite.getLocalizedMessage() + ite.getCause());
            throw new OwsExceptionReport(ite.getMessage(), ite.getCause());
        }
    }
