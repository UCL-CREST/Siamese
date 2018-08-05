    public void fetch(Util util) throws ConversationException {
        List<Integer> positions = new ArrayList<Integer>();
        setUpgradeable(false);
        setFoodShortage(false);
        Pattern p;
        Matcher m;
        String page = util.httpGetPage(getUrlString());
        int pageLimit = page.length();
        p = Pattern.compile("soon_link");
        m = p.matcher(page);
        if (m.find()) {
            pageLimit = m.start();
        }
        p = Pattern.compile("(?s)(?i)<h2>(?:\\d+\\.\\s*)?(.*?)</h2>");
        m = p.matcher(page);
        m.region(0, pageLimit);
        while (m.find()) {
            String buildingTypeString = m.group(1);
            String name = buildingTypeString;
            ConstructionData item = new ConstructionData(name, getUrlString(), getTranslator());
            item.setCurrentLevel(0);
            this.buildings.add(item);
            BuildingType buildingType = BuildingType.fromKey(getTranslator().getKeyword(buildingTypeString));
            item.setType(buildingType);
            if ((desiredBuildingType != null && desiredBuildingType.equals(buildingType)) || isOneChoiceOnly()) {
                super.setConstructionData(item);
            }
            positions.add(new Integer(m.start()));
        }
        int lastPos = 0;
        int nextPos = pageLimit;
        for (ConstructionData item : buildings) {
            for (ResourceType resourceType : ResourceType.values()) {
                int res = resourceType.toInt() + 1;
                p = Pattern.compile("(?s)<img .*? src=\".*?img/un/r/" + res + ".gif\"[^>]*>(\\d\\d*) \\|");
                m = p.matcher(page);
                m.region(lastPos, pageLimit);
                if (m.find()) {
                    String stringNumber = m.group(1);
                    lastPos = m.end();
                    try {
                        item.setNeededResource(resourceType, Integer.parseInt(stringNumber));
                    } catch (NumberFormatException e) {
                        throw new ConversationException("Invalid number for \"" + this.getName() + "\": " + stringNumber);
                    }
                }
            }
        }
        lastPos = 0;
        for (int i = 0; i < positions.size(); i++) {
            lastPos = positions.get(i).intValue();
            if (i == positions.size() - 1) {
                nextPos = pageLimit;
            } else {
                nextPos = positions.get(i + 1).intValue();
            }
            p = Pattern.compile(String.format("(?s)(?i)<span class=\"c\">%s[^>]*</span>", translator.getMatchingPattern(Translator.FOOD_SHORTAGE)));
            m = p.matcher(page);
            m.region(lastPos, nextPos);
            if (!m.find()) {
                p = Pattern.compile("(?s)<img .*? src=\".*?img/un/a/clock.gif\"[^>]*>([^<]*)<");
                m = p.matcher(page);
                m.region(lastPos, nextPos);
                if (!m.find()) {
                    p = Pattern.compile("(?s)<img[^>]*class=\"clock\"[^>]*>([^<]*)");
                    m = p.matcher(page);
                    m.region(lastPos, page.length());
                }
                if (m.find()) {
                    String timeString = m.group(1).trim();
                    buildings.get(i).setSecondsForNextLevel(Util.timeToSeconds(timeString));
                } else {
                    throw new ConversationException("Can't find time to complete " + this.getName());
                }
            } else {
                buildings.get(i).setUpgradeable(false);
                buildings.get(i).setFoodShortage(true);
            }
            if (!buildings.get(i).isUpgradeable()) {
                buildings.get(i).setSubmitUrlString(null);
                return;
            }
            p = Pattern.compile("(?s)<a href=\"(dorf\\d\\.php\\?.*?)\">");
            m = p.matcher(page);
            m.region(lastPos, nextPos);
            if (m.find()) {
                String submitUrlString = m.group(1);
                buildings.get(i).setSubmitUrlString(Util.getFullUrl(this.getUrlString(), submitUrlString));
            } else {
                buildings.get(i).setSubmitUrlString(null);
            }
        }
    }
