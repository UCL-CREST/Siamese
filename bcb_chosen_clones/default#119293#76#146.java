    private void encryptAndWriteToFile() {
        String originalText = fileContentsEditorPane.getText();
        String password = passwordTextField.getText();
        String fileName = fileNameTextField.getText();
        Cipher cipher = null;
        try {
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterationCount);
            cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
            System.exit(1);
        } catch (InvalidKeySpecException exception) {
            exception.printStackTrace();
            System.exit(1);
        } catch (InvalidKeyException exception) {
            exception.printStackTrace();
            System.exit(1);
        } catch (NoSuchPaddingException exception) {
            exception.printStackTrace();
            System.exit(1);
        } catch (InvalidAlgorithmParameterException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        byte[] outputArray = null;
        try {
            outputArray = originalText.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        File file = new File(fileName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        CipherOutputStream out = new CipherOutputStream(fileOutputStream, cipher);
        try {
            out.write(outputArray);
            out.flush();
            out.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        Vector fileBytes = new Vector();
        try {
            FileInputStream in = new FileInputStream(file);
            byte contents;
            while (in.available() > 0) {
                contents = (byte) in.read();
                fileBytes.add(new Byte(contents));
            }
            in.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        byte[] encryptedText = new byte[fileBytes.size()];
        for (int i = 0; i < fileBytes.size(); i++) {
            encryptedText[i] = ((Byte) fileBytes.elementAt(i)).byteValue();
        }
        fileContentsEditorPane.setText(new String(encryptedText));
    }
