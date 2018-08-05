    @SuppressWarnings({ "ResultOfMethodCallIgnored" })
    public static Node combineCss(URL base, List<Node> linkCss, List<File> newFiles) throws IOException {
        File dir = File.createTempFile("css", "" + System.currentTimeMillis());
        StringBuilder name = new StringBuilder();
        try {
            if (dir.delete() && dir.mkdirs()) {
                File minDir = new File(dir, "min");
                minDir.mkdir();
                File combineFile = new File(minDir, "style.css");
                Writer writer = new FileWriter(combineFile);
                boolean first = true;
                for (Node link : linkCss) {
                    String path = ((Element) link).getAttribute("href");
                    URL url = new URL(buildUrl(base, path));
                    InputStream inputStream = url.openStream();
                    File cssFile = new File(dir, fileName(url));
                    FileOutputStream outputStream = new FileOutputStream(cssFile);
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                    inputStream.close();
                    if (!first) {
                        writer.write('\n');
                    } else {
                        first = false;
                    }
                    Reader reader = new FileReader(cssFile);
                    if (Configuration.cssMinification()) {
                        CssCompressor cssCompressor = new CssCompressor(reader);
                        cssCompressor.compress(writer, 0);
                    } else {
                        IOUtils.copy(reader, writer);
                    }
                    reader.close();
                    String fileName = cssFile.getName();
                    int pos = fileName.lastIndexOf('.');
                    if (pos >= 0) {
                        fileName = fileName.substring(0, pos);
                    }
                    name.append(fileName).append(",");
                }
                writer.close();
                FileReader reader = new FileReader(combineFile);
                name.append(hashCode(IOUtils.toString(reader))).append(".css");
                reader.close();
                File targetFile = new File(Configuration.getCssLocalDir(), name.toString());
                if (!targetFile.exists()) {
                    targetFile.getParentFile().mkdirs();
                    FileUtils.copyFile(combineFile, targetFile);
                    newFiles.add(targetFile);
                    logger.info("Combined several css files into the single " + targetFile + " [size=" + targetFile.length() + "].");
                }
            }
        } finally {
            FileUtils.deleteQuietly(dir);
        }
        if (name.length() != 0) {
            Element element = (Element) linkCss.get(0);
            element.setAttribute("href", Configuration.getCssUrlPrefix() + name.toString());
            return element;
        } else {
            return null;
        }
    }
