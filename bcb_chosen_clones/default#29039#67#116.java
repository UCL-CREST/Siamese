    public void run() {
        ProxyClient pc;
        Socket acceptedSock = null;
        _running = true;
        boolean done = false;
        while (!done) {
            try {
                if (_serverSock != null) {
                    acceptedSock = _serverSock.accept();
                }
            } catch (IOException e) {
                if (!_halted) {
                    ErrorManagement.handleException("Exception raised during server accept.", e);
                }
            }
            try {
                synchronized (this) {
                    while (_halted) {
                        acceptedSock = null;
                        wait();
                    }
                }
            } catch (InterruptedException e) {
                done = true;
            }
            if (acceptedSock != null) {
                Class[] subProxyParamClasses;
                Object[] subProxyParamObjects;
                if (_objectToPass == null) {
                    Class[] paramClasses = { acceptedSock.getClass() };
                    Object[] paramObjects = { acceptedSock };
                    subProxyParamClasses = paramClasses;
                    subProxyParamObjects = paramObjects;
                } else {
                    Class[] paramClasses = { acceptedSock.getClass(), _objectToPass.getClass() };
                    Object[] paramObjects = { acceptedSock, _objectToPass };
                    subProxyParamClasses = paramClasses;
                    subProxyParamObjects = paramObjects;
                }
                try {
                    Constructor maker;
                    maker = _subProxy.getConstructor(subProxyParamClasses);
                    pc = (ProxyClient) maker.newInstance(subProxyParamObjects);
                    pc.start();
                } catch (Exception e) {
                    ErrorManagement.handleException("Serious failure trying to create a ProxyClient object.", e);
                }
            }
        }
    }
