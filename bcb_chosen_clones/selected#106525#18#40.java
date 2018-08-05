    public static void fillTemplate(String sourceFileName, String targetFileName, Map<String, String> values) throws IOException {
        File targetDirectory = new File(targetFileName.substring(0, targetFileName.lastIndexOf(File.separator)));
        targetDirectory.mkdirs();
        File file = new File(sourceFileName);
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
        Charset charset = Charset.forName("ISO-8859-15");
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer cb = decoder.decode(bb);
        String inString = cb.toString();
        fis.close();
        fc.close();
        Set<String> keys = values.keySet();
        for (Object k : keys) {
            inString = inString.replace((String) k, escape(values.get(k)));
        }
        FileOutputStream fos = new FileOutputStream(targetFileName);
        PrintStream ps = new PrintStream(fos);
        ps.print(inString);
        fos.close();
    }
