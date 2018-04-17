    public void setParams(int framecount, int fps, String interpolateClass) {
        this.fps = fps;
        this.frameCount = framecount;
        try {
            Class<?> cls = Class.forName(interpolateClass);
            Constructor<?>[] crt = cls.getConstructors();
            interpolate = (XMLinterpolator) crt[0].newInstance();
        } catch (Exception e) {
            System.out.println("Fatal error instantiating class " + interpolateClass);
            e.printStackTrace();
            System.exit(-1);
        }
    }
