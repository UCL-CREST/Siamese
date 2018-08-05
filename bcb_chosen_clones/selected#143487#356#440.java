    public static long writePropertiesInOpenXMLDocument(String ext, InputStream in, OutputStreamProvider outProvider, Map<String, String> properties) {
        in = new BufferedInputStream(in);
        try {
            File tempPptx = null;
            POIXMLDocument doc;
            if (ext.toLowerCase().equals("docx")) {
                doc = new XWPFDocument(in);
            } else if (ext.toLowerCase().equals("xlsx")) {
                doc = new XSSFWorkbook(in);
            } else if (ext.toLowerCase().equals("pptx")) {
                tempPptx = File.createTempFile("temp", "pptx");
                OutputStream tempPptxOut = new FileOutputStream(tempPptx);
                tempPptxOut = new BufferedOutputStream(tempPptxOut);
                IOUtils.copy(in, tempPptxOut);
                tempPptxOut.close();
                doc = new XSLFSlideShow(tempPptx.getAbsolutePath());
            } else {
                throw new IllegalArgumentException("Writing properties for a " + ext + " file is not supported");
            }
            for (Map.Entry<String, String> property : properties.entrySet()) {
                CoreProperties coreProperties = doc.getProperties().getCoreProperties();
                if (property.getKey().equals(Metadata.TITLE)) {
                    coreProperties.setTitle(property.getValue());
                } else if (property.getKey().equals(Metadata.AUTHOR)) {
                    coreProperties.setCreator(property.getValue());
                } else if (property.getKey().equals(Metadata.KEYWORDS)) {
                    coreProperties.getUnderlyingProperties().setKeywordsProperty(property.getValue());
                } else if (property.getKey().equals(Metadata.COMMENTS)) {
                    coreProperties.setDescription(property.getValue());
                } else if (property.getKey().equals(Metadata.SUBJECT)) {
                    coreProperties.setSubjectProperty(property.getValue());
                } else if (property.getKey().equals(Metadata.COMPANY)) {
                    doc.getProperties().getExtendedProperties().getUnderlyingProperties().setCompany(property.getValue());
                } else {
                    org.apache.poi.POIXMLProperties.CustomProperties customProperties = doc.getProperties().getCustomProperties();
                    if (customProperties.contains(property.getKey())) {
                        int index = 0;
                        for (CTProperty prop : customProperties.getUnderlyingProperties().getPropertyArray()) {
                            if (prop.getName().equals(property.getKey())) {
                                customProperties.getUnderlyingProperties().removeProperty(index);
                                break;
                            }
                            index++;
                        }
                    }
                    customProperties.addProperty(property.getKey(), property.getValue());
                }
            }
            in.close();
            File tempOpenXMLDocumentFile = File.createTempFile("temp", "tmp");
            OutputStream tempOpenXMLDocumentOut = new FileOutputStream(tempOpenXMLDocumentFile);
            tempOpenXMLDocumentOut = new BufferedOutputStream(tempOpenXMLDocumentOut);
            doc.write(tempOpenXMLDocumentOut);
            tempOpenXMLDocumentOut.close();
            long length = tempOpenXMLDocumentFile.length();
            InputStream tempOpenXMLDocumentIn = new FileInputStream(tempOpenXMLDocumentFile);
            tempOpenXMLDocumentIn = new BufferedInputStream(tempOpenXMLDocumentIn);
            OutputStream out = null;
            try {
                out = outProvider.getOutputStream();
                out = new BufferedOutputStream(out);
                IOUtils.copy(tempOpenXMLDocumentIn, out);
                out.flush();
            } finally {
                IOUtils.closeQuietly(out);
            }
            if (!FileUtils.deleteQuietly(tempOpenXMLDocumentFile)) {
                tempOpenXMLDocumentFile.deleteOnExit();
            }
            if (tempPptx != null && !FileUtils.deleteQuietly(tempPptx)) {
                tempPptx.deleteOnExit();
            }
            return length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        } catch (OpenXML4JException e) {
            throw new RuntimeException(e);
        } catch (XmlException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
