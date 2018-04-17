    @Override
    @Transactional
    public FileData store(FileData data, InputStream stream) {
        try {
            FileData file = save(data);
            file.setPath(file.getGroup() + File.separator + file.getId());
            file = save(file);
            File folder = new File(PATH, file.getGroup());
            if (!folder.exists()) folder.mkdirs();
            File filename = new File(folder, file.getId() + "");
            IOUtils.copyLarge(stream, new FileOutputStream(filename));
            return file;
        } catch (IOException e) {
            throw new ServiceException("storage", e);
        }
    }
