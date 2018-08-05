    public static byte[] handleNestedImages(String resourceFolder, String resourceName, boolean isCustomResource, boolean isResourceFromAssetsFolder, byte[] srcBytesBuff) {
        String fullResourcePath = Yui4JSFResourceLoaderPhaseListener.populateResourcePath(resourceFolder, resourceName, isCustomResource, isResourceFromAssetsFolder);
        int start = 0, end = 0;
        String srcBuffer = new String(srcBytesBuff);
        Pattern pattern = Pattern.compile(imagesFilesRegex);
        Matcher matcher = pattern.matcher(srcBuffer);
        String output = "";
        while (matcher.find()) {
            List fullDirectoryPathTokens = getFullResourcePathTokens(fullResourcePath);
            if (srcBuffer.charAt(matcher.start()) == '\"' || srcBuffer.charAt(matcher.start()) == '(' || srcBuffer.charAt(matcher.start()) == '\'') {
                start = matcher.start() + 1;
                end = matcher.end() - 1;
            } else {
                start = matcher.start();
                end = matcher.end();
            }
            String beforeContent = srcBuffer.substring(0, start);
            output += beforeContent;
            String imagePathString = srcBuffer.substring(start, end);
            List imagePathStringTokens = getImagePathStringTokens(imagePathString);
            boolean isUnderCurrentDirectory = isUnderCurrentDirectory(imagePathStringTokens);
            if (isUnderCurrentDirectory) {
                output += "yui4jsfResources.jsf?name=" + getCurrentFileDirectoryPath(resourceName) + srcBuffer.substring(start, end) + "&folder=" + resourceFolder + "&fromAssets=" + isResourceFromAssetsFolder + "&custom=" + isCustomResource;
            } else {
                output += applyFolderNavigation(fullDirectoryPathTokens, imagePathStringTokens);
            }
            srcBuffer = srcBuffer.substring(end);
            matcher = pattern.matcher(srcBuffer);
        }
        output += srcBuffer;
        return output.getBytes();
    }
