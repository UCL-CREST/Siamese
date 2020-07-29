    @HttpAction(name = "map.saveOrUpdate", method = { HttpAction.Method.post }, responseType = "text/plain")
    @HttpAuthentication(method = { HttpAuthentication.Method.WSSE })
    public String saveOrUpdate(FileItem file, User user, MapOriginal map) throws HttpRpcException {
        File tmpFile;
        GenericDAO<MapOriginal> mapDao = DAOFactory.createDAO(MapOriginal.class);
        try {
            assert (file != null);
            String jobid = null;
            if (file.getContentType().startsWith("image/")) {
                tmpFile = File.createTempFile("gmap", "img");
                OutputStream out = new FileOutputStream(tmpFile);
                IOUtils.copy(file.getInputStream(), out);
                out.flush();
                out.close();
                map.setState(MapOriginal.MapState.UPLOAD);
                map.setUser(user);
                map.setMapPath(tmpFile.getPath());
                map.setThumbnailUrl("/map/inproc.gif");
                map.setMimeType(file.getContentType());
                mapDao.saveOrUpdate(map);
                jobid = PoolFactory.getClientPool().put(map, TaskState.STATE_MO_FINISH, MapOverrideStrategy.class);
            }
            return jobid;
        } catch (IOException e) {
            logger.error(e);
            throw ERROR_INTERNAL;
        } catch (DAOException e) {
            logger.error(e);
            throw ERROR_INTERNAL;
        }
    }
