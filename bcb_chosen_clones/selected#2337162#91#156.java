    public ValidationReport validate(OriginalDeployUnitDescription unit) throws UnitValidationException {
        ValidationReport vr = new DefaultValidationReport();
        errorHandler = new SimpleErrorHandler(vr);
        vr.setFileUri(unit.getAbsolutePath());
        SAXParser parser;
        SAXReader reader = null;
        try {
            parser = factory.newSAXParser();
            reader = new SAXReader(parser.getXMLReader());
            reader.setValidation(false);
            reader.setErrorHandler(this.errorHandler);
        } catch (ParserConfigurationException e) {
            throw new UnitValidationException("The configuration of parser is illegal.", e);
        } catch (SAXException e) {
            String m = "Something is wrong when register schema";
            logger.error(m, e);
            throw new UnitValidationException(m, e);
        }
        ZipInputStream zipInputStream;
        InputStream tempInput = null;
        try {
            tempInput = new FileInputStream(unit.getAbsolutePath());
        } catch (FileNotFoundException e1) {
            String m = String.format("The file [%s] don't exist.", unit.getAbsolutePath());
            logger.error(m, e1);
            throw new UnitValidationException(m, e1);
        }
        zipInputStream = new ZipInputStream(tempInput);
        ZipEntry zipEntry = null;
        try {
            zipEntry = zipInputStream.getNextEntry();
            if (zipEntry == null) {
                String m = String.format("Error when get zipEntry. Maybe the [%s] is not zip file!", unit.getAbsolutePath());
                logger.error(m);
                throw new UnitValidationException(m);
            }
            while (zipEntry != null) {
                if (configFiles.contains(zipEntry.getName())) {
                    byte[] extra = new byte[(int) zipEntry.getSize()];
                    zipInputStream.read(extra);
                    File file = File.createTempFile("temp", "extra");
                    file.deleteOnExit();
                    logger.info("[TempFile:]" + file.getAbsoluteFile());
                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(extra);
                    FileOutputStream tempFileOutputStream = new FileOutputStream(file);
                    IOUtils.copy(byteInputStream, tempFileOutputStream);
                    tempFileOutputStream.flush();
                    IOUtils.closeQuietly(tempFileOutputStream);
                    InputStream inputStream = new FileInputStream(file);
                    reader.read(inputStream, unit.getAbsolutePath() + ":" + zipEntry.getName());
                    IOUtils.closeQuietly(inputStream);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            ValidationMessage vm = new XMLValidationMessage("IOError", 0, 0, unit.getUrl() + ":" + zipEntry.getName(), e);
            vr.addValidationMessage(vm);
        } catch (DocumentException e) {
            ValidationMessage vm = new XMLValidationMessage("Document Error.", 0, 0, unit.getUrl() + ":" + zipEntry.getName(), e);
            vr.addValidationMessage(vm);
        } finally {
            IOUtils.closeQuietly(tempInput);
            IOUtils.closeQuietly(zipInputStream);
        }
        return vr;
    }
