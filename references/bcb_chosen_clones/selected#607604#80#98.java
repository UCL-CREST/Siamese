    private byte[] _generate() throws NoSuchAlgorithmException {
        if (host == null) {
            try {
                seed = InetAddress.getLocalHost().toString();
            } catch (UnknownHostException e) {
                seed = "localhost/127.0.0.1";
            } catch (SecurityException e) {
                seed = "localhost/127.0.0.1";
            }
            host = seed;
        } else {
            seed = host;
        }
        seed = seed + new Date().toString();
        seed = seed + Long.toString(rnd.nextLong());
        md = MessageDigest.getInstance(algorithm);
        md.update(seed.getBytes());
        return md.digest();
    }
