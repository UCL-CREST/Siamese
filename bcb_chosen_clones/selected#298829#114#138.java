    protected Object getMethodKey(String methodName, Object[] args) {
        StringBuffer key = new StringBuffer(methodName.trim().replace(" ", ".")).append(".");
        for (Object o : args) {
            if (o != null) key.append(o.hashCode());
        }
        LOGGER.info("Generation key ->" + key.toString());
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.reset();
            messageDigest.update(key.toString().getBytes(Charset.forName("UTF8")));
            final byte[] resultByte = messageDigest.digest();
            String hex = null;
            for (int i = 0; i < resultByte.length; i++) {
                hex = Integer.toHexString(0xFF & resultByte[i]);
                if (hex.length() < 2) {
                    key.append("0");
                }
                key.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.severe("No hash generated for method key! " + StackTraceUtil.getStackTrace(e));
        }
        LOGGER.info("Generation key ->" + key.toString());
        return new String(key);
    }
