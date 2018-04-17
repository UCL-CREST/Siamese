    public ITreeProcessor[] getProcessors(String text) {
        ArrayList<ITreeProcessor> list = new ArrayList<ITreeProcessor>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String found = text.substring(matcher.start(), matcher.end());
            StringTokenizer st = new StringTokenizer(found, "=; ");
            st.nextToken();
            list.add(new SeedHandler(Integer.parseInt(st.nextToken())));
        }
        return list.toArray(new ITreeProcessor[list.size()]);
    }
