    public static String getDigest(String user, String realm, String password, String method, String uri, String nonce, String nc, String cnonce, String qop) {
        String digest1 = user + ":" + realm + ":" + password;
        String digest2 = method + ":" + uri;
        try {
            MessageDigest digestOne = MessageDigest.getInstance("md5");
            digestOne.update(digest1.getBytes());
            String hexDigestOne = getHexString(digestOne.digest());
            MessageDigest digestTwo = MessageDigest.getInstance("md5");
            digestTwo.update(digest2.getBytes());
            String hexDigestTwo = getHexString(digestTwo.digest());
            String digest3 = hexDigestOne + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + hexDigestTwo;
            MessageDigest digestThree = MessageDigest.getInstance("md5");
            digestThree.update(digest3.getBytes());
            String hexDigestThree = getHexString(digestThree.digest());
            return hexDigestThree;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
