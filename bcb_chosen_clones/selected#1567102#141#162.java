    private void zip(File folderToZip) throws Exception {
        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(zipOut);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        byte data[] = new byte[BUFFER];
        List list = getListFiles(folderToZip, new ArrayList());
        for (int i = 0; i < list.size(); i++) {
            FileInputStream fi = new FileInputStream((File) list.get(i));
            origin = new BufferedInputStream(fi, BUFFER);
            File f = (File) list.get(i);
            String entryName = f.getAbsolutePath().substring(folderToZip.getAbsolutePath().length() + 1);
            entryName = entryName.replaceAll("\\\\", "/");
            ZipEntry entry = new ZipEntry(entryName);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }
        out.close();
    }
