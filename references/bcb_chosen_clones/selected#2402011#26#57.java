    public ArrayList<ReferenceEntity> refindSplit(int offset) {
        String id;
        String regex;
        Pattern p;
        Matcher m;
        String fc;
        ArrayList<ReferenceEntity> found;
        int i = 0;
        for (ReferenceEntity suplEnt : suplementList) {
            id = suplEnt.getIdInRef();
            regex = reformIndexRegex(id);
            p = Pattern.compile(regex);
            found = new ArrayList<ReferenceEntity>();
            if (regex != null) {
                for (ReferenceEntity entity : rEntList) {
                    fc = entity.getFullRef();
                    m = p.matcher(fc);
                    i = 0;
                    while (m.find(i) && (m.end() < fc.length())) {
                        if (checkContext(fc, m.start(), m.end(), offset)) {
                            suplEnt.setFullRef(fc.substring(m.end()));
                            found.add(suplEnt);
                            entity.setFullRef(fc.substring(0, m.start()));
                            break;
                        } else i = m.end();
                    }
                }
                rEntList.addAll(found);
            }
        }
        return rEntList;
    }
