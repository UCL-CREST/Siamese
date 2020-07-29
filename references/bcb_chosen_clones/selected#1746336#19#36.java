    public static String SHAEncrypt(String originalString) {
        String encryptedString = new String("");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(originalString.getBytes());
            byte b[] = md.digest();
            for (int i = 0; i < b.length; i++) {
                char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
                char[] ob = new char[2];
                ob[0] = digit[(b[i] >>> 4) & 0X0F];
                ob[1] = digit[b[i] & 0X0F];
                encryptedString += new String(ob);
            }
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("the algorithm doesn't exist");
        }
        return encryptedString;
    }
