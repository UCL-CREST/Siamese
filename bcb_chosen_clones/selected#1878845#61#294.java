    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PortletsForm portletsform = (PortletsForm) form;
        if ("delete".equalsIgnoreCase(request.getParameter("action"))) {
            ActionErrors errors = new ActionErrors();
            try {
                String webappid = request.getParameter("webapp");
                RepositoryBasic.getInstance(getServlet().getServletContext());
                Webapplication deleteWebapp = RepositoryBasic.getWebapplications().getWebapplication(webappid);
                WebApplications apps = RepositoryBasic.getWebapplications();
                ArrayList temp = apps.getWebApplications();
                for (int i = 0; i < temp.size(); i++) {
                    Webapplication tempApp = (Webapplication) temp.get(i);
                    if (tempApp.getIdWebapplication().equalsIgnoreCase(deleteWebapp.getIdWebapplication())) {
                        temp.remove(i);
                    }
                }
                apps.setWebApplications(temp);
                RepositoryBasic.getInstance(getServlet().getServletContext()).savePortlets(apps);
                Session hbsession = HibernateUtil.currentSession();
                Transaction tx = hbsession.beginTransaction();
                Query query = hbsession.createQuery("delete from org.nodevision.portal.hibernate.om.NvPreferences where webapp_id =:webappid");
                query.setString("webappid", webappid);
                query = hbsession.createQuery("from org.nodevision.portal.hibernate.om.NvUserportlets");
                Iterator it = query.iterate();
                while (it.hasNext()) {
                    NvUserportlets up = (NvUserportlets) it.next();
                    SerializableBlob blob = (SerializableBlob) up.getPortletsList();
                    if (blob.length() > 0) {
                        DataInputStream in = new DataInputStream(blob.getBinaryStream());
                        ByteArrayOutputStream bout = new ByteArrayOutputStream();
                        int c;
                        while ((c = in.read()) != -1) {
                            bout.write(c);
                        }
                        final ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
                        final ObjectInputStream oin = new ObjectInputStream(bin);
                        ArrayList portlets = (ArrayList) oin.readObject();
                        boolean changed = false;
                        for (int z = 0; z < portlets.size(); z++) {
                            UserPortletsHolder tempPortlet = (UserPortletsHolder) portlets.get(z);
                            if (tempPortlet.getWebapplication().equalsIgnoreCase(webappid)) {
                                portlets.remove(z);
                                changed = true;
                            }
                        }
                        if (changed) {
                            bout = new ByteArrayOutputStream();
                            ObjectOutputStream oout = new ObjectOutputStream(bout);
                            oout.writeObject(portlets);
                            oout.flush();
                            bout.close();
                            oout.close();
                            up.setPortletsList(Hibernate.createBlob(bout.toByteArray()));
                            hbsession.update(up);
                            hbsession.flush();
                            if (!hbsession.connection().getAutoCommit()) {
                                tx.commit();
                            }
                        }
                    }
                }
            } catch (Exception sqle) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("portlets.error", sqle.toString()));
                saveErrors(request, errors);
                sqle.printStackTrace();
            } finally {
                HibernateUtil.closeSession();
            }
        }
        if (portletsform.getWarFile() != null) {
            ActionErrors errors = new ActionErrors();
            boolean hasWebXml = false;
            boolean hasPortletXml = false;
            try {
                ZipInputStream zipInputStream = new ZipInputStream(portletsform.getWarFile().getInputStream());
                ZipEntry zipEntry;
                StringTokenizer tokenizer = new StringTokenizer(portletsform.getWarFile().getFileName(), ".");
                String prefix = tokenizer.nextToken();
                String suffix = tokenizer.nextToken();
                File f = File.createTempFile(prefix, "." + suffix);
                f.deleteOnExit();
                ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(f));
                ArrayList portlets = new ArrayList();
                PortletApplicationDefinition appDefs = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.getName().toLowerCase().endsWith("/portlet.xml")) {
                        hasPortletXml = true;
                        File portletxml = File.createTempFile("portlet", ".xml");
                        FileOutputStream pout = new FileOutputStream(portletxml);
                        zipOut.putNextEntry(new ZipEntry(zipEntry.getName()));
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = zipInputStream.read(buf)) > 0) {
                            zipOut.write(buf, 0, len);
                            pout.write(buf, 0, len);
                        }
                        pout.close();
                        zipOut.flush();
                        zipOut.closeEntry();
                        appDefs = createPortletList(zipEntry, portletxml);
                        portlets = appDefs.getPortletdefinitions();
                    } else if (zipEntry.getName().toLowerCase().endsWith("/web.xml")) {
                        hasWebXml = true;
                        zipOut.putNextEntry(new ZipEntry("/WEB-INF/web.xml"));
                        FileInputStream fin = new FileInputStream(new File(readWebXml(zipEntry, zipInputStream)));
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = fin.read(buf)) > 0) {
                            zipOut.write(buf, 0, len);
                        }
                        zipOut.flush();
                        zipOut.closeEntry();
                    } else {
                        zipOut.putNextEntry(new ZipEntry(zipEntry.getName()));
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = zipInputStream.read(buf)) > 0) {
                            zipOut.write(buf, 0, len);
                        }
                        zipOut.flush();
                        zipOut.closeEntry();
                    }
                }
                if (!hasWebXml || !hasPortletXml) {
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("portlets.nowebxml"));
                    saveErrors(request, errors);
                    this.setApps(request);
                    return mapping.getInputForward();
                }
                String pathToLib = getServlet().getServletContext().getRealPath("/WEB-INF/lib/nvportlet-tags.jar");
                zipOut.putNextEntry(new ZipEntry("WEB-INF/lib/nvportlet-tags.jar"));
                FileInputStream fin = new FileInputStream(pathToLib);
                byte[] buf = new byte[1024];
                int len;
                while ((len = fin.read(buf)) > 0) {
                    zipOut.write(buf, 0, len);
                }
                fin.close();
                pathToLib = getServlet().getServletContext().getRealPath("/WEB-INF/lib/nvlib.jar");
                zipOut.putNextEntry(new ZipEntry("WEB-INF/lib/nvlib.jar"));
                fin = new FileInputStream(pathToLib);
                buf = new byte[1024];
                while ((len = fin.read(buf)) > 0) {
                    zipOut.write(buf, 0, len);
                }
                fin.close();
                zipOut.flush();
                zipOut.closeEntry();
                zipOut.close();
                zipInputStream.close();
                String dest = "";
                dest = getServlet().getServletContext().getRealPath("/");
                dest = dest.substring(0, dest.length() - 1);
                dest = dest.substring(0, dest.lastIndexOf(dirDelim) + 1);
                if (portletsform.isExploded()) {
                    if (!RepositoryBasic.getConfig().getFilesystem().getExplodedDir().equalsIgnoreCase("")) {
                        dest = RepositoryBasic.getConfig().getFilesystem().getExplodedDir() + dirDelim;
                    }
                    dest += prefix;
                    dest += dirDelim;
                    new File(dest).mkdirs();
                    InputStream in = new BufferedInputStream(new FileInputStream(f));
                    ZipInputStream zin = new ZipInputStream(in);
                    ZipEntry e;
                    while ((e = zin.getNextEntry()) != null) {
                        if (e.isDirectory()) {
                            new File(dest + e.getName()).mkdirs();
                        } else {
                            unzip(zin, dest, e);
                        }
                    }
                    zin.close();
                } else {
                    if (!RepositoryBasic.getConfig().getFilesystem().getWarDir().equalsIgnoreCase("")) {
                        dest = RepositoryBasic.getConfig().getFilesystem().getWarDir() + dirDelim;
                    }
                    dest += prefix + "." + suffix;
                    InputStream in = new FileInputStream(f);
                    OutputStream out = new FileOutputStream(dest);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                }
                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("portlets.deployed", prefix));
                RepositoryBasic.getInstance(getServlet().getServletContext());
                Webapplication oldWebapp = RepositoryBasic.getWebapplications().getWebapplication(prefix);
                ArrayList rportlets = new ArrayList();
                if (oldWebapp == null) {
                    Webapplication newApp = new Webapplication();
                    newApp.setContext("/" + prefix);
                    newApp.setIdWebapplication(prefix);
                    newApp.setApplicationDefinition(appDefs);
                    for (int z = 0; z < portlets.size(); z++) {
                        PortletDefinition def = (PortletDefinition) portlets.get(z);
                        RegisteredPortlet regPortlet = new RegisteredPortlet();
                        regPortlet.setNamePortlet(def.getPortletName());
                        regPortlet.setProvideRequest(Boolean.FALSE);
                        regPortlet.setProvideRequest(Boolean.FALSE);
                        regPortlet.setPortletDefinition(def);
                        rportlets.add(regPortlet);
                    }
                    newApp.setPortlets(rportlets);
                    WebApplications apps = RepositoryBasic.getWebapplications();
                    ArrayList temp = apps.getWebApplications();
                    temp.add(newApp);
                    apps.setWebApplications(temp);
                    RepositoryBasic.getInstance(getServlet().getServletContext()).savePortlets(apps);
                } else {
                    for (int z = 0; z < portlets.size(); z++) {
                        PortletDefinition def = (PortletDefinition) portlets.get(z);
                        RegisteredPortlet regPortlet = new RegisteredPortlet();
                        regPortlet.setPortletDefinition(def);
                        regPortlet.setNamePortlet(def.getPortletName());
                        rportlets.add(regPortlet);
                        getServlet().getServletContext().setAttribute(Constants.PORTLET_INSTANCE + "." + prefix + "." + def.getPortletName(), null);
                    }
                    oldWebapp.setPortlets(rportlets);
                    WebApplications apps = RepositoryBasic.getWebapplications();
                    RepositoryBasic.getInstance(getServlet().getServletContext()).savePortlets(apps);
                }
                saveErrors(request, errors);
            } catch (Exception e) {
                e.printStackTrace();
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("portlets.error", e.toString()));
                saveErrors(request, errors);
            }
        }
        this.setApps(request);
        return mapping.getInputForward();
    }
