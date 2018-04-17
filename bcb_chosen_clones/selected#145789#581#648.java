    private void readArchives(final VideoArchiveSet vas) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = null;
        try {
            while ((line = in.readLine()) != null) {
                if (line.startsWith("ARCHIVE")) {
                    final StringTokenizer s = new StringTokenizer(line);
                    s.nextToken();
                    final Integer tapeNumber = Integer.valueOf(s.nextToken());
                    final Timecode timeCode = new Timecode(s.nextToken());
                    final VideoArchive va = new VideoArchive();
                    va.setTimeCode(timeCode);
                    va.setTapeNumber(tapeNumber);
                    vas.addVideoArchive(va);
                    archives.put(tapeNumber, va);
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            in.close();
        }
        if (archives.size() == 0) {
            log.warn("No lines with ARCHIVE were found in the current vif file, will try to look at another vif with same yearday, " + "ship and platform and try to get archives from there:");
            String urlBase = url.getPath().toString().substring(0, url.getPath().toString().lastIndexOf("/"));
            File vifDir = new File(urlBase);
            File[] allYeardayFiles = vifDir.listFiles();
            for (int i = 0; i < allYeardayFiles.length; i++) {
                if (allYeardayFiles[i].toString().endsWith(".vif")) {
                    String filename = allYeardayFiles[i].toString().substring(allYeardayFiles[i].toString().lastIndexOf("/"));
                    String fileLC = filename.toLowerCase();
                    String toLookFor = new String(new Character(vifMetadata.shipCode).toString() + new Character(vifMetadata.platformCode).toString());
                    String toLookForLC = toLookFor.toLowerCase();
                    if (fileLC.indexOf(toLookForLC) >= 0) {
                        log.warn("Will try to read archives from file " + allYeardayFiles[i]);
                        final BufferedReader tempIn = new BufferedReader(new FileReader(allYeardayFiles[i]));
                        String tempLine = null;
                        try {
                            while ((tempLine = tempIn.readLine()) != null) {
                                if (tempLine.startsWith("ARCHIVE")) {
                                    final StringTokenizer s = new StringTokenizer(tempLine);
                                    s.nextToken();
                                    final Integer tapeNumber = Integer.valueOf(s.nextToken());
                                    final Timecode timeCode = new Timecode(s.nextToken());
                                    final VideoArchive va = new VideoArchive();
                                    va.setTimeCode(timeCode);
                                    va.setTapeNumber(tapeNumber);
                                    vas.addVideoArchive(va);
                                    archives.put(tapeNumber, va);
                                }
                            }
                        } catch (IOException e) {
                            throw e;
                        } finally {
                            tempIn.close();
                        }
                    }
                }
                if (archives.size() > 0) {
                    log.warn("Found " + archives.size() + " archives in that vif so will use that");
                    break;
                }
            }
            if (archives.size() == 0) {
                log.warn("Still no archives were found in the file. Unable to process it.");
            }
        }
    }
