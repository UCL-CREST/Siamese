    public void reqservmodif(HttpServletRequest req, HttpServletResponse resp, SessionCommand command) {
        try {
            System.err.println(req.getSession().getServletContext().getRealPath("WEB-INF/syncWork"));
            File tempFile = File.createTempFile("localmodif-", ".medoorequest");
            OutputStream fos = new FileOutputStream(tempFile);
            syncServer.getServerModifications(command.getSession(), fos);
            InputStream fis = new FileInputStream(tempFile);
            resp.setContentLength(fis.available());
            while (fis.available() > 0) {
                resp.getOutputStream().write(fis.read());
            }
            resp.getOutputStream().flush();
            resp.flushBuffer();
            tempFile.delete();
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        } catch (ImogSerializationException ex) {
            logger.error(ex.getMessage());
        }
    }
