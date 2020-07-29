    public static String getMD5Hash(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(input.getBytes());
            byte[] result = md5.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                String byteStr = Integer.toHexString(result[i]);
                String swap = null;
                switch(byteStr.length()) {
                    case 1:
                        swap = "0" + Integer.toHexString(result[i]);
                        break;
                    case 2:
                        swap = Integer.toHexString(result[i]);
                        break;
                    case 8:
                        swap = (Integer.toHexString(result[i])).substring(6, 8);
                        break;
                }
                hexString.append(swap);
            }
            return hexString.toString();
        } catch (Exception ex) {
            System.out.println("Fehler beim Ermitteln eines Hashs (" + ex.getMessage() + ")");
        }
        return null;
    }
