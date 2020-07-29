    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String fileName = request.getParameter("tegsoftFileName");
            if (fileName.startsWith("Tegsoft_BACKUP_")) {
                fileName = fileName.substring("Tegsoft_BACKUP_".length());
                String targetFileName = "/home/customer/" + fileName;
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                FileInputStream is = new FileInputStream(targetFileName);
                IOUtils.copy(is, response.getOutputStream());
                is.close();
                return;
            }
            if (fileName.equals("Tegsoft_ASTMODULES")) {
                String targetFileName = tobeHome + "/setup/Tegsoft_ASTMODULES.tgz";
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                FileInputStream is = new FileInputStream(targetFileName);
                IOUtils.copy(is, response.getOutputStream());
                is.close();
                return;
            }
            if (fileName.equals("Tegsoft_ASTSBIN")) {
                String targetFileName = tobeHome + "/setup/Tegsoft_ASTSBIN.tgz";
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                FileInputStream is = new FileInputStream(targetFileName);
                IOUtils.copy(is, response.getOutputStream());
                is.close();
                return;
            }
            if (!fileName.startsWith("Tegsoft_")) {
                return;
            }
            if (!fileName.endsWith(".zip")) {
                return;
            }
            if (fileName.indexOf("_") < 0) {
                return;
            }
            fileName = fileName.substring(fileName.indexOf("_") + 1);
            if (fileName.indexOf("_") < 0) {
                return;
            }
            String fileType = fileName.substring(0, fileName.indexOf("_"));
            String destinationFileName = tobeHome + "/setup/Tegsoft_" + fileName;
            if (!new File(destinationFileName).exists()) {
                if ("FORMS".equals(fileType)) {
                    FileUtil.createZipPackage(tobeHome + "/forms", tobeHome + "/setup/Tegsoft_" + fileName);
                } else if ("IMAGES".equals(fileType)) {
                    FileUtil.createZipPackage(tobeHome + "/image", tobeHome + "/setup/Tegsoft_" + fileName);
                } else if ("VIDEOS".equals(fileType)) {
                    FileUtil.createZipPackage(tobeHome + "/videos", tobeHome + "/setup/Tegsoft_" + fileName);
                } else if ("TEGSOFTJARS".equals(fileType)) {
                    FileUtil.createZipPackage(tobeHome + "/WEB-INF/lib/", tobeHome + "/setup/Tegsoft_" + fileName, "Tegsoft", "jar");
                } else if ("TOBEJARS".equals(fileType)) {
                    FileUtil.createZipPackage(tobeHome + "/WEB-INF/lib/", tobeHome + "/setup/Tegsoft_" + fileName, "Tobe", "jar");
                } else if ("ALLJARS".equals(fileType)) {
                    FileUtil.createZipPackage(tobeHome + "/WEB-INF/lib/", tobeHome + "/setup/Tegsoft_" + fileName);
                } else if ("DB".equals(fileType)) {
                    FileUtil.createZipPackage(tobeHome + "/sql", tobeHome + "/setup/Tegsoft_" + fileName);
                } else if ("CONFIGSERVICE".equals(fileType)) {
                    FileUtil.createZipPackage("/tegsoft/src/java/TegsoftTelecom/configFiles/init.d/", tobeHome + "/setup/Tegsoft_" + fileName, "tegsoft", null);
                } else if ("CONFIGSCRIPTS".equals(fileType)) {
                    FileUtil.createZipPackage("/tegsoft/src/java/TegsoftTelecom/configFiles/root/", tobeHome + "/setup/Tegsoft_" + fileName, "tegsoft", null);
                } else if ("CONFIGFOP".equals(fileType)) {
                    FileUtil.createZipPackage("/tegsoft/src/java/TegsoftTelecom/configFiles/fop/", tobeHome + "/setup/Tegsoft_" + fileName);
                } else if ("CONFIGASTERISK".equals(fileType)) {
                    FileUtil.createZipPackage("/tegsoft/src/java/TegsoftTelecom/configFiles/asterisk/", tobeHome + "/setup/Tegsoft_" + fileName);
                }
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            FileInputStream is = new FileInputStream(destinationFileName);
            IOUtils.copy(is, response.getOutputStream());
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
