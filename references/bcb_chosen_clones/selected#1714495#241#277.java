    public void setApplicationIconImage(BufferedImage image) {
        if (isMac()) {
            try {
                Method setDockIconImage = application.getClass().getMethod("setDockIconImage", Image.class);
                try {
                    setDockIconImage.invoke(application, image);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            } catch (NoSuchMethodException mnfe) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "png", stream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Class nsDataClass = Class.forName("com.apple.cocoa.foundation.NSData");
                    Constructor constructor = nsDataClass.getConstructor(new Class[] { new byte[0].getClass() });
                    Object nsData = constructor.newInstance(new Object[] { stream.toByteArray() });
                    Class nsImageClass = Class.forName("com.apple.cocoa.application.NSImage");
                    Object nsImage = nsImageClass.getConstructor(new Class[] { nsDataClass }).newInstance(new Object[] { nsData });
                    Object application = getNSApplication();
                    application.getClass().getMethod("setApplicationIconImage", new Class[] { nsImageClass }).invoke(application, new Object[] { nsImage });
                } catch (ClassNotFoundException e) {
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
