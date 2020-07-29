    private void addEMInformation() {
        try {
            long emDate = System.currentTimeMillis();
            if (_local == true) {
                File emFile = new File("emprotz.dat");
                if (!emFile.exists()) {
                    return;
                }
                emDate = emFile.lastModified();
            }
            if (emDate > this._emFileDate) {
                this._emFileDate = emDate;
                this._emDate = emDate;
                for (int ii = 0; ii < this._projectInfo.size(); ii++) {
                    Information info = getInfo(ii);
                    if (info != null) {
                        info._emDeadline = null;
                        info._emFrames = null;
                        info._emValue = null;
                    }
                }
                Reader reader = null;
                if (_local == true) {
                    reader = new FileReader("emprotz.dat");
                } else {
                    StringBuffer urlName = new StringBuffer();
                    urlName.append("http://home.comcast.net/");
                    urlName.append("~wxdude1/emsite/download/");
                    urlName.append("emprotz.zip");
                    try {
                        URL url = new URL(urlName.toString());
                        InputStream stream = url.openStream();
                        ZipInputStream zip = new ZipInputStream(stream);
                        zip.getNextEntry();
                        reader = new InputStreamReader(zip);
                    } catch (MalformedURLException mue) {
                        mue.printStackTrace();
                    }
                }
                BufferedReader file = new BufferedReader(reader);
                try {
                    String line1 = null;
                    int count = 0;
                    while ((line1 = file.readLine()) != null) {
                        String line2 = (line1 != null) ? file.readLine() : null;
                        String line3 = (line2 != null) ? file.readLine() : null;
                        String line4 = (line3 != null) ? file.readLine() : null;
                        count++;
                        if ((count > 1) && (line1 != null) && (line2 != null) && (line3 != null) && (line4 != null)) {
                            if (line1.length() > 2) {
                                int posBegin = line1.indexOf("\"", 0);
                                int posEnd = line1.indexOf("\"", posBegin + 1);
                                if ((posBegin >= 0) && (posEnd >= 0)) {
                                    String project = line1.substring(posBegin + 1, posEnd - posBegin);
                                    int projectNum = Integer.parseInt(project);
                                    Integer deadline = Integer.valueOf(line2.trim());
                                    Double value = Double.valueOf(line3.trim());
                                    Integer frames = Integer.valueOf(line4.trim());
                                    Information info = getInfo(projectNum);
                                    if (info == null) {
                                        info = createInfo(projectNum);
                                    }
                                    if (info._emValue == null) {
                                        info._emDeadline = deadline;
                                        info._emFrames = frames;
                                        info._emValue = value;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    file.close();
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }
