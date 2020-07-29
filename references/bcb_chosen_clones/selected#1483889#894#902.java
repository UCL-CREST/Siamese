    private void copyParseFileToCodeFile() throws IOException {
        InputStream in = new FileInputStream(new File(filenameParse));
        OutputStream out = new FileOutputStream(new File(filenameMisc));
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) out.write(buffer, 0, length);
        in.close();
        out.close();
    }
