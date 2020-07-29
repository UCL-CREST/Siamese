        @Override
        protected Boolean doInBackground(String... params) {
            if (isCancelled()) {
                return true;
            }
            try {
                Pattern pattern;
                if (ignoreCase) {
                    pattern = Pattern.compile(mPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                } else {
                    pattern = Pattern.compile(mPattern);
                }
                Matcher m = pattern.matcher(mJecEditor.getEditText().getString());
                if (replaceAll) {
                    while (m.find()) {
                        if (mCancelled) {
                            break;
                        }
                        mData.add(new int[] { m.start(), m.end() });
                    }
                } else if (next) {
                    if (m.find(start)) {
                        mData.add(new int[] { m.start(), m.end() });
                    }
                } else {
                    if (start <= 0) return true;
                    while (m.find()) {
                        if (mCancelled) {
                            break;
                        } else if (m.end() >= start) {
                            if (mData.size() > 0) {
                                int[] ret = mData.get(mData.size() - 1);
                                mData.clear();
                                mData.add(ret);
                            }
                            break;
                        }
                        mData.add(new int[] { m.start(), m.end() });
                    }
                }
            } catch (Exception e) {
                Toast.makeText(mJecEditor.getApplicationContext(), mJecEditor.getString(R.string.search_error), Toast.LENGTH_LONG).show();
            }
            return true;
        }
