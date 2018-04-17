        public TemplateCollectionModel getRedirectTargets() {
            if (this.redirectTargets == null) {
                HashMap<String, RedirectTarget> redirectTargets = new HashMap<String, RedirectTarget>();
                if (content != null && !"vexi.util.redirect".equals(name)) {
                    String code = content.toString();
                    Pattern p = Pattern.compile("addRedirect(.*)", Pattern.MULTILINE);
                    Matcher m = p.matcher(code);
                    HashMap targets = new HashMap();
                    while (m.find()) {
                        int end = code.indexOf(")", m.start() + 1);
                        String addRedirect = code.substring(m.start(), end + 1);
                        Pattern pid = Pattern.compile("\\$[a-zA-Z]*");
                        Matcher mid = pid.matcher(addRedirect);
                        if (!mid.find()) continue;
                        String id = mid.group(0).substring(1);
                        RedirectTarget target = redirectTargets.get(id);
                        if (target == null) {
                            target = new RedirectTarget(id);
                            redirectTargets.put(id, target);
                        }
                        Pattern pprop = Pattern.compile("\"[^\"]*\"", Pattern.MULTILINE);
                        Matcher mprop = pprop.matcher(addRedirect);
                        while (mprop.find()) {
                            String prop = addRedirect.substring(mprop.start() + 1, mprop.end() - 1);
                            target.add(prop);
                        }
                    }
                }
                this.redirectTargets = new SimpleCollection(redirectTargets.values());
            }
            return this.redirectTargets;
        }
