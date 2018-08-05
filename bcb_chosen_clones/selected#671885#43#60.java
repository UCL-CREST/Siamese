    public LinkedList<Image> scanForMissingImages() {
        LinkedList<Image> undefinedImages = new LinkedList<Image>();
        Pattern p = Pattern.compile(imagePattern);
        Matcher m = p.matcher(targetCode.toLowerCase());
        while (m.find()) {
            Pattern p1 = Pattern.compile(innerImagePattern);
            Matcher m1 = p1.matcher(m.group().toLowerCase());
            if (m1.find()) {
                String[] src = null;
                if (m1.group().contains("\"")) src = m1.group().split("\""); else src = m1.group().split("'");
                if (!isImageAvailable(src[1])) {
                    undefinedImages.add(new Image(src[1], m.start(), m.end()));
                    Activator.getDefault().logDebuggingData("[ImageScanner]: Added '" + src[1] + "' to the list of undefined images.");
                }
            }
        }
        return undefinedImages;
    }
