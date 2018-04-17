    private void loadSecretKey(File encryptedSecretKeyFile, char[] password) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream in = null;
            try {
                in = new FileInputStream(encryptedSecretKeyFile);
                byte[] bytes = new byte[1024];
                int n = 0;
                while ((n = in.read(bytes)) != -1) {
                    baos.write(bytes, 0, n);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
            byte[] encryptedSecretKey = Base64.decodeBase64(baos.toByteArray());
            String alias = getKeyStore().aliases().nextElement();
            Key key = getKeyStore().getKey(alias, password);
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(Cipher.UNWRAP_MODE, key);
            secretKey = (SecretKey) cipher.unwrap(encryptedSecretKey, "DESede", Cipher.SECRET_KEY);
        } catch (Exception ex) {
            throw new RuntimeException("Could not load secret key from " + encryptedSecretKeyFile.getPath(), ex);
        }
    }
