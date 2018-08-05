    public StandardDeviceInfo() {
        try {
            Enumeration<NetworkInterface> eni = NetworkInterface.getNetworkInterfaces();
            while (eni.hasMoreElements()) {
                NetworkInterface ni = eni.nextElement();
                if (!ni.isLoopback() && !ni.isPointToPoint() && !ni.isVirtual() && ni.isUp()) {
                    byte[] mac = ni.getHardwareAddress();
                    this.deviceMacAddress = String.format("%02X:%02X:%02X:%02X:%02X:%02X", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
                    Enumeration<InetAddress> eia = ni.getInetAddresses();
                    while (eia.hasMoreElements()) {
                        InetAddress ia = eia.nextElement();
                        System.out.println(ia.toString());
                        if (ia.getAddress().length == 4) {
                            System.out.println(": selected");
                            deviceIpAddress = ia.getHostAddress();
                            url = String.format("rmi://%s/%s", deviceIpAddress, getDeviceName());
                            break;
                        }
                        System.out.println();
                    }
                    break;
                }
            }
            this.deviceUUID = new UUID().toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
