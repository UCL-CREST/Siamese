    public static String writeZip(String paneModelUid, DOBO bo, DOService aService, String allSelects) throws IOException {
        String deptCode = "a000000";
        BOInstance aUser = DOGlobals.getInstance().getSessoinContext().getUser();
        if (aUser != null) {
            deptCode = aUser.getValue("deptcode");
        }
        StringBuilder zipFilePath = new StringBuilder(DOGlobals.WORK_DIR).append(File.separator).append(deptCode);
        File aWkDir = new File(zipFilePath.toString());
        if (!aWkDir.exists()) {
            aWkDir.mkdir();
        }
        zipFilePath.append(File.separator).append("batch.zip");
        File aFile = new File(zipFilePath.toString());
        aFile.createNewFile();
        OutputStream os = new FileOutputStream(aFile);
        ZipOutputStream zos = new ZipOutputStream(os);
        String[] arraySelect = allSelects.split(",");
        for (int i = 0; i < arraySelect.length; i++) {
            String aSelect = arraySelect[i];
            if (aSelect == null || aSelect.trim().equals("")) {
                continue;
            }
            BOInstance aInstance = bo.refreshContext(aSelect);
            if (aService != null) {
                try {
                    aService.invokeAll();
                } catch (ExedoException e) {
                    e.printStackTrace();
                }
            }
            if (aInstance != null) {
                String id_applyid = aInstance.getValue("id_applyid");
                ZipEntry ze = new ZipEntry(id_applyid + ".xml");
                zos.putNextEntry(ze);
                zos.write(DODownLoadFile.outHtmlCode(paneModelUid).getBytes("utf-8"));
                ze.clone();
            }
        }
        zos.close();
        return zipFilePath.toString();
    }
