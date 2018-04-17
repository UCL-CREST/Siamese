    public static boolean writeFile(HttpServletResponse resp, File reqFile) {
        boolean retVal = false;
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(reqFile));
            IOUtils.copy(in, resp.getOutputStream());
            logger.debug("File successful written to servlet response: " + reqFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            logger.error("Resource not found: " + reqFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error(String.format("Error while rendering [%s]: %s", reqFile.getAbsolutePath(), e.getMessage()), e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return retVal;
    }
