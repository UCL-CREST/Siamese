    public static void buildKMZ(File kmzFile, Layer layer) throws Exception {
        if (!(layer instanceof TimeLoopGroundOverlay) && !(layer instanceof GroundOverlayLayer)) throw new IOException("Invalid layer type:" + layer.getClass().getName());
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(kmzFile));
        String kmlDoc;
        if (layer instanceof TimeLoopGroundOverlay) {
            final TimeLoopGroundOverlay loop = (TimeLoopGroundOverlay) layer;
            kmlDoc = loop.toKML(false, true);
            for (GroundOverlayLayer ov : loop.getOverlays()) {
                File f = ov.getFileFromCache();
                String fName = f.getName();
                logger.debug("Adding Time Loop Ov to KMZ:" + fName);
                out.putNextEntry(new ZipEntry(fName));
                out.write(Messages.readFile(f));
            }
            if (loop.getLegend() != null) {
                try {
                    File f = loop.getLegend().getFile();
                    logger.debug("Adding Time Loop LEGEND to KMZ:" + f.getName());
                    out.putNextEntry(new ZipEntry(f.getName()));
                    out.write(Messages.readFile(f));
                } catch (Exception e) {
                    logger.error("Error adding legend " + loop.getLegend() + " to kmz." + e);
                }
            }
        } else {
            final GroundOverlayLayer gov = (GroundOverlayLayer) layer;
            kmlDoc = gov.toKML(false, false);
            File f = new File(gov.getTextureURL().toURI());
            String fName = f.getName();
            logger.debug("Adding Ground Overlay to KMZ:" + fName);
            out.putNextEntry(new ZipEntry(fName));
            out.write(Messages.readFile(f));
        }
        out.putNextEntry(new ZipEntry("doc.kml"));
        out.write(kmlDoc.getBytes());
        out.close();
    }
