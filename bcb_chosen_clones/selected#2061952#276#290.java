    public static Body decodeBody(InputStream in, String contentTransferEncoding) throws IOException {
        if (contentTransferEncoding != null) {
            contentTransferEncoding = MimeUtility.getHeaderParameter(contentTransferEncoding, null);
            if ("quoted-printable".equalsIgnoreCase(contentTransferEncoding)) {
                in = new QuotedPrintableInputStream(in);
            } else if ("base64".equalsIgnoreCase(contentTransferEncoding)) {
                in = new Base64InputStream(in);
            }
        }
        BinaryTempFileBody tempBody = new BinaryTempFileBody();
        OutputStream out = tempBody.getOutputStream();
        IOUtils.copy(in, out);
        out.close();
        return tempBody;
    }
