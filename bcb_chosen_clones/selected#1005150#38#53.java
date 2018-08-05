    private static String processStr(String srcStr, String sign) throws NoSuchAlgorithmException, NullPointerException {
        if (null == srcStr) {
            throw new java.lang.NullPointerException("��Ҫ���ܵ��ַ�ΪNull");
        }
        MessageDigest digest;
        String algorithm = "MD5";
        String result = "";
        digest = MessageDigest.getInstance(algorithm);
        digest.update(srcStr.getBytes());
        byte[] byteRes = digest.digest();
        int length = byteRes.length;
        for (int i = 0; i < length; i++) {
            result = result + byteHEX(byteRes[i], sign);
        }
        return result;
    }
