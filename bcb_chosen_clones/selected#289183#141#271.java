    private void createHomeTab() {
        Tabpanel homeTab = new Tabpanel();
        windowContainer.addWindow(homeTab, Msg.getMsg(EnvWeb.getCtx(), "Home").replaceAll("&", ""), false);
        Portallayout portalLayout = new Portallayout();
        portalLayout.setWidth("100%");
        portalLayout.setHeight("100%");
        portalLayout.setStyle("position: absolute; overflow: auto");
        homeTab.appendChild(portalLayout);
        Portalchildren portalchildren = null;
        int currentColumnNo = 0;
        String sql = "SELECT COUNT(DISTINCT COLUMNNO) " + "FROM PA_DASHBOARDCONTENT " + "WHERE (AD_CLIENT_ID=0 OR AD_CLIENT_ID=?) AND ISACTIVE='Y'";
        int noOfCols = DB.getSQLValue(null, sql, EnvWeb.getCtx().getAD_Client_ID());
        int width = noOfCols <= 0 ? 100 : 100 / noOfCols;
        sql = "SELECT x.*, m.AD_MENU_ID " + "FROM PA_DASHBOARDCONTENT x " + "LEFT OUTER JOIN AD_MENU m ON x.AD_WINDOW_ID=m.AD_WINDOW_ID " + "WHERE (x.AD_CLIENT_ID=0 OR x.AD_CLIENT_ID=?) AND x.ISACTIVE='Y' " + "AND x.zulfilepath not in (?, ?, ?) " + "ORDER BY x.COLUMNNO, x.AD_CLIENT_ID, x.LINE ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, EnvWeb.getCtx().getAD_Client_ID());
            pstmt.setString(2, ACTIVITIES_PATH);
            pstmt.setString(3, FAVOURITES_PATH);
            pstmt.setString(4, VIEWS_PATH);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int columnNo = rs.getInt("ColumnNo");
                if (portalchildren == null || currentColumnNo != columnNo) {
                    portalchildren = new Portalchildren();
                    portalLayout.appendChild(portalchildren);
                    portalchildren.setWidth(width + "%");
                    portalchildren.setStyle("padding: 5px");
                    currentColumnNo = columnNo;
                }
                Panel panel = new Panel();
                panel.setStyle("margin-bottom:10px");
                panel.setTitle(rs.getString("Name"));
                String description = rs.getString("Description");
                if (description != null) panel.setTooltiptext(description);
                String collapsible = rs.getString("IsCollapsible");
                panel.setCollapsible(collapsible.equals("Y"));
                panel.setBorder("normal");
                portalchildren.appendChild(panel);
                Panelchildren content = new Panelchildren();
                panel.appendChild(content);
                boolean panelEmpty = true;
                String htmlContent = rs.getString("HTML");
                if (htmlContent != null) {
                    StringBuffer result = new StringBuffer("<html><head>");
                    URL url = getClass().getClassLoader().getResource("org/compiere/images/PAPanel.css");
                    InputStreamReader ins;
                    try {
                        ins = new InputStreamReader(url.openStream());
                        BufferedReader bufferedReader = new BufferedReader(ins);
                        String cssLine;
                        while ((cssLine = bufferedReader.readLine()) != null) result.append(cssLine + "\n");
                    } catch (IOException e1) {
                        logger.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
                    }
                    result.append("</head><body><div class=\"content\">\n");
                    result.append(stripHtml(htmlContent, false) + "<br>\n");
                    result.append("</div>\n</body>\n</html>\n</html>");
                    Html html = new Html();
                    html.setContent(result.toString());
                    content.appendChild(html);
                    panelEmpty = false;
                }
                int AD_Window_ID = rs.getInt("AD_Window_ID");
                if (AD_Window_ID > 0) {
                    int AD_Menu_ID = rs.getInt("AD_Menu_ID");
                    ToolBarButton btn = new ToolBarButton(String.valueOf(AD_Menu_ID));
                    MMenu menu = new MMenu(EnvWeb.getCtx(), AD_Menu_ID, null);
                    btn.setLabel(menu.getName());
                    btn.addEventListener(Events.ON_CLICK, this);
                    content.appendChild(btn);
                    panelEmpty = false;
                }
                int PA_Goal_ID = rs.getInt("PA_Goal_ID");
                if (PA_Goal_ID > 0) {
                    StringBuffer result = new StringBuffer("<html><head>");
                    URL url = getClass().getClassLoader().getResource("org/compiere/images/PAPanel.css");
                    InputStreamReader ins;
                    try {
                        ins = new InputStreamReader(url.openStream());
                        BufferedReader bufferedReader = new BufferedReader(ins);
                        String cssLine;
                        while ((cssLine = bufferedReader.readLine()) != null) result.append(cssLine + "\n");
                    } catch (IOException e1) {
                        logger.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
                    }
                    result.append("</head><body><div class=\"content\">\n");
                    result.append(renderGoals(PA_Goal_ID, content));
                    result.append("</div>\n</body>\n</html>\n</html>");
                    Html html = new Html();
                    html.setContent(result.toString());
                    content.appendChild(html);
                    panelEmpty = false;
                }
                String url = rs.getString("ZulFilePath");
                if (url != null) {
                    try {
                        Component component = Executions.createComponents(url, content, null);
                        if (component != null) {
                            if (component instanceof DashboardPanel) {
                                DashboardPanel dashboardPanel = (DashboardPanel) component;
                                if (!dashboardPanel.getChildren().isEmpty()) {
                                    content.appendChild(dashboardPanel);
                                    dashboardRunnable.add(dashboardPanel);
                                    panelEmpty = false;
                                }
                            } else {
                                content.appendChild(component);
                                panelEmpty = false;
                            }
                        }
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Failed to create components. zul=" + url, e);
                    }
                }
                if (panelEmpty) panel.detach();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to create dashboard content", e);
        } finally {
            Util.closeCursor(pstmt, rs);
        }
        registerWindow(homeTab);
        if (!portalLayout.getDesktop().isServerPushEnabled()) portalLayout.getDesktop().enableServerPush(true);
        dashboardRunnable.refreshDashboard();
        dashboardThread = new Thread(dashboardRunnable, "UpdateInfo");
        dashboardThread.setDaemon(true);
        dashboardThread.start();
    }
