    @Override
    public RServiceResponse execute(final NexusServiceRequest inData) throws NexusServiceException {
        final RServiceRequest data = (RServiceRequest) inData;
        final RServiceResponse retval = new RServiceResponse();
        final StringBuilder result = new StringBuilder("R service call results:\n");
        RSession session;
        RConnection connection = null;
        try {
            result.append("Session Attachment: \n");
            final byte[] sessionBytes = data.getSession();
            if (sessionBytes != null && sessionBytes.length > 0) {
                session = RUtils.getInstance().bytesToSession(sessionBytes);
                result.append("  attaching to " + session + "\n");
                connection = session.attach();
            } else {
                result.append("  creating new session\n");
                connection = new RConnection(data.getServerAddress());
            }
            result.append("Input Parameters: \n");
            for (String attributeName : data.getInputVariables().keySet()) {
                final Object parameter = data.getInputVariables().get(attributeName);
                if (parameter instanceof URI) {
                    final FileObject file = VFS.getManager().resolveFile(((URI) parameter).toString());
                    final RFileOutputStream ros = connection.createFile(file.getName().getBaseName());
                    IOUtils.copy(file.getContent().getInputStream(), ros);
                    connection.assign(attributeName, file.getName().getBaseName());
                } else {
                    connection.assign(attributeName, RUtils.getInstance().convertToREXP(parameter));
                }
                result.append("  " + parameter.getClass().getSimpleName() + " " + attributeName + "=" + parameter + "\n");
            }
            final REXP rExpression = connection.eval(RUtils.getInstance().wrapCode(data.getCode().replace('\r', '\n')));
            result.append("Execution results:\n" + rExpression.asString() + "\n");
            if (rExpression.isNull() || rExpression.asString().startsWith("Error")) {
                retval.setErr(rExpression.asString());
                throw new NexusServiceException("R error: " + rExpression.asString());
            }
            result.append("Output Parameters:\n");
            final String[] rVariables = connection.eval("ls();").asStrings();
            for (String varname : rVariables) {
                final String[] rVariable = connection.eval("class(" + varname + ")").asStrings();
                if (rVariable.length == 2 && "file".equals(rVariable[0]) && "connection".equals(rVariable[1])) {
                    final String rFileName = connection.eval("showConnections(TRUE)[" + varname + "]").asString();
                    result.append("  R File ").append(varname).append('=').append(rFileName).append('\n');
                    final RFileInputStream rInputStream = connection.openFile(rFileName);
                    final File file = File.createTempFile("nexus-" + data.getRequestId(), ".dat");
                    IOUtils.copy(rInputStream, new FileOutputStream(file));
                    retval.getOutputVariables().put(varname, file.getCanonicalFile().toURI());
                } else {
                    final Object varvalue = RUtils.getInstance().convertREXP(connection.eval(varname));
                    retval.getOutputVariables().put(varname, varvalue);
                    final String printValue = varvalue == null ? "null" : varvalue.getClass().isArray() ? Arrays.asList(varvalue).toString() : varvalue.toString();
                    result.append("  ").append(varvalue == null ? "" : varvalue.getClass().getSimpleName()).append(' ').append(varname).append('=').append(printValue).append('\n');
                }
            }
        } catch (ClassNotFoundException cnfe) {
            retval.setErr(cnfe.getMessage());
            LOGGER.error("Rserve Exception", cnfe);
        } catch (RserveException rse) {
            retval.setErr(rse.getMessage());
            LOGGER.error("Rserve Exception", rse);
        } catch (REXPMismatchException rme) {
            retval.setErr(rme.getMessage());
            LOGGER.error("REXP Mismatch Exception", rme);
        } catch (IOException rme) {
            retval.setErr(rme.getMessage());
            LOGGER.error("IO Exception copying file ", rme);
        } finally {
            result.append("Session Detachment:\n");
            if (connection != null) {
                RSession outSession;
                if (retval.isKeepSession()) {
                    try {
                        outSession = connection.detach();
                    } catch (RserveException e) {
                        LOGGER.debug("Error detaching R session", e);
                        outSession = null;
                    }
                } else {
                    outSession = null;
                }
                final boolean close = outSession == null;
                if (!close) {
                    retval.setSession(RUtils.getInstance().sessionToBytes(outSession));
                    result.append("  suspended session for later use\n");
                }
                connection.close();
                retval.setSession(null);
                result.append("  session closed.\n");
            }
        }
        retval.setOut(result.toString());
        return retval;
    }
