    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Locale locale = getLocale(request);
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        User voUser = (User) session.getAttribute("voUser");
        if (!voUser.isAdministrator()) {
            log.error("User from " + request.getRemoteAddr() + " is not authorize to use this page.");
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.voUser.authorize"));
            saveErrors(request, errors);
            return (mapping.findForward("logon"));
        }
        String section = VOAccess.getRequestUnicodeParameter(request, "section");
        String xquery = "xquery version \"1.0\";" + "\n   let $hits := for $doc in /* return $doc, " + "\n   $count := count($hits) " + "\n" + "\n return" + "\n<query-results total_hits=\"{$count}\">" + "\n{" + "\n   for $current_doc in $hits" + "\n   let $doc_id := replace(util:document-name($current_doc), \".xml\", \"\")" + "\n   let $current_node := doc(concat(\"" + forumDB + "/\",$doc_id,\".xml\"))/DISCUSSION" + "\n   let $doc_name := $current_node/OBJECT_NAME/text()" + "\n   return" + "\n      <resource doc-uri=\"{document-uri(root($current_doc))}\" doc-id=\"{$doc_id}\" doc-name=\"{$doc_name}\">" + "\n      </resource>" + "\n}" + "\n</query-results>";
        log.debug(xquery);
        Collection col = vobs.dbaccess.CollectionsManager.getCollection(rootDB, true);
        Element resultDocument = null;
        if (null != col) {
            XQueryService service = (XQueryService) vobs.dbaccess.CollectionsManager.getService(rootDB + section, true, "XQueryService");
            ResourceSet result = service.query(xquery);
            if (result.getSize() > 0) {
                XMLResource resource = (XMLResource) result.getResource(0);
                resultDocument = (domBuild.build((org.w3c.dom.Document) resource.getContentAsDOM())).getRootElement();
            }
        }
        String zipFileName = "vo_backup_" + System.currentTimeMillis() + ".zip";
        File backups_dir = new File(fileStore + "/vo_backups");
        if (!backups_dir.exists()) {
            if (!backups_dir.mkdir()) {
                log.debug("Can't create backups dir " + fileStore + "/vo_backups");
                throw new Exception("Can't create backups dir " + fileStore + "/vo_backups");
            }
        }
        File outZipFile = new File(fileStore + "/vo_backups/" + zipFileName);
        ZipOutputStream outp = null;
        try {
            outp = new ZipOutputStream(new FileOutputStream(outZipFile));
        } catch (FileNotFoundException e) {
            log.debug("Can't create out file: " + e.toString());
            e.printStackTrace();
            throw new Exception("Can't create out file: " + e.toString());
        }
        for (Iterator it = resultDocument.getChildren("resource").iterator(); it.hasNext(); ) {
            Element res = (Element) it.next();
            if (null == res.getAttributeValue("doc-name") || res.getAttributeValue("doc-name").length() == 0) {
                log.error("Entry name of " + res.getAttributeValue("doc-id") + " is null!");
            } else {
                String filePath = res.getAttributeValue("doc-uri");
                filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
                filePath = filePath.replaceAll(rootDB, "");
                outp.putNextEntry(new ZipEntry(filePath + res.getAttributeValue("doc-name").replaceAll(" ", "")));
                URL fileUrl = new URL(httpUri + res.getAttributeValue("doc-uri"));
                URLConnection urlconn = fileUrl.openConnection();
                InputStream inp = urlconn.getInputStream();
                BufferedInputStream fileInp = new BufferedInputStream(inp);
                byte[] b = new byte[1024];
                int read = fileInp.read(b, 0, b.length);
                while (read > 0) {
                    outp.write(b, 0, read);
                    read = fileInp.read(b, 0, b.length);
                }
                inp.close();
                fileInp.close();
                outp.closeEntry();
            }
        }
        outp.close();
        return (mapping.findForward("success"));
    }
