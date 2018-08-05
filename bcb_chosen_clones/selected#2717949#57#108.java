    private void migratePage(String pageName) {
        if (matchName != null) {
            if (pageName.matches(matchName) == false) {
                return;
            }
        }
        String newPageName = pageName;
        if (pageName.indexOf("_blogentry_") != -1) {
            newPageName = pageName.substring(0, pageName.indexOf("_blogentry_")) + "/blog/";
            try {
                String dateString = pageName.substring(pageName.indexOf("_blogentry_") + 11);
                Date entryDate = null;
                if (dateString.length() == 8) {
                    entryDate = new SimpleDateFormat("ddMMyy_h").parse(dateString);
                } else {
                    entryDate = new SimpleDateFormat("ddMMyyyy_h").parse(dateString);
                }
                String entryName = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss").format(entryDate);
                newPageName = newPageName + entryName + "/";
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        System.out.println("Migrating " + pageName + " to " + WikiNameHelper.titleToWikiName(newPageName));
        File htmlFile = new File(webRoot + "/" + pageName + ".html");
        File newHtmlFile = new File(newRoot + "/" + WikiNameHelper.titleToWikiName(newPageName) + "/page.html");
        File wikiFile = new File(wikiRoot + "/" + pageName + ".txt");
        File newWikiFile = new File(newRoot + "/" + WikiNameHelper.titleToWikiName(newPageName) + "/page.wiki");
        File wikiPropertyFile = new File(wikiRoot + "/" + pageName + ".properties");
        File newWikiPropertyFile = new File(newRoot + "/" + WikiNameHelper.titleToWikiName(newPageName) + "/" + WikiControllerInterface.OWNER_FILE);
        if (htmlFile.exists()) {
            FileHelper.copy(htmlFile, newHtmlFile);
            newHtmlFile.setLastModified(htmlFile.lastModified());
        }
        if (wikiFile.exists()) {
            FileHelper.copy(wikiFile, newWikiFile);
            newWikiFile.setLastModified(wikiFile.lastModified());
        }
        if (wikiPropertyFile.exists()) {
            try {
                String data = FileHelper.readFile(wikiPropertyFile.getAbsolutePath());
                String[] lines = StringArrayHelper.parseFields(data, '\n');
                String author = KeyValuePair.parseKeyValuePair(lines[2], "=").getValue();
                FileHelper.buildFile(newWikiPropertyFile.getAbsolutePath(), author);
                newWikiPropertyFile.setLastModified(wikiPropertyFile.lastModified());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        migratePageAttachments(pageName, newPageName);
    }
