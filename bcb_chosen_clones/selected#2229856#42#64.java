    private static void processFiles(final File packDir, final File file, final ZipOutputStream out) throws IOException {
        if (file.isDirectory()) {
            final ZipEntry e = new ZipEntry(ZipUtils.convertFileName(packDir, file) + "/");
            out.putNextEntry(e);
            out.closeEntry();
            final File[] files = file.listFiles();
            for (final File f : files) ZipUtils.processFiles(packDir, f, out);
        } else {
            final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            final ZipEntry e = new ZipEntry(ZipUtils.convertFileName(packDir, file));
            final CRC32 crc = new CRC32();
            out.putNextEntry(e);
            final byte[] buf = new byte[4096];
            int len = 0;
            while ((len = bis.read(buf)) > 0) {
                out.write(buf, 0, len);
                crc.update(buf, 0, len);
            }
            e.setCrc(crc.getValue());
            out.closeEntry();
            bis.close();
        }
    }
