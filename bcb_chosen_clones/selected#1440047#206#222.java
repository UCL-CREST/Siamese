    public void sendFile(File file, String filename, String contentType) throws SearchLibException {
        response.setContentType(contentType);
        response.addHeader("Content-Disposition", "attachment; filename=" + filename);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            ServletOutputStream outputStream = getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new SearchLibException(e);
        } catch (IOException e) {
            throw new SearchLibException(e);
        } finally {
            if (inputStream != null) IOUtils.closeQuietly(inputStream);
        }
    }
