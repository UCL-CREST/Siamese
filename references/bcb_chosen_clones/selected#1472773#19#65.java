    @SuppressWarnings("unchecked")
    public void handle(Map<String, Object> data, String urlPath) {
        try {
            URL url = new URL(urlPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            String line = null;
            CMGroup currentGroup = null;
            List<CMGroup> groups = (List<CMGroup>) data.get(CMConstants.GROUP);
            List<CMTag> tags = (List<CMTag>) data.get(CMConstants.TAG);
            List<CMTagGroup> tagGroups = (List<CMTagGroup>) data.get(CMConstants.TAG_GROUP);
            while ((line = reader.readLine()) != null) {
                CMGroup group = null;
                try {
                    group = FetchUtil.getCMGroup(line);
                } catch (Exception e) {
                    CMLog.getLogger(this).severe("getCMGroup error:" + line);
                }
                if (group != null) {
                    if (currentGroup != null) {
                        groups.add(currentGroup);
                    }
                    currentGroup = group;
                }
                CMTag tag = null;
                try {
                    tag = FetchUtil.getCMTag(line);
                } catch (Exception e) {
                    CMLog.getLogger(this).severe("getCMTag error:" + line);
                }
                if (tag != null) {
                    CMTagGroup tagGroup = new CMTagGroup();
                    tagGroup.setGroupName(currentGroup.getName());
                    tagGroup.setTagName(tag.getName());
                    tags.add(tag);
                    tagGroups.add(tagGroup);
                }
            }
            groups.add(currentGroup);
            reader.close();
        } catch (MalformedURLException e) {
            CMLog.getLogger(this).severe("GTagHandler error:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            CMLog.getLogger(this).severe("GTagHandler error:" + e.getMessage());
            e.printStackTrace();
        }
    }
