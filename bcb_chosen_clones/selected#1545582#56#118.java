    public void prepareOutput(HttpServletRequest req) {
        EaasyStreet.logTrace(METHOD_IN + className + OUTPUT_METHOD);
        super.prepareOutput(req);
        String content = Constants.EMPTY_STRING;
        String rawContent = null;
        List parts = null;
        try {
            URL url = new URL(sourceUrl);
            BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
                buffer.append(Constants.LF);
            }
            rawContent = buffer.toString();
        } catch (FileNotFoundException nf) {
            req.setAttribute(Constants.RAK_SYSTEM_ACTION, Constants.SYSTEM_ACTION_BACK);
            EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0012I, new String[] { "URL", nf.getMessage(), nf.toString() }));
        } catch (Exception e) {
            req.setAttribute(Constants.RAK_SYSTEM_ACTION, Constants.SYSTEM_ACTION_BACK);
            EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0012I, new String[] { "URL", e.getMessage(), e.toString() }));
        }
        if (rawContent != null) {
            if (startDelimiter != null) {
                parts = StringUtils.split(rawContent, startDelimiter);
                if (parts != null && parts.size() > 1) {
                    rawContent = (String) parts.get(1);
                    if (parts.size() > 2) {
                        for (int x = 2; x < parts.size(); x++) {
                            rawContent += startDelimiter;
                            rawContent += parts.get(x);
                        }
                    }
                } else {
                    rawContent = null;
                }
            }
        }
        if (rawContent != null) {
            if (endDelimiter != null) {
                parts = StringUtils.split(rawContent, endDelimiter);
                if (parts != null && parts.size() > 0) {
                    rawContent = (String) parts.get(0);
                } else {
                    rawContent = null;
                }
            }
        }
        if (rawContent != null) {
            if (replacementValues != null && !replacementValues.isEmpty()) {
                for (int x = 0; x < replacementValues.size(); x++) {
                    LabelValueBean bean = (LabelValueBean) replacementValues.get(x);
                    rawContent = StringUtils.replace(rawContent, bean.getLabel(), bean.getValue());
                }
            }
        }
        if (rawContent != null) {
            content = rawContent;
        }
        req.setAttribute(getFormName(), content);
        EaasyStreet.logTrace(METHOD_OUT + className + OUTPUT_METHOD);
    }
