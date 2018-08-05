        public void processFile(Report report, String outputType, Object reportDataSource) throws ReportingException, IOException {
            if (report == null || report.getData() == null) return;
            File file;
            String suffix = getSuffix(report.getFileName());
            try {
                file = File.createTempFile(getPrefix(report.getFileName()), suffix);
                file.deleteOnExit();
                FileUtils.writeByteArrayToFile(file, report.getData());
                if (".zip".equalsIgnoreCase(suffix)) {
                    String dir = System.getProperty("java.io.tmpdir") + "/" + getPrefix(report.getFileName());
                    ZipFileUtils.unzip(new ZipFile(file), dir);
                    File dirFile = new File(dir);
                    parameters.put(JRParameter.REPORT_FILE_RESOLVER, new ReportFileResolver(dirFile));
                    Iterator<File> iter = FileUtils.iterateFiles(dirFile, new String[] { "jrxml", "jasper" }, false);
                    while (iter.hasNext()) {
                        file = iter.next();
                        suffix = getSuffix(file.getName());
                        break;
                    }
                }
                if (".jasper".equals(suffix)) {
                    fileProcessor = new JasperReportFileProcessor();
                } else if (".jrxml".equals(suffix)) {
                    fileProcessor = new JasperReportXMLFileProcessor();
                } else {
                    throw new ReportingException("Process not yet implemented for file type " + suffix);
                }
                fileProcessor.setParameters(parameters);
                setReportDataSource(fileProcessor, reportDataSource);
                fileProcessor.processFile(file, outputType, report.getHasQuery());
                if (interactive && Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    File outputFile;
                    try {
                        outputFile = File.createTempFile(getPrefix(report.getFileName()), "." + outputType);
                        outputFile.deleteOnExit();
                        FileUtils.writeByteArrayToFile(outputFile, fileProcessor.getRawData());
                        desktop.open(outputFile);
                    } catch (IOException e) {
                        throw new ReportingException("No ha sido posible abrir el fichero del informe: " + report.getFileName(), e);
                    }
                }
            } catch (IOException e) {
                throw new ReportingException("No ha sido posible abrir el fichero del informe: " + report.getFileName(), e);
            }
        }
