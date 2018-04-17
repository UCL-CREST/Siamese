    private final void copyTargetFileToSourceFile(File sourceFile, File targetFile) throws MJProcessorException {
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();
            try {
                if (!targetFile.exists()) {
                    targetFile.createNewFile();
                }
            } catch (IOException e) {
                throw new MJProcessorException(e.getMessage(), e);
            }
        }
        FileChannel in = null, out = null;
        try {
            in = new FileInputStream(sourceFile).getChannel();
            out = new FileOutputStream(targetFile).getChannel();
            long size = in.size();
            MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
            out.write(buf);
        } catch (IOException e) {
            log.error(e);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                log.error(e);
            }
            if (out != null) try {
                out.close();
            } catch (IOException e) {
                log.error(e);
            }
        }
    }
