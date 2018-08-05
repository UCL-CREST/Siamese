    public void store() throws FileNotFoundException, IOException, Exception {
        byte[] data = getData("localhost", "3306", "root", "", "ceha").getBytes();
        File filedst = new File(filename);
        FileOutputStream dest = new FileOutputStream(filedst);
        ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(dest));
        zip.setMethod(ZipOutputStream.DEFLATED);
        zip.setLevel(Deflater.BEST_COMPRESSION);
        zip.putNextEntry(new ZipEntry("data.sql"));
        zip.write(data);
        zip.close();
        dest.close();
    }
