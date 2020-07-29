    private void runSingle() {
        try {
            int page = 1;
            String url = parser.getMangaLocation(manga.getName(), chapter);
            if (url == null) {
                if (parser.checkManga(manga.getName())) JOptionPane.showMessageDialog(gui, "The chapter " + chapter + " of " + manga.getName() + " is no more available on mangastream.", "Chapter not available.", JOptionPane.ERROR_MESSAGE); else JOptionPane.showMessageDialog(gui, "The manga " + manga.getName() + " was not found. It was probably dropped by mangastream.", "Manga not found.", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<String> links = parser.getPageLinks(url);
            gui.setChapProgressBar(links.size());
            gui.nextPage(String.valueOf(page), links.size(), manga, chapter);
            for (String string : links) {
                if (!running) break;
                ImageProperties img = parser.getImageLink(string);
                File out = new File(f.getAbsolutePath() + File.separator + "Page" + (page < 10 ? "0" : "") + page + img.getFileType());
                gui.setPgProgressBar(100);
                float percentage = 0;
                for (String ID : img.getIDs()) {
                    URI uri = new URI(img.getLocation(ID));
                    HttpURLConnection huc = new HttpURLConnection(uri.toURL(), Proxy.NO_PROXY);
                    InputStream is = huc.getInputStream();
                    ImageInputStream iis = ImageIO.createImageInputStream(is);
                    Iterator<ImageReader> it = ImageIO.getImageReaders(iis);
                    ImageReader ir = it.next();
                    float sp = img.getSizePercentage(ID);
                    ir.addIIOReadProgressListener(new ProgressListener(gui, sp, percentage));
                    percentage += sp * 100;
                    ir.setInput(iis);
                    Image image = ir.read(0);
                    img.setData(ID, image);
                    is.close();
                }
                BufferedImage image = img.getFullImage();
                FileOutputStream fos = new FileOutputStream(out);
                String type = img.getFileType().substring(1);
                ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
                Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix(type);
                ImageWriter iw = it.next();
                iw.setOutput(ios);
                iw.write(image);
                gui.nextPage(String.valueOf(page), links.size(), manga, chapter);
                page++;
                gui.updateChapProgressBar(page - 1);
                fos.close();
                ios.close();
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (gui.isZip()) {
                int BUFFER = 2048;
                BufferedInputStream origin = null;
                File z = new File(f.getParent() + File.separator + f.getName() + ".zip");
                FileOutputStream fos = new FileOutputStream(z);
                ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
                String files[] = f.list();
                byte data[] = new byte[BUFFER];
                for (int i = 0; i < files.length; i++) {
                    FileInputStream fi = new FileInputStream(f + File.separator + files[i]);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry ze = new ZipEntry(files[i]);
                    zos.putNextEntry(ze);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        zos.write(data, 0, count);
                    }
                    origin.close();
                    fi.close();
                }
                zos.close();
                fos.close();
                if (gui.isDel()) {
                    String f2[] = f.list();
                    for (int i = 0; i < f2.length; i++) {
                        File fd = new File(f + File.separator + f2[i]);
                        System.out.println("Deleting " + fd);
                        if (!fd.delete()) System.out.println("Failed to delete " + fd);
                    }
                    f.delete();
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
