    public static String getStringHash(String fileName) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.reset();
            digest.update(fileName.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) builder.append(Integer.toHexString(0xFF & messageDigest[i]));
            String result = builder.toString();
            return result;
        } catch (NoSuchAlgorithmException ex) {
            return fileName;
        }
    }
