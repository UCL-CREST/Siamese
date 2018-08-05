    public static void prettyPrintFile(Tidy tidy, File inputFile, File outputFile, boolean silent) throws IOException {
        log.debug("XMLPrettyPrinting " + inputFile.getAbsolutePath());
        InputStream is;
        OutputStream os;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        is = new BufferedInputStream(new FileInputStream(inputFile));
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();
        os = new BufferedOutputStream(bos);
        tidy.parse(is, os);
        byte[] bs = bos.toByteArray();
        try {
            is.close();
        } catch (IOException e1) {
        }
        try {
            os.flush();
            os.close();
        } catch (IOException e1) {
        }
        if (tidy.getParseErrors() == 0) {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));
            InputStream in = new ByteArrayInputStream(bs);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        if (tidy.getParseErrors() > 0) {
            if (silent) {
                log.warn("Tidy was unable to process file " + inputFile + ", " + tidy.getParseErrors() + " errors found.");
            } else {
                throw new ExporterException("Tidy was unable to process file " + inputFile + ", " + tidy.getParseErrors() + " errors found.");
            }
        } else {
            log.debug("XMLPrettyPrinting completed");
        }
    }
