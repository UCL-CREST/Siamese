    private void addAllSpecialPages(Environment en, ZipOutputStream zipout, int progressStart, int progressLength) throws Exception, IOException {
        if (virtualWiki == null || virtualWiki.length() < 1) {
            virtualWiki = WikiBase.DEFAULT_VWIKI;
        }
        ResourceBundle messages = ResourceBundle.getBundle("ApplicationResources", locale);
        Template tpl;
        int count = 0;
        int numberOfSpecialPages = 7;
        int bytesRead = 0;
        byte[] byteArray = new byte[4096];
        progress = Math.min(progressStart + (int) ((double) count * (double) progressLength / (double) numberOfSpecialPages), 99);
        count++;
        ZipEntry entry = new ZipEntry("vqwiki.css");
        zipout.putNextEntry(entry);
        StringReader readin = new StringReader(WikiBase.getInstance().readRaw(virtualWiki, "StyleSheet"));
        int read = 0;
        while ((read = readin.read()) != -1) {
            zipout.write(read);
        }
        zipout.closeEntry();
        zipout.flush();
        progress = Math.min(progressStart + (int) ((double) count * (double) progressLength / (double) numberOfSpecialPages), 99);
        count++;
        tpl = getTemplateFilledWithContent("search");
        tpl.setFieldGlobal("TOPICNAME", "WikiSearch");
        entry = new ZipEntry("WikiSearch.html");
        StringReader strin = new StringReader(tpl.getContent());
        zipout.putNextEntry(entry);
        while ((bytesRead = strin.read()) != -1) {
            zipout.write(bytesRead);
        }
        zipout.closeEntry();
        zipout.flush();
        progress = Math.min(progressStart + (int) ((double) count * (double) progressLength / (double) numberOfSpecialPages), 99);
        count++;
        entry = new ZipEntry("applets/vqapplets.jar");
        zipout.putNextEntry(entry);
        InputStream in = new BufferedInputStream(new FileInputStream(ctx.getRealPath("/WEB-INF/classes/export2html/vqapplets.jar")));
        while (in.available() > 0) {
            bytesRead = in.read(byteArray, 0, Math.min(4096, in.available()));
            zipout.write(byteArray, 0, bytesRead);
        }
        zipout.closeEntry();
        zipout.flush();
        entry = new ZipEntry("applets/log4j.jar");
        zipout.putNextEntry(entry);
        in = new BufferedInputStream(new FileInputStream(ctx.getRealPath("/WEB-INF/lib/log4j-1.2.12.jar")));
        while (in.available() > 0) {
            bytesRead = in.read(byteArray, 0, Math.min(4096, in.available()));
            zipout.write(byteArray, 0, bytesRead);
        }
        zipout.closeEntry();
        zipout.flush();
        entry = new ZipEntry("applets/lucene-1.2a.jar");
        zipout.putNextEntry(entry);
        in = new BufferedInputStream(new FileInputStream(ctx.getRealPath("/WEB-INF/lib/lucene-1.2a.jar")));
        while (in.available() > 0) {
            bytesRead = in.read(byteArray, 0, Math.min(4096, in.available()));
            zipout.write(byteArray, 0, bytesRead);
        }
        zipout.closeEntry();
        zipout.flush();
        entry = new ZipEntry("applets/commons-httpclient-2.0.jar");
        zipout.putNextEntry(entry);
        in = new BufferedInputStream(new FileInputStream(ctx.getRealPath("/WEB-INF/lib/commons-httpclient-2.0.jar")));
        while (in.available() > 0) {
            bytesRead = in.read(byteArray, 0, Math.min(4096, in.available()));
            zipout.write(byteArray, 0, bytesRead);
        }
        zipout.closeEntry();
        zipout.flush();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            JarOutputStream indexjar = new JarOutputStream(bos);
            JarEntry jarEntry;
            File searchDir = new File(WikiBase.getInstance().getSearchEngineInstance().getSearchIndexPath(virtualWiki));
            String files[] = searchDir.list();
            StringBuffer listOfAllFiles = new StringBuffer();
            for (int i = 0; i < files.length; i++) {
                if (listOfAllFiles.length() > 0) {
                    listOfAllFiles.append(",");
                }
                listOfAllFiles.append(files[i]);
                jarEntry = new JarEntry("lucene/index/" + files[i]);
                indexjar.putNextEntry(jarEntry);
                in = new FileInputStream(new File(searchDir, files[i]));
                while (in.available() > 0) {
                    bytesRead = in.read(byteArray, 0, Math.min(4096, in.available()));
                    indexjar.write(byteArray, 0, bytesRead);
                }
                indexjar.closeEntry();
            }
            indexjar.flush();
            jarEntry = new JarEntry("lucene/index.dir");
            strin = new StringReader(listOfAllFiles.toString());
            indexjar.putNextEntry(jarEntry);
            while ((bytesRead = strin.read()) != -1) {
                indexjar.write(bytesRead);
            }
            indexjar.closeEntry();
            indexjar.flush();
            indexjar.close();
            entry = new ZipEntry("applets/index.jar");
            zipout.putNextEntry(entry);
            zipout.write(bos.toByteArray());
            zipout.closeEntry();
            zipout.flush();
            bos.reset();
        } catch (Exception e) {
            logger.debug("Exception while adding lucene index: ", e);
        }
        progress = Math.min(progressStart + (int) ((double) count * (double) progressLength / (double) numberOfSpecialPages), 99);
        count++;
        tpl = new Template(ctx.getRealPath("/WEB-INF/classes/export2html/mastertemplate.tpl"));
        tpl.setFieldGlobal("VERSION", Environment.WIKI_VERSION);
        StringBuffer content = new StringBuffer();
        content.append("<table><tr><th>" + messages.getString("common.date") + "</th><th>" + messages.getString("common.topic") + "</th><th>" + messages.getString("common.user") + "</th></tr>\n");
        Collection all = null;
        try {
            Calendar cal = Calendar.getInstance();
            ChangeLog cl = WikiBase.getInstance().getChangeLogInstance();
            int n = Environment.getInstance().getIntSetting(Environment.PROPERTY_RECENT_CHANGES_DAYS);
            if (n == 0) {
                n = 5;
            }
            all = new ArrayList();
            for (int i = 0; i < n; i++) {
                Collection col = cl.getChanges(virtualWiki, cal.getTime());
                if (col != null) {
                    all.addAll(col);
                }
                cal.add(Calendar.DATE, -1);
            }
        } catch (Exception e) {
            ;
        }
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        for (Iterator iter = all.iterator(); iter.hasNext(); ) {
            Change change = (Change) iter.next();
            content.append("<tr><td class=\"recent\">" + df.format(change.getTime()) + "</td><td class=\"recent\"><a href=\"" + safename(change.getTopic()) + ".html\">" + change.getTopic() + "</a></td><td class=\"recent\">" + change.getUser() + "</td></tr>");
        }
        content.append("</table>\n");
        tpl.setFieldGlobal("TOPICNAME", "RecentChanges");
        tpl.setFieldGlobal("VERSION", Environment.WIKI_VERSION);
        tpl.setFieldGlobal("CONTENTS", content.toString());
        entry = new ZipEntry("RecentChanges.html");
        strin = new StringReader(tpl.getContent());
        zipout.putNextEntry(entry);
        while ((read = strin.read()) != -1) {
            zipout.write(read);
        }
        zipout.closeEntry();
        zipout.flush();
        logger.debug("Done adding all special topics.");
    }
