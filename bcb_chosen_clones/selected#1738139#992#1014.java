    private void convertOutputZip(String userId, String workflowId, String jobId, String fileName, InputStream is, OutputStream os) throws IOException {
        InputStream exactFile = null;
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry;
        String runtimeID = getRuntimeID(userId, workflowId);
        ZipOutputStream zos = new ZipOutputStream(os);
        while ((entry = zis.getNextEntry()) != null) {
            if (jobId == null || (entry.getName().contains(jobId + "/outputs/" + runtimeID + "/") && (fileName == null || (fileName != null && entry.getName().endsWith(fileName))))) {
                int size;
                byte[] buffer = new byte[2048];
                String parentDir = entry.getName().split("/")[entry.getName().split("/").length - 2];
                String fileNameInZip = parentDir + "/" + entry.getName().split("/")[entry.getName().split("/").length - 1];
                ZipEntry newFile = new ZipEntry(fileNameInZip);
                zos.putNextEntry(newFile);
                while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, size);
                }
                zos.closeEntry();
            }
        }
        zis.close();
        zos.close();
    }
