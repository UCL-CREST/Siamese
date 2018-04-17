    public String button1_action() {
        RowKey[] selectedRowKeys = getTrgAttachments().getSelectedRowKeys();
        ServletOutputStream out = null;
        FileInputStream fileToDownload = null;
        ZipOutputStream zout = null;
        try {
            byte[] buf = new byte[1024];
            String outFilename = "mewit_files_" + UUID.randomUUID().toString() + ".zip";
            zout = new ZipOutputStream(new FileOutputStream(outFilename));
            for (int i = 0; i < selectedRowKeys.length; i++) {
                Attachment attachment = (Attachment) getSessionBean1().getFileRepositoryDP().getObject(selectedRowKeys[i]);
                FileInputStream in = new FileInputStream(attachment.getFilePath());
                zout.putNextEntry(new ZipEntry(attachment.getFileName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    zout.write(buf, 0, len);
                }
                zout.closeEntry();
                in.close();
            }
            zout.close();
            java.io.File zipFile = new java.io.File(outFilename);
            fileToDownload = new FileInputStream(zipFile);
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            out = response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFile.getName() + "\"");
            response.setContentLength(fileToDownload.available());
            int c;
            while ((c = fileToDownload.read()) != -1) {
                out.write(c);
            }
            out.flush();
        } catch (Exception ex) {
            LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
        } finally {
            if (fileToDownload != null) {
                try {
                    fileToDownload.close();
                } catch (IOException ex) {
                    LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    LogUtil.getLogger().log(Level.SEVERE, Thread.currentThread().getStackTrace()[3].getMethodName(), ex);
                }
            }
        }
        return null;
    }
