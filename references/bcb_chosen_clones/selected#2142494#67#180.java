    protected void setupService(MessageContext msgContext) throws Exception {
        String realpath = msgContext.getStrProp(Constants.MC_REALPATH);
        String extension = (String) getOption(OPTION_JWS_FILE_EXTENSION);
        if (extension == null) extension = DEFAULT_JWS_FILE_EXTENSION;
        if ((realpath != null) && (realpath.endsWith(extension))) {
            String jwsFile = realpath;
            String rel = msgContext.getStrProp(Constants.MC_RELATIVE_PATH);
            File f2 = new File(jwsFile);
            if (!f2.exists()) {
                throw new FileNotFoundException(rel);
            }
            if (rel.charAt(0) == '/') {
                rel = rel.substring(1);
            }
            int lastSlash = rel.lastIndexOf('/');
            String dir = null;
            if (lastSlash > 0) {
                dir = rel.substring(0, lastSlash);
            }
            String file = rel.substring(lastSlash + 1);
            String outdir = msgContext.getStrProp(Constants.MC_JWS_CLASSDIR);
            if (outdir == null) outdir = ".";
            if (dir != null) {
                outdir = outdir + File.separator + dir;
            }
            File outDirectory = new File(outdir);
            if (!outDirectory.exists()) {
                outDirectory.mkdirs();
            }
            if (log.isDebugEnabled()) log.debug("jwsFile: " + jwsFile);
            String jFile = outdir + File.separator + file.substring(0, file.length() - extension.length() + 1) + "java";
            String cFile = outdir + File.separator + file.substring(0, file.length() - extension.length() + 1) + "class";
            if (log.isDebugEnabled()) {
                log.debug("jFile: " + jFile);
                log.debug("cFile: " + cFile);
                log.debug("outdir: " + outdir);
            }
            File f1 = new File(cFile);
            String clsName = null;
            if (clsName == null) clsName = f2.getName();
            if (clsName != null && clsName.charAt(0) == '/') clsName = clsName.substring(1);
            clsName = clsName.substring(0, clsName.length() - extension.length());
            clsName = clsName.replace('/', '.');
            if (log.isDebugEnabled()) log.debug("ClsName: " + clsName);
            if (!f1.exists() || f2.lastModified() > f1.lastModified()) {
                log.debug(Messages.getMessage("compiling00", jwsFile));
                log.debug(Messages.getMessage("copy00", jwsFile, jFile));
                FileReader fr = new FileReader(jwsFile);
                FileWriter fw = new FileWriter(jFile);
                char[] buf = new char[4096];
                int rc;
                while ((rc = fr.read(buf, 0, 4095)) >= 0) fw.write(buf, 0, rc);
                fw.close();
                fr.close();
                log.debug("javac " + jFile);
                Compiler compiler = CompilerFactory.getCompiler();
                compiler.setClasspath(ClasspathUtils.getDefaultClasspath(msgContext));
                compiler.setDestination(outdir);
                compiler.addFile(jFile);
                boolean result = compiler.compile();
                (new File(jFile)).delete();
                if (!result) {
                    (new File(cFile)).delete();
                    Document doc = XMLUtils.newDocument();
                    Element root = doc.createElementNS("", "Errors");
                    StringBuffer message = new StringBuffer("Error compiling ");
                    message.append(jFile);
                    message.append(":\n");
                    List errors = compiler.getErrors();
                    int count = errors.size();
                    for (int i = 0; i < count; i++) {
                        CompilerError error = (CompilerError) errors.get(i);
                        if (i > 0) message.append("\n");
                        message.append("Line ");
                        message.append(error.getStartLine());
                        message.append(", column ");
                        message.append(error.getStartColumn());
                        message.append(": ");
                        message.append(error.getMessage());
                    }
                    root.appendChild(doc.createTextNode(message.toString()));
                    throw new AxisFault("Server.compileError", Messages.getMessage("badCompile00", jFile), null, new Element[] { root });
                }
                ClassUtils.removeClassLoader(clsName);
                soapServices.remove(clsName);
            }
            ClassLoader cl = ClassUtils.getClassLoader(clsName);
            if (cl == null) {
                cl = new JWSClassLoader(clsName, msgContext.getClassLoader(), cFile);
            }
            msgContext.setClassLoader(cl);
            SOAPService rpc = (SOAPService) soapServices.get(clsName);
            if (rpc == null) {
                rpc = new SOAPService(new RPCProvider());
                rpc.setName(clsName);
                rpc.setOption(RPCProvider.OPTION_CLASSNAME, clsName);
                rpc.setEngine(msgContext.getAxisEngine());
                String allowed = (String) getOption(RPCProvider.OPTION_ALLOWEDMETHODS);
                if (allowed == null) allowed = "*";
                rpc.setOption(RPCProvider.OPTION_ALLOWEDMETHODS, allowed);
                String scope = (String) getOption(RPCProvider.OPTION_SCOPE);
                if (scope == null) scope = Scope.DEFAULT.getName();
                rpc.setOption(RPCProvider.OPTION_SCOPE, scope);
                rpc.getInitializedServiceDesc(msgContext);
                soapServices.put(clsName, rpc);
            }
            rpc.setEngine(msgContext.getAxisEngine());
            rpc.init();
            msgContext.setService(rpc);
        }
        if (log.isDebugEnabled()) {
            log.debug("Exit: JWSHandler::invoke");
        }
    }
