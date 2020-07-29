    public static String computeSignature(List words) {
        if (words.size() == 0) return "(empty word list)";
        readCaches();
        firstNew = true;
        int len = words.size() + 1;
        String[] word = new String[len];
        words.toArray(word);
        word[len - 1] = String.valueOf((char) 0xffff);
        Arrays.sort(word);
        List list = new ArrayList();
        String prev = word[0];
        int c = 0;
        for (int i = 0, imax = len; i < imax; i++) {
            String w = word[i];
            if (w.equals(prev)) c++; else {
                int freq = getFreq(prev);
                if (freq < Integer.MAX_VALUE) list.add(new WordFreq(prev, c, freq));
                prev = w;
                c = 1;
            }
        }
        if (list.size() == 0) return "(no valid words)";
        WordFreq[] bogus = new WordFreq[0];
        WordFreq[] wordfreq = (WordFreq[]) list.toArray(bogus);
        int validlen = Math.min(SignatureLength, wordfreq.length);
        if ("tfidf".equals(Algorithm)) {
            Arrays.sort(wordfreq, new byRelFreq());
        } else if ("rarest".equals(Algorithm)) {
            Arrays.sort(wordfreq, new byWebFreq());
        } else if ("random".equals(Algorithm)) {
            Random rand = new Random();
            for (int i = 0, imax = validlen; i < imax; i++) {
                int swapi = rand.nextInt(imax);
                WordFreq tmp = wordfreq[i];
                wordfreq[i] = wordfreq[swapi];
                wordfreq[swapi] = tmp;
            }
        } else if ("random100k".equals(Algorithm)) {
            Random rand = new Random();
            validlen = 0;
            for (int i = 0, imax = wordfreq.length; i < imax; i++) {
                WordFreq tmp = wordfreq[i];
                if (tmp.webcnt < 100000) {
                    int swapi = rand.nextInt(validlen + 1);
                    wordfreq[i] = wordfreq[validlen];
                    wordfreq[validlen] = wordfreq[swapi];
                    wordfreq[swapi] = tmp;
                    validlen++;
                }
            }
            validlen = Math.min(validlen, SignatureLength);
        } else {
            Arrays.sort(wordfreq, new byRoFreq());
        }
        if (DEBUG) {
            System.out.println("* Rankings *");
            for (int i = 0; i < Math.min(25, wordfreq.length); i++) System.out.println(wordfreq[i]);
        }
        StringBuffer sigsb = new StringBuffer(100);
        for (int i = 0, imax = validlen; i < imax; i++) {
            if (i > 0) sigsb.append(' ');
            sigsb.append(wordfreq[i].word);
            if (StudyOut != null) StudyOut.print(wordfreq[i].pagecnt + "/" + wordfreq[i].webcnt + " ");
        }
        if (StudyOut != null) StudyOut.println();
        if (Verbose && newwords.size() > 0) {
            System.out.println();
        }
        writeCache();
        return sigsb.substring(0);
    }
