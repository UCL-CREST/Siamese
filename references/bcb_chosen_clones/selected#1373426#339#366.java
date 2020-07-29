    public static String getHostSystemMacAddress() {
        String macAddress = null;
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            String ipAddress = thisIp.getHostAddress();
            System.out.println("-- Ip address --:" + ipAddress);
            InetAddress address = InetAddress.getByName(ipAddress);
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            StringBuffer sb = new StringBuffer();
            if (ni != null) {
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    System.out.println("--mac addr--:" + sb);
                } else {
                    System.out.println("Address doesn't exist or is not accessible.");
                }
            } else {
                System.out.println("Network Interface for the specified address is not found.");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }
