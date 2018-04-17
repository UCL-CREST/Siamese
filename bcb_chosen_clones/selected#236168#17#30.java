    public File Compress(File input_file) throws Exception {
        byte[] data = new byte[(int) input_file.length()];
        FileInputStream fis = new FileInputStream(input_file);
        fis.read(data);
        fis.close();
        File comp_file = new File(input_file.getAbsolutePath() + ".zip");
        FileOutputStream fos = new FileOutputStream(comp_file);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zos.putNextEntry(new ZipEntry(input_file.getName()));
        zos.write(data);
        zos.close();
        fos.close();
        return new File(input_file.getAbsolutePath() + ".zip");
    }
