    public EncodedScript(PackageScript source, DpkgData data) throws IOException {
        _source = source;
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        OutputStream output = null;
        try {
            output = MimeUtility.encode(bytes, BASE64);
        } catch (final MessagingException e) {
            throw new IOException("Failed to uuencode script. name=[" + _source.getFriendlyName() + "], reason=[" + e.getMessage() + "].");
        }
        IOUtils.write(HEADER, bytes, Dpkg.CHAR_ENCODING);
        bytes.flush();
        IOUtils.copy(_source.getSource(data), output);
        output.flush();
        IOUtils.write(FOOTER, bytes, Dpkg.CHAR_ENCODING);
        bytes.flush();
        output.close();
        bytes.close();
        _encoded = bytes.toString(Dpkg.CHAR_ENCODING);
    }
