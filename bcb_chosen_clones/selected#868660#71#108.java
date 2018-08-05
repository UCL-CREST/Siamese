    public void init() {
        Logger.addLogListener(this);
        ServletContext context = getServletContext();
        Configuration.setServletContext(context);
        Enumeration initParamNames = context.getInitParameterNames();
        String name, value;
        while (initParamNames.hasMoreElements()) {
            name = (String) initParamNames.nextElement();
            value = context.getInitParameter(name);
            Configuration.addConfigValue(name, value);
        }
        Logger.setLogLevel(Configuration.getInt("debugLevel"));
        String listenerList;
        if ((listenerList = Configuration.getString("logListeners")) != null) {
            String[] listeners = listenerList.split(",");
            for (String listener : listeners) {
                try {
                    Class c = Class.forName("edu.uga.galileo.slash.logging." + listener.trim());
                    Constructor constructor = c.getConstructor((Class[]) null);
                    Logger.addLogListener((LogListener) constructor.newInstance((Object[]) null));
                } catch (Exception e) {
                    Logger.error("Couldn't instantiate LogListener '" + listener + "'", e);
                }
            }
        }
        try {
            institutionLookupService = InstitutionLookupService.getInstance();
            Logger.info("InstitutionLookupService INITIALIZED");
            reverseDNSLookupService = ReverseDNSLookupService.getInstance();
            Logger.info("ReverseDNSLookupService INITIALIZED");
            geolocationLookupService = GeolocationLookupService.getInstance();
            Logger.info("GeolocationLookupService INITIALIZED");
            parser = StatisticsParser.getInstance();
        } catch (IOException e) {
            Logger.fatal("Couldn't get institution lookup service.", e);
        }
        Logger.info("Controller servlet INITIALIZED");
    }
