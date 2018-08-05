    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        copy(new FileInputStream("moduls/ESBClient/test/files/cades-implicito.csig"), bos);
        byte[] firma = bos.toByteArray();
        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
        copy(new FileInputStream("moduls/ESBClient/test/files/datos.txt"), bos2);
        byte[] documento = bos2.toByteArray();
        byte[] datosEnFirma = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(firma);
        ASN1InputStream lObjDerOut = new ASN1InputStream(bis);
        DERObject lObjDER = lObjDerOut.readObject();
        ContentInfo lObjPKCS7 = ContentInfo.getInstance(lObjDER);
        SignedData lObjSignedData = SignedData.getInstance(lObjPKCS7.getContent());
        ContentInfo lObjContent = lObjSignedData.getContentInfo();
        datosEnFirma = ((ASN1OctetString) lObjContent.getContent()).getOctets();
        System.out.println("Son iguales? " + Arrays.equals(documento, datosEnFirma));
    }
