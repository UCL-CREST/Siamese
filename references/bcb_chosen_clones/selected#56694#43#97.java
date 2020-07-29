    public boolean execute(PlugInContext context) throws Exception {
        WorkbenchContext workbenchContext = context.getWorkbenchContext();
        if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(workbenchContext.getLayerViewPanel())) {
            String zipFileName = fileChooser.getSelectedFile().getPath();
            Collection layerCollection = (Collection) workbenchContext.getLayerNamePanel().getLayerManager().getLayers();
            List filesToZip = new ArrayList();
            try {
                for (Iterator l = layerCollection.iterator(); l.hasNext(); ) {
                    Layer layer = (Layer) l.next();
                    if (layer.hasReadableDataSource()) {
                        DataSourceQuery dsq = layer.getDataSourceQuery();
                        String fname = "";
                        Object fnameObj = dsq.getDataSource().getProperties().get("File");
                        if (fnameObj != null) {
                            fname = fnameObj.toString();
                            if (new File(fname).exists()) {
                                filesToZip.add(fname);
                            }
                        }
                    }
                    if (filesToZip.size() > 0) {
                        byte[] buffer = new byte[18024];
                        try {
                            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
                            out.setLevel(Deflater.DEFAULT_COMPRESSION);
                            for (int i = 0; i < filesToZip.size(); i++) {
                                System.out.println(i);
                                FileInputStream in = new FileInputStream((String) filesToZip.get(i));
                                out.putNextEntry(new ZipEntry((String) filesToZip.get(i)));
                                int len;
                                while ((len = in.read(buffer)) > 0) {
                                    out.write(buffer, 0, len);
                                }
                                out.closeEntry();
                                in.close();
                            }
                            out.close();
                        } catch (IllegalArgumentException iae) {
                            iae.printStackTrace();
                        } catch (FileNotFoundException fnfe) {
                            fnfe.printStackTrace();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                context.getWorkbenchFrame().getOutputFrame().createNewDocument();
                context.getWorkbenchFrame().warnUser("Error: see output window");
                context.getWorkbenchFrame().getOutputFrame().addText("CreateBackupPlugIn Exception:" + e.toString());
                return false;
            }
        }
        return true;
    }
