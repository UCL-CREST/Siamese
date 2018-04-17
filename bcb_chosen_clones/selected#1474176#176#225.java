    private Socket createSocket(String host, int port, String socketFactory, int connectTimeout) throws Exception {
        Socket socket = null;
        if (socketFactory != null) {
            Class socketFactoryClass = Obj.helper.loadClass(socketFactory);
            Method getDefault = socketFactoryClass.getMethod("getDefault", new Class[] {});
            Object factory = getDefault.invoke(null, new Object[] {});
            Method createSocket = null;
            if (connectTimeout > 0) {
                try {
                    createSocket = socketFactoryClass.getMethod("createSocket", new Class[] {});
                    Method connect = Socket.class.getMethod("connect", new Class[] { Class.forName("java.net.SocketAddress"), int.class });
                    Object endpoint = createInetSocketAddress(host, port);
                    socket = (Socket) createSocket.invoke(factory, new Object[] {});
                    if (debug) {
                        System.err.println("Connection: creating socket with " + "a timeout using supplied socket factory");
                    }
                    connect.invoke(socket, new Object[] { endpoint, new Integer(connectTimeout) });
                } catch (NoSuchMethodException e) {
                }
            }
            if (socket == null) {
                createSocket = socketFactoryClass.getMethod("createSocket", new Class[] { String.class, int.class });
                if (debug) {
                    System.err.println("Connection: creating socket using " + "supplied socket factory");
                }
                socket = (Socket) createSocket.invoke(factory, new Object[] { host, new Integer(port) });
            }
        } else {
            if (connectTimeout > 0) {
                try {
                    Constructor socketCons = Socket.class.getConstructor(new Class[] {});
                    Method connect = Socket.class.getMethod("connect", new Class[] { Class.forName("java.net.SocketAddress"), int.class });
                    Object endpoint = createInetSocketAddress(host, port);
                    socket = (Socket) socketCons.newInstance(new Object[] {});
                    if (debug) {
                        System.err.println("Connection: creating socket with " + "a timeout");
                    }
                    connect.invoke(socket, new Object[] { endpoint, new Integer(connectTimeout) });
                } catch (NoSuchMethodException e) {
                }
            }
            if (socket == null) {
                if (debug) {
                    System.err.println("Connection: creating socket");
                }
                socket = new Socket(host, port);
            }
        }
        return socket;
    }
