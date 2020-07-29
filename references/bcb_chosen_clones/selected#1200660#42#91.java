    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long t0 = System.currentTimeMillis();
        String id = request.getRequestURI().split("/")[3];
        Song song = manager.find(id);
        if (song != null) {
            if (song.getArtwork()) {
                if (log.isDebugEnabled()) log.debug("song has embedded artwork");
                try {
                    AudioFile af = AudioFileIO.read(new File(song.getFile()));
                    Tag tag = af.getTag();
                    Artwork aw = tag.getFirstArtwork();
                    byte[] bytes = aw.getBinaryData();
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(aw.getMimeType());
                    response.getOutputStream().write(bytes);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            } else {
                if (log.isDebugEnabled()) log.debug("search in directory");
                File directory = new File(song.getFile()).getParentFile();
                File[] files = directory.listFiles(filter);
                if (files != null && files.length > 0) {
                    File file = files[0];
                    String type = FilenameUtils.getExtension(file.getName()).toLowerCase();
                    if (type.startsWith("jp")) type = "jpeg";
                    String mime = "image/" + type;
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(mime);
                    FileInputStream stream = new FileInputStream(file);
                    try {
                        IOUtils.copy(stream, response.getOutputStream());
                    } catch (IOException e) {
                        log.warn(e.getMessage());
                    } finally {
                        IOUtils.closeQuietly(stream);
                    }
                } else {
                    if (log.isDebugEnabled()) log.debug("image not found: " + id + ", sending redirect: " + redirect);
                    response.sendRedirect(redirect);
                }
            }
        } else {
            log.info("song not found: " + id);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        long t1 = System.currentTimeMillis();
        log.info("request: " + id + " duration: " + (t1 - t0) + "ms");
    }
