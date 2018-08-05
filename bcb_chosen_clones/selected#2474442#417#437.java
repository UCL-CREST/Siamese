    private void buildJar(List<Foto> fotos, OutputStream stream) throws IOException {
        JarOutputStream jar = new JarOutputStream(stream);
        logger.info("Zipping");
        HashMap<String, String> filenames = new HashMap<String, String>();
        for (Foto foto : fotos) {
            logger.info("Zipping " + foto.getTitle());
            String title = foto.getTitle();
            title = title.replaceAll("\\\\", "|");
            title = title.replaceAll("\\/", "|");
            title = title.replaceAll("\\~", "-");
            for (; filenames.containsKey(title); ) {
                title = title + "_";
            }
            filenames.put(title, "");
            title = title + ".jpg";
            jar.putNextEntry(new ZipEntry(title));
            jar.write(foto.getImgAsBytes());
        }
        jar.close();
        logger.info("Zipping done");
    }
