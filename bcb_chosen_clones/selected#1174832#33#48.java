    public synchronized String encrypt(String password) {
        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes("UTF-8"));
            byte raw[] = md.digest();
            byte[] hash = (new Base64()).encode(raw);
            return new String(hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algorithm SHA-1 is not supported");
            return null;
        } catch (UnsupportedEncodingException e) {
            System.out.println("UTF-8 encoding is not supported");
            return null;
        }
    }
