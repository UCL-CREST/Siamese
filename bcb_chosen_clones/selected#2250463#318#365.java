    public String merge(int width, int height) throws Exception {
        htErrors.clear();
        sendGetImageRequests(width, height);
        Vector files = new Vector();
        ConcurrentHTTPTransactionHandler c = new ConcurrentHTTPTransactionHandler();
        c.setCache(cache);
        c.checkIfModified(false);
        for (int i = 0; i < vImageUrls.size(); i++) {
            if ((String) vImageUrls.get(i) != null) {
                c.register((String) vImageUrls.get(i));
            } else {
            }
        }
        c.doTransactions();
        vTransparency = new Vector();
        for (int i = 0; i < vImageUrls.size(); i++) {
            if (vImageUrls.get(i) != null) {
                String path = c.getResponseFilePath((String) vImageUrls.get(i));
                if (path != null) {
                    String contentType = c.getHeaderValue((String) vImageUrls.get(i), "content-type");
                    if (contentType.startsWith("image")) {
                        files.add(path);
                        vTransparency.add(htTransparency.get(vRank.get(i)));
                    }
                }
            }
        }
        if (files.size() > 1) {
            File output = TempFiles.getFile();
            String path = output.getPath();
            ImageMerger.mergeAndSave(files, vTransparency, path, ImageMerger.GIF);
            imageName = output.getName();
            imagePath = output.getPath();
            return (imageName);
        } else if (files.size() == 1) {
            File f = new File((String) files.get(0));
            File out = TempFiles.getFile();
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(out));
            byte buf[] = new byte[1024];
            for (int nRead; (nRead = is.read(buf, 0, 1024)) > 0; os.write(buf, 0, nRead)) ;
            os.flush();
            os.close();
            is.close();
            imageName = out.getName();
            return imageName;
        } else return "";
    }
