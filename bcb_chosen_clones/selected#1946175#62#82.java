    private void zipFiles(List<File> files, HttpServletResponse response) {
        try {
            BufferedInputStream origin = null;
            ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
            byte data[] = new byte[BUFFER];
            for (int i = 0; i < files.size(); i++) {
                FileInputStream fi = new FileInputStream(files.get(i));
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(files.get(i).getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            log.error(e);
        }
    }
