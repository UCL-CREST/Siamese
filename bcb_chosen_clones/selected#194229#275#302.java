    public void resumereceive(HttpServletRequest req, HttpServletResponse resp, SessionCommand command) {
        setHeader(resp);
        try {
            logger.debug("ResRec: Resume a 'receive' session with session id " + command.getSession() + " this client already received " + command.getLen() + " bytes");
            File tempFile = new File(this.getSyncWorkDirectory(req), command.getSession() + ".smodif");
            if (!tempFile.exists()) {
                logger.debug("ResRec: the file doesn't exist, so we created it by serializing the entities");
                try {
                    OutputStream fos = new FileOutputStream(tempFile);
                    syncServer.getServerModifications(command.getSession(), fos);
                    fos.close();
                } catch (ImogSerializationException mse) {
                    logger.error(mse.getMessage(), mse);
                }
            }
            InputStream fis = new FileInputStream(tempFile);
            fis.skip(command.getLen());
            resp.setContentLength(fis.available());
            while (fis.available() > 0) {
                resp.getOutputStream().write(fis.read());
            }
            resp.getOutputStream().flush();
            resp.flushBuffer();
            fis.close();
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }
