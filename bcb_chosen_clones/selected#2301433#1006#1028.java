    private File getTempFile(DigitalObject object, String pid) throws Exception {
        File directory = new File(tmpDir, object.getId());
        File target = new File(directory, pid);
        if (!target.exists()) {
            target.getParentFile().mkdirs();
            target.createNewFile();
        }
        Payload payload = object.getPayload(pid);
        InputStream in = payload.open();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(target);
            IOUtils.copyLarge(in, out);
        } catch (Exception ex) {
            close(out);
            target.delete();
            payload.close();
            throw ex;
        }
        close(out);
        payload.close();
        return target;
    }
