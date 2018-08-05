    @Override
    public String compute_hash(String plaintext) {
        MessageDigest d;
        try {
            d = MessageDigest.getInstance(get_algorithm_name());
            d.update(plaintext.getBytes());
            byte[] hash = d.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
