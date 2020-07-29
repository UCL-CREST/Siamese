    private static URL downLoadZippedFile(URL url, File destDir) throws Exception {
        URLConnection urlConnection = url.openConnection();
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("remoteLib_", null);
            InputStream in = null;
            FileOutputStream out = null;
            try {
                in = urlConnection.getInputStream();
                out = new FileOutputStream(tmpFile);
                IOUtils.copy(in, out);
            } finally {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            unzip(tmpFile, destDir);
        } finally {
            if (tmpFile != null) {
                tmpFile.delete();
            }
        }
        URL localURL = destDir.toURI().toURL();
        return localURL;
    }
