    public String getMACAddress() {
        try {
            Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
            String ret = new String();
            while (ni.hasMoreElements()) {
                NetworkInterface intf = ni.nextElement();
                if (!intf.isLoopback() && !intf.isVirtual() && intf.isUp()) {
                    byte[] mac = intf.getHardwareAddress();
                    if (mac == null) continue;
                    ret = new String();
                    for (int i = 0; i < mac.length; i++) {
                        ret += String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
                    }
                    if (verbose) Logger.log("Network", "MAC detected: " + ret);
                }
            }
            return ret;
        } catch (SocketException e) {
            Logger.log("Network", "Couldn't determine MAC address");
            return null;
        }
    }
