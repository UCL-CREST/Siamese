    public RemoteDomain(SCNode parentNode, ServiceAdapterIfc adapter, OscURI oscUri) throws RemoteException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {
        this.parentNode = parentNode;
        this.adapter = new ServiceAdapter();
        this.adapter.cl = adapter.getClassLoader();
        ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.adapter.cl);
        this.adapter.adapterClass = adapter.getAdapterClass();
        this.adapter.ifc = adapter.getIfc();
        this.adapter.remoteClient = adapter.getRemoteClient();
        this.adapter.remoteIfc = adapter.getRemoteIfc();
        this.adapter.remoteStub = adapter.getRemoteStub();
        this.adapter.stub = adapter.getStub();
        this.adapter.uri = adapter.getUri();
        this.adapter.localDomainRemoteIfc = adapter.getLocalDomainRemoteIfc();
        service = this.adapter.remoteClient.getConstructors()[0].newInstance(this.adapter.stub);
        Thread.currentThread().setContextClassLoader(oldcl);
        this.oscUri = oscUri;
        this.osnUri = oscUri.toOsnURI();
        LogInterface l = parentNode.getRootDomain().getLoggerService();
        if (l != null) log = l.getLogger(this, ""); else log = Application.getLogger(this.oscUri.getURI());
    }
