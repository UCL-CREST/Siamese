        private void loadBinaryStream(String streamName, InputStream streamToLoad, long sz, HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType(getContentType(req, streamName));
            resp.setHeader("Content-Disposition", "inline;filename=" + streamName);
            resp.setContentLength((int) sz);
            OutputStream out = resp.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out, 2048);
            try {
                IOUtils.copy(streamToLoad, bos);
            } finally {
                IOUtils.closeQuietly(streamToLoad);
                IOUtils.closeQuietly(bos);
            }
            getCargo().put(GWT_ENTRY_POINT_PAGE_PARAM, null);
        }
