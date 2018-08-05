    private void appendAndDelete(FileOutputStream outstream, String file) throws FileNotFoundException, IOException {
        FileInputStream input = new FileInputStream(file);
        byte[] buffer = new byte[65536];
        int l;
        while ((l = input.read(buffer)) != -1) outstream.write(buffer, 0, l);
        input.close();
        new File(file).delete();
    }
