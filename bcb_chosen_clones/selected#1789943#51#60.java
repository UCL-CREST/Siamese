    private void copyFile(File f) throws IOException {
        File newFile = new File(destdir + "/" + f.getName());
        newFile.createNewFile();
        FileInputStream fin = new FileInputStream(f);
        FileOutputStream fout = new FileOutputStream(newFile);
        int c;
        while ((c = fin.read()) != -1) fout.write(c);
        fin.close();
        fout.close();
    }
