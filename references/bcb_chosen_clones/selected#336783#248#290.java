    @Override
    public boolean copy(Document document, Folder folder) throws Exception {
        boolean isCopied = false;
        if (document.getId() != null && folder.getId() != null) {
            Document copiedDoc = new DocumentModel();
            copiedDoc.setValues(document.getValues());
            copiedDoc.setFolder(folder);
            copiedDoc.setId(null);
            em.persist(copiedDoc);
            resourceAuthorityService.applyAuthority(copiedDoc);
            List<Preference> preferences = prefService.findAll();
            Preference preference = new PreferenceModel();
            if (preferences != null && !preferences.isEmpty()) {
                preference = preferences.get(0);
            }
            String repo = preference.getRepository();
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATEFORMAT_YYYYMMDD);
            Calendar calendar = Calendar.getInstance();
            StringBuffer sbRepo = new StringBuffer(repo);
            sbRepo.append(File.separator);
            StringBuffer sbFolder = new StringBuffer(sdf.format(calendar.getTime()));
            sbFolder.append(File.separator).append(calendar.get(Calendar.HOUR_OF_DAY));
            File fFolder = new File(sbRepo.append(sbFolder).toString());
            if (!fFolder.exists()) {
                fFolder.mkdirs();
            }
            copiedDoc.setLocation(sbFolder.toString());
            em.merge(copiedDoc);
            File in = new File(repo + File.separator + document.getLocation() + File.separator + document.getId() + "." + document.getExt());
            File out = new File(fFolder.getAbsolutePath() + File.separator + copiedDoc.getId() + "." + copiedDoc.getExt());
            FileChannel inChannel = new FileInputStream(in).getChannel();
            FileChannel outChannel = new FileOutputStream(out).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } catch (IOException e) {
                throw e;
            } finally {
                if (inChannel != null) inChannel.close();
                if (outChannel != null) outChannel.close();
            }
        }
        return isCopied;
    }
