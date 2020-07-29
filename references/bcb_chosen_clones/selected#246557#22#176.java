    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jar = request.getParameter("jar");
        String file = request.getParameter("file");
        if (file != null) file = StealthnetServer.FINISHED_DOWNLOAD_FOLDER + "/" + IOUtils.getFilename(file);
        if (request.getParameter("shfile") != null) file = request.getParameter("shfile");
        String img = request.getParameter("img");
        File tmpFile = null;
        String filename = null;
        byte[] b = new byte[0];
        if (jar != null) {
            try {
                response.setHeader("Cache-Control", "max-age=" + 60 * 30);
                filename = IOUtils.getFilename(jar);
                b = IOUtils.readJarFile(jar);
                if (jar.indexOf(".css") > 0) response.setContentType("text/css"); else if (jar.indexOf(".js") > 0) response.setContentType("application/x-javascript");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (request.getParameter("generateCollection") != null) {
            String[] values = request.getParameterValues("collection");
            filename = request.getParameter("collectionName");
            if (HelperStd.isEmpty(filename)) filename = request.getParameter("collectionName2");
            if (HelperStd.isEmpty(filename)) filename = "mycollection.sncollection"; else filename += ".sncollection";
            tmpFile = File.createTempFile(filename, ".sncollection");
            tmpFile.deleteOnExit();
            FileOutputStream fout = new FileOutputStream(tmpFile);
            File xml = new File(StealthnetServer.WORKING_DIR + "preferences/sharedfiles.xml");
            for (String path : values) {
                try {
                    File f = new File(path);
                    path = path.replace('/', File.separatorChar);
                    String name = path.substring(path.lastIndexOf(File.separatorChar) + 1);
                    if (f.exists()) {
                        String hex = IOUtils.getFileHashFromXML(xml, f);
                        String val = "stealthnet://?hash=" + hex.toUpperCase() + "&name=" + name + "&size=" + f.length() + "\n";
                        fout.write(val.getBytes());
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            fout.close();
            response.setContentType("text/plain");
        } else if (request.getParameter("generateZIPDownload") != null) {
            filename = request.getParameter("collectionName");
            if (HelperStd.isEmpty(filename)) filename = request.getParameter("collectionName2");
            if (HelperStd.isEmpty(filename)) filename = "myzip.zip"; else filename += ".zip";
            tmpFile = File.createTempFile(filename, ".zip");
            tmpFile.deleteOnExit();
            FileOutputStream fout = new FileOutputStream(tmpFile);
            String[] values = request.getParameterValues("collection");
            String folder = StealthnetServer.getSettingsValue("IncomingDirectory");
            folder = folder.replace('/', File.separatorChar);
            if (folder.charAt(folder.length() - 1) != File.separatorChar) folder = folder + File.separatorChar;
            ZipOutputStream out = new ZipOutputStream(fout);
            out.setMethod(ZipOutputStream.DEFLATED);
            for (String path : values) {
                try {
                    File f = new File(path);
                    path = path.replace('/', File.separatorChar);
                    if (path.toLowerCase().indexOf(folder.toLowerCase()) == 0) {
                        path = path.substring(folder.length());
                        if (path.charAt(0) == File.separatorChar) path = path.substring(1);
                    } else {
                        int p = path.lastIndexOf(File.separatorChar);
                        int p2 = path.lastIndexOf(File.separatorChar, p - 1);
                        if (p2 > 0) {
                            p = p2;
                            p2 = path.lastIndexOf(File.separatorChar, p - 1);
                        }
                        if (p2 > 0) p = p2;
                        path = path.substring(p + 1);
                        if (path.charAt(0) == File.separatorChar) path = path.substring(1);
                    }
                    String name = path.replace(File.separatorChar, '/');
                    if (f.exists()) {
                        FileInputStream fin = new FileInputStream(f);
                        byte[] da = new byte[(int) f.length()];
                        fin.read(da);
                        fin.close();
                        ZipEntry entry = new ZipEntry(name);
                        out.putNextEntry(entry);
                        out.write(da);
                        out.closeEntry();
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            out.finish();
            out.flush();
            out.close();
            response.setContentType("application/zip");
        } else if (file != null) {
            boolean ok = false;
            for (IdValuePair dir : StealthnetServer.getSharedDirectories()) {
                if (file.indexOf(dir.value) == 0) {
                    ok = true;
                    break;
                }
            }
            File f = new File(file);
            if (ok && f.exists()) {
                filename = IOUtils.getFilename(file);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                response.setContentType(guessContentType(response, file));
                OutputStream out = response.getOutputStream();
                response.setContentLength((int) f.length());
                FileInputStream fin = new FileInputStream(f);
                b = new byte[1024 * 10];
                int read = 0;
                while (read < f.length()) {
                    int r = fin.read(b);
                    out.write(b, 0, r);
                    read += r;
                }
                fin.close();
                out.flush();
                out.close();
                return;
            }
        } else if (img != null) {
            filename = IOUtils.getFilename(img);
            while (img.charAt(0) == '.' || img.charAt(0) == '/') {
                img = img.substring(1);
            }
            try {
                b = IOUtils.readBinaryFile(img);
                if (img.indexOf(".png") > 0) response.setContentType("image/png"); else if (img.indexOf(".jpg") > 0) response.setContentType("image/jpg"); else if (img.indexOf(".jpeg") > 0) response.setContentType("image/jpeg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (filename != null) response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        OutputStream out = response.getOutputStream();
        if (tmpFile != null) {
            response.setContentLength((int) tmpFile.length());
            FileInputStream fin = new FileInputStream(tmpFile);
            b = new byte[1024 * 10];
            int read = 0;
            while (read < tmpFile.length()) {
                int r = fin.read(b);
                out.write(b, 0, r);
                read += r;
            }
            fin.close();
            tmpFile.delete();
        } else {
            response.setContentLength(b.length);
            out.write(b);
        }
        out.flush();
        out.close();
    }
