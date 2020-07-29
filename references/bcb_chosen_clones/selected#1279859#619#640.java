    public static String getMessageDigest(String[] inputs) {
        if (inputs.length == 0) return null;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            for (String input : inputs) sha.update(input.getBytes());
            byte[] hash = sha.digest();
            String CPass = "";
            int h = 0;
            String s = "";
            for (int i = 0; i < 20; i++) {
                h = hash[i];
                if (h < 0) h += 256;
                s = Integer.toHexString(h);
                if (s.length() < 2) CPass = CPass.concat("0");
                CPass = CPass.concat(s);
            }
            CPass = CPass.toUpperCase();
            return CPass;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
