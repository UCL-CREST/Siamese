    public static String digest(String pBase, String pCharSet) {
        String wdgs = null;
        try {
            MessageDigest wmd = MessageDigest.getInstance("MD5");
            wmd.reset();
            wmd.update(pBase.getBytes(pCharSet));
            byte[] wdg = wmd.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < wdg.length; i++) {
                String w_dup = Integer.toHexString(0xFF & wdg[i]);
                if (w_dup.length() < 2) {
                    w_dup = "0" + w_dup;
                }
                hexString.append(w_dup);
            }
            wdgs = hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } finally {
            return wdgs;
        }
    }
