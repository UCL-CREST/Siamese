    public final String getMessage(final String key, final Object args[]) {
        try {
            final CXmlMessageResourceBundle resource = this;
            final Ci18n result = resource.keyMap.getMessage(key);
            if ((result != null) && (args != null)) {
                String strResult = result.getMessage();
                for (int j = 0; j < args.length; j++) {
                    final StringBuffer tmp = new StringBuffer();
                    final Pattern p = Pattern.compile("\\{" + j + "\\}");
                    final Matcher match = p.matcher(strResult);
                    int offset = 0;
                    while (match.find(offset)) {
                        final int start = match.start();
                        final int end = match.end();
                        tmp.append(strResult.substring(offset, start));
                        if (args[j] != null) {
                            tmp.append(args[j]);
                        }
                        offset = end;
                    }
                    if (offset < strResult.length()) {
                        tmp.append(strResult.substring(offset));
                    }
                    strResult = tmp.toString();
                }
                return strResult;
            } else if (result != null) {
                return result.getMessage();
            } else {
                return null;
            }
        } catch (final Exception ignore) {
            return null;
        }
    }
