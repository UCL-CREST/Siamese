    public static String generateUUID() {
        String UUID = "0d";
        byte[] systemID = null;
        try {
            NetworkInterface netInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            systemID = netInterface.getHardwareAddress();
        } catch (SocketException se) {
            se.printStackTrace();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        } catch (NullPointerException ne) {
            System.err.println("~OS does not support MAC address~");
            try {
                InetAddress inet = InetAddress.getLocalHost();
                String hostName = inet.getHostName();
                systemID = hostName.getBytes();
            } catch (UnknownHostException uhe) {
                uhe.printStackTrace();
            } catch (SecurityException se) {
                se.printStackTrace();
                System.err.println("~Retrieving host name forbidden by security manager~");
            }
        } finally {
            BigInteger macInt = new BigInteger(systemID);
            String macHex = macInt.toString(16);
            int macHash = macHex.hashCode();
            String macHashHex = Integer.toHexString(macHash);
            if (macHashHex.length() % 2 != 0) macHashHex = "0" + macHashHex;
            UUID = (UUID + "-" + macHashHex);
        }
        Long unixTime = System.currentTimeMillis() / 1000L;
        String unixHex = Long.toHexString(unixTime);
        if (unixHex.length() % 2 != 0) unixHex = "0" + unixHex;
        UUID = (UUID + "-" + unixHex);
        return UUID;
    }
