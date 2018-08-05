    public void reqservmodif(HttpServletRequest req, HttpServletResponse resp, SessionCommand command) {
        setHeader(resp);
        try {
            logger.debug("SeMo: Requesting server modification for session " + command.getSession());
            File tempFile = new File(getSyncWorkDirectory(req), command.getSession() + ".smodif");
            OutputStream fos = new FileOutputStream(tempFile);
            syncServer.getServerModifications(command.getSession(), fos);
            InputStream fis = new FileInputStream(tempFile);
            resp.setContentLength(fis.available());
            while (fis.available() > 0) {
                resp.getOutputStream().write(fis.read());
            }
            resp.getOutputStream().flush();
            resp.flushBuffer();
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        } catch (ImogSerializationException ex) {
            logger.error(ex.getMessage());
        }
    }
