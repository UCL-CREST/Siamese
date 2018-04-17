    public static void copyAndCloseStream(InputStream is, OutputStream os) throws IOException {
        try {
            IOUtils.copy(is, os);
        } finally {
            try {
                os.close();
            } finally {
                is.close();
            }
        }
    }
