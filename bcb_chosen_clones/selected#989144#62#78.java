    public String insertBuilding() {
        homeMap = homeMapDao.getHomeMapById(homeMap.getId());
        homeBuilding.setHomeMap(homeMap);
        Integer id = homeBuildingDao.saveHomeBuilding(homeBuilding);
        String dir = "E:\\ganymede_workspace\\training01\\web\\user_buildings\\";
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
        return execute();
    }
