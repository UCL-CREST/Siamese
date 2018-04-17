    public void prepareDocumentForEncryption(PDDocument doc) throws CryptographyException, IOException {
        document = doc;
        PDEncryptionDictionary encryptionDictionary = document.getEncryptionDictionary();
        if (encryptionDictionary == null) {
            encryptionDictionary = new PDEncryptionDictionary();
        }
        version = computeVersionNumber();
        revision = computeRevisionNumber();
        encryptionDictionary.setFilter(FILTER);
        encryptionDictionary.setVersion(version);
        encryptionDictionary.setRevision(revision);
        encryptionDictionary.setLength(keyLength);
        String ownerPassword = policy.getOwnerPassword();
        String userPassword = policy.getUserPassword();
        if (ownerPassword == null) {
            ownerPassword = "";
        }
        if (userPassword == null) {
            userPassword = "";
        }
        int permissionInt = policy.getPermissions().getPermissionBytes();
        encryptionDictionary.setPermissions(permissionInt);
        int length = keyLength / 8;
        COSArray idArray = document.getDocument().getDocumentID();
        if (idArray == null || idArray.size() < 2) {
            idArray = new COSArray();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                BigInteger time = BigInteger.valueOf(System.currentTimeMillis());
                md.update(time.toByteArray());
                md.update(ownerPassword.getBytes());
                md.update(userPassword.getBytes());
                md.update(document.getDocument().toString().getBytes());
                byte[] id = md.digest(this.toString().getBytes());
                COSString idString = new COSString();
                idString.append(id);
                idArray.add(idString);
                idArray.add(idString);
                document.getDocument().setDocumentID(idArray);
            } catch (NoSuchAlgorithmException e) {
                throw new CryptographyException(e);
            } catch (IOException e) {
                throw new CryptographyException(e);
            }
        }
        COSString id = (COSString) idArray.getObject(0);
        byte[] o = computeOwnerPassword(ownerPassword.getBytes("ISO-8859-1"), userPassword.getBytes("ISO-8859-1"), revision, length);
        byte[] u = computeUserPassword(userPassword.getBytes("ISO-8859-1"), o, permissionInt, id.getBytes(), revision, length);
        encryptionKey = computeEncryptedKey(userPassword.getBytes("ISO-8859-1"), o, permissionInt, id.getBytes(), revision, length);
        encryptionDictionary.setOwnerKey(o);
        encryptionDictionary.setUserKey(u);
        document.setEncryptionDictionary(encryptionDictionary);
        document.getDocument().setEncryptionDictionary(encryptionDictionary.getCOSDictionary());
    }
