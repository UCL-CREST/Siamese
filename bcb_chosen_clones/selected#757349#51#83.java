    public static String machineInfo() {
        StringBuilder machineInfo = new StringBuilder();
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
                if ("eth0".equals(networkInterface.getDisplayName())) {
                    for (byte b : networkInterface.getHardwareAddress()) {
                        StringTools.appendWithDelimiter(machineInfo, String.format("%02x", b).toUpperCase(), ":");
                    }
                    machineInfo.append("\n");
                    break;
                }
            }
        } catch (IOException x) {
            System.out.println("LicenseTools.machineInfo: " + x.getMessage());
            x.printStackTrace();
        }
        if (machineInfo.length() == 0) {
            return null;
        }
        String info = machineInfo.toString();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5", "SUN");
            messageDigest.update(info.getBytes());
            byte[] md5 = messageDigest.digest(info.getBytes());
            return new String(Base64.encodeBase64(md5));
        } catch (Exception x) {
            System.out.println("LicenseTools.machineInfo: " + x.getMessage());
            x.printStackTrace();
        }
        return null;
    }
