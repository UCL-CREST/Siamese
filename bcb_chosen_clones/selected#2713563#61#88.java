    public <T extends Stub> T getClient(Class<T> type, String target) throws AxisFault {
        try {
            Constructor<T> c = type.getConstructor(ConfigurationContext.class, String.class);
            String service = type.getSimpleName().replace("Stub", "");
            String sUrl = target + (target.endsWith("/") ? "" : "/") + "services/" + service;
            T stub = c.newInstance(manager.getConfigCtx(), sUrl);
            Logger.getLogger(ClientFactory.class.getName()).log(Level.FINE, "Creating stub for endpoint '" + sUrl + "' with target '" + target + "'.");
            ServiceClient sc = stub._getServiceClient();
            Security security = manager.get(Security.class);
            if (security != null) {
                EndpointStorager eps = manager.get(EndpointStorager.class);
                security.configureService(sc.getAxisService(), eps.getEndpointAlias(target));
            } else {
                Logger.getLogger(ClientFactory.class.getName()).log(Level.WARNING, "Skipping security setting of generated stub due to missing Security implementation in configuration.");
            }
            Options o = new Options();
            o.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
            sc.setOverrideOptions(o);
            sc.engageModule("addressing");
            if (security != null) {
                sc.engageModule("rampart");
            }
            return stub;
        } catch (Exception ex) {
            Logger.getLogger(ClientFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new AxisFault("Could not configure instance of " + type.getName() + " to communicate with: " + target);
        }
    }
