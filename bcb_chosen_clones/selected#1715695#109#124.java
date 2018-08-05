    private String getHardwareAddresses() {
        StringBuilder bldr = new StringBuilder();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface intr = interfaces.nextElement();
                bldr.append(intr.getDisplayName());
                bldr.append(" ");
                bldr.append(toHexString(intr.getHardwareAddress()));
                bldr.append(", ");
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return rmLastTwo(bldr.toString());
    }
