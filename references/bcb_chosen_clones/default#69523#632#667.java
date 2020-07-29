    public synchronized void startServer() {
        if (serverSocketClass == null) {
            System.err.println("Server mode not supported: need Foundation Profile or higher");
            return;
        }
        if (isServerMode) {
            if (isVerbose) {
                System.err.println("Server already started on port " + port);
            }
            return;
        }
        isServerMode = true;
        if (port == 0) {
            port = DEFAULT_PORT;
            if (isVerbose) {
                System.out.println("port not set.  Using default port: " + port);
            }
        }
        try {
            Class[] argsTypes = new Class[1];
            argsTypes[0] = Integer.TYPE;
            Constructor ct = serverSocketClass.getConstructor(argsTypes);
            Object[] argsList = new Object[1];
            argsList[0] = new Integer(port);
            serverSocket = ct.newInstance(argsList);
            server = new CVMSHServer(this, serverSocketClass, serverSocket, port);
            Thread serverThread = new Thread(server);
            serverThread.start();
        } catch (InvocationTargetException e) {
            System.err.println("ERROR: Cannot start server on port " + port);
            return;
        } catch (Exception e) {
            System.err.println("ERROR: Unexpected exception while " + "creating server: " + e);
            return;
        }
    }
