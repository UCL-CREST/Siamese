    public void encryptFile(File fileToBeEncrypted, File encryptedFile) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            keyGenerator.init(128, random);
            SecretKey secretKey = keyGenerator.generateKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.WRAP_MODE, publicKey);
            byte[] wrappedKey = cipher.wrap(secretKey);
            DataOutputStream out = new DataOutputStream(new FileOutputStream(encryptedFile));
            out.writeInt(wrappedKey.length);
            out.write(wrappedKey);
            InputStream in = new FileInputStream(fileToBeEncrypted);
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            crypt(in, out, cipher);
            in.close();
            out.close();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NoSuchPaddingException nspe) {
            nspe.printStackTrace();
        } catch (InvalidKeyException ike) {
            ike.printStackTrace();
        } catch (IllegalBlockSizeException ibse) {
            ibse.printStackTrace();
        } catch (GeneralSecurityException gse) {
            gse.printStackTrace();
        }
    }
