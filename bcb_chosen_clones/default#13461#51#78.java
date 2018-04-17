    @SuppressWarnings("unchecked")
    private void getAllDataAsJSON() {
        List<Poll> polls = Poll.Select.All();
        List<Map> mapList = new ArrayList<Map>();
        for (Poll poll : polls) {
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
            mapList.add(map);
        }
        try {
            JSONValue.writeJSONString(mapList, this.writer);
        } catch (IOException ex) {
            Logger.getLogger(Polls.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
