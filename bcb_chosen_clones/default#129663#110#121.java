    private void addAdditional2(Message response, int section) {
        Enumeration e = response.getSection(section);
        while (e.hasMoreElements()) {
            Record r = (Record) e.nextElement();
            try {
                Method m = r.getClass().getMethod("getTarget", null);
                Name glueName = (Name) m.invoke(r, null);
                addGlue(response, glueName);
            } catch (Exception ex) {
            }
        }
    }
