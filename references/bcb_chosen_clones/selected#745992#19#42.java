    public void zip(String resourceName) {
        String[] filenames = new String[2];
        if (new File(ResourceSettings.UPLOAD_PATH + resourceName + ResourceSettings.DATASET_EXTENSION_1).exists()) filenames[0] = ResourceSettings.UPLOAD_PATH + resourceName + ResourceSettings.DATASET_EXTENSION_1; else if (new File(ResourceSettings.UPLOAD_PATH + resourceName + ResourceSettings.DATASET_EXTENSION_2).exists()) filenames[0] = ResourceSettings.UPLOAD_PATH + resourceName + ResourceSettings.DATASET_EXTENSION_2;
        if (new File(ResourceSettings.UPLOAD_PATH + resourceName + ResourceSettings.METADATA_EXTENSION_1).exists()) filenames[1] = ResourceSettings.UPLOAD_PATH + resourceName + ResourceSettings.METADATA_EXTENSION_1; else if (new File(ResourceSettings.UPLOAD_PATH + resourceName + ResourceSettings.METADATA_EXTENSION_2).exists()) filenames[1] = ResourceSettings.UPLOAD_PATH + resourceName + ResourceSettings.METADATA_EXTENSION_2;
        byte[] buf = new byte[1024];
        try {
            String outFilename = ResourceSettings.DOWNLOAD_PATH + resourceName + ResourceSettings.ARCHIVE_EXTENSION_1;
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
            for (int i = 0; i < filenames.length; i++) {
                File tmpFile = new File(filenames[i]);
                FileInputStream in = new FileInputStream(tmpFile);
                out.putNextEntry(new ZipEntry(tmpFile.getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            System.out.println("IO EXCEPTION: " + e.getMessage());
        }
    }
