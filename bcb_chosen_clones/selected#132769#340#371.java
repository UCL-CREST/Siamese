    private String createZipArchive(List<String> filenames) {
        String outFilename = "defaultName";
        WorkflowResultItem wfResultItem = new WorkflowResultItem(WorkflowResultItem.SERVICE_ACTION_IDENTIFICATION, System.currentTimeMillis());
        wfResultItem.addLogInfo("createZipArchive");
        byte[] buf = new byte[1024];
        try {
            outFilename = this.getWorkflowReportingLogger().getOutputFolder().getAbsolutePath() + URI_SEPARATOR + SIP_NAME + now(DATE_FORMAT) + "-" + now(TIME_FORMAT) + SIP_FORMAT;
            wfResultItem.addLogInfo("outFilename: " + outFilename);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
            if (filenames != null) {
                for (int idx = 0; idx < filenames.size(); idx++) {
                    wfResultItem.addLogInfo("zip i: " + idx + ", filename: " + filenames.get(idx));
                    if (filenames.get(idx) != null && filenames.get(idx).length() > 0) {
                        FileInputStream in = new FileInputStream(filenames.get(idx));
                        out.putNextEntry(new ZipEntry(filenames.get(idx)));
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                        in.close();
                        wfResultItem.addLogInfo("zip close entry out, close in.");
                    }
                }
            }
            out.close();
            wfResultItem.addLogInfo("zip close out.");
        } catch (Exception e) {
            wfResultItem.addLogInfo("zip error: " + e.getMessage());
        }
        return outFilename;
    }
