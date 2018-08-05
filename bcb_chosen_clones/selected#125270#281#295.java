    public byte[] downloadFile(String sampleName) throws BinBaseException, IOException {
        Iterator it = this.getImportDirectories().iterator();
        while (it.hasNext()) {
            String dir = it.next().toString();
            File file = new File(generateFileName(dir, sampleName));
            if (file.exists()) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                FileInputStream in = new FileInputStream(file);
                Copy.copy(in, out);
                byte[] result = out.toByteArray();
                return result;
            }
        }
        return null;
    }
