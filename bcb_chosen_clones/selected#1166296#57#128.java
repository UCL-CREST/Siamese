    private Object doAction(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args, final int actionType) {
        if (RA_REPLACE == actionType && args.length == 2 && (args[1] instanceof String)) {
            final String thisString = Context.toString(thisObj);
            String replacement = (String) args[1];
            final Object arg0 = args[0];
            if (arg0 instanceof String) {
                replacement = REPLACE_PATTERN.matcher(replacement).replaceAll("\\$");
                return StringUtils.replaceOnce(thisString, (String) arg0, replacement);
            } else if (arg0 instanceof NativeRegExp) {
                try {
                    final NativeRegExp regexp = (NativeRegExp) arg0;
                    final RegExpData reData = new RegExpData(regexp);
                    final String regex = reData.getJavaPattern();
                    final int flags = reData.getJavaFlags();
                    final Pattern pattern = Pattern.compile(regex, flags);
                    final Matcher matcher = pattern.matcher(thisString);
                    return doReplacement(thisString, replacement, matcher, reData.hasFlag('g'));
                } catch (final PatternSyntaxException e) {
                    LOG.warn(e.getMessage(), e);
                }
            }
        } else if (RA_MATCH == actionType || RA_SEARCH == actionType) {
            if (args.length == 0) {
                return null;
            }
            final Object arg0 = args[0];
            final String thisString = Context.toString(thisObj);
            final RegExpData reData;
            if (arg0 instanceof NativeRegExp) {
                reData = new RegExpData((NativeRegExp) arg0);
            } else {
                reData = new RegExpData(Context.toString(arg0));
            }
            final Pattern pattern = Pattern.compile(reData.getJavaPattern(), reData.getJavaFlags());
            final Matcher matcher = pattern.matcher(thisString);
            final boolean found = matcher.find();
            if (RA_SEARCH == actionType) {
                if (found) {
                    setProperties(matcher, thisString, matcher.start(), matcher.end());
                    return matcher.start();
                }
                return -1;
            }
            if (!found) {
                return null;
            }
            final int index = matcher.start(0);
            final List<Object> groups = new ArrayList<Object>();
            if (reData.hasFlag('g')) {
                groups.add(matcher.group(0));
                setProperties(matcher, thisString, matcher.start(0), matcher.end(0));
                while (matcher.find()) {
                    groups.add(matcher.group(0));
                    setProperties(matcher, thisString, matcher.start(0), matcher.end(0));
                }
            } else {
                for (int i = 0; i <= matcher.groupCount(); ++i) {
                    Object group = matcher.group(i);
                    if (group == null) {
                        group = Context.getUndefinedValue();
                    }
                    groups.add(group);
                }
                setProperties(matcher, thisString, matcher.start(), matcher.end());
            }
            final Scriptable response = cx.newArray(scope, groups.toArray());
            response.put("index", response, Integer.valueOf(index));
            response.put("input", response, thisString);
            return response;
        }
        return wrappedAction(cx, scope, thisObj, args, actionType);
    }
