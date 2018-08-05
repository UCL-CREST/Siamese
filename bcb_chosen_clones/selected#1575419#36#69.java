    public ArrayList<ReferenceEntity> extractSpot(String regex, char rcoat) {
        spotCount = 0;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(fullText);
        int openIdx = 0;
        int closeIdx;
        int fullTextLength = fullText.length();
        ArrayList<Integer> idList = new ArrayList<Integer>();
        suplList = new ArrayList<ReferenceEntity>();
        while (m.find(openIdx) && m.start() < fullTextLength) {
            closeIdx = fullText.indexOf(rcoat, m.start() + 1);
            if (closeIdx == -1 || m.start() == -1 || m.start() + 1 > closeIdx) break;
            String fieldCand = fullText.substring(m.start() + 1, closeIdx);
            if (!fieldCand.equals("")) idList = checkField(fieldCand, m.start(), m.end(), fullText);
            String context = StringUtil.getContext(fullText, m.start(), m.end(), 15);
            if (idList.size() != 0) {
                spotCount += idList.size();
                for (Integer num : idList) {
                    boolean flag = false;
                    for (ReferenceEntity entity : rEntList) {
                        if (entity.getIdInRef().equals(String.valueOf(num))) {
                            entity.getContextList().add(context);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) addToSuplList(num, context);
                }
            } else System.err.println("No referece for the field : " + fieldCand);
            openIdx = closeIdx;
        }
        System.out.println("\nspotCount=" + spotCount);
        return rEntList;
    }
