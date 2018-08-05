    public File executeExport(PersistenceManager pm, Collection<Long> accessLogsIds) throws IOException {
        File exportFile = File.createTempFile("export", ".zip", config.getTmpDir());
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(exportFile));
        Set<String> traceDigests = new HashSet<String>();
        for (Long id : accessLogsIds) {
            AccessLog accessLog = null;
            try {
                accessLog = (AccessLog) pm.detachCopy(pm.getObjectById(AccessLog.class, id));
            } catch (JDOObjectNotFoundException e) {
                continue;
            }
            ZipEntry ze = new ZipEntry("/accessLog/" + accessLog.getId());
            addDigest(traceDigests, accessLog.getRequestHeaderDigest());
            addDigest(traceDigests, accessLog.getRequestBodyDigest());
            addDigest(traceDigests, accessLog.getResponseHeaderDigest());
            addDigest(traceDigests, accessLog.getResponseBodyDigest());
            String json = accessLog.toJson();
            byte[] jsonBytes = json.getBytes("utf-8");
            int length = jsonBytes.length;
            ze.setSize(length);
            zos.putNextEntry(ze);
            zos.write(jsonBytes);
            zos.closeEntry();
        }
        for (String digest : traceDigests) {
            long length = StoreManager.getStoreLength(digest);
            long storeId = StoreManager.getStoreId(digest);
            if (length < 0 || storeId == Store.FREE_ID) {
                logger.warn("illegal digest:" + digest);
                continue;
            }
            ZipEntry ze = new ZipEntry("/store/" + Long.toString(storeId));
            ze.setSize(length);
            zos.putNextEntry(ze);
            StoreStream.storeToStream(storeId, zos);
            zos.closeEntry();
        }
        zos.close();
        return exportFile;
    }
