    public static void zipDir(String dirToZip, String zipName) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipName));
        List<String> l = new ArrayList<String>();
        listOfFiles(new File(dirToZip), l);
        byte[] readBuffer = null;
        RandomAccessFile raf = null;
        for (Iterator iterator = l.iterator(); iterator.hasNext(); ) {
            String filename = (String) iterator.next();
            raf = new RandomAccessFile(filename, "r");
            readBuffer = new byte[(int) raf.length()];
            raf.read(readBuffer);
            ZipEntry anEntry = new ZipEntry(filename.substring(dirToZip.length()));
            zos.putNextEntry(anEntry);
            zos.write(readBuffer, 0, (int) raf.length());
            raf.close();
        }
        zos.close();
    }
