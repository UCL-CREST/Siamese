    protected String contentString() {
        String result = null;
        URL url;
        String encoding = null;
        try {
            url = url();
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setUseCaches(false);
            for (Enumeration e = bindingKeys().objectEnumerator(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                if (key.startsWith("?")) {
                    connection.setRequestProperty(key.substring(1), valueForBinding(key).toString());
                }
            }
            if (connection.getContentEncoding() != null) {
                encoding = connection.getContentEncoding();
            }
            if (encoding == null) {
                encoding = (String) valueForBinding("encoding");
            }
            if (encoding == null) {
                encoding = "UTF-8";
            }
            InputStream stream = connection.getInputStream();
            byte bytes[] = ERXFileUtilities.bytesFromInputStream(stream);
            stream.close();
            result = new String(bytes, encoding);
        } catch (IOException ex) {
            throw NSForwardException._runtimeExceptionForThrowable(ex);
        }
        return result;
    }
