    protected String processNestedTags(String str, String tagName, String openSubstWithParam, String closeSubstWithParam, String openSubstWithoutParam, String closeSubstWithoutParam, String internalSubst, boolean processInternalTags, boolean acceptParam, boolean requiresQuotedParam) {
        StringBuffer buffer = new StringBuffer();
        Stack<Object> openStack = new Stack<Object>();
        Set<Object> subsOpen = new HashSet<Object>();
        Set<Object> subsClose = new HashSet<Object>();
        Set<Object> subsInternal = new HashSet<Object>();
        String openTag = CR_LF + "\\[" + tagName + (acceptParam ? (requiresQuotedParam ? "(?:=\"(.*?)\")?" : "(?:=\"?(.*?)\"?)?") : "") + "\\]" + CR_LF;
        String closeTag = CR_LF + "\\[/" + tagName + "\\]" + CR_LF;
        String internTag = CR_LF + "\\[\\*\\]" + CR_LF;
        String patternString = "(" + openTag + ")|(" + closeTag + ")";
        if (processInternalTags) {
            patternString += "|(" + internTag + ")";
        }
        Pattern tagsPattern = Pattern.compile(patternString);
        Matcher matcher = tagsPattern.matcher(str);
        int openTagGroup;
        int paramGroup;
        int closeTagGroup;
        int internalTagGroup;
        if (acceptParam) {
            openTagGroup = 1;
            paramGroup = 2;
            closeTagGroup = 3;
            internalTagGroup = 4;
        } else {
            openTagGroup = 1;
            paramGroup = -1;
            closeTagGroup = 2;
            internalTagGroup = 3;
        }
        while (matcher.find()) {
            int length = matcher.end() - matcher.start();
            MutableCharSequence matchedSeq = new MutableCharSequence(str, matcher.start(), length);
            if (matcher.group(openTagGroup) != null) {
                if (acceptParam && (matcher.group(paramGroup) != null)) {
                    matchedSeq.param = matcher.group(paramGroup);
                }
                openStack.push(matchedSeq);
            } else if ((matcher.group(closeTagGroup) != null) && !openStack.isEmpty()) {
                MutableCharSequence openSeq = (MutableCharSequence) openStack.pop();
                if (acceptParam) {
                    matchedSeq.param = openSeq.param;
                }
                subsOpen.add(openSeq);
                subsClose.add(matchedSeq);
            } else if (processInternalTags && (matcher.group(internalTagGroup) != null) && (!openStack.isEmpty())) {
                subsInternal.add(matchedSeq);
            } else {
            }
        }
        LinkedList<Object> subst = new LinkedList<Object>();
        subst.addAll(subsOpen);
        subst.addAll(subsClose);
        subst.addAll(subsInternal);
        Collections.sort(subst, new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
                MutableCharSequence s1 = (MutableCharSequence) o1;
                MutableCharSequence s2 = (MutableCharSequence) o2;
                return -(s1.start - s2.start);
            }
        });
        int start = 0;
        while (!subst.isEmpty()) {
            MutableCharSequence seq = (MutableCharSequence) subst.removeLast();
            buffer.append(str.substring(start, seq.start));
            if (subsClose.contains(seq)) {
                if (seq.param != null) {
                    buffer.append(closeSubstWithParam);
                } else {
                    buffer.append(closeSubstWithoutParam);
                }
            } else if (subsInternal.contains(seq)) {
                buffer.append(internalSubst);
            } else if (subsOpen.contains(seq)) {
                Matcher m = Pattern.compile(openTag).matcher(str.substring(seq.start, seq.start + seq.length));
                if (m.matches()) {
                    if (acceptParam && (seq.param != null)) {
                        buffer.append(openSubstWithParam.replaceAll("\\{BBCODE_PARAM\\}", seq.param));
                    } else {
                        buffer.append(openSubstWithoutParam);
                    }
                }
            }
            start = seq.start + seq.length;
        }
        buffer.append(str.substring(start));
        return buffer.toString();
    }
