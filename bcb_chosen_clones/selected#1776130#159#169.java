    public static DataElement createMD5Sum(int type, String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            byte[] dt = md.digest();
            return new DataElement(type, hexEncode(dt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataElement(type);
    }
