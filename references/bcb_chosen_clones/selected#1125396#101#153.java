    private InetAddress selectNetworkConnection() {
        InetAddress loopback;
        try {
            loopback = InetAddress.getByName("127.0.0.1");
        } catch (Exception e) {
            loopback = null;
        }
        String desiredConnection = ServerConfig.getInstance().getStringPref(ServerConfig.NETWORK_CONNECTION);
        if (desiredConnection.endsWith("127.0.0.1")) return loopback;
        if (desiredConnection.startsWith("Any ")) return null;
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
            InetAddress[] networks = InetAddress.getAllByName(localhost.getHostName());
            for (InetAddress network : networks) {
                if (desiredConnection.endsWith(network.getHostAddress())) return network;
            }
            for (InetAddress network : networks) {
                String name = null;
                try {
                    NetworkInterface netAdapter = NetworkInterface.getByInetAddress(network);
                    if (netAdapter != null) {
                        name = netAdapter.getDisplayName();
                        if (name == null) {
                            byte[] mac = netAdapter.getHardwareAddress();
                            if (mac != null) {
                                StringBuilder macAddress = new StringBuilder(21);
                                macAddress.append("MAC: ");
                                for (int b : mac) {
                                    if (b < 0) b = 255 + b;
                                    if (b < 16) macAddress.append("0");
                                    macAddress.append(Integer.toHexString(b).toUpperCase());
                                    macAddress.append("-");
                                }
                                macAddress.deleteCharAt(macAddress.length() - 1);
                                name = macAddress.toString();
                            }
                        }
                        if ((name != null) && desiredConnection.startsWith(name)) return network;
                    }
                } catch (SocketException e) {
                }
            }
        } catch (UnknownHostException e) {
            this.io.error("No network card supporting TCP/IP could be detected.  MighTyD requires TCP/IP networking capabilities.");
            return null;
        } catch (SecurityException e) {
            return localhost;
        }
        this.io.error("The configured network connection could not be found.");
        this.error = true;
        return null;
    }
