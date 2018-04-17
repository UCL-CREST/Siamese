    public static void zipFile(String[] sources, String outputFile) throws BaseException {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
            for (int i = 0; i < sources.length; i++) {
                FileInputStream in = new FileInputStream(sources[i]);
                out.putNextEntry(new ZipEntry(sources[i]));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (Exception e) {
            throw new BaseException(ErrorCodes.CODE_248);
        }
    }
