    public void writeImage(List<BufferedImage> frames, String mimeType, OutputStream out) throws IOException {
        StringBuffer kml = new StringBuffer();
        for (int frameIndex = 0; frameIndex < frames.size(); frameIndex++) {
            if (frameIndex == 0) {
                kml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                kml.append(System.getProperty("line.separator"));
                kml.append("<kml xmlns=\"http://earth.google.com/kml/2.0\">");
                kml.append("<Folder>");
                kml.append("<visibility>1</visibility>");
                kml.append("<name>" + this.layer.getDataset().getId() + ", " + this.layer.getId() + "</name>");
                kml.append("<description>" + this.layer.getDataset().getTitle() + ", " + this.layer.getTitle() + ": " + this.layer.getAbstract() + "</description>");
                kml.append("<ScreenOverlay>");
                kml.append("<name>Colour scale</name>");
                kml.append("<Icon><href>" + COLOUR_SCALE_FILENAME + "</href></Icon>");
                kml.append("<overlayXY x=\"0\" y=\"1\" xunits=\"fraction\" yunits=\"fraction\"/>");
                kml.append("<screenXY x=\"0\" y=\"1\" xunits=\"fraction\" yunits=\"fraction\"/>");
                kml.append("<rotationXY x=\"0\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>");
                kml.append("<size x=\"0\" y=\"0\" xunits=\"fraction\" yunits=\"fraction\"/>");
                kml.append("</ScreenOverlay>");
            }
            kml.append("<GroundOverlay>");
            String timestamp = null;
            String z = null;
            if (this.tValues.get(frameIndex) != null && !this.tValues.get(frameIndex).equals("")) {
                long millisecondsSinceEpoch = WmsUtils.iso8601ToMilliseconds(this.tValues.get(frameIndex));
                timestamp = WmsUtils.millisecondsToISO8601(millisecondsSinceEpoch);
                kml.append("<TimeStamp><when>" + timestamp + "</when></TimeStamp>");
            }
            if (this.zValue != null && !this.zValue.equals("") && this.layer.getZvalues() != null) {
                z = "";
                if (timestamp != null) z += "<br />";
                z += "Elevation: " + this.zValue + " " + this.layer.getZunits();
            }
            kml.append("<name>");
            if (timestamp == null && z == null) {
                kml.append("Frame " + frameIndex);
            } else {
                kml.append("<![CDATA[");
                if (timestamp != null) {
                    kml.append("Time: " + timestamp);
                }
                if (z != null) {
                    kml.append(z);
                }
                kml.append("]]>");
            }
            kml.append("</name>");
            kml.append("<visibility>1</visibility>");
            kml.append("<Icon><href>" + getPicFileName(frameIndex) + "</href></Icon>");
            kml.append("<LatLonBox id=\"" + frameIndex + "\">");
            kml.append("<west>" + this.bbox[0] + "</west>");
            kml.append("<south>" + this.bbox[1] + "</south>");
            kml.append("<east>" + this.bbox[2] + "</east>");
            kml.append("<north>" + this.bbox[3] + "</north>");
            kml.append("<rotation>0</rotation>");
            kml.append("</LatLonBox>");
            kml.append("</GroundOverlay>");
        }
        kml.append("</Folder>");
        kml.append("</kml>");
        ZipOutputStream zipOut = new ZipOutputStream(out);
        logger.debug("Writing KML file to KMZ file");
        ZipEntry kmlEntry = new ZipEntry(this.layer.getDataset().getId() + "_" + this.layer.getId() + ".kml");
        kmlEntry.setTime(System.currentTimeMillis());
        zipOut.putNextEntry(kmlEntry);
        zipOut.write(kml.toString().getBytes());
        int frameIndex = 0;
        logger.debug("Writing frames to KMZ file");
        for (BufferedImage frame : frames) {
            ZipEntry picEntry = new ZipEntry(getPicFileName(frameIndex));
            frameIndex++;
            zipOut.putNextEntry(picEntry);
            ImageIO.write(frame, PICEXT, zipOut);
        }
        logger.debug("Constructing colour scale image");
        ZipEntry scaleEntry = new ZipEntry(COLOUR_SCALE_FILENAME);
        zipOut.putNextEntry(scaleEntry);
        logger.debug("Writing colour scale image to KMZ file");
        ImageIO.write(this.getLegend(), PICEXT, zipOut);
        zipOut.close();
    }
