    public void unzip(String resource) {
        File f = new File(resource);
        if (!f.exists()) throw new RuntimeException("The specified resources does not exist (" + resource + ")");
        String parent = f.getParent().toString();
        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(resource);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                log.info("Extracting archive entry: " + entry);
                String entryPath = new StringBuilder(parent).append(System.getProperty("file.separator")).append(entry.getName()).toString();
                if (entry.isDirectory()) {
                    log.info("Creating directory: " + entryPath);
                    (new File(entryPath)).mkdir();
                    continue;
                }
                int count;
                byte data[] = new byte[BUFFER];
                FileOutputStream fos = new FileOutputStream(entryPath);
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) dest.write(data, 0, count);
                dest.flush();
                dest.close();
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
