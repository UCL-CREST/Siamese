    private CGAnimation createAnimation(CGCanvas canvas) {
        String key = getDemo().getResourceName() + ".animation";
        String animationName = getDemo().getString(key);
        try {
            Class animationClass = Class.forName(animationName);
            Constructor animationConstructor = animationClass.getConstructor(new Class[] { canvas.getClass() });
            Object[] args = new Object[] { canvas };
            CGAnimation animation = (CGAnimation) animationConstructor.newInstance(args);
            return animation;
        } catch (Exception ex) {
            getDemo().setStatus("Cannot create animation: " + ex);
            System.err.println("Cannot create animation: " + ex);
            return null;
        }
    }
