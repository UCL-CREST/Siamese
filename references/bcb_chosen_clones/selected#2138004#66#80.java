    public void doBody(JWebLiteRequestWrapper req, JWebLiteResponseWrapper resp) throws SkipException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(this.loadData(req)));
            bos = new BufferedOutputStream(resp.getOutputStream());
            IOUtils.copy(bis, bos);
            bos.flush();
        } catch (Exception e) {
            _cat.warn("Write data failed!", e);
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(bos);
        }
    }
