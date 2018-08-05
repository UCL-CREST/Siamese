    public void process(InputStream is, InputFileInfo info, FileProcessorEnvironment env) throws IOException {
        ZipOutputStream zip = getZipOutputStream(info, env);
        zip.putNextEntry(new ZipEntry(info.getProposedRelativePath()));
        holder.inputFileStarted();
        int i;
        while (((i = is.read()) >= 0) && env.shouldContinue()) {
            zip.write(i);
            holder.bytesProcessed(1);
        }
        zip.closeEntry();
    }
