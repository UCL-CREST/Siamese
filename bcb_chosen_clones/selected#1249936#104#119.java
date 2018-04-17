    private void writeAndCheckFile(DataFileReader reader, String base, String path, String hash, Reference ref, boolean hashall) throws Exception {
        Data data = ref.data;
        File file = new File(base + path);
        file.getParentFile().mkdirs();
        if (Debug.level > 1) System.err.println("read file " + data.file + " at index " + data.index);
        OutputStream output = new FileOutputStream(file);
        if (hashall) output = new DigestOutputStream(output, MessageDigest.getInstance("MD5"));
        reader.read(output, data.index, data.file);
        output.close();
        if (hashall) {
            String filehash = StringUtils.toHex(((DigestOutputStream) output).getMessageDigest().digest());
            if (!hash.equals(filehash)) throw new RuntimeException("hash wasn't equal for " + file);
        }
        file.setLastModified(ref.lastmod);
        if (file.length() != data.size) throw new RuntimeException("corrupted file " + file);
    }
