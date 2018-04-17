    public void run() {
        LOG.debug(this);
        String[] parts = createCmdArray(getCommand());
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(parts);
            if (isBlocking()) {
                process.waitFor();
                StringWriter out = new StringWriter();
                IOUtils.copy(process.getInputStream(), out);
                String stdout = out.toString().replaceFirst("\\s+$", "");
                if (StringUtils.isNotBlank(stdout)) {
                    LOG.info("Process stdout:\n" + stdout);
                }
                StringWriter err = new StringWriter();
                IOUtils.copy(process.getErrorStream(), err);
                String stderr = err.toString().replaceFirst("\\s+$", "");
                if (StringUtils.isNotBlank(stderr)) {
                    LOG.error("Process stderr:\n" + stderr);
                }
            }
        } catch (IOException ioe) {
            LOG.error(String.format("Could not exec [%s]", getCommand()), ioe);
        } catch (InterruptedException ie) {
            LOG.error(String.format("Interrupted [%s]", getCommand()), ie);
        }
    }
