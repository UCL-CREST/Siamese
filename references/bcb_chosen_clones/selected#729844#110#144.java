    @Override
    public void close() throws FreezeDryingException {
        try {
            if (!reading) {
                DateFormat format = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
                String tmp = zipFileName;
                if (tmp.endsWith(".zip")) {
                    tmp = tmp.substring(0, tmp.length() - 4);
                }
                File zipFile = new File(new File(path).getParentFile(), new File(tmp).getName() + "_" + format.format(new Date()) + ".zip");
                if (!zipFile.exists()) zipFile.createNewFile();
                byte[] buf = new byte[1024];
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
                for (String path : writtenFiles) {
                    File file = new File(path);
                    FileInputStream in = new FileInputStream(file);
                    out.putNextEntry(new ZipEntry(file.getName()));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
                out.close();
            }
            File dir = new File(path);
            for (File file : dir.listFiles()) {
                file.delete();
            }
            dir.delete();
        } catch (IOException ex) {
            throw new FreezeDryingException("Error while zipping freezedry files", ex);
        }
    }
