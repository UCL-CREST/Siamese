    public String[][] getProjectTreeData() {
        String[][] treeData = null;
        String filename = dms_home + FS + "temp" + FS + username + "adminprojects.xml";
        String urlString = dms_url + "/servlet/com.ufnasoft.dms.server.ServerGetAdminProjects";
        try {
            String urldata = urlString + "?username=" + URLEncoder.encode(username, "UTF-8") + "&key=" + URLEncoder.encode(key, "UTF-8") + "&filename=" + URLEncoder.encode(username, "UTF-8") + "adminprojects.xml";
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder parser = factory.newDocumentBuilder();
            URL u = new URL(urldata);
            DataInputStream is = new DataInputStream(u.openStream());
            FileOutputStream os = new FileOutputStream(filename);
            int iBufSize = is.available();
            byte inBuf[] = new byte[20000 * 1024];
            int iNumRead;
            while ((iNumRead = is.read(inBuf, 0, iBufSize)) > 0) os.write(inBuf, 0, iNumRead);
            os.close();
            is.close();
            File f = new File(filename);
            InputStream inputstream = new FileInputStream(f);
            Document document = parser.parse(inputstream);
            NodeList nodelist = document.getElementsByTagName("proj");
            int num = nodelist.getLength();
            treeData = new String[num][3];
            for (int i = 0; i < num; i++) {
                treeData[i][0] = new String(DOMUtil.getSimpleElementText((Element) nodelist.item(i), "pid"));
                treeData[i][1] = new String(DOMUtil.getSimpleElementText((Element) nodelist.item(i), "ppid"));
                treeData[i][2] = new String(DOMUtil.getSimpleElementText((Element) nodelist.item(i), "p"));
            }
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        } catch (ParserConfigurationException ex) {
            System.out.println(ex);
        } catch (NullPointerException e) {
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return treeData;
    }
