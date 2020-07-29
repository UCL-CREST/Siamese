    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Split requestSplit = requestWaitTime.start();
        Throttler requestThrottler = new Throttler(Long.valueOf(getRequestsPerMinute()).intValue(), 60000);
        requestThrottler.StartRequest();
        requestSplit.stop();
        long start = new Date().getTime();
        try {
            numberOfZips++;
            Long id = Long.valueOf(request.getParameter("identifier"));
            org.ochan.entity.Thread t = new org.ochan.entity.Thread();
            t.setIdentifier(id);
            List<Post> posts = postService.retrieveThreadPosts(t.getIdentifier());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zipfile = new ZipOutputStream(bos);
            for (Post post : posts) {
                if (post instanceof ImagePost) {
                    ImagePost image = (ImagePost) post;
                    Byte[] fullImageBytes = blobService.getBlob(image.getImageIdentifier());
                    ZipEntry zipentry = new ZipEntry(post.getIdentifier() + ".jpg");
                    zipfile.putNextEntry(zipentry);
                    byte[] datum = new byte[fullImageBytes.length];
                    int i = 0;
                    for (Byte val : fullImageBytes) {
                        datum[i] = val.byteValue();
                        i++;
                    }
                    zipfile.write((byte[]) datum);
                }
            }
            zipfile.close();
            byte[] datum = bos.toByteArray();
            long generationEnd = new Date().getTime();
            lastGenerationTimeInMillis = generationEnd - start;
            totalGenerationTimeInMillis += generationEnd - start;
            response.setContentType("application/zip");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            LOG.debug("file length is " + datum.length);
            response.setContentLength(datum.length);
            response.setHeader("Content-Disposition", " inline; filename=" + id + ".zip");
            FileCopyUtils.copy(datum, response.getOutputStream());
        } catch (SocketException se) {
            LOG.trace("Socket exception", se);
        } catch (Exception e) {
            LOG.error("Unable to create thumbnail", e);
        }
        long end = new Date().getTime();
        lastTimeInMillis = end - start;
        totalTimeInMillis += end - start;
        if (lastTimeInMillis > getTimeLength()) {
            LOG.warn("Zip times are getting excessive: " + lastTimeInMillis + " ms. for thread id:" + request.getParameter("identifier"));
        }
        return null;
    }
