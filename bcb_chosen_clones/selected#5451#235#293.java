    private void processBasicContent() {
        String[] packageNames = sourceCollector.getPackageNames();
        for (int i = 0; i < packageNames.length; i++) {
            XdcSource[] sources = sourceCollector.getXdcSources(packageNames[i]);
            File dir = new File(outputDir, packageNames[i]);
            dir.mkdirs();
            Set pkgDirs = new HashSet();
            for (int j = 0; j < sources.length; j++) {
                XdcSource source = sources[j];
                Properties patterns = source.getPatterns();
                if (patterns != null) {
                    tables.put("patterns", patterns);
                }
                pkgDirs.add(source.getFile().getParentFile());
                DialectHandler dialectHandler = source.getDialectHandler();
                Writer out = null;
                try {
                    String sourceFilePath = source.getFile().getAbsolutePath();
                    source.setProcessingProperties(baseProperties, j > 0 ? sources[j - 1].getFileName() : null, j < sources.length - 1 ? sources[j + 1].getFileName() : null);
                    String rootComment = XslUtils.transformToString(sourceFilePath, XSL_PKG + "/source-header.xsl", tables);
                    source.setRootComment(rootComment);
                    Document htmlDoc = XslUtils.transform(sourceFilePath, encoding, dialectHandler.getXslResourcePath(), tables);
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Processing source file " + sourceFilePath);
                    }
                    out = IOUtils.getWriter(new File(dir, source.getFile().getName() + ".html"), docencoding);
                    XmlUtils.printHtml(out, htmlDoc);
                    if (sourceProcessor != null) {
                        sourceProcessor.processSource(source, encoding, docencoding);
                    }
                    XdcSource.clearProcessingProperties(baseProperties);
                } catch (XmlException e) {
                    LOG.error(e.getMessage(), e);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            LOG.error(e.getMessage(), e);
                        }
                    }
                }
            }
            for (Iterator iter = pkgDirs.iterator(); iter.hasNext(); ) {
                File docFilesDir = new File((File) iter.next(), "xdc-doc-files");
                if (docFilesDir.exists() && docFilesDir.isDirectory()) {
                    File targetDir = new File(dir, "xdc-doc-files");
                    targetDir.mkdirs();
                    try {
                        IOUtils.copyTree(docFilesDir, targetDir);
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }
        }
    }
