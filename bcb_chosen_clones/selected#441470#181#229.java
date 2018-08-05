    public void doExportQuestions() throws Exception {
        logger.debug(">>> Export questions...");
        int currentRowsOnPage = getRowsOnPage();
        int currentPage = getCurrentPage();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        ZipOutputStream zipStream = new ZipOutputStream(result);
        try {
            setRowsOnPage(EXPORT_ROWS_ON_PAGE);
            for (int i = 1; i <= getPageQuantity(); i++) {
                setCurrentPage(i);
                doExactPage();
                for (Object row : formTable.getRows()) {
                    Question question = (Question) row;
                    zipStream.putNextEntry(new ZipEntry(String.valueOf(question.getId()) + XML_EXTENSION));
                    questionService.exportQuestion(question.getId(), zipStream);
                    List<String> images = getImagesFromText(question.getDefinition().getDescription());
                    for (String image : images) {
                        logger.debug("  found image reference : #0", image);
                        FileInputStream fi = null;
                        try {
                            fi = new FileInputStream(ImageResource.getResourceDirectory() + File.separator + image);
                            zipStream.putNextEntry(new ZipEntry(image));
                            byte[] buf = new byte[100];
                            int size;
                            while ((size = fi.read(buf)) > 0) {
                                zipStream.write(buf, 0, size);
                            }
                        } finally {
                            closeStream(fi);
                        }
                    }
                }
            }
            zipStream.close();
            byte[] data = result.toByteArray();
            Contexts.getSessionContext().set(DownloadResource.SESSION_KEY, data);
            Contexts.getSessionContext().set(DownloadResource.FILENAME_KEY, EXPORT_DEFAULT_FILENAME);
        } catch (IOException e) {
            throw new Exception("error during exporting to xml", e);
        } catch (JAXBException e) {
            throw new Exception("error during exporting to xml", e);
        } finally {
            closeStream(result);
            closeStream(zipStream);
            setRowsOnPage(currentRowsOnPage);
            setCurrentPage(currentPage);
        }
        logger.debug("<<< Export questions...Ok");
    }
