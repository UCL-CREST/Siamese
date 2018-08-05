    public static void main(String[] argv) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        md.update("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes(), 0, 56);
        String exp = "84983E441C3BD26EBAAE4AA1F95129E5E54670F1";
        String result = toString(md.digest());
        System.out.println(exp);
        System.out.println(result);
        if (!exp.equals(result)) System.out.println("NOT EQUAL!");
    }
