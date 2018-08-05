    public void write(PDDocument doc) throws COSVisitorException {
        document = doc;
        SecurityHandler securityHandler = document.getSecurityHandler();
        if (securityHandler != null) {
            try {
                securityHandler.prepareDocumentForEncryption(document);
                this.willEncrypt = true;
            } catch (IOException e) {
                throw new COSVisitorException(e);
            } catch (CryptographyException e) {
                throw new COSVisitorException(e);
            }
        } else {
            this.willEncrypt = false;
        }
        COSDocument cosDoc = document.getDocument();
        COSDictionary trailer = cosDoc.getTrailer();
        COSArray idArray = (COSArray) trailer.getDictionaryObject("ID");
        if (idArray == null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(Long.toString(System.currentTimeMillis()).getBytes());
                COSDictionary info = (COSDictionary) trailer.getDictionaryObject("Info");
                if (info != null) {
                    Iterator values = info.getValues().iterator();
                    while (values.hasNext()) {
                        md.update(values.next().toString().getBytes());
                    }
                }
                idArray = new COSArray();
                COSString id = new COSString(md.digest());
                idArray.add(id);
                idArray.add(id);
                trailer.setItem("ID", idArray);
            } catch (NoSuchAlgorithmException e) {
                throw new COSVisitorException(e);
            }
        }
        cosDoc.accept(this);
    }
