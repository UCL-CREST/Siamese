    public void run() {
        try {
            try {
                IOUtils.copy(_src, _dest);
            } finally {
                _dest.close();
            }
        } catch (Exception e) {
        }
    }
