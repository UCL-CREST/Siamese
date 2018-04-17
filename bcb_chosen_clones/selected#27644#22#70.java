    public static final void buildZipBall(ZipMap zipMap, String outputFilepath) throws Exception {
        String outFilename = outputFilepath;
        if (!outFilename.endsWith(".zip")) {
            outFilename += ".zip";
        }
        logger.info("Build ZIP file <" + outputFilepath + ">");
        ZipOutputStream out;
        out = new ZipOutputStream(new FileOutputStream(outFilename));
        long file_size = 0;
        boolean full = false;
        for (String dir : zipMap.keySet()) {
            Set<ZipEntryRef> zers = zipMap.get(dir);
            for (ZipEntryRef zer : zers) {
                String fileitem = zer.getUri();
                byte[] buf = new byte[1024];
                FileInputStream in;
                File f;
                if ((f = new File(fileitem)).exists()) {
                    in = new FileInputStream(fileitem);
                    file_size += f.length() / 1000000;
                } else if ((f = new File(fileitem + ".gz")).exists()) {
                    in = new FileInputStream(fileitem + ".gz");
                    file_size += f.length() / 1000000;
                } else {
                    logger.error("File <" + fileitem + "> or <" + fileitem + ".gz> not found");
                    continue;
                }
                logger.info("Compress " + fileitem);
                ZipEntry ze = new ZipEntry(dir + "/" + (new File(fileitem)).getName());
                out.putNextEntry(ze);
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.closeEntry();
                if (file_size > MAX_SIZE) {
                    logger.warn("The size of zipped file exceed " + MAX_SIZE + "Mb: zipball truncated");
                    full = true;
                    break;
                }
            }
            if (full) {
                break;
            }
        }
        out.closeEntry();
        out.close();
    }
