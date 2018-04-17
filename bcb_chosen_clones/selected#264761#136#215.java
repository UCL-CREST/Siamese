    private static void archiveBasketObjects(String profileName, HttpServletRequest request, boolean onlySelected) throws XMLDBException, MalformedURLException, IOException, DataStoreException {
        String archiveId = "BA_" + profileName;
        String note = "User basket archive";
        String objType = "basketArchive";
        removeBasketObject(profileName, archiveId);
        Vector objIds = new Vector();
        String prdString = "";
        String[] items = null;
        if (onlySelected) {
            items = request.getParameterValues("selectedItem");
            prdString = prdString + "[";
            for (int i = 0; i < items.length; i++) {
                if (i < (items.length - 1)) {
                    prdString = prdString + "ID/text()='" + items[i].toString() + "' or ";
                } else {
                    prdString = prdString + "ID/text()='" + items[i].toString() + "'";
                }
            }
            prdString = prdString + "]";
        }
        org.w3c.dom.Element archiveResultDocument = null;
        String metadataFile = "";
        if (null != profileName) {
            String queryStr = "xquery version \"1.0\"; " + "  let $basketObjects := document(\"" + profileName + "\")/USER_PROFILE/USER_DATA_BASKET/BASKET_OBJECT";
            if (onlySelected) {
                queryStr = queryStr + prdString;
            }
            queryStr = queryStr + " " + "  return <BasketArchive><User>" + VOAccess.getUserNameById(profileName) + "</User> {" + "    for $obj in $basketObjects " + "  	return <Item> " + "       <Id>{$obj/ID/text()}</Id> " + "       <Time>{$obj/TIME/text()}</Time> " + "       <Note>{$obj/NOTE/text()}</Note> " + "       <Type>{$obj/OBJECT_TYPE/text()}</Type> " + "  	</Item> }" + "  </BasketArchive> ";
            XQueryService queryService = (XQueryService) CollectionsManager.getService(userDB, true, "XQueryService");
            ResourceSet queryResult = queryService.query(queryStr);
            if (queryResult.getSize() > 0) {
                XMLResource resource = (XMLResource) queryResult.getResource(0);
                archiveResultDocument = ((org.w3c.dom.Document) resource.getContentAsDOM()).getDocumentElement();
                metadataFile = (String) resource.getContent();
            }
            if (archiveResultDocument != null) {
                NodeList resources = archiveResultDocument.getElementsByTagName("Item");
                for (int i = 0; i < resources.getLength(); i++) {
                    org.w3c.dom.Element res = (org.w3c.dom.Element) resources.item(i);
                    if (res.getElementsByTagName("Id").getLength() > 0 && res.getElementsByTagName("Id").item(0).getFirstChild() != null) {
                        String id = res.getElementsByTagName("Id").item(0).getFirstChild().getNodeValue();
                        objIds.add(id);
                    }
                }
            }
        }
        if (null == Settings.get("vo_store.dir") || !(new File(Settings.get("vo_store.dir"))).isDirectory()) {
            log.error("Error accessing directory vo_store.dir: " + Settings.get("vo_store.dir"));
            return;
        }
        File archiveFile = new File(Settings.get("vo_store.dir") + "/" + archiveId + ".tmp");
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(archiveFile));
        for (Iterator it = objIds.iterator(); it.hasNext(); ) {
            String nextObjId = (String) it.next();
            URL objectUrl = new URL(request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf("/")) + "/filestore?objId=" + nextObjId);
            InputStream inp = objectUrl.openStream();
            if (null != inp) {
                zout.putNextEntry(new ZipEntry(nextObjId));
                byte[] buf = new byte[1048576];
                while (inp.available() > 0) {
                    int count = inp.read(buf, 0, buf.length);
                    zout.write(buf, 0, count);
                    zout.flush();
                }
                inp.close();
                zout.closeEntry();
            } else {
                log.error("Error getting " + nextObjId + " basket object");
            }
        }
        zout.putNextEntry(new ZipEntry("metadata.xml"));
        zout.write(metadataFile.getBytes());
        zout.closeEntry();
        zout.close();
        FileInputStream inputDataStream = new FileInputStream(archiveFile);
        FileStoreSave.storeFile(inputDataStream, archiveId, objType, inputDataStream.available() + "", archiveId, "fileAction");
        inputDataStream.close();
        archiveFile.delete();
        writeBasketObject(profileName, archiveId, "", note + ": " + objIds.size() + " item(s)", objType);
    }
