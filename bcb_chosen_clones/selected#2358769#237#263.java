    private void extractZipFile(String filename, JTextPane progressText) throws IOException {
        String destinationname = "";
        byte[] buf = new byte[1024];
        ZipInputStream zipinputstream = null;
        ZipEntry zipentry;
        zipinputstream = new ZipInputStream(new FileInputStream(filename));
        while ((zipentry = zipinputstream.getNextEntry()) != null) {
            String entryName = zipentry.getName();
            if (progressText != null) {
                progressText.setText("extracting " + entryName);
            }
            int n;
            FileOutputStream fileoutputstream;
            if (zipentry.isDirectory()) {
                (new File(destinationname + entryName)).mkdir();
                continue;
            }
            fileoutputstream = new FileOutputStream(destinationname + entryName);
            while ((n = zipinputstream.read(buf, 0, 1024)) > -1) fileoutputstream.write(buf, 0, n);
            fileoutputstream.close();
            zipinputstream.closeEntry();
        }
        if (progressText != null) {
            progressText.setText("Files extracted");
        }
        zipinputstream.close();
    }
