    private void addQDInformation() {
        try {
            long qdDate = System.currentTimeMillis();
            if (_local == true) {
                File qdFile = new File("qdinfo.dat");
                if (!qdFile.exists()) {
                    return;
                }
                qdDate = qdFile.lastModified();
            }
            if (qdDate > this._qdFileDate) {
                this._qdFileDate = qdDate;
                for (int ii = 0; ii < this._projectInfo.size(); ii++) {
                    Information info = getInfo(ii);
                    if (info != null) {
                        info._qdValue = null;
                    }
                }
                Reader reader = null;
                if (_local == true) {
                    reader = new FileReader("qdinfo.dat");
                } else {
                    StringBuffer urlName = new StringBuffer();
                    urlName.append("http://boston.quik.com/rph/");
                    urlName.append("qdinfo.dat");
                    try {
                        URL url = new URL(urlName.toString());
                        InputStream stream = url.openStream();
                        reader = new InputStreamReader(stream);
                    } catch (MalformedURLException mue) {
                        mue.printStackTrace();
                    }
                }
                BufferedReader file = new BufferedReader(reader);
                try {
                    String line = null;
                    while ((line = file.readLine()) != null) {
                        if (line.startsWith("pg ")) {
                            this._qdDate = Long.parseLong(line.substring(3), 16);
                            this._qdDate = (this._qdDate + 946684800) * 1000;
                        } else if (line.startsWith("pt ")) {
                            line = line.substring(3).trim();
                            int pos = -1;
                            while ((line.length() > 0) && ((pos = line.indexOf(' ')) > 0)) {
                                int projectNum = 0;
                                Double value = null;
                                if (pos > 0) {
                                    projectNum = Integer.parseInt(line.substring(0, pos));
                                    line = line.substring(pos).trim();
                                }
                                pos = line.indexOf(' ');
                                if (pos > 0) {
                                    value = new Double((double) Integer.parseInt(line.substring(0, pos)) / 100);
                                    line = line.substring(pos).trim();
                                }
                                Information info = getInfo(projectNum);
                                if (info == null) {
                                    info = createInfo(projectNum);
                                }
                                if (info._qdValue == null) {
                                    info._qdValue = value;
                                }
                            }
                        }
                    }
                } finally {
                    file.close();
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }
