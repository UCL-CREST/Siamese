    public static boolean saveMap(LWMap map, boolean saveAs, boolean export) {
        Log.info("saveMap: " + map);
        GUI.activateWaitCursor();
        if (map == null) return false;
        File file = map.getFile();
        int response = -1;
        if (map.getSaveFileModelVersion() == 0) {
            final Object[] defaultOrderButtons = { VueResources.getString("saveaction.saveacopy"), VueResources.getString("saveaction.save") };
            Object[] messageObject = { map.getLabel() };
            response = VueUtil.option(VUE.getDialogParent(), VueResources.getFormatMessage(messageObject, "dialog.saveaction.message"), VueResources.getFormatMessage(messageObject, "dialog.saveaction.title"), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, defaultOrderButtons, VueResources.getString("saveaction.saveacopy"));
        }
        if (response == 0) {
            saveAs = true;
        }
        if ((saveAs || file == null) && !export) {
            file = ActionUtil.selectFile("Save Map", null);
        } else if (export) {
            file = ActionUtil.selectFile("Export Map", "export");
        }
        if (file == null) {
            try {
                return false;
            } finally {
                GUI.clearWaitCursor();
            }
        }
        try {
            Log.info("saveMap: target[" + file + "]");
            final String name = file.getName().toLowerCase();
            if (name.endsWith(".rli.xml")) {
                new IMSResourceList().convert(map, file);
            } else if (name.endsWith(".xml") || name.endsWith(".vue")) {
                ActionUtil.marshallMap(file, map);
            } else if (name.endsWith(".jpeg") || name.endsWith(".jpg")) ImageConversion.createActiveMapJpeg(file, VueResources.getDouble("imageExportFactor")); else if (name.endsWith(".png")) ImageConversion.createActiveMapPng(file, VueResources.getDouble("imageExportFactor")); else if (name.endsWith(".svg")) SVGConversion.createSVG(file); else if (name.endsWith(".pdf")) {
                PresentationNotes.createMapAsPDF(file);
            } else if (name.endsWith(".zip")) {
                Vector resourceVector = new Vector();
                Iterator i = map.getAllDescendents(LWComponent.ChildKind.PROPER).iterator();
                while (i.hasNext()) {
                    LWComponent component = (LWComponent) i.next();
                    System.out.println("Component:" + component + " has resource:" + component.hasResource());
                    if (component.hasResource() && (component.getResource() instanceof URLResource)) {
                        URLResource resource = (URLResource) component.getResource();
                        try {
                            if (resource.isLocalFile()) {
                                String spec = resource.getSpec();
                                System.out.println(resource.getSpec());
                                Vector row = new Vector();
                                row.add(new Boolean(true));
                                row.add(resource);
                                row.add(new Long(file.length()));
                                row.add("Ready");
                                resourceVector.add(row);
                            }
                        } catch (Exception ex) {
                            System.out.println("Publisher.setLocalResourceVector: Resource " + resource.getSpec() + ex);
                            ex.printStackTrace();
                        }
                    }
                }
                File savedCMap = PublishUtil.createZip(map, resourceVector);
                InputStream istream = new BufferedInputStream(new FileInputStream(savedCMap));
                OutputStream ostream = new BufferedOutputStream(new FileOutputStream(file));
                int fileLength = (int) savedCMap.length();
                byte bytes[] = new byte[fileLength];
                try {
                    while (istream.read(bytes, 0, fileLength) != -1) ostream.write(bytes, 0, fileLength);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    istream.close();
                    ostream.close();
                }
            } else if (name.endsWith(".html")) {
                HtmlOutputDialog hod = new HtmlOutputDialog();
                hod.setVisible(true);
                if (hod.getReturnVal() > 0) new ImageMap().createImageMap(file, hod.getScale(), hod.getFormat());
            } else if (name.endsWith(".rdf")) {
                edu.tufts.vue.rdf.RDFIndex index = new edu.tufts.vue.rdf.RDFIndex();
                String selectionType = VueResources.getString("rdf.export.selection");
                if (selectionType.equals("ALL")) {
                    Iterator<LWMap> maps = VUE.getLeftTabbedPane().getAllMaps();
                    while (maps.hasNext()) {
                        index.index(maps.next());
                    }
                } else if (selectionType.equals("ACTIVE")) {
                    index.index(VUE.getActiveMap());
                } else {
                    index.index(VUE.getActiveMap());
                }
                FileWriter writer = new FileWriter(file);
                index.write(writer);
                writer.close();
            } else if (name.endsWith(VueUtil.VueArchiveExtension)) {
                Archive.writeArchive(map, file);
            } else {
                Log.warn("Unknown save type for filename extension: " + name);
                return false;
            }
            Log.debug("Save completed for " + file);
            if (!VUE.isApplet()) {
                VueFrame frame = (VueFrame) VUE.getMainWindow();
                String title = VUE.getName() + ": " + name;
                frame.setTitle(title);
            }
            if (name.endsWith(".vue")) {
                RecentlyOpenedFilesManager rofm = RecentlyOpenedFilesManager.getInstance();
                rofm.updateRecentlyOpenedFiles(file.getAbsolutePath());
            }
            return true;
        } catch (Throwable t) {
            Log.error("Exception attempting to save file " + file + ": " + t);
            Throwable e = t;
            if (t.getCause() != null) e = t.getCause();
            if (e instanceof java.io.FileNotFoundException) {
                Log.error("Save Failed: " + e);
            } else {
                Log.error("Save failed for \"" + file + "\"; ", e);
            }
            if (e != t) Log.error("Exception attempting to save file " + file + ": " + e);
            VueUtil.alert(String.format(Locale.getDefault(), VueResources.getString("saveaction.savemap.error") + "\"%s\";\n" + VueResources.getString("saveaction.targetfiel") + "\n\n" + VueResources.getString("saveaction.problem"), map.getLabel(), file, Util.formatLines(e.toString(), 80)), "Problem Saving Map");
        } finally {
            GUI.invokeAfterAWT(new Runnable() {

                public void run() {
                    GUI.clearWaitCursor();
                }
            });
        }
        return false;
    }
