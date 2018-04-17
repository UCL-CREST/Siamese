    public void publishContent(String contentFile, ViewManager fromViewManager) {
        if (!isConfigured()) {
            return;
        }
        try {
            boolean isBundle = ((contentFile == null) ? false : getIdv().getArgsManager().isBundleFile(contentFile));
            boolean isZidv = ((contentFile == null) ? false : getIdv().getArgsManager().isZidvFile(contentFile));
            doMakeContents();
            List myComps = new ArrayList(comps);
            JCheckBox addAssociationCbx = null;
            List topComps = new ArrayList();
            List myDataSources = new ArrayList();
            List myDataSourcesCbx = new ArrayList();
            List myDataSourcesIds = new ArrayList();
            List notMineDataSources = new ArrayList();
            List dataSources = getIdv().getDataSources();
            for (int i = 0; i < dataSources.size(); i++) {
                DataSource dataSource = (DataSource) dataSources.get(i);
                String ramaddaId = (String) dataSource.getProperty("ramadda.id");
                String ramaddaHost = (String) dataSource.getProperty("ramadda.host");
                if ((ramaddaId == null) || (ramaddaHost == null)) {
                    notMineDataSources.add(dataSource);
                    continue;
                }
                if (!Misc.equals(ramaddaHost, repositoryClient.getHostname())) {
                    notMineDataSources.add(dataSource);
                    continue;
                }
                myDataSources.add(dataSource);
                myDataSourcesCbx.add(new JCheckBox(dataSource.toString(), false));
                myDataSourcesIds.add(ramaddaId);
            }
            boolean isImage = false;
            if ((contentFile != null) && !isBundle) {
                topComps.add(GuiUtils.rLabel("File:"));
                JComponent extra;
                if (ImageUtils.isImage(contentFile)) {
                    isImage = true;
                } else {
                    extra = GuiUtils.filler(1, 1);
                }
                if (isImage) {
                    doBundleCbx.setText("Publish bundle and attach image");
                } else {
                    doBundleCbx.setText("Publish bundle and attach product");
                }
                doBundleCbx.setToolTipText("<html>Instead of publishing the product actually make and <br>publish a bundle and add the product as an attachment</html>");
                topComps.add(GuiUtils.left(GuiUtils.hbox(new JLabel(IOUtil.getFileTail(contentFile)), GuiUtils.filler(10, 5), doBundleCbx, doZidvCbx)));
                if (lastBundleId != null) {
                    addAssociationCbx = myAddAssociationCbx;
                    addAssociationCbx.setText("Add association with last bundle: " + IOUtil.getFileTail(lastBundleFile));
                    topComps.add(GuiUtils.filler());
                    topComps.add(addAssociationCbx);
                }
            }
            if (isZidv) {
                topComps.add(GuiUtils.filler());
                topComps.add(GuiUtils.left(GuiUtils.hbox(uploadZidvDataCbx, uploadZidvBundleCbx)));
            }
            int numTopComps = topComps.size();
            topComps.addAll(myComps);
            if (myDataSourcesCbx.size() > 0) {
                topComps.add(GuiUtils.rLabel("Make associations to:"));
                topComps.add(GuiUtils.left(GuiUtils.vbox(myDataSourcesCbx)));
            }
            GuiUtils.tmpInsets = GuiUtils.INSETS_5;
            double[] wts = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            wts[numTopComps / 2 + 1] = 0.2;
            wts[numTopComps / 2 + 3] = 1.0;
            JComponent contents = GuiUtils.doLayout(topComps, 2, GuiUtils.WT_NY, wts);
            if (fromViewManager == null) {
                List viewManagers = getIdv().getVMManager().getViewManagers();
                if (viewManagers.size() == 1) {
                    fromViewManager = (ViewManager) viewManagers.get(0);
                }
            }
            if (fromViewManager != null) {
                if ((fromViewManager instanceof MapViewManager)) {
                    MapViewManager mvm = (MapViewManager) fromViewManager;
                    NavigatedDisplay navDisplay = mvm.getNavigatedDisplay();
                    Rectangle2D.Double bbox = navDisplay.getLatLonBox(false, false);
                    if (bbox != null) {
                        southFld.setText(getIdv().getDisplayConventions().formatLatLon(bbox.getY()));
                        northFld.setText(getIdv().getDisplayConventions().formatLatLon((bbox.getY() + bbox.getHeight())));
                        westFld.setText(getIdv().getDisplayConventions().formatLatLon(bbox.getX()));
                        eastFld.setText(getIdv().getDisplayConventions().formatLatLon((bbox.getX() + bbox.getWidth())));
                    }
                }
                Animation anim = fromViewManager.getAnimation();
                if (anim != null) {
                    DateTime[] dttms = anim.getTimes();
                    if ((dttms != null) && (dttms.length > 0)) {
                        fromDateFld.setDate(Util.makeDate(dttms[0]));
                        toDateFld.setDate(Util.makeDate(dttms[dttms.length - 1]));
                    }
                }
            }
            if (contentFile != null) {
                nameFld.setText(IOUtil.stripExtension(IOUtil.getFileTail(contentFile)));
            } else {
                nameFld.setText("");
            }
            dialogOk = false;
            String parentId = null;
            while (true) {
                while (true) {
                    if (!GuiUtils.showOkCancelDialog(null, "Publish to RAMADDA", contents, null)) {
                        return;
                    }
                    parentId = repositoryClient.getSelectedGroup();
                    if (parentId == null) {
                        LogUtil.userMessage("You must select a parent folder");
                    } else {
                        break;
                    }
                }
                GuiUtils.ProgressDialog dialog = new GuiUtils.ProgressDialog("Publishing to RAMADDA");
                List<String> files = new ArrayList<String>();
                List<String> zipEntryNames = new ArrayList<String>();
                String bundleFile = null;
                if (isBundle) {
                    bundleFile = contentFile;
                    contentFile = null;
                    files.add(bundleFile);
                    zipEntryNames.add(IOUtil.getFileTail(bundleFile));
                } else if (doBundleCbx.isSelected()) {
                    String tmpFile = contentFile;
                    if (tmpFile == null) {
                        tmpFile = "publish.xidv";
                    }
                    bundleFile = getIdv().getObjectStore().getTmpFile(IOUtil.stripExtension(IOUtil.getFileTail(tmpFile)) + (doZidvCbx.isSelected() ? ".zidv" : ".xidv"));
                    getIdv().getPersistenceManager().doSave(bundleFile);
                    files.add(bundleFile);
                    zipEntryNames.add(IOUtil.getFileTail(bundleFile));
                    if (contentFile != null) {
                        files.add(contentFile);
                        zipEntryNames.add(IOUtil.getFileTail(contentFile));
                    }
                } else if (contentFile != null) {
                    files.add(contentFile);
                    zipEntryNames.add(IOUtil.getFileTail(contentFile));
                }
                String fromDate = repositoryClient.formatDate(fromDateFld.getDate());
                String toDate = repositoryClient.formatDate(toDateFld.getDate());
                int cnt = 0;
                Document doc = XmlUtil.makeDocument();
                Element root = XmlUtil.create(doc, TAG_ENTRIES);
                List tags = StringUtil.split(tagFld.getText().trim(), ",", true, true);
                String mainId = (cnt++) + "";
                String contentId = (cnt++) + "";
                String mainFile;
                if (bundleFile != null) {
                    mainFile = bundleFile;
                } else {
                    mainFile = contentFile;
                    contentFile = null;
                    contentId = mainId;
                }
                String zidvFile = (isZidv ? bundleFile : null);
                if (isZidv && !uploadZidvBundleCbx.isSelected()) {
                    bundleFile = null;
                    mainFile = null;
                }
                List attrs;
                Element node = null;
                if (mainFile != null) {
                    attrs = Misc.toList(new String[] { ATTR_ID, mainId, ATTR_FILE, IOUtil.getFileTail(mainFile), ATTR_PARENT, parentId, ATTR_TYPE, TYPE_FILE, ATTR_NAME, nameFld.getText().trim(), ATTR_DESCRIPTION, descFld.getText().trim(), ATTR_FROMDATE, fromDate, ATTR_TODATE, toDate });
                    checkAndAdd(attrs, ATTR_NORTH, northFld);
                    checkAndAdd(attrs, ATTR_SOUTH, southFld);
                    checkAndAdd(attrs, ATTR_EAST, eastFld);
                    checkAndAdd(attrs, ATTR_WEST, westFld);
                    node = XmlUtil.create(TAG_ENTRY, root, attrs);
                    repositoryClient.addTags(node, tags);
                    for (int i = 0; i < myDataSourcesCbx.size(); i++) {
                        if (((JCheckBox) myDataSourcesCbx.get(i)).isSelected()) {
                            String id = (String) myDataSourcesIds.get(i);
                            repositoryClient.addAssociation(root, id, mainId, "uses data");
                        }
                    }
                }
                if ((contentFile != null) && (node != null)) {
                    if (isImage && doThumbnailCbx.isSelected()) {
                        repositoryClient.addThumbnail(node, IOUtil.getFileTail(contentFile));
                    } else {
                        repositoryClient.addAttachment(node, IOUtil.getFileTail(contentFile));
                    }
                    if (false && isImage && doThumbnailCbx.isSelected()) {
                        Image image = ImageUtils.readImage(contentFile);
                        image = ImageUtils.resize(image, 75, -1);
                        String filename = "thumb_" + IOUtil.getFileTail(contentFile);
                        String tmpFile = getIdv().getObjectStore().getTmpFile(filename);
                        ImageUtils.writeImageToFile(image, tmpFile);
                        repositoryClient.addThumbnail(node, filename);
                        zipEntryNames.add(filename);
                        files.add(tmpFile);
                    }
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(bos);
                for (int i = 0; i < files.size(); i++) {
                    String file = files.get(i);
                    String name = zipEntryNames.get(i);
                    if (file == null) {
                        continue;
                    }
                    zos.putNextEntry(new ZipEntry(name));
                    byte[] bytes = IOUtil.readBytes(new FileInputStream(file));
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                }
                if ((zidvFile != null) && isZidv && uploadZidvDataCbx.isSelected()) {
                    ZipInputStream zin = new ZipInputStream(new FileInputStream(new File(zidvFile)));
                    ZipEntry ze;
                    SimpleDateFormat sdf = new SimpleDateFormat(DataSource.DATAPATH_DATE_FORMAT);
                    while ((ze = zin.getNextEntry()) != null) {
                        String entryName = ze.getName();
                        String dateString = StringUtil.findPattern(entryName, "(" + DataSource.DATAPATH_DATE_PATTERN + ")");
                        Date dttm = null;
                        if (dateString != null) {
                            dttm = sdf.parse(dateString);
                        }
                        if (getIdv().getArgsManager().isBundleFile(entryName)) {
                            continue;
                        }
                        dialog.setText("Adding " + entryName);
                        zos.putNextEntry(new ZipEntry(entryName));
                        byte[] bytes = IOUtil.readBytes(zin, null, false);
                        zos.write(bytes, 0, bytes.length);
                        zos.closeEntry();
                        String id = (cnt++) + "";
                        attrs = Misc.toList(new String[] { ATTR_ID, id, ATTR_FILE, entryName, ATTR_PARENT, parentId, ATTR_TYPE, TYPE_FILE, ATTR_NAME, entryName });
                        if (dttm != null) {
                            attrs.addAll(Misc.newList(ATTR_FROMDATE, repositoryClient.formatDate(dttm), ATTR_TODATE, repositoryClient.formatDate(dttm)));
                        }
                        node = XmlUtil.create(TAG_ENTRY, root, attrs);
                    }
                }
                if ((addAssociationCbx != null) && addAssociationCbx.isSelected()) {
                    repositoryClient.addAssociation(root, lastBundleId, contentId, "generated product");
                }
                String xml = XmlUtil.toString(root);
                zos.putNextEntry(new ZipEntry("entries.xml"));
                byte[] bytes = xml.getBytes();
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
                zos.close();
                bos.close();
                List<HttpFormEntry> entries = new ArrayList<HttpFormEntry>();
                repositoryClient.addUrlArgs(entries);
                entries.add(new HttpFormEntry(ARG_FILE, "entries.zip", bos.toByteArray()));
                dialog.setText("Posting to RAMADDA");
                String[] result = repositoryClient.doPost(repositoryClient.URL_ENTRY_XMLCREATE, entries);
                dialog.dispose();
                if (result[0] != null) {
                    LogUtil.userErrorMessage("Error publishing:\n" + result[0]);
                    return;
                }
                Element response = XmlUtil.getRoot(result[1]);
                if (repositoryClient.responseOk(response)) {
                    if (bundleFile != null) {
                        Element firstResult = XmlUtil.findChild(response, TAG_ENTRY);
                        if (firstResult != null) {
                            lastBundleId = XmlUtil.getAttribute(firstResult, ATTR_ID);
                            lastBundleFile = bundleFile;
                        }
                    }
                    LogUtil.userMessage("Publication was successful");
                    return;
                }
                String body = XmlUtil.getChildText(response).trim();
                LogUtil.userErrorMessage("Error publishing:" + body);
            }
        } catch (Exception exc) {
            LogUtil.logException("Publishing", exc);
        }
    }
