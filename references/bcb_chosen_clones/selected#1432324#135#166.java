    private IdleService getService(String serviceName) {
        if (serviceName == null || serviceName.length() == 0) {
            IdleServiceServlet.log_.debug(IdleServiceServlet.messages_.getString("idleServiceServlet.log.debug.serviceNameNull"));
            serviceName = null;
        }
        IdleService idleService = idleServices.get(serviceName);
        if (idleService == null) {
            IdleServiceServlet.log_.debug(IdleServiceServlet.messages_.getString("idleServiceServlet.log.debug.serviceObjectNotFound"));
            try {
                IdleService s = (IdleService) ((idleServiceClasses.get(serviceName)).getConstructor().newInstance());
                IdleServiceServlet.log_.debug(IdleServiceServlet.messages_.getString("idleServiceServlet.log.debug.serviceInstantiated"));
                s.init(idleServiceInitParams.get(serviceName));
                IdleServiceServlet.log_.debug(IdleServiceServlet.messages_.getString("idleServiceServlet.log.debug.serviceInitialized"));
                idleServices.put(serviceName, s);
                idleService = s;
            } catch (NoSuchMethodException e) {
                IdleServiceServlet.log_.error(IdleServiceServlet.messages_.getString("idleServiceServlet.log.error.serviceInstantiation"), e);
            } catch (InstantiationException e) {
                IdleServiceServlet.log_.error(IdleServiceServlet.messages_.getString("idleServiceServlet.log.error.serviceInstantiation"), e);
            } catch (IllegalAccessException e) {
                IdleServiceServlet.log_.error(IdleServiceServlet.messages_.getString("idleServiceServlet.log.error.serviceInstantiation"), e);
            } catch (InvocationTargetException e) {
                IdleServiceServlet.log_.error(IdleServiceServlet.messages_.getString("idleServiceServlet.log.error.serviceInstantiation"), e);
            }
        }
        if (idleService == null) {
            IdleServiceServlet.log_.info(IdleServiceServlet.messages_.getString("idleServiceServlet.log.info.defaultService"));
            idleService = idleServices.get(null);
        }
        IdleServiceServlet.log_.debug(IdleServiceServlet.messages_.getString("idleServiceServlet.log.debug.serviceReturned") + idleService.getIdleServiceName());
        return idleService;
    }
