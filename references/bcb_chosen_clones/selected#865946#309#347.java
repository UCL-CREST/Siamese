    @SuppressWarnings("unchecked")
    public RequestOperator buildRequestOperator() throws OwsExceptionReport {
        RequestOperator ro = new RequestOperator();
        ArrayList<String> listeners = loadListeners();
        Iterator<String> iter = listeners.iterator();
        while (iter.hasNext()) {
            String classname = iter.next();
            try {
                Class listenerClass = Class.forName(classname);
                Class[] constrArgs = {};
                Object[] args = {};
                Constructor<ISosRequestListener> constructor = listenerClass.getConstructor(constrArgs);
                ro.addRequestListener(constructor.newInstance(args));
            } catch (ClassNotFoundException cnfe) {
                log.fatal("Error while loading RequestListeners, required class could not be loaded: " + cnfe.toString());
                throw new OwsExceptionReport(cnfe.getMessage(), cnfe.getCause());
            } catch (SecurityException se) {
                log.fatal("Error while loading RequestListeners");
                throw new OwsExceptionReport(se.getMessage(), se.getCause());
            } catch (NoSuchMethodException nsme) {
                log.fatal("Error while loading RequestListeners," + " no required constructor available: " + nsme.toString());
                throw new OwsExceptionReport(nsme.getMessage(), nsme.getCause());
            } catch (IllegalArgumentException iae) {
                log.fatal("Error while loading RequestListeners, " + "parameters for the constructor are illegal: " + iae.toString());
                throw new OwsExceptionReport(iae.getMessage(), iae.getCause());
            } catch (InstantiationException ie) {
                log.fatal("The instatiation of a RequestListener failed: " + ie.toString());
                throw new OwsExceptionReport(ie.getMessage(), ie.getCause());
            } catch (IllegalAccessException iace) {
                log.fatal("The instatiation of a RequestListener failed: " + iace.toString());
                throw new OwsExceptionReport(iace.getMessage(), iace.getCause());
            } catch (InvocationTargetException ite) {
                log.fatal("The instatiation of a RequestListener failed: " + ite.toString());
                throw new OwsExceptionReport(ite.getMessage(), ite.getCause());
            }
        }
        log.info("\n******\nRequestOperator built successfully!\n******\n");
        return ro;
    }
