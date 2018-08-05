    private Document saveFile(Document document, File file) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATEFORMAT_YYYYMMDD);
        List<Preference> preferences = prefService.findAll();
        if (preferences != null && !preferences.isEmpty()) {
            Preference preference = preferences.get(0);
            String repo = preference.getRepository();
            StringBuffer sbRepo = new StringBuffer(repo);
            sbRepo.append(File.separator);
            StringBuffer sbFolder = new StringBuffer(document.getLocation());
            File folder = new File(sbRepo.append(sbFolder).toString());
            log.info("Check in file ID [" + document.getId() + "] to " + folder.getAbsolutePath());
            if (!folder.exists()) {
                folder.mkdirs();
            }
            FileChannel fcSource = null, fcDest = null, fcVersionDest = null;
            try {
                StringBuffer sbFile = new StringBuffer(folder.getAbsolutePath()).append(File.separator).append(document.getId()).append(".").append(document.getExt());
                StringBuffer sbVersionFile = new StringBuffer(folder.getAbsolutePath()).append(File.separator).append(document.getId()).append("_").append(document.getVersion().toString()).append(".").append(document.getExt());
                fcSource = new FileInputStream(file).getChannel();
                fcDest = new FileOutputStream(sbFile.toString()).getChannel();
                fcVersionDest = new FileOutputStream(sbVersionFile.toString()).getChannel();
                fcDest.transferFrom(fcSource, 0, fcSource.size());
                fcSource = new FileInputStream(file).getChannel();
                fcVersionDest.transferFrom(fcSource, 0, fcSource.size());
                document.setLocation(sbFolder.toString());
                documentService.save(document);
            } catch (FileNotFoundException notFoundEx) {
                log.error("saveFile file not found: " + document.getName(), notFoundEx);
            } catch (IOException ioEx) {
                log.error("saveFile IOException: " + document.getName(), ioEx);
            } finally {
                try {
                    if (fcSource != null) {
                        fcSource.close();
                    }
                    if (fcDest != null) {
                        fcDest.close();
                    }
                    if (fcVersionDest != null) {
                        fcVersionDest.close();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return document;
    }
