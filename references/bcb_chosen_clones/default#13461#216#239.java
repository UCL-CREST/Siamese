    public void Select(String primaryKey) {
        List<Poll> polls = Poll.Select.Where("Poll.id=" + primaryKey);
        Poll poll = polls.get(0);
        Map map = new HashMap();
        for (Field classFeild : poll.getClass().getDeclaredFields()) {
            Annotation ann = classFeild.getAnnotation(Column.class);
            if (ann == null) {
                continue;
            }
            String methodName = "get" + classFeild.getName().substring(0, 1).toUpperCase() + classFeild.getName().substring(1);
            Method m;
            try {
                m = poll.getClass().getMethod(methodName, new Class[] {});
                map.put(classFeild.getName(), m.invoke(poll, new Object[] {}));
            } catch (Exception ex) {
                Logger.getLogger(Polls.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            JSONValue.writeJSONString(map, this.writer);
        } catch (IOException ex) {
            Logger.getLogger(Polls.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
