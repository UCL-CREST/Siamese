    @Override
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String filename = ServletRequestUtils.getRequiredStringParameter(request, "id");
        final File file = new File(path, filename + ".html");
        logger.debug("Getting static content from: " + file.getPath());
        final InputStream is = getServletContext().getResourceAsStream(file.getPath());
        OutputStream out = null;
        if (is != null) {
            try {
                out = response.getOutputStream();
                IOUtils.copy(is, out);
            } catch (IOException ioex) {
                logger.error(ioex);
            } finally {
                is.close();
                if (out != null) {
                    out.close();
                }
            }
        }
        return null;
    }
