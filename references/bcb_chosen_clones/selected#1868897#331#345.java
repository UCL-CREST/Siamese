    public static Object deployNewService(String scNodeRmiName, String userName, String password, String name, String jarName, String serviceClass, String serviceInterface, Logger log) throws RemoteException, MalformedURLException, StartServiceException, NotBoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, SessionException {
        try {
            SCNodeInterface node = (SCNodeInterface) Naming.lookup(scNodeRmiName);
            String session = node.login(userName, password);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(new FileInputStream(jarName), baos);
            ServiceAdapterIfc adapter = node.deploy(session, name, baos.toByteArray(), jarName, serviceClass, serviceInterface);
            if (adapter != null) {
                return new ExternalDomain(node, adapter, adapter.getUri(), log).getProxy(Thread.currentThread().getContextClassLoader());
            }
        } catch (Exception e) {
            log.warn("Could not send deploy command: " + e.getMessage(), e);
        }
        return null;
    }
