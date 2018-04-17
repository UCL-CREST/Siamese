    public void execute(File tsvFile, File xmlFile) {
        BufferedReader reader = null;
        Writer writer = null;
        Boolean isFileSuccessfullyConverted = Boolean.TRUE;
        TableConfiguration tableConfig = null;
        try {
            xmlFile.getParentFile().mkdirs();
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(tsvFile), INPUT_ENCODING));
            writer = new OutputStreamWriter(new FileOutputStream(xmlFile), OUTPUT_ENCODING);
            tableConfig = Tsv2DocbookConverter.convert2(tableConfigManager, idScanner.extractIdentification(tsvFile), reader, writer, inputPolisher);
            isFileSuccessfullyConverted = (tableConfig != null);
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to create reader with UTF-8 encoding: " + e.getMessage(), e);
        } catch (FileNotFoundException fnfe) {
            logger.error("Failed to open tsv input file '" + tsvFile + "'. " + fnfe.getMessage());
        } catch (Throwable cause) {
            logger.error("Failed to convert input tsv file '" + tsvFile + "'.", cause);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    logger.warn("Unable to close input file.", ioe);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ioe) {
                    logger.warn("Unable to close output file.", ioe);
                }
            }
        }
        if (isFileSuccessfullyConverted) {
            String newOutputFileName = tableConfig.getFileName(idScanner.extractIdentification(tsvFile));
            if (newOutputFileName != null) {
                File newOutputFile = new File(xmlFile.getParentFile(), newOutputFileName);
                if (!xmlFile.renameTo(newOutputFile)) {
                    logger.warn("Unable to rename '" + xmlFile + "' to '" + newOutputFile + "'.");
                    logger.info("Created successfully '" + xmlFile + "'.");
                } else {
                    logger.info("Created successfully '" + newOutputFileName + "'.");
                }
            } else {
                logger.info("Created successfully '" + xmlFile + "'.");
            }
        } else {
            logger.warn("Unable to convert input tsv file '" + Tsv2DocBookApplication.trimPath(sourceDir, tsvFile) + "' to docbook.");
            if (xmlFile.exists() && !xmlFile.delete()) {
                logger.warn("Unable to remove (empty) output file '" + xmlFile + "', which was created as target for the docbook table.");
            }
        }
    }
