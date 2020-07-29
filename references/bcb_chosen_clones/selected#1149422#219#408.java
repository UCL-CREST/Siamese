    public int findString(String search, int startPos, boolean downward, boolean loopSearch, boolean senseCaseSearch, boolean regexSearch, boolean wordUnitSearch) {
        StdStyledDocument ssd = getStdStyledDocument();
        if (ssd == null || search.length() == 0) {
            return -1;
        }
        StringBuilder target = ssd.getStringBuilder();
        if (!senseCaseSearch) {
            StringBuilder tsb = new StringBuilder(target.toString().toUpperCase());
            target = tsb;
            search = search.toUpperCase();
        }
        int result = -1;
        if (regexSearch) {
            int patternFlags;
            if (senseCaseSearch) {
                patternFlags = Pattern.MULTILINE;
            } else {
                patternFlags = Pattern.MULTILINE | Pattern.CASE_INSENSITIVE;
            }
            Pattern ptn = Pattern.compile(search, patternFlags);
            if (downward) {
                if (startPos == -1) {
                    startPos = getSelectionEnd();
                }
                Matcher matcher = ptn.matcher(target);
                int s = -1;
                int e = -1;
                if (matcher.find(startPos)) {
                    s = matcher.start();
                    e = matcher.end();
                }
                if (s == -1 && loopSearch) {
                    if (matcher.find(0)) {
                        s = matcher.start();
                        e = matcher.end();
                    }
                }
                if (s != -1) {
                    select(s, e);
                    result = s;
                }
            } else {
                if (startPos == -1) {
                    startPos = getSelectionStart();
                }
                Matcher matcher = ptn.matcher(target);
                int s = -1;
                int e = -1;
                if (matcher.find(0)) {
                    do {
                        if (matcher.end() < startPos) {
                            s = matcher.start();
                            e = matcher.end();
                        } else {
                            break;
                        }
                    } while (matcher.find());
                }
                if (s == -1 && loopSearch) {
                    if (matcher.find(startPos)) {
                        do {
                            s = matcher.start();
                            e = matcher.end();
                        } while (matcher.find());
                    }
                }
                if (s != -1) {
                    select(s, e);
                    result = s;
                }
            }
        } else if (wordUnitSearch) {
            if (downward) {
                if (startPos == -1) {
                    startPos = getSelectionEnd();
                }
                int s = -1;
                int r = startPos;
                while ((r = target.indexOf(search, r)) != -1) {
                    int e = r + search.length();
                    if (r > 0 && wordDelimiter.indexOf(target.charAt(r - 1)) == -1) {
                        r = r + 1;
                        continue;
                    }
                    if (e < target.length() && wordDelimiter.indexOf(target.charAt(e)) == -1) {
                        r = r + 1;
                        continue;
                    }
                    s = r;
                    break;
                }
                if (s == -1 && loopSearch) {
                    r = 0;
                    while ((r = target.indexOf(search, r)) != -1) {
                        int e = r + search.length();
                        if (r > 0 && wordDelimiter.indexOf(target.charAt(r - 1)) == -1) {
                            r = r + 1;
                            continue;
                        }
                        if (e < target.length() && wordDelimiter.indexOf(target.charAt(e)) == -1) {
                            r = r + 1;
                            continue;
                        }
                        s = r;
                        break;
                    }
                }
                if (s != -1) {
                    select(s, s + search.length());
                    result = s;
                }
            } else {
                if (startPos == -1) {
                    startPos = getSelectionStart();
                }
                int s = -1;
                int r = startPos - 1;
                if (r >= 0) {
                    while ((r = target.lastIndexOf(search, r)) != -1) {
                        int e = r + search.length();
                        if (r > 0 && wordDelimiter.lastIndexOf(target.charAt(r - 1)) == -1) {
                            r = r - 1;
                            continue;
                        }
                        if (e < target.length() && wordDelimiter.lastIndexOf(target.charAt(e)) == -1) {
                            r = r - 1;
                            continue;
                        }
                        r = s;
                        break;
                    }
                } else {
                    s = -1;
                }
                if (s == -1 && loopSearch) {
                    r = target.length();
                    while ((r = target.lastIndexOf(search, r)) != -1) {
                        int e = r + search.length();
                        if (r > 0 && wordDelimiter.lastIndexOf(target.charAt(r - 1)) == -1) {
                            r = r - 1;
                            continue;
                        }
                        if (e < target.length() && wordDelimiter.lastIndexOf(target.charAt(e)) == -1) {
                            r = r - 1;
                            continue;
                        }
                        s = r;
                        break;
                    }
                }
                if (s != -1) {
                    select(s, s + search.length());
                    result = s;
                }
            }
        } else {
            if (downward) {
                if (startPos == -1) {
                    startPos = getSelectionEnd();
                }
                int s = startPos;
                s = target.indexOf(search, s);
                if (s == -1 && loopSearch) {
                    s = target.indexOf(search, 0);
                }
                if (s != -1) {
                    select(s, s + search.length());
                    result = s;
                }
            } else {
                if (startPos == -1) {
                    startPos = getSelectionStart();
                }
                int s = startPos - 1;
                if (s >= 0) {
                    s = target.lastIndexOf(search, s);
                } else {
                    s = -1;
                }
                if (s == -1 && loopSearch) {
                    s = target.lastIndexOf(search, target.length());
                }
                if (s != -1) {
                    select(s, s + search.length());
                    result = s;
                }
            }
        }
        return result;
    }
