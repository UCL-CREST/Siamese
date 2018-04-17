    private void packageDestZip(File tmpFile) throws FileNotFoundException, IOException {
        log("Creating launch profile package " + destfile);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destfile)));
        ZipEntry e = new ZipEntry(RESOURCE_JAR_FILENAME);
        e.setMethod(ZipEntry.STORED);
        e.setSize(tmpFile.length());
        e.setCompressedSize(tmpFile.length());
        e.setCrc(calcChecksum(tmpFile, new CRC32()));
        out.putNextEntry(e);
        InputStream in = new BufferedInputStream(new FileInputStream(tmpFile));
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.closeEntry();
        out.finish();
        out.close();
    }
