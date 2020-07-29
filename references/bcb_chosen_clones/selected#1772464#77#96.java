    public void compress(File outputFile) {
        try {
            FileOutputStream dest = new FileOutputStream((outputFile == null) ? new File(file.toString() + ".zip") : outputFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            FileInputStream fi = new FileInputStream(file);
            BufferedInputStream origin = null;
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(file.toString());
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
