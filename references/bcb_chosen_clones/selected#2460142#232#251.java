    @Override
    public void runTask(HashMap pjobParameters) throws Exception {
        if (hasRequiredResources(isSubTask())) {
            String lstrSource = getSourceFilename();
            String lstrTarget = getTargetFilename();
            if (getSourceDirectory() != null) {
                lstrSource = getSourceDirectory() + File.separator + getSourceFilename();
            }
            if (getTargetDirectory() != null) {
                lstrTarget = getTargetDirectory() + File.separator + getTargetFilename();
            }
            GZIPInputStream lgzipInput = new GZIPInputStream(new FileInputStream(lstrSource));
            OutputStream lfosGUnzip = new FileOutputStream(lstrTarget);
            byte[] buf = new byte[1024];
            int len;
            while ((len = lgzipInput.read(buf)) > 0) lfosGUnzip.write(buf, 0, len);
            lgzipInput.close();
            lfosGUnzip.close();
        }
    }
