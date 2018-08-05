    private static void downloadFile(String downloadFileName) throws Exception {
        URL getFileUrl = new URL("http://www.tegsoft.com/Tobe/getFile" + "?tegsoftFileName=" + downloadFileName);
        URLConnection getFileUrlConnection = getFileUrl.openConnection();
        InputStream is = getFileUrlConnection.getInputStream();
        String tobeHome = UiUtil.getParameter("RealPath.Context");
        OutputStream out = new FileOutputStream(tobeHome + "/setup/" + downloadFileName);
        IOUtils.copy(is, out);
        is.close();
        out.close();
    }
