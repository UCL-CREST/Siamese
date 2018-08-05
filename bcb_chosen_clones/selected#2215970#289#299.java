    public File copyFile(File f) throws IOException {
        File t = createNewFile("fm", "cpy");
        FileOutputStream fos = new FileOutputStream(t);
        FileChannel foc = fos.getChannel();
        FileInputStream fis = new FileInputStream(f);
        FileChannel fic = fis.getChannel();
        foc.transferFrom(fic, 0, fic.size());
        foc.close();
        fic.close();
        return t;
    }
