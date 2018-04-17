    public void unsplit(String newFilename, File[] files) throws Exception {
        FileOutputStream stream = new FileOutputStream(new File(newFilename));
        for (int i = 0; i < files.length; i++) {
            FileInputStream fin = new FileInputStream(files[i].getAbsolutePath());
            DataInputStream din = new DataInputStream(fin);
            while (din.available() > 0) {
                stream.write(din.read());
            }
            din.close();
            fin.close();
        }
        stream.close();
    }
