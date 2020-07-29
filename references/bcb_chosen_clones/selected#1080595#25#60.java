    public void bindDownload(Download download) throws BindingException {
        List<ChunkDownload> chunks = download.getChunks();
        File destination = download.getFile();
        FileOutputStream fos = null;
        try {
            fos = FileUtils.openOutputStream(destination);
            for (ChunkDownload chunk : chunks) {
                String filePath = chunk.getChunkFilePath();
                InputStream ins = null;
                try {
                    File chunkFile = new File(filePath);
                    ins = FileUtils.openInputStream(chunkFile);
                    IOUtils.copy(ins, fos);
                    chunkFile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    ins.close();
                }
            }
            download.getWorkDir().delete();
            download.complete();
        } catch (IOException e) {
            logger.error("IO Exception while copying the chunk " + e.getMessage(), e);
            e.printStackTrace();
            throw new BindingException("IO Exception while copying the chunk " + e.getMessage(), e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                logger.error("IO Exception while copying closing stream of the target file " + e.getMessage(), e);
                e.printStackTrace();
                throw new BindingException("IO Exception while copying closing stream of the target file " + e.getMessage(), e);
            }
        }
    }
