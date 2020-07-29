    private static void putMacAddress(NetworkInterface netint) throws SocketException {
        byte[] mac = netint.getHardwareAddress();
        String formattata = "";
        if (mac != null) {
            for (int i = 0; i < mac.length; i++) {
                formattata = formattata.concat(String.format("%02x%s", mac[i], (i < mac.length - 1) ? ":" : ""));
            }
            macs.add(formattata);
        }
    }
