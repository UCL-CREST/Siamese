    public void SplitFile(File in, File out0, File out1, long pos) throws IOException {
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out0);
        FileChannel fic = fis.getChannel();
        FileChannel foc = fos.getChannel();
        foc.transferFrom(fic, 0, pos);
        foc.close();
        fos = new FileOutputStream(out1);
        foc = fos.getChannel();
        foc.transferFrom(fic, 0, fic.size() - pos);
        foc.close();
        fic.close();
    }
