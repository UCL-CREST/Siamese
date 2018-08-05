    private String mergeSingleFile(String fromFileName, String toFileName, Element dataRootElement) throws FileNotFoundException, IOException, BarcodeException {
        List<ImageToMerge> imagesToMerge = new ArrayList<ImageToMerge>();
        List<ImageInFile> imagesInFile = new ArrayList<ImageInFile>();
        StoredManifest manifest = null;
        StoredStyle style = null;
        FileInputStream fis = new FileInputStream(fromFileName);
        FileOutputStream fos = new FileOutputStream(toFileName);
        ZipInputStream zi = new ZipInputStream(fis);
        ZipOutputStream zo = new ZipOutputStream(fos);
        ZipEntry ze = zi.getNextEntry();
        XMLContent officeText = null;
        while (ze != null) {
            if (ze.getName().endsWith(CONTENT_FILE)) {
                ZipEntry ze1 = new ZipEntry(ze.getName().replace(File.pathSeparatorChar, '/'));
                zo.putNextEntry(ze1);
                officeText = mergeFile(zi, zo, dataRootElement, imagesToMerge);
                for (ImageToMerge imageToMerge : imagesToMerge) imageToMerge.addToODF(zo);
            } else if (ze.getName().contains(PICTURES_DIR)) {
                imagesInFile.add(new ImageInFile(ze.getName(), zi));
            } else if (ze.getName().endsWith(MANIFEST_FILE)) {
                manifest = new StoredManifest(zi);
            } else if (ze.getName().endsWith(STYLES_FILE)) {
                ZipEntry ze1 = new ZipEntry(ze.getName().replace(File.pathSeparatorChar, '/'));
                zo.putNextEntry(ze1);
                style = new StoredStyle(zi);
                zo.write(style.getData());
            } else {
                ZipEntry ze1 = new ZipEntry(ze.getName().replace(File.pathSeparatorChar, '/'));
                zo.putNextEntry(ze1);
                moveFile(zi, zo);
            }
            zo.flush();
            ze = zi.getNextEntry();
        }
        if (officeText != null) {
            List<String> fileNames = new ArrayList<String>();
            for (ImageInFile imageInFile : imagesInFile) {
                String fileName = imageInFile.getFileName();
                if (officeText.containsImageFile(fileName) || (style != null && style.containsImageFile(fileName))) {
                    ZipEntry ze1 = new ZipEntry(fileName.replace(File.pathSeparatorChar, '/'));
                    zo.putNextEntry(ze1);
                    zo.write(imageInFile.getData());
                    zo.flush();
                    fileNames.add(fileName);
                }
            }
            for (ImageToMerge imageToMerge : imagesToMerge) fileNames.add(imageToMerge.getImageName());
            if (manifest != null) {
                manifest.setAllPictures(fileNames);
                ZipEntry ze1 = new ZipEntry("META-INF/manifest.xml");
                zo.putNextEntry(ze1);
                zo.write(manifest.getData());
                zo.flush();
            }
        }
        zo.close();
        zi.close();
        fis.close();
        fos.close();
        return toFileName;
    }
