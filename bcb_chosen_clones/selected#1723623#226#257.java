    public void outputRights(OutputStream outputStream, DataResourceAuditor resultsOutputter, String rightsFileName, Locale locale, String hostUrl) throws IOException {
        if (outputStream instanceof ZipOutputStream) {
            ((ZipOutputStream) outputStream).putNextEntry(new ZipEntry(rightsFileName));
        }
        Map<String, String> dataResources = resultsOutputter.getDataResources();
        StringBuffer sb = new StringBuffer();
        sb.append(messageSource.getMessage("rights.introduction", null, locale));
        try {
            for (String dataResourceId : dataResources.keySet()) {
                DataResourceDTO dataResourceDTO = dataResourceManager.getDataResourceFor(dataResourceId);
                Object[] paramsEntry = new Object[2];
                paramsEntry[0] = dataResourceDTO.getName();
                paramsEntry[1] = hostUrl + datasetBaseUrl + dataResourceDTO.getKey();
                sb.append(messageSource.getMessage("rights.entry", paramsEntry, locale));
                sb.append('\n');
                Object[] paramsRights = new Object[1];
                if (dataResourceDTO.getRights() != null && !dataResourceDTO.getRights().equals("")) {
                    paramsRights[0] = dataResourceDTO.getRights();
                } else {
                    paramsRights[0] = "Not supplied";
                }
                sb.append(messageSource.getMessage("rights.supplied", paramsRights, locale));
                sb.append("\n\n");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        outputStream.write(sb.toString().getBytes());
        if (outputStream instanceof ZipOutputStream) {
            ((ZipOutputStream) outputStream).closeEntry();
        }
    }
