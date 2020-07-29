    public static void toZip(File zippedFile, File[] filesToZip, String zipComment, boolean savePath, int compressionLevel) throws IOException, FileNotFoundException, ZipException {
        if (zippedFile != null && filesToZip != null) {
            new File(zippedFile.getParent()).mkdirs();
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new CheckedOutputStream(new FileOutputStream(zippedFile), new CRC32())));
            if (ZIP_NO_COMPRESSION <= compressionLevel && compressionLevel <= ZIP_MAX_COMPRESSION) out.setLevel(compressionLevel); else out.setLevel(ZIP_MAX_COMPRESSION);
            if (zipComment != null) out.setComment(zipComment);
            for (int i = 0; i < filesToZip.length; i++) {
                BufferedInputStream in;
                if (savePath) {
                    in = new BufferedInputStream(new FileInputStream(filesToZip[i]));
                    out.putNextEntry(new ZipEntry(cleanPath(filesToZip[i].getAbsolutePath())));
                } else {
                    in = new BufferedInputStream(new FileInputStream(filesToZip[i]));
                    out.putNextEntry(new ZipEntry(filesToZip[i].getName()));
                }
                for (int c = in.read(); c != -1; c = in.read()) out.write(c);
                in.close();
            }
            out.close();
        } else throw new ZipException(MAIN_RESOURCE_BUNDLE.getString("default.ZipException.text"));
    }
