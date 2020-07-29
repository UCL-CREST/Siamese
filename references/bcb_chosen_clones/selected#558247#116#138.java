    private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        String serverClassName = "wheel.EngineImpl";
        if (isMultipartRequest(request)) serverClassName = "wheel.MultipartEngineImpl";
        IResourceManager resourceManager = null;
        if (developmentMode) resourceManager = new DevelopmentResourceManager(context); else resourceManager = new DefaultResourceManager(context);
        try {
            Class serverClass = resourceManager.loadClass(serverClassName);
            Constructor cons = serverClass.getConstructor(HttpServletRequest.class, HttpServletResponse.class, ServletContext.class);
            Object serverObject = cons.newInstance(request, response, context);
            Method processRequestMethod = serverClass.getMethod("processRequest");
            processRequestMethod.invoke(serverObject);
        } catch (ClassNotFoundException e) {
            log.error("Could not load wheel.EngineImpl.", e);
        } catch (NoSuchMethodException e) {
            log.error("Could not execute processRequest.", e);
        } catch (IllegalAccessException e) {
            log.error("Could not access wheel.Engine1Impl", e);
        } catch (InvocationTargetException e) {
            log.error("Error while processing request.", e);
        } catch (InstantiationException e) {
            log.error("Could not instantiate wheel.EngineImpl.", e);
        }
    }
