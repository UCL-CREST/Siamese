    public static String GetMAC() {
        try {
            StringBuilder sb = new StringBuilder("");
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                if (ni != null) {
                    byte[] mac = ni.getHardwareAddress();
                    if (mac != null && mac.length >= 6) {
                        for (int i = 0; i < mac.length; i++) {
                            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                        }
                        break;
                    }
                }
            }
            return sb.toString();
        } catch (SocketException ex) {
            return EmptyString;
        }
    }
