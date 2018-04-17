    public static void createFinalZip(Destination destination, String id, Collection<String> entryIds) throws Exception {
        logger.info("create zip file for id: " + id);
        ZipOutputStream ou = new ZipOutputStream(destination.getOutputStream());
        byte[] buf = new byte[1024];
        for (String file : entryIds) {
            logger.info("working on: " + file);
            String name = file.substring(file.indexOf(id) + id.length() + 1, file.length());
            logger.info("generated internal name: " + name);
            Source source = createSource(file);
            InputStream in = source.getStream();
            logger.info("write...");
            ou.putNextEntry(new ZipEntry(name));
            int len;
            while ((len = in.read(buf)) > 0) {
                ou.write(buf, 0, len);
            }
            in.close();
            ou.closeEntry();
        }
        ou.flush();
        ou.close();
        logger.info("done with zip creation");
    }
