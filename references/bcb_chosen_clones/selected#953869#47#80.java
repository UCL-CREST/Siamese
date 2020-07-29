    public static void zip(File zipFile, List<File> filesToZip, boolean useAbsolutePath, String removePrefix) throws IOException {
        byte[] buffer = new byte[BUFFER];
        zipFile.createNewFile();
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        out.setLevel(Deflater.DEFAULT_COMPRESSION);
        for (File file : filesToZip) {
            String name = file.getName();
            if (useAbsolutePath) {
                name = file.getAbsolutePath();
                if (removePrefix != null) {
                    name = name.replace(removePrefix, "");
                }
            }
            FileInputStream in = new FileInputStream(file);
            try {
                out.putNextEntry(new ZipEntry(name));
                System.out.println(name);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.closeEntry();
            } catch (Exception ex) {
                Log.exception(ex);
                try {
                    out.closeEntry();
                } catch (Exception ex2) {
                }
            } finally {
                in.close();
            }
        }
        out.close();
    }
