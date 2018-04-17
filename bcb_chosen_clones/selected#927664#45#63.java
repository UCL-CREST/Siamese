    public static void replaceAll(File file, String substitute, String substituteReplacement) throws IOException {
        log.debug("Replace " + substitute + " by " + substituteReplacement);
        Pattern pattern = Pattern.compile(substitute);
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
        Charset charset = Charset.forName("ISO-8859-15");
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer cb = decoder.decode(bb);
        Matcher matcher = pattern.matcher(cb);
        String outString = matcher.replaceAll(substituteReplacement);
        log.debug(outString);
        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
        PrintStream ps = new PrintStream(fos);
        ps.print(outString);
        ps.close();
        fos.close();
    }
