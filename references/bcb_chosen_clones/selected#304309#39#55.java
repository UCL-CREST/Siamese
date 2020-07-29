    public String execute() {
        String dir = "E:\\ganymede_workspace\\training01\\web\\user_imgs\\";
        HomeMap map = new HomeMap();
        map.setDescription(description);
        Integer id = homeMapDao.saveHomeMap(map);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(dir + id);
            IOUtils.copy(new FileInputStream(imageFile), fos);
            IOUtils.closeQuietly(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list();
    }
