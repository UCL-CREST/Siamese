    public boolean upload(Distribution dist) throws FileNotFoundException {
        byte[] buf = new byte[4096];
        int i;
        logger.log(Level.INFO, "Zipping...");
        try {
            FileOutputStream fos = new FileOutputStream(new File(tempDir, tile.toString() + ".zip"));
            ZipOutputStream zipout = new ZipOutputStream(fos);
            zipout.setLevel(0);
            int j, k;
            int maxz;
            int num = 1;
            int z = tile.getZ(), x = tile.getX(), y = tile.getY();
            if (z < 11) {
                maxz = z;
            } else {
                maxz = Integer.parseInt(config.get("layers." + tile.getLayer() + ".maxz"));
            }
            Tile t = new Tile();
            t.setLayer(tile.getLayer());
            for (; z <= maxz; z++, num <<= 1, x <<= 1, y <<= 1) {
                for (k = 0; k < num; k++) {
                    for (j = 0; j < num; j++) {
                        t.setLayer(tile.getLayer());
                        t.setZXY(z, x + k, y + j);
                        InputStream is = dist.getFromLocal(t);
                        ZipEntry zipe = new ZipEntry(t.toFileName());
                        zipout.putNextEntry(zipe);
                        while ((i = is.read(buf)) >= 0) {
                            zipout.write(buf, 0, i);
                        }
                        zipout.closeEntry();
                        is.close();
                    }
                }
            }
            zipout.close();
            fos.close();
            logger.log(Level.INFO, "done!");
            logger.log(Level.INFO, "uploading..");
            HttpClient httpclient = new HttpClient();
            PostMethod method = new PostMethod(config.get("uploadURL"));
            Part[] formparam = new Part[] { new StringPart("user", config.get("username")), new StringPart("passwd", config.get("password")), new StringPart("layer", tile.getLayer()), new StringPart("z", Integer.toString(tile.getZ())), new FilePart("file", new File(tempDir, tile.toString() + ".zip")) };
            MultipartRequestEntity m = new MultipartRequestEntity(formparam, method.getParams());
            method.setRequestEntity(m);
            int response = httpclient.executeMethod(method);
            if (response == 200) {
                logger.log(Level.INFO, ".." + method.getStatusText());
                BufferedReader br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    logger.log(Level.FINE, line);
                }
                for (File f : tempDir.listFiles()) {
                    f.delete();
                }
            }
            return true;
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (ZipException ex) {
            logger.log(Level.SEVERE, "Zip exception: " + ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IO exception: " + ex);
        }
        return false;
    }
