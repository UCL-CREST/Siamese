    public static void main(String[] args) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String text = "YUMyfj";
        md.update(text.getBytes(), 0, text.length());
        System.out.println(new BigInteger(1, md.digest()).toString(16).toString());
    }
