    @Override
    public Set<AbstractGestureDevice<?, ?>> discover() {
        LOGGER.log(Level.INFO, "Android Device discovery started!");
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ipAddr = addr.getHostAddress();
            LOGGER.log(Level.INFO, "Connect to: " + ipAddr.toString());
            LOGGER.log(Level.INFO, "Starting Android Gesture Server on port 80");
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(80);
            } catch (IOException e) {
                System.out.println("Could not listen on port: 80");
                System.exit(1);
            }
            LOGGER.log(Level.INFO, "Socket created.");
            LOGGER.log(Level.INFO, "Listening to socket on port 80");
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                @SuppressWarnings("rawtypes") Constructor ctor;
                try {
                    ctor = AndroidReader3D.class.getConstructor(Socket.class, BufferedReader.class);
                    try {
                        BufferedReader in = null;
                        String name = "";
                        try {
                            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            name = in.readLine();
                        } catch (IOException e1) {
                            System.out.println("Error:" + e1);
                        }
                        AbstractGestureDevice<?, ?> device = (AbstractGestureDevice<?, ?>) ctor.newInstance(clientSocket, in);
                        String[] temp = name.split(":");
                        device.setName(temp[0]);
                        device.setDeviceType(Constant.TYPE_3D);
                        device.setConnectionType(Constant.CONNECTION_IP);
                        device.setIsConnected(true);
                        device.setDeviceID(temp[1]);
                        devices.add(device);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(1);
            }
        } catch (UnknownHostException e) {
        }
        System.out.println("return devices:" + devices);
        return devices;
    }
