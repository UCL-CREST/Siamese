    public static ExecResult exec(final String command) throws ExtException {
        try {
            logger.debug("Running [" + command + "]...");
            final Process p = Runtime.getRuntime().exec(command);
            final StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream());
            final StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream());
            errorGobbler.start();
            outputGobbler.start();
            final int exitCode = p.waitFor();
            logger.debug("  command terminated with exit code " + exitCode);
            return new ExecResult(outputGobbler.getBuffer(), errorGobbler.getBuffer(), exitCode);
        } catch (Throwable t) {
            logger.debug(t);
            throw new ExtException(I18N.get("ERROR_LINUX_EXEC_FAILED", "Error while running command '{0}'.", new String[] { command }), t);
        }
    }
