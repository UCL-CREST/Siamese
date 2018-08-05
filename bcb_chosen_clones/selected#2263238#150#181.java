    private void zipFiles(List<File> files, String modelFileName) {
        logger.debug("zipping files");
        try {
            BufferedInputStream origin = null;
            File zip = new File(dataPath, modelFileName);
            FileOutputStream dest = new FileOutputStream(zip);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            HashSet<String> fileNames = new HashSet<String>(files.size());
            byte data[] = new byte[BUFFER];
            for (File file : files) {
                logger.debug("Adding: {}", file.getName());
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry;
                if (fileNames.add(file.getName())) entry = new ZipEntry(file.getName()); else {
                    entry = new ZipEntry(file.getName() + "_1");
                    fileNames.add(entry.getName());
                }
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
            responseContent = new JSONResponse(true, zip.getAbsolutePath(), FileSize.getFileSizeAsString(zip.length()));
        } catch (Exception e) {
            logger.error("Error while packing files to zip! msg: {}", e.getMessage());
            responseContent.setErrorMsg(e.getMessage());
        }
    }
