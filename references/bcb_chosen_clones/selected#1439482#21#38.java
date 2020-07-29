    protected static String getFileContentAsString(URL url, String encoding) throws IOException {
        InputStream input = null;
        StringWriter sw = new StringWriter();
        try {
            System.out.println("Free mem :" + Runtime.getRuntime().freeMemory());
            input = url.openStream();
            IOUtils.copy(input, sw, encoding);
            System.out.println("Free mem :" + Runtime.getRuntime().freeMemory());
        } finally {
            if (input != null) {
                input.close();
                System.gc();
                input = null;
                System.out.println("Free mem :" + Runtime.getRuntime().freeMemory());
            }
        }
        return sw.toString();
    }
