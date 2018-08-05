    public static String getUniqueKey() {
        String digest = "";
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final String timeVal = "" + (System.currentTimeMillis() + 1);
            String localHost = "";
            try {
                localHost = InetAddress.getLocalHost().toString();
            } catch (UnknownHostException e) {
                println("Warn: getUniqueKey(), Error trying to get localhost" + e.getMessage());
            }
            final String randVal = "" + new Random().nextInt();
            final String val = timeVal + localHost + randVal;
            md.reset();
            md.update(val.getBytes());
            digest = toHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            println("Warn: getUniqueKey() " + e);
        }
        return digest;
    }
