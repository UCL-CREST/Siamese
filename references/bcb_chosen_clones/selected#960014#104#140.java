    private Image retrievePdsImage(double lat, double lon) {
        imageDone = false;
        try {
            StringBuffer urlBuff = new StringBuffer(psdUrl + psdCgi + "?");
            urlBuff.append("DATA_SET_NAME=" + dataSet);
            urlBuff.append("&VERSION=" + version);
            urlBuff.append("&PIXEL_TYPE=" + pixelType);
            urlBuff.append("&PROJECTION=" + projection);
            urlBuff.append("&STRETCH=" + stretch);
            urlBuff.append("&GRIDLINE_FREQUENCY=" + gridlineFrequency);
            urlBuff.append("&SCALE=" + URLEncoder.encode(scale));
            urlBuff.append("&RESOLUTION=" + resolution);
            urlBuff.append("&LATBOX=" + latbox);
            urlBuff.append("&LONBOX=" + lonbox);
            urlBuff.append("&BANDS_SELECTED=" + bandsSelected);
            urlBuff.append("&LAT=" + lat);
            urlBuff.append("&LON=" + lon);
            URL url = new URL(urlBuff.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String result = null;
            String line;
            String imageSrc;
            int count = 0;
            while ((line = in.readLine()) != null) {
                if (count == 6) result = line;
                count++;
            }
            int startIndex = result.indexOf("<TH COLSPAN=2 ROWSPAN=2><IMG SRC = \"") + 36;
            int endIndex = result.indexOf("\"", startIndex);
            imageSrc = result.substring(startIndex, endIndex);
            URL imageUrl = new URL(imageSrc);
            return (Toolkit.getDefaultToolkit().getImage(imageUrl));
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return null;
    }
