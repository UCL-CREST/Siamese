    public void extractSong(Song s, File dir) {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        File dest = new File(dir, s.file.getName());
        if (dest.equals(s.file)) return;
        byte[] buf = new byte[COPY_BLOCKSIZE];
        try {
            fin = new FileInputStream(s.file);
            fout = new FileOutputStream(dest);
            int read = 0;
            do {
                read = fin.read(buf);
                if (read > 0) fout.write(buf, 0, read);
            } while (read > 0);
        } catch (IOException ex) {
            ex.printStackTrace();
            Dialogs.showErrorDialog("xtract.error");
        } finally {
            try {
                fin.close();
                fout.close();
            } catch (Exception ex) {
            }
        }
    }
