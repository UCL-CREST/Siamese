    private static void setCompressedInputStream(EboContext ctx, List iFileList, DocumentContainer docContainer) throws boRuntimeException, IOException, iFilePermissionDenied {
        StringBuffer comments = new StringBuffer();
        String filetmp = docContainer.getFileTmp();
        FileOutputStream f = new FileOutputStream(filetmp);
        CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(csum));
        long tInicial = System.currentTimeMillis();
        long tFinal = 0;
        for (int i = 0; i < iFileList.size(); i++) {
            iFile file = (iFile) iFileList.get(i);
            createComments(ctx, file, comments);
            InputStream in = file.getInputStream();
            out.putNextEntry(new ZipEntry(file.getName()));
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
        }
        out.setComment(comments.toString());
        out.close();
        FileInputStream input = new FileInputStream(filetmp);
        InputStream bufferedIn = new BufferedInputStream(input);
        docContainer.setInputStream(bufferedIn);
        tFinal = System.currentTimeMillis();
        logger.finer(LoggerMessageLocalizer.getMessage("TOTAL_CHECKOUT_TIME") + ": " + (tFinal - tInicial) / 1000 + "s");
    }
