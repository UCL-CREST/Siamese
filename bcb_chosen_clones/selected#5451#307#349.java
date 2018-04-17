    private void processStylesheetFile() {
        InputStream in = null;
        OutputStream out = null;
        try {
            String filename;
            if (line.hasOption("stylesheetfile")) {
                filename = line.getOptionValue("stylesheetfile");
                in = new FileInputStream(filename);
                filename = filename.replace('\\', '/');
                filename = filename.substring(filename.lastIndexOf('/') + 1);
            } else {
                ClassLoader cl = this.getClass().getClassLoader();
                filename = "stylesheet.css";
                in = cl.getResourceAsStream(RESOURCE_PKG + "/stylesheet.css");
            }
            baseProperties.setProperty("stylesheetfilename", filename);
            File outFile = new File(outputDir, filename);
            if (LOG.isInfoEnabled()) {
                LOG.info("Processing generated file " + outFile.getAbsolutePath());
            }
            out = new FileOutputStream(outFile);
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
    }
