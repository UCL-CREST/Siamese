    public void run() {
        int requestCount = 0;
        long i0 = System.currentTimeMillis();
        while (requestCount != maxRequests) {
            long r0 = System.currentTimeMillis();
            try {
                url = new URL(requestedUrl);
                logger.debug("Requesting Url, " + url.toString());
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                while ((httpResponse = in.readLine()) != null) {
                    logger.trace("Http Response = " + httpResponse);
                }
            } catch (Exception e) {
                logger.fatal("Exception thrown retrievng Url = " + requestedUrl + ", " + e);
                notification.setNotification(e.toString());
            }
            long r1 = System.currentTimeMillis();
            requestedElapsedTime = r1 - r0;
            logger.debug("Request(" + this.getName() + "/" + this.getId() + ") #" + requestCount + " processed, took " + requestedElapsedTime + "ms");
            requestCount++;
        }
        long i1 = System.currentTimeMillis();
        iterationElapsedTime = i1 - i0;
        logger.trace("Iteration elapsed time is " + iterationElapsedTime + "ms for thread ID " + this.getId());
        status.incrementIterationsComplete();
        logger.info("Iteration for Url = " + requestedUrl + ", (" + this.getName() + "/" + this.getId() + ") took " + iterationElapsedTime + "ms");
        try {
            logger.debug("Joining thread(" + this.getId() + ")");
            this.join(100);
        } catch (Exception e) {
            logger.fatal(e);
            notification.setNotification(e.toString());
        }
    }
