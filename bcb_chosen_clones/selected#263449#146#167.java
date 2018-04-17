    private void zip(File folderToZip) throws Exception {
        BufferedInputStream origin = null;
        FileOutputStream fOutputStream = new FileOutputStream(zipOut);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(fOutputStream));
        byte data[] = new byte[BUFFER];
        List<File> list = getListFiles(folderToZip, new ArrayList<File>());
        for (int i = 0; i < list.size(); i++) {
            FileInputStream fi = new FileInputStream(list.get(i));
            origin = new BufferedInputStream(fi, BUFFER);
            File f = list.get(i);
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
