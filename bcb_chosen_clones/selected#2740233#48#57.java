    public static void copyToFileAndCloseStreams(InputStream istr, File destFile) throws IOException {
        OutputStream ostr = null;
        try {
            ostr = new FileOutputStream(destFile);
            IOUtils.copy(istr, ostr);
        } finally {
            if (ostr != null) ostr.close();
            if (istr != null) istr.close();
        }
    }
