    protected File downloadUpdate(String resource) throws AgentException {
        RESTCall call = makeRESTCall(resource);
        call.invoke();
        File tmpFile;
        try {
            tmpFile = File.createTempFile("controller-update-", ".war", new File(tmpPath));
        } catch (IOException e) {
            throw new AgentException("Failed to create temporary file", e);
        }
        InputStream is;
        try {
            is = call.getInputStream();
        } catch (IOException e) {
            throw new AgentException("Failed to open input stream", e);
        }
        try {
            FileOutputStream os;
            try {
                os = new FileOutputStream(tmpFile);
            } catch (FileNotFoundException e) {
                throw new AgentException("Failed to open temporary file for writing", e);
            }
            boolean success = false;
            try {
                IOUtils.copy(is, os);
                success = true;
            } catch (IOException e) {
                throw new AgentException("Failed to download update", e);
            } finally {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    if (!success) throw new AgentException("Failed to flush to disk", e);
                }
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error("Failed to close input stream", e);
            }
            call.disconnect();
        }
        return tmpFile;
    }
