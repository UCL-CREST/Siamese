    protected static Object getNextMatchPosRegExImpl(String regEx, CharSequence searchIn, boolean goForward, boolean matchCase, boolean wholeWord, String replaceStr) {
        int flags = matchCase ? 0 : (Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Pattern pattern = Pattern.compile(regEx, flags);
        Matcher m = pattern.matcher(searchIn);
        if (goForward) {
            if (!wholeWord) {
                if (m.find()) {
                    if (replaceStr == null) {
                        return new Point(m.start(), m.end());
                    } else {
                        return new RegExReplaceInfo(m.group(0), m.start(), m.end(), getReplacementText(m, replaceStr));
                    }
                }
            } else {
                while (m.find()) {
                    Point loc = new Point(m.start(), m.end());
                    if (isWholeWord(searchIn, loc.x, loc.y - loc.x)) {
                        if (replaceStr == null) {
                            return loc;
                        } else {
                            return new RegExReplaceInfo(m.group(0), loc.x, loc.y, getReplacementText(m, replaceStr));
                        }
                    }
                }
            }
        } else {
            List matches = getMatches(m, replaceStr);
            if (matches.isEmpty()) return null;
            int pos = matches.size() - 1;
            if (wholeWord == false) {
                if (replaceStr == null) {
                    return matches.get(pos);
                } else {
                    return matches.get(pos);
                }
            }
            while (pos >= 0) {
                Object matchObj = matches.get(pos);
                if (replaceStr == null) {
                    Point loc = (Point) matchObj;
                    if (isWholeWord(searchIn, loc.x, loc.y - loc.x)) {
                        return matchObj;
                    }
                } else {
                    RegExReplaceInfo info = (RegExReplaceInfo) matchObj;
                    int x = info.getStartIndex();
                    int y = info.getEndIndex();
                    if (isWholeWord(searchIn, x, y - x)) {
                        return matchObj;
                    }
                }
                pos--;
            }
        }
        return null;
    }
