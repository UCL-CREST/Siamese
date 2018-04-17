    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        byte[] aeskey = args[0].getBytes();
        System.out.println(args[1]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SecretKeySpec keyspec = new SecretKeySpec(aeskey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keyspec);
        CipherOutputStream aesOut = new CipherOutputStream(out, cipher);
        aesOut.write(args[1].getBytes());
        aesOut.write("\n".getBytes());
        aesOut.write(args[2].getBytes());
        aesOut.close();
        FileOutputStream fileout = new FileOutputStream("token");
        out.writeTo(fileout);
        fileout.close();
        byte[] encyptedBytes = out.toByteArray();
        System.out.println("Encypted: " + new String(encyptedBytes));
        ByteArrayInputStream input = new ByteArrayInputStream(encyptedBytes);
        keyspec = new SecretKeySpec(aeskey, "AES");
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keyspec);
        CipherInputStream aesIn = new CipherInputStream(input, cipher);
        Properties props = new Properties();
        props.load(aesIn);
        for (Object key : props.keySet()) {
            System.out.println(key + "=" + props.get(key));
        }
    }
