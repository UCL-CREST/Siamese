    public void searchEntity(HttpServletRequest req, HttpServletResponse resp, SearchCommand command) {
        setHeader(resp);
        logger.debug("Search: Looking for the entity with the id:" + command.getSearchedid());
        String login = command.getLogin();
        String password = command.getPassword();
        SynchronizableUser currentUser = userAccessControl.authenticate(login, password);
        if (currentUser != null) {
            try {
                File tempFile = File.createTempFile("medoo", "search");
                OutputStream fos = new FileOutputStream(tempFile);
                syncServer.searchEntity(currentUser, command.getSearchedid(), fos);
                InputStream fis = new FileInputStream(tempFile);
                resp.setContentLength(fis.available());
                while (fis.available() > 0) {
                    resp.getOutputStream().write(fis.read());
                }
                resp.getOutputStream().flush();
                resp.flushBuffer();
            } catch (IOException ioe) {
                logger.error(ioe.getMessage(), ioe);
            } catch (ImogSerializationException ex) {
                logger.error(ex.getMessage(), ex);
            }
        } else {
            try {
                OutputStream out = resp.getOutputStream();
                out.write("-ERROR-".getBytes());
                out.flush();
                out.close();
                logger.debug("Search: user " + login + " has not been authenticated");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
