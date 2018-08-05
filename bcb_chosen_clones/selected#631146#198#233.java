    protected static IBpelAdmin createRemoteDebugImpl(Map aMap) throws SdlException {
        logger.debug("createRemoteDebugImpl(Map)");
        try {
            Class c;
            String debugClassName;
            String eventLocatorClass;
            String bpLocatorClass;
            String defaultRDebugClass = ServiceRemoteDebugImpl.class.getName();
            String defaultEventLocatorClass = "org.dbe.composer.wfengine.bpel.webserver.rdebug.client.ServiceEventHandlerLocator";
            String defaultBpLocatorClass = "org.dbe.composer.wfengine.bpel.webserver.rdebug.client.ServiceBreakpointHandlerLocator";
            if (aMap == null || aMap.isEmpty()) {
                debugClassName = defaultRDebugClass;
                eventLocatorClass = defaultEventLocatorClass;
                bpLocatorClass = defaultBpLocatorClass;
            } else {
                debugClassName = (String) aMap.get(ISdlEngineConfiguration.REMOTE_DEBUG_IMPL_ENTRY);
                if (SdlUtil.isNullOrEmpty(debugClassName)) {
                    debugClassName = defaultRDebugClass;
                }
                eventLocatorClass = (String) aMap.get(ISdlEngineConfiguration.EVENT_HANDLER_LOCATOR_ENTRY);
                if (SdlUtil.isNullOrEmpty(eventLocatorClass)) {
                    eventLocatorClass = defaultEventLocatorClass;
                }
                bpLocatorClass = (String) aMap.get(ISdlEngineConfiguration.BREAKPOINT_HANDLER_LOCATOR_ENTRY);
                if (SdlUtil.isNullOrEmpty(bpLocatorClass)) {
                    bpLocatorClass = defaultBpLocatorClass;
                }
            }
            c = Class.forName(debugClassName);
            Constructor constructor = c.getConstructor(new Class[] { String.class, String.class });
            return (IBpelAdmin) constructor.newInstance(new Object[] { eventLocatorClass, bpLocatorClass });
        } catch (Exception e) {
            logger.error("Error: " + e);
            throw new SdlException("Error creating remote debug engine implementation.", e);
        }
    }
