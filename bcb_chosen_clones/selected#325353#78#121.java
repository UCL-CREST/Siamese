    public void writeToFile(List<TitledURLEntry> entries, File bookmarksFile) throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        if ((entries == null) || (bookmarksFile == null)) {
            throw new NullPointerException();
        }
        String write_type = "b1";
        if (write_type.equals("")) {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bookmarksFile), "UTF-8"));
            writeData(entries);
            writer.flush();
            writer.close();
        } else if (write_type.equals("b1")) {
            writer = (new StringWriter());
            writeData(entries);
            byte[] out = writer.toString().getBytes();
            byte[] raw = new byte[16];
            raw[0] = (byte) 1;
            raw[2] = (byte) 23;
            raw[3] = (byte) 24;
            raw[4] = (byte) 2;
            raw[5] = (byte) 99;
            raw[6] = (byte) 200;
            raw[7] = (byte) 202;
            raw[8] = (byte) 209;
            raw[9] = (byte) 199;
            raw[10] = (byte) 181;
            raw[11] = (byte) 255;
            raw[12] = (byte) 33;
            raw[13] = (byte) 210;
            raw[14] = (byte) 214;
            raw[15] = (byte) 216;
            SecretKeySpec skeyspec = new SecretKeySpec(raw, "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted = cipher.doFinal(out);
            BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bookmarksFile), "UTF-8"));
            writer2.write("b1");
            writer2.write(Util.byteArraytoHexString(encrypted));
            writer2.flush();
            writer2.close();
        } else {
            System.out.println("FATAL ERROR -- BookmarksWriter.java  unknown write style");
            System.exit(10);
        }
    }
