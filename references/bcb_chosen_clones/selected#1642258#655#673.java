    protected static String fileName2md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(input.getBytes("iso-8859-1"));
            byte[] byteHash = md.digest();
            md.reset();
            StringBuffer resultString = new StringBuffer();
            for (int i = 0; i < byteHash.length; i++) {
                resultString.append(Integer.toHexString(0xFF & byteHash[i]));
            }
            return (resultString.toString());
        } catch (Exception ex) {
            Logger.error(ex.getClass() + " " + ex.getMessage());
            for (int i = 0; i < ex.getStackTrace().length; i++) Logger.error("     " + ex.getStackTrace()[i].toString());
            ex.printStackTrace();
        }
        return String.valueOf(Math.random() * Long.MAX_VALUE);
    }
