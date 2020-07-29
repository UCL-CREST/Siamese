    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        InputStream input = null;
        ServletOutputStream output = null;
        try {
            String savePath = request.getSession().getServletContext().getRealPath("/upload");
            String fileType = ".log";
            String dbFileName = "83tomcat日志测试哦";
            String downloadFileName = dbFileName + fileType;
            String finalPath = "\\2011-12\\01\\8364b45f-244d-41b6-bbf48df32064a935";
            downloadFileName = new String(downloadFileName.getBytes("GBK"), "ISO-8859-1");
            File downloadFile = new File(savePath + finalPath);
            if (!downloadFile.getParentFile().exists()) {
                downloadFile.getParentFile().mkdirs();
            }
            if (!downloadFile.isFile()) {
                FileUtils.touch(downloadFile);
            }
            response.setContentType("aapplication/vnd.ms-excel ;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-disposition", "attachment; filename=" + downloadFileName);
            input = new FileInputStream(downloadFile);
            output = response.getOutputStream();
            IOUtils.copy(input, output);
            output.flush();
        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
        }
    }
