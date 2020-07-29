    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String selectedPage = request.getParameter("SelectedPage");
        Page page = null;
        PortalRequest portalRequest = PortalRequest.getCurrentRequest();
        if (selectedPage == null) {
            try {
                Property pageProp = Property.getProperty("HomePage");
                selectedPage = pageProp.getValue();
            } catch (PersistentModelException e) {
                myLogger.info("Page could not be found due to an exception");
                response.sendError(response.SC_NOT_FOUND);
                return;
            }
        }
        myLogger.info("Handling Page Request for Page " + selectedPage);
        try {
            if (page == null) {
                try {
                    page = Page.getPage(Long.parseLong(selectedPage));
                } catch (NumberFormatException e) {
                    List<Page> possiblePage = Page.findByName(selectedPage);
                    if (possiblePage.size() > 0) {
                        page = possiblePage.get(0);
                    } else {
                        response.sendError(response.SC_NOT_FOUND);
                        return;
                    }
                }
            }
            if (!page.isVisibleTo(portalRequest.getCurrentUser())) {
                Property pageProp = Property.getProperty("HomePage");
                selectedPage = pageProp.getValue();
                page = Page.getPage(Long.parseLong(selectedPage));
            }
            try {
                Property property = Property.getProperty("LogPageRequests");
                if (property.getValue().toLowerCase().equals("true")) {
                    String referer = request.getHeader("Referer");
                    if (referer == null || referer.indexOf(portalRequest.getRequest().getServerName()) > 0) {
                        referer = " ";
                    } else {
                        if (referer.length() >= 200) {
                            referer = referer.substring(0, 198);
                        }
                    }
                    PageRequest.createRequest(portalRequest.getCurrentHREF(), page, portalRequest.getCurrentUser(), portalRequest.getRequest().getRemoteAddr(), referer);
                }
            } catch (PersistentModelException e) {
                myLogger.log(Level.WARNING, "Log Page Requests property was not found.", e);
            }
            if (page.isVisibleTo(portalRequest.getCurrentUser())) {
                HttpSession session = request.getSession();
                if (session != null) {
                    RecentlyViewedManager rvm = (RecentlyViewedManager) session.getAttribute("ipoint.RecentlyViewedManager");
                    if (rvm == null) {
                        rvm = new RecentlyViewedManager();
                    }
                    rvm.add(page);
                    session.setAttribute("ipoint.RecentlyViewedManager", rvm);
                }
                PageCacheEntry entry = null;
                if (!portalRequest.isPost() && !page.isEditableBy(portalRequest.getCurrentUser())) {
                    try {
                        int pageCacheTime = 60000;
                        try {
                            Property pageCacheTimeProperty = Property.getProperty("PageCacheTime");
                            pageCacheTime = Integer.parseInt(pageCacheTimeProperty.getValue());
                            pageCacheTime *= 1000;
                        } catch (PersistentModelException pme) {
                            myLogger.warning("Ignoring Exception when retrieving PageCacheTime property");
                        } catch (NumberFormatException nfe) {
                        }
                        entry = PageCacheEntry.find(portalRequest.getCurrentHREF(), page, portalRequest.getCurrentUser(), pageCacheTime);
                    } catch (PersistentModelException e) {
                        myLogger.log(Level.WARNING, "An error occurred finding a page cache entry", e);
                    }
                }
                response.setContentType("text/html");
                response.setHeader("Cache-Control", "no-cache");
                boolean processed = false;
                if (entry != null) {
                    String tempFile = entry.getTempFile();
                    File file = new File(tempFile);
                    if (file.exists() && file.isFile() && file.canRead()) {
                        FileReader reader = new FileReader(file);
                        BufferedReader br = new BufferedReader(reader);
                        PrintWriter out = response.getWriter();
                        out.write("<!-- Starting to output cached page in " + portalRequest.elapsedTime() + " ms -->\n");
                        while (br.ready()) {
                            out.write(br.readLine() + "\n");
                        }
                        out.write("<!-- Cached output produced in " + portalRequest.elapsedTime() + " ms -->\n");
                        reader.close();
                        processed = true;
                        portalRequest.getMBeans().incrementStatistics(ManagementMBeans.StatisticsType.PageTotal, page.getName() + "-" + page.getID(), portalRequest.elapsedTime(), true);
                    } else {
                        entry.delete();
                    }
                }
                if (!processed) {
                    PrintWriter out = response.getWriter();
                    out.write("<!-- Output produced by iPoint Portal -->\n");
                    PageRenderer renderer = new PageRenderer(page);
                    long startTime = System.currentTimeMillis();
                    request.setAttribute(ELConstants.IPOINT_USER, portalRequest.getCurrentUser());
                    request.setAttribute(ELConstants.IPOINT_PAGE, page);
                    request.setAttribute(ELConstants.IPOINT_TEMPLATE, page.getTemplate());
                    request.setAttribute(ELConstants.IPOINT_PORTAL_PROPERTIES, Property.getPropertiesMap());
                    request.setAttribute(ELConstants.IPOINT_USER_PROPERTIES, portalRequest.getCurrentUser().getProperties());
                    renderer.preProcess();
                    portalRequest.getMBeans().incrementStatistics(ManagementMBeans.StatisticsType.PagePreProcess, page.getName() + "-" + page.getID(), System.currentTimeMillis() - startTime, false);
                    long renderStart = System.currentTimeMillis();
                    renderer.render();
                    portalRequest.getMBeans().incrementStatistics(ManagementMBeans.StatisticsType.PageRender, page.getName() + "-" + page.getID(), System.currentTimeMillis() - renderStart, false);
                    portalRequest.getMBeans().incrementStatistics(ManagementMBeans.StatisticsType.PageTotal, page.getName() + "-" + page.getID(), System.currentTimeMillis() - startTime, false);
                    out.write("<!-- output produced in " + PortalRequest.getCurrentRequest().elapsedTime() + " ms -->\n");
                    if (!page.isEditableBy(portalRequest.getCurrentUser())) {
                        if (response instanceof CachingResponseWrapper) {
                            CachingResponseWrapper crw = (CachingResponseWrapper) response;
                            crw.flushBuffer();
                            Property tempProperty = Property.getProperty("UploadLocation");
                            File tempDirectory = new File(tempProperty.getValue());
                            if (tempDirectory.exists() && tempDirectory.canWrite()) {
                                File file = File.createTempFile("iPointPage", ".html", tempDirectory);
                                try {
                                    FileWriter fw = new FileWriter(file);
                                    fw.write(crw.getOutput());
                                    fw.flush();
                                    fw.close();
                                    PageCacheEntry.create(portalRequest.getCurrentHREF(), page, portalRequest.getCurrentUser(), file.getAbsolutePath());
                                } catch (IOException e) {
                                }
                            } else {
                                myLogger.warning("Can not write to directory " + tempProperty.getValue());
                            }
                        }
                    }
                }
            } else {
                myLogger.info("Current User is Forbidden from seeing this Page");
                response.sendError(response.SC_FORBIDDEN);
            }
        } catch (PersistentModelException e) {
            myLogger.log(Level.INFO, "Unable to find page " + selectedPage, e);
            response.sendError(response.SC_NOT_FOUND);
        } catch (PresentationException pe) {
            pe.printStackTrace();
            throw new ServletException("Error processing the page", pe);
        }
    }
