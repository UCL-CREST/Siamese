    public static File zipFolder(File inFolder, File outFile) {
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
            BufferedInputStream in = null;
            byte[] data = new byte[4024];
            String files[] = inFolder.list();
            for (int i = 0; i < files.length; i++) {
                in = new BufferedInputStream(new FileInputStream(inFolder.getPath() + "/" + files[i]), 4024);
                out.putNextEntry(new ZipEntry(files[i]));
                int count;
                while ((count = in.read(data, 0, 4024)) != -1) {
                    out.write(data, 0, count);
                }
                out.closeEntry();
            }
            cleanUp(out);
            cleanUp(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(outFile + ".zip");
    }
