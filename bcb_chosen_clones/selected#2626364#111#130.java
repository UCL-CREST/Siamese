    @Override
    public String toString() {
        if (byteArrayOutputStream == null) return "<Unparsed binary data: Content-Type=" + getHeader("Content-Type") + " >";
        String charsetName = getCharsetName();
        if (charsetName == null) charsetName = "ISO-8859-1";
        try {
            if (unzip) {
                GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                ByteArrayOutputStream unzippedResult = new ByteArrayOutputStream();
                IOUtils.copy(gzipInputStream, unzippedResult);
                return unzippedResult.toString(charsetName);
            } else {
                return byteArrayOutputStream.toString(charsetName);
            }
        } catch (UnsupportedEncodingException e) {
            throw new OutputException(e);
        } catch (IOException e) {
            throw new OutputException(e);
        }
    }
