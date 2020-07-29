    public void createPalette() {
        try {
            String paletteName = demo.getString(getResourceName() + ".palette");
            Class paletteClass = Class.forName(paletteName);
            Constructor paletteConstructor = paletteClass.getConstructor(new Class[] { CGDesktop.class });
            Object[] args = new Object[] { this };
            palette = (CGPalette) paletteConstructor.newInstance(args);
            palette.setLocation(PALETTE_X, PALETTE_Y);
            palette.show();
            add(palette, PALETTE_LAYER);
        } catch (Exception ex) {
            getDemo().setStatus("Cannot create palette: " + ex);
        }
    }
