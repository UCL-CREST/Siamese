    private void startUpload(ProgressListener progressListener) {
        final boolean stripComments = excludeDescription.isSelected();
        final boolean stripFiles = excludeFiles.isSelected();
        final boolean stripAtts = excludeAttributes.isSelected();
        final boolean stripAll = onlyStructureButton.isSelected();
        final Visitor<Operator> stripParametersVisitor = new Visitor<Operator>() {

            @Override
            public void visit(Operator op) {
                if (stripComments) {
                    log("Removing comment");
                    op.setUserDescription(null);
                }
                Parameters params = op.getParameters();
                for (ParameterType type : params.getParameterTypes()) {
                    if (stripAll || (type instanceof ParameterTypePassword) || (stripFiles && ((type instanceof ParameterTypeFile) || (type instanceof ParameterTypeDirectory) || (type instanceof ParameterTypeRepositoryLocation))) || (stripAtts && ((type instanceof ParameterTypeAttributes) || (type instanceof ParameterTypeAttributes)))) {
                        log("Removing: " + type.getKey());
                        params.setParameter(type.getKey(), "--STRIPPED--");
                    }
                }
            }
        };
        final AtomicInteger count = new AtomicInteger();
        final ZipOutputStream zipOut;
        try {
            zipOut = new ZipOutputStream(new FileOutputStream(new File("/home/simon/test.zip")));
        } catch (FileNotFoundException e1) {
            SwingTools.showSimpleErrorMessage("community.error_uploading_process_bundle", e1, e1.toString());
            return;
        }
        RepositoryVisitor<ProcessEntry> visitor = new RepositoryVisitor<ProcessEntry>() {

            @Override
            public boolean visit(ProcessEntry entry) {
                log("Visiting: " + entry.getLocation());
                try {
                    Process process = new Process(entry.retrieveXML());
                    process.getRootOperator().walk(stripParametersVisitor);
                    ZipEntry zipEntry = new ZipEntry(count.incrementAndGet() + ".xml");
                    zipOut.putNextEntry(zipEntry);
                    zipOut.write(process.getRootOperator().getXML(false).getBytes(XMLImporter.PROCESS_FILE_CHARSET));
                    zipOut.closeEntry();
                } catch (Exception e) {
                    SwingTools.showSimpleErrorMessage("community.error_uploading_process_bundle", e, e.toString());
                    return false;
                }
                return true;
            }
        };
        try {
            for (TreePath path : repositoryTree.getSelectionPaths()) {
                Entry entry = (Entry) path.getLastPathComponent();
                RepositoryManager.getInstance(null).walk(entry, visitor, ProcessEntry.class);
            }
        } catch (RepositoryException e) {
            SwingTools.showSimpleErrorMessage("community.error_uploading_process_bundle", e, e.toString());
        } finally {
            try {
                zipOut.close();
            } catch (IOException e) {
                SwingTools.showSimpleErrorMessage("community.error_uploading_process_bundle", e, e.toString());
            }
        }
        super.ok();
    }
