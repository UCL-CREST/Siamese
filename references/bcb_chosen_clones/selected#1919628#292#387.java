    public Login authenticateClient() {
        Object o;
        String user, password;
        Vector<Login> clientLogins = ClientLoginsTableModel.getClientLogins();
        Login login = null;
        try {
            socket.setSoTimeout(25000);
            objectOut.writeObject("JFRITZ SERVER 1.1");
            objectOut.flush();
            o = objectIn.readObject();
            if (o instanceof String) {
                user = (String) o;
                objectOut.flush();
                for (Login l : clientLogins) {
                    if (l.getUser().equals(user)) {
                        login = l;
                        break;
                    }
                }
                if (login != null) {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(login.getPassword().getBytes());
                    DESKeySpec desKeySpec = new DESKeySpec(md.digest());
                    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
                    Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                    desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
                    byte[] dataKeySeed = new byte[32];
                    Random random = new Random();
                    random.nextBytes(dataKeySeed);
                    md.reset();
                    md.update(dataKeySeed);
                    dataKeySeed = md.digest();
                    SealedObject dataKeySeedSealed;
                    dataKeySeedSealed = new SealedObject(dataKeySeed, desCipher);
                    objectOut.writeObject(dataKeySeedSealed);
                    objectOut.flush();
                    desKeySpec = new DESKeySpec(dataKeySeed);
                    secretKey = keyFactory.generateSecret(desKeySpec);
                    inCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                    outCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                    inCipher.init(Cipher.DECRYPT_MODE, secretKey);
                    outCipher.init(Cipher.ENCRYPT_MODE, secretKey);
                    SealedObject sealedObject = (SealedObject) objectIn.readObject();
                    o = sealedObject.getObject(inCipher);
                    if (o instanceof String) {
                        String response = (String) o;
                        if (response.equals("OK")) {
                            SealedObject ok_sealed = new SealedObject("OK", outCipher);
                            objectOut.writeObject(ok_sealed);
                            return login;
                        } else {
                            Debug.netMsg("Client sent false response to challenge!");
                        }
                    } else {
                        Debug.netMsg("Client sent false object as response to challenge!");
                    }
                } else {
                    Debug.netMsg("client sent unkown username: " + user);
                }
            }
        } catch (IllegalBlockSizeException e) {
            Debug.netMsg("Wrong blocksize for sealed object!");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Debug.netMsg("received unrecognized object from client!");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            Debug.netMsg("MD5 Algorithm not present in this JVM!");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            Debug.netMsg("Error generating cipher, problems with key spec?");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            Debug.netMsg("Error genertating cipher, problems with key?");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            Debug.netMsg("Error generating cipher, problems with padding?");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Debug.netMsg("Error authenticating client!");
            Debug.error(e.toString());
            e.printStackTrace();
        } catch (BadPaddingException e) {
            Debug.netMsg("Bad padding exception!");
            Debug.error(e.toString());
            e.printStackTrace();
        }
        return null;
    }
