    private void processHelpFile() {
        InputStream in = null;
        if (line.hasOption("helpfile")) {
            OutputStream out = null;
            try {
                String filename = line.getOptionValue("helpfile");
                in = new FileInputStream(filename);
                filename = filename.replace('\\', '/');
                filename = filename.substring(filename.lastIndexOf('/') + 1);
                File outFile = new File(outputDir, filename);
                if (LOG.isInfoEnabled()) {
                    LOG.info("Processing generated file " + outFile.getAbsolutePath());
                }
                out = new FileOutputStream(outFile);
                baseProperties.setProperty("helpfile", filename);
                IOUtils.copy(in, out);
            } catch (FileNotFoundException e) {
                LOG.error(e.getMessage(), e);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }
            return;
        }
        Properties props = new Properties(baseProperties);
        ClassLoader cl = this.getClass().getClassLoader();
        Document doc = null;
        try {
            in = cl.getResourceAsStream(RESOURCE_PKG + "/help-doc.xml");
            doc = XmlUtils.parse(in);
        } catch (XmlException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        transformResource(doc, "help-doc.xsl", props, "help-doc.html");
        baseProperties.setProperty("helpfile", "help-doc.html");
    }
