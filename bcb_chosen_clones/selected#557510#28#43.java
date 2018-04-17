    public static String createNormalizedJarDescriptorDigest(String path) throws Exception {
        String descriptor = createNormalizedDescriptor(new JarFile2(path));
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(descriptor.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
