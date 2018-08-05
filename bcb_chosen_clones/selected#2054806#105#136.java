    public void createBackup(File outFolder) {
        try {
            File inFolder = new File(DB_FOLDER);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFolder)));
            BufferedInputStream in = null;
            byte[] data = new byte[BUF_SIZE];
            List<File> folders = new ArrayList<File>();
            folders.add(inFolder);
            while (folders.size() != 0) {
                inFolder = folders.get(0);
                File files[] = inFolder.listFiles();
                folders.remove(0);
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        folders.add(files[i]);
                        continue;
                    }
                    in = new BufferedInputStream(new FileInputStream(inFolder.getPath() + "/" + files[i].getName()), BUF_SIZE);
                    out.putNextEntry(new ZipEntry(files[i].getPath()));
                    int count;
                    while ((count = in.read(data, 0, 1000)) != -1) {
                        out.write(data, 0, count);
                    }
                    out.closeEntry();
                }
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
