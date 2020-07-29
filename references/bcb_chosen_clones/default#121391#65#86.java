    public CGCanvas createCanvas() {
        try {
            String canvasName = demo.getString(getResourceName() + ".canvas");
            Class canvasClass = Class.forName(canvasName);
            Constructor canvasConstructor = canvasClass.getConstructor(new Class[] { getClass() });
            Object[] args = new Object[] { this };
            CGCanvas canvas = (CGCanvas) canvasConstructor.newInstance(args);
            CanvasFrame iframe = new CanvasFrame(this, canvas, ++canvasCount);
            iframe.show();
            add(iframe, CANVAS_LAYER);
            try {
                iframe.setSelected(true);
                setActiveFrame(iframe);
            } catch (java.beans.PropertyVetoException ex) {
            }
            return canvas;
        } catch (Exception ex) {
            getDemo().setStatus("Cannot create a new canvas: " + ex);
            ex.printStackTrace();
            return null;
        }
    }
