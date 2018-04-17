    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ServletOutputStream out = response.getOutputStream();
        String filename;
        String path;
        FTPTree tree = (FTPTree) session.getAttribute("tree");
        List<Object> selectedFiles = ItemSelectorHelper.getSelected(tree.getCurrentNode().getFiles());
        ZipOutputStream zos = new ZipOutputStream(out);
        if (selectedFiles.size() == 0) {
            return;
        }
        filename = "downlodFTP" + ".zip";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "inline; filename=\"" + filename + "\"");
        path = tree.getCurrentNode().getFolderPath();
        System.out.println("path" + path);
        for (Object zippingfiles : selectedFiles) {
            FTPFile file = (FTPFile) zippingfiles;
            if (file.isFile()) {
                String downloadFile = path + "/" + file.getName();
                System.out.println("FileName: " + downloadFile);
                zos.putNextEntry(new ZipEntry(file.getName()));
                System.out.println("connection stat: " + tree.getClient().isConnected());
                InputStream stream = tree.getClient().retrieveFileStream(downloadFile);
                byte[] buffer = new byte[1024];
                int len;
                System.out.println(stream + "&&&&&");
                while ((len = stream.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                stream.close();
                tree.getClient().completePendingCommand();
                System.out.println("connection stat: " + tree.getClient().isConnected());
                zos.closeEntry();
            }
        }
        zos.finish();
        out.flush();
        out.close();
    }
