    public void writeTo(OutputStream out) throws IOException, MessagingException {
        InputStream in = getInputStream();
        Base64OutputStream base64Out = new Base64OutputStream(out);
        IOUtils.copy(in, base64Out);
        base64Out.close();
        mFile.delete();
    }
