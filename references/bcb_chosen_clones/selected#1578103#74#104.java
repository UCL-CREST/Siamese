    public void invoke() throws PreconditionException, PostconditionException {
        String namespace = null;
        if (args.getNamespacesArg() == null) {
            throw new PreconditionException("args.getNamespacesArg()==null");
        }
        if (args.getNamespacesArg().size() == 0) {
            throw new PreconditionException("args.getNamespacesArg().size()==0");
        }
        try {
            XMLString config = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            for (Iterator<String> it = args.getNamespacesArg().iterator(); it.hasNext(); ) {
                namespace = it.next();
                config = core.toXML(ConfigurationHandler.getInstance().loadConfiguration(namespace, core, core));
                zos.putNextEntry(new ZipEntry(namespace));
                zos.write(config.toString().getBytes(), 0, config.toString().getBytes().length);
                zos.closeEntry();
            }
            zos.close();
            args.setCarRet(baos.toByteArray());
            baos.close();
        } catch (IOException ex) {
            throw new CarProcessingError("Failed to build Car file: ", ex);
        } catch (UnknownNamespaceError e) {
            throw new PreconditionException("Configuration is not defined:" + namespace);
        }
        if (args.getCarRet() == null) {
            throw new PostconditionException("args.getCarRet()==null");
        }
    }
