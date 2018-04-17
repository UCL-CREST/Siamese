    public File convert(URI uri) throws DjatokaException {
        processing.add(uri.toString());
        File urlLocal = null;
        try {
            logger.info("processingRemoteURI: " + uri.toURL());
            boolean isJp2 = false;
            InputStream src = IOUtils.getInputStream(uri.toURL());
            String ext = uri.toURL().toString().substring(uri.toURL().toString().lastIndexOf(".") + 1).toLowerCase();
            if (ext.equals(FORMAT_ID_TIF) || ext.equals(FORMAT_ID_TIFF)) {
                urlLocal = File.createTempFile("convert" + uri.hashCode(), "." + FORMAT_ID_TIF);
            } else if (formatMap.containsKey(ext) && (formatMap.get(ext).equals(FORMAT_MIMEYPE_JP2) || formatMap.get(ext).equals(FORMAT_MIMEYPE_JPX))) {
                urlLocal = File.createTempFile("cache" + uri.hashCode(), "." + ext);
                isJp2 = true;
            } else {
                if (src.markSupported()) src.mark(15);
                if (ImageProcessingUtils.checkIfJp2(src)) urlLocal = File.createTempFile("cache" + uri.hashCode(), "." + FORMAT_ID_JP2);
                if (src.markSupported()) src.reset(); else {
                    src.close();
                    src = IOUtils.getInputStream(uri.toURL());
                }
            }
            if (urlLocal == null) {
                urlLocal = File.createTempFile("convert" + uri.hashCode(), ".img");
            }
            urlLocal.deleteOnExit();
            FileOutputStream dest = new FileOutputStream(urlLocal);
            IOUtils.copyStream(src, dest);
            if (!isJp2) urlLocal = processImage(urlLocal, uri);
            src.close();
            dest.close();
            return urlLocal;
        } catch (Exception e) {
            urlLocal.delete();
            throw new DjatokaException(e);
        } finally {
            if (processing.contains(uri.toString())) processing.remove(uri.toString());
        }
    }
