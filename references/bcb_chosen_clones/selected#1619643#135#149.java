    public static Map<Integer, String> getModifications(String sequence) {
        HashMap<Integer, String> res = new HashMap<Integer, String>();
        Pattern regex = Pattern.compile(MOD_REGEX);
        Matcher m = regex.matcher(sequence);
        int totalModsSize = 0;
        int numMods = 1;
        while (m.find()) {
            int pos = m.start() - 1;
            String mod = sequence.substring(pos, m.end());
            res.put(pos - totalModsSize + numMods, mod);
            totalModsSize += mod.length();
            numMods++;
        }
        return res;
    }
