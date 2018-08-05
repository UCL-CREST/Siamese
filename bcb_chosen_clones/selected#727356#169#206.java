    public synchronized void backupStore(String name) throws IOException {
        if (null != name && name.length() > 0) {
            String path = this.directoryPath + File.separator + name;
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                String list[] = file.list();
                byte[] buf = new byte[1024];
                ZipOutputStream out = null;
                String zipFileName = null;
                try {
                    zipFileName = this.directoryPath + File.separator + "backup_" + System.currentTimeMillis() + "_" + name + ".zip";
                    out = new ZipOutputStream(new FileOutputStream(zipFileName));
                } catch (FileNotFoundException x) {
                    getLogger().error("This should never happen", x);
                    throw new IOException("Couldn't get ZipFile (" + zipFileName + ")!");
                }
                for (int i = 0; i < list.length; i++) {
                    try {
                        FileInputStream in = new FileInputStream(path + File.separator + list[i]);
                        out.putNextEntry(new ZipEntry(list[i]));
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                        in.close();
                    } catch (IOException x) {
                        getLogger().error(x.getMessage(), x);
                    }
                }
                out.close();
            } else {
                throw new IOException("Invalid Store (" + name + ")!");
            }
        } else {
            throw new IOException("Invalid Store (" + name + ")!");
        }
    }
