    @SuppressWarnings({ "ResultOfMethodCallIgnored" })
    public static Node combineJs(URL base, List<Node> linkJs, List<File> newFiles) throws IOException {
        File dir = File.createTempFile("javascript", "" + System.currentTimeMillis());
        StringBuilder name = new StringBuilder();
        try {
            if (dir.delete() && dir.mkdirs()) {
                File minDir = new File(dir, "min");
                minDir.mkdir();
                File combineFile = new File(minDir, "script.js");
                File concatFile = new File(minDir, "concat.js");
                Writer combineWriter = new FileWriter(combineFile);
                Writer concatWriter = new FileWriter(concatFile);
                final List<Boolean> fails = new LinkedList<Boolean>();
                boolean first = true;
                for (Node link : linkJs) {
                    String path = ((Element) link).getAttribute("src");
                    URL url = new URL(buildUrl(base, path));
                    InputStream inputStream = url.openStream();
                    File jsFile = new File(dir, fileName(url));
                    FileOutputStream outputStream = new FileOutputStream(jsFile);
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                    inputStream.close();
                    if (!first) {
                        combineWriter.write("\n;\n");
                        concatWriter.write("\n;\n");
                    } else {
                        first = false;
                    }
                    if (Configuration.jsMinification()) {
                        Reader reader = new FileReader(jsFile);
                        try {
                            JavaScriptCompressor jsCompressor = new JavaScriptCompressor(reader, new ErrorReporter() {

                                @Override
                                public void warning(String s, String s1, int i, String s2, int i1) {
                                    fails.add(true);
                                }

                                @Override
                                public void error(String s, String s1, int i, String s2, int i1) {
                                    fails.add(true);
                                }

                                @Override
                                public EvaluatorException runtimeError(String s, String s1, int i, String s2, int i1) {
                                    fails.add(true);
                                    return null;
                                }
                            });
                            jsCompressor.compress(combineWriter, 0, false, false, true, true);
                        } catch (Exception e) {
                            fails.add(true);
                        }
                        reader.close();
                    }
                    Reader reader = new FileReader(jsFile);
                    IOUtils.copy(reader, concatWriter);
                    reader.close();
                    String fileName = jsFile.getName();
                    int pos = fileName.lastIndexOf('.');
                    if (pos >= 0) {
                        fileName = fileName.substring(0, pos);
                    }
                    name.append(fileName).append(",");
                }
                combineWriter.close();
                concatWriter.close();
                FileReader reader;
                if (fails.size() == 0 && Configuration.jsMinification()) {
                    reader = new FileReader(combineFile);
                } else {
                    reader = new FileReader(concatFile);
                }
                name.append(hashCode(IOUtils.toString(reader))).append(".js");
                reader.close();
                File targetFile = new File(Configuration.getJsLocalDir(), name.toString());
                if (!targetFile.exists()) {
                    targetFile.getParentFile().mkdirs();
                    if (fails.size() == 0 && Configuration.jsMinification()) {
                        FileUtils.copyFile(combineFile, targetFile);
                    } else {
                        FileUtils.copyFile(concatFile, targetFile);
                    }
                    newFiles.add(targetFile);
                    logger.info("Combined several js files into the single " + targetFile + " [size=" + targetFile.length() + "].");
                }
            }
        } finally {
            FileUtils.deleteQuietly(dir);
        }
        if (name.length() != 0) {
            Element element = (Element) linkJs.get(0);
            element.setAttribute("src", Configuration.getJsUrlPrefix() + name.toString());
            return element;
        } else {
            return null;
        }
    }
