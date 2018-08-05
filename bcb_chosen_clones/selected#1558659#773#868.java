    private void update() {
        if (VERSION.contains("dev")) return;
        System.out.println(updateURL_s);
        try {
            URL updateURL = new URL(updateURL_s);
            InputStream uis = updateURL.openStream();
            InputStreamReader uisr = new InputStreamReader(uis);
            BufferedReader ubr = new BufferedReader(uisr);
            String header = ubr.readLine();
            if (header.equals("GENREMANUPDATEPAGE")) {
                String cver = ubr.readLine();
                String cdl = ubr.readLine();
                if (!cver.equals(VERSION)) {
                    System.out.println("Update available!");
                    int i = JOptionPane.showConfirmDialog(this, Language.get("UPDATE_AVAILABLE_MSG").replaceAll("%o", VERSION).replaceAll("%c", cver), Language.get("UPDATE_AVAILABLE_TITLE"), JOptionPane.YES_NO_OPTION);
                    if (i == 0) {
                        URL url = new URL(cdl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        if (connection.getResponseCode() / 100 != 2) {
                            throw new Exception("Server error! Response code: " + connection.getResponseCode());
                        }
                        int contentLength = connection.getContentLength();
                        if (contentLength < 1) {
                            throw new Exception("Invalid content length!");
                        }
                        int size = contentLength;
                        File tempfile = File.createTempFile("genreman_update", ".zip");
                        tempfile.deleteOnExit();
                        RandomAccessFile file = new RandomAccessFile(tempfile, "rw");
                        InputStream stream = connection.getInputStream();
                        int downloaded = 0;
                        ProgressWindow pwin = new ProgressWindow(this, "Downloading");
                        pwin.setVisible(true);
                        pwin.setProgress(0);
                        pwin.setText("Connecting...");
                        while (downloaded < size) {
                            byte buffer[];
                            if (size - downloaded > 1024) {
                                buffer = new byte[1024];
                            } else {
                                buffer = new byte[size - downloaded];
                            }
                            int read = stream.read(buffer);
                            if (read == -1) break;
                            file.write(buffer, 0, read);
                            downloaded += read;
                            pwin.setProgress(downloaded / size);
                        }
                        file.close();
                        System.out.println("Downloaded file to " + tempfile.getAbsolutePath());
                        pwin.setVisible(false);
                        pwin.dispose();
                        pwin = null;
                        ZipInputStream zin = new ZipInputStream(new FileInputStream(tempfile));
                        ZipEntry entry;
                        while ((entry = zin.getNextEntry()) != null) {
                            File outf = new File(entry.getName());
                            System.out.println(outf.getAbsoluteFile());
                            if (outf.exists()) outf.delete();
                            OutputStream out = new FileOutputStream(outf);
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = zin.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                            out.close();
                        }
                        JOptionPane.showMessageDialog(this, Language.get("UPDATE_SUCCESS_MSG"), Language.get("UPDATE_SUCCESS_TITLE"), JOptionPane.INFORMATION_MESSAGE);
                        setVisible(false);
                        if (System.getProperty("os.name").indexOf("Windows") != -1) {
                            Runtime.getRuntime().exec("iTunesGenreArtManager.exe");
                        } else {
                            Runtime.getRuntime().exec("java -jar \"iTunes Genre Art Manager.app/Contents/Resources/Java/iTunes_Genre_Art_Manager.jar\"");
                        }
                        System.exit(0);
                    } else {
                    }
                }
                ubr.close();
                uisr.close();
                uis.close();
            } else {
                while (ubr.ready()) {
                    System.out.println(ubr.readLine());
                }
                ubr.close();
                uisr.close();
                uis.close();
                throw new Exception("Update page had invalid header: " + header);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, Language.get("UPDATE_ERROR_MSG"), Language.get("UPDATE_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
