    public synchronized void serverStopped() {
        if (isServerMode) {
            try {
                Class[] argsTypes = new Class[0];
                Method closeMtd;
                closeMtd = serverSocketClass.getMethod("close", argsTypes);
                Object[] voidArgs = new Object[0];
                closeMtd.invoke(serverSocket, voidArgs);
            } catch (InvocationTargetException ioe) {
                System.err.println("ERROR: IO error while closing " + "server socket");
            } catch (Exception e) {
                System.err.println("ERROR: Unexpected exception while " + "closing server socket: " + e);
                return;
            }
            serverSocket = null;
            server = null;
            isServerMode = false;
        }
    }
