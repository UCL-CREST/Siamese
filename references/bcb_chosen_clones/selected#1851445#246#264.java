    private void zipStream(InputStream inStream, String streamName, File zipFile) throws IOException {
        if (inStream == null) {
            log.warn("No stream to zip.");
        } else {
            try {
                FileOutputStream dest = new FileOutputStream(zipFile);
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                ZipEntry entry = new ZipEntry(streamName);
                out.putNextEntry(entry);
                copyInputStream(inStream, out);
                out.close();
                dest.close();
                inStream.close();
            } catch (IOException e) {
                log.error("IOException while zipping stream");
                throw e;
            }
        }
    }
