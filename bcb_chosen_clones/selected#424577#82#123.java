    public void initForEncryption() throws CryptographyException, IOException {
        String ownerPassword = pdDocument.getOwnerPasswordForEncryption();
        String userPassword = pdDocument.getUserPasswordForEncryption();
        if (ownerPassword == null) {
            ownerPassword = "";
        }
        if (userPassword == null) {
            userPassword = "";
        }
        PDStandardEncryption encParameters = (PDStandardEncryption) pdDocument.getEncryptionDictionary();
        int permissionInt = encParameters.getPermissions();
        int revision = encParameters.getRevision();
        int length = encParameters.getLength() / 8;
        COSArray idArray = document.getDocumentID();
        if (idArray == null || idArray.size() < 2) {
            idArray = new COSArray();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                BigInteger time = BigInteger.valueOf(System.currentTimeMillis());
                md.update(time.toByteArray());
                md.update(ownerPassword.getBytes());
                md.update(userPassword.getBytes());
                md.update(document.toString().getBytes());
                byte[] id = md.digest(this.toString().getBytes());
                COSString idString = new COSString();
                idString.append(id);
                idArray.add(idString);
                idArray.add(idString);
                document.setDocumentID(idArray);
            } catch (NoSuchAlgorithmException e) {
                throw new CryptographyException(e);
            }
        }
        COSString id = (COSString) idArray.getObject(0);
        encryption = new PDFEncryption();
        byte[] o = encryption.computeOwnerPassword(ownerPassword.getBytes("ISO-8859-1"), userPassword.getBytes("ISO-8859-1"), revision, length);
        byte[] u = encryption.computeUserPassword(userPassword.getBytes("ISO-8859-1"), o, permissionInt, id.getBytes(), revision, length);
        encryptionKey = encryption.computeEncryptedKey(userPassword.getBytes("ISO-8859-1"), o, permissionInt, id.getBytes(), revision, length);
        encParameters.setOwnerKey(o);
        encParameters.setUserKey(u);
        document.setEncryptionDictionary(encParameters.getCOSDictionary());
    }
