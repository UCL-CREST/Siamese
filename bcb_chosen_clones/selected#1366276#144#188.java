    private Document saveFile(Document document, File file) throws Exception {
        List<Preference> preferences = prefService.findAll();
        if (preferences != null && !preferences.isEmpty()) {
            preference = preferences.get(0);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATEFORMAT_YYYYMMDD);
        String repo = preference.getRepository();
        Calendar calendar = Calendar.getInstance();
        StringBuffer sbRepo = new StringBuffer(repo);
        sbRepo.append(File.separator);
        StringBuffer sbFolder = new StringBuffer(sdf.format(calendar.getTime()));
        sbFolder.append(File.separator).append(calendar.get(Calendar.HOUR_OF_DAY));
        File folder = new File(sbRepo.append(sbFolder).toString());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        FileChannel fcSource = null, fcDest = null;
        try {
            StringBuffer sbFile = new StringBuffer(folder.getAbsolutePath());
            StringBuffer fname = new StringBuffer(document.getId().toString());
            fname.append(".").append(document.getExt());
            sbFile.append(File.separator).append(fname);
            fcSource = new FileInputStream(file).getChannel();
            fcDest = new FileOutputStream(sbFile.toString()).getChannel();
            fcDest.transferFrom(fcSource, 0, fcSource.size());
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
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return document;
    }
