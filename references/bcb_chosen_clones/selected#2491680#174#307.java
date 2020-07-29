    private File getArticleZip(InfoAccess infoaccess, ArticleFoxConfig config, Dictionary dict, String uploadPath, InfoBean article) throws IOException {
        boolean prefixComments = config.isDownloadPrefixCommentsWithLanguage();
        boolean generateSeparate = config.isDownloadGenerateSeparateTexts();
        boolean generateCombined = config.isDownloadGenerateCombinedText();
        boolean utf8 = config.isDownloadGenerateUTF8();
        boolean utf16 = config.isDownloadGenerateUTF16();
        Date articleDate = (Date) article.getProperty(TArticles.F_DATE);
        String dateStr = DateUtils.dateFormatter.format(articleDate);
        String categoryName = article.getOneRelation(TArticles.F_CATEGORY).getString(TCategories.F_NAME);
        String downloadName = dateStr + "_" + categoryName + "_" + article.getString(TArticles.F_NAME);
        downloadName = downloadName.replaceAll("\\s", "-");
        downloadName = StringUtils.removeSpecialChars(downloadName);
        new File(ArticleFoxConstants.TMP_DIR_PATH).mkdirs();
        File file = new File(ArticleFoxConstants.TMP_DIR_PATH + downloadName + "_" + new Date().getTime() + ".zip");
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(file));
        final String topDirPath = downloadName + "/";
        zipOut.putNextEntry(new ZipEntry(topDirPath));
        Map<Integer, InfoBean> latestOfTask = new HashMap<Integer, InfoBean>();
        String filter = TTexts.F_ARTICLE + "=" + article.getId();
        Collection<InfoBean> coll = infoaccess.query(TTexts.TBL_NAME, filter, null, true);
        for (InfoBean text : coll) {
            Integer curPhase = (Integer) text.getProperty(TTexts.F_PHASE);
            Integer curLanguage = (Integer) text.getProperty(TTexts.F_LANGUAGE);
            if (curPhase >= Phases.CORRECT) {
                latestOfTask.put(curLanguage, text);
            }
        }
        final String uploadDirPath = uploadPath + article.getId() + "/";
        Map<String, AttachmentInfo> attachmentInfos = new HashMap<String, AttachmentInfo>();
        for (InfoBean text : latestOfTask.values()) {
            Integer curPhase = (Integer) text.getProperty(TTexts.F_PHASE);
            Integer curLanguage = (Integer) text.getProperty(TTexts.F_LANGUAGE);
            String title = text.getString(TTexts.F_TITLE);
            String lead = text.getString(TTexts.F_LEAD);
            String textStr = text.getString(TTexts.F_TEXT);
            if (title.trim().length() > 0 || lead.trim().length() > 0 || textStr.trim().length() > 0) {
                String shortTaskName = dict.getShortTaskName(curPhase, curLanguage);
                String taskDirPath = topDirPath + shortTaskName + "/";
                zipOut.putNextEntry(new ZipEntry(taskDirPath));
                if (generateSeparate) {
                    if (title.trim().length() > 0) {
                        title = StringUtils.replaceConfiguredChars(title);
                        createTextFile(zipOut, title, taskDirPath + "1_title", utf8, utf16);
                    }
                    if (lead.trim().length() > 0) {
                        lead = StringUtils.replaceConfiguredChars(lead);
                        createTextFile(zipOut, lead, taskDirPath + "2_lead", utf8, utf16);
                    }
                    if (textStr.trim().length() > 0) {
                        textStr = StringUtils.replaceConfiguredChars(textStr);
                        createTextFile(zipOut, textStr, taskDirPath + "3_text", utf8, utf16);
                    }
                }
                if (generateCombined) {
                    StringBuffer buf = new StringBuffer();
                    if (title.trim().length() > 0) {
                        title = StringUtils.replaceConfiguredChars(title);
                        buf.append(dict.title()).append(":\n");
                        buf.append(title);
                        buf.append("\n\n");
                    }
                    if (lead.trim().length() > 0) {
                        lead = StringUtils.replaceConfiguredChars(lead);
                        buf.append(dict.lead()).append(":\n");
                        buf.append(lead);
                        buf.append("\n\n");
                    }
                    if (textStr.trim().length() > 0) {
                        textStr = StringUtils.replaceConfiguredChars(textStr);
                        buf.append(dict.text()).append(":\n");
                        buf.append(textStr);
                        buf.append("\n\n");
                    }
                    String all = buf.toString();
                    createTextFile(zipOut, all, taskDirPath + "9_all-combined", utf8, utf16);
                }
            }
            String attachmentsStr = text.getString(TTexts.F_ATTACHMENTS);
            HashMap<String, String> attachmentMap = AttachmentsAndComments.getAttachmentMap(attachmentsStr);
            List<String> comments = new ArrayList<String>(attachmentMap.values());
            Collections.sort(comments);
            for (Map.Entry<String, String> attEntry : attachmentMap.entrySet()) {
                String filename = attEntry.getKey();
                String comment = attEntry.getValue();
                int priority = 0;
                for (priority = 0; priority < comments.size(); priority++) {
                    if (comment.equals(comments.get(priority))) {
                        break;
                    }
                }
                priority++;
                int pos = comment.indexOf(":");
                if (pos != -1) {
                    try {
                        Integer.parseInt(comment.substring(0, pos));
                        comment = comment.substring(pos + 1).trim();
                    } catch (NumberFormatException nfe) {
                    }
                }
                if (prefixComments) {
                    String language = dict.getLanguageName(curLanguage);
                    comment = language + ":\n" + comment;
                }
                AttachmentInfo info = attachmentInfos.get(filename);
                if (info == null) {
                    info = new AttachmentInfo(filename);
                    attachmentInfos.put(filename, info);
                }
                info.addComment(priority, comment);
            }
        }
        if (attachmentInfos.size() > 0) {
            String attachmentsDirPath = topDirPath + "attachments/";
            List<AttachmentInfo> infos = new ArrayList<AttachmentInfo>(attachmentInfos.values());
            Collections.sort(infos, new AttachmentInfoComparator());
            for (AttachmentInfo info : infos) {
                String filename = info.getFilename();
                String comments = info.getComments();
                int priority = info.getAvgPriority();
                String priorityStr = priority < 10 ? "0" + priority + "_" : String.valueOf(priority) + "_";
                ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
                FileInputStream bytesIn = new FileInputStream(uploadDirPath + filename);
                IOUtils.pipe(bytesIn, bytesOut);
                bytesIn.close();
                bytesOut.close();
                byte[] bytes = bytesOut.toByteArray();
                zipOut.putNextEntry(new ZipEntry(attachmentsDirPath + priorityStr + filename));
                zipOut.write(bytes, 0, bytes.length);
                createTextFile(zipOut, comments, attachmentsDirPath + priorityStr + filename + "_comment", utf8, utf16);
            }
        }
        zipOut.close();
        return file;
    }
