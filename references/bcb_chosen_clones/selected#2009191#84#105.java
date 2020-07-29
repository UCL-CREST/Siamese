    public CompressStringResponse compressString(DPWSContext context, CompressString parameters) throws DPWSException {
        if (ALGORITHM_ZIP.equals(parameters.getMethod())) {
            try {
                CompressStringResponse resp = new CompressStringResponseImpl();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zipOutputStream = new ZipOutputStream(baos);
                ZipEntry entry = new ZipEntry("d");
                String tocompress = parameters.getTocompress();
                entry.setSize(tocompress.getBytes("UTF-8").length);
                zipOutputStream.putNextEntry(entry);
                OutputStreamWriter osw = new OutputStreamWriter(zipOutputStream, "UTF-8");
                osw.write(tocompress, 0, tocompress.length());
                osw.close();
                resp.setCompressed(baos.toByteArray());
                return resp;
            } catch (Exception e) {
                e.printStackTrace();
                throw new DPWSException(e);
            }
        }
        throw new DPWSException("Unimplemented algorithm:" + parameters.getMethod());
    }
