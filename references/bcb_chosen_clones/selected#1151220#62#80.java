    protected byte[] readGZippedBytes(TupleInput in) {
        final boolean is_compressed = in.readBoolean();
        byte array[] = readBytes(in);
        if (array == null) return null;
        if (!is_compressed) {
            return array;
        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(array);
            GZIPInputStream gzin = new GZIPInputStream(bais);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(array.length);
            IOUtils.copyTo(gzin, baos);
            gzin.close();
            bais.close();
            return baos.toByteArray();
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }
