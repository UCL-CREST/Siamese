    private void Process(TextView document) {
        Pattern re = Pattern.compile("(http://)?(www.)?jaap.nl/koophuis/[^\\s]+/[^\\s]+/(\\d{1,10})/[^\\s]+");
        Matcher ma = re.matcher(document.getText());
        List<UrlMatch> list = new ArrayList<UrlMatch>();
        while (ma.find()) {
            if (ma.groupCount() > 2) {
                UrlMatch u = new UrlMatch();
                u.propertyID = Integer.parseInt(ma.group(3));
                u.start = ma.start();
                u.end = ma.end();
                list.add(u);
            }
        }
        Collections.reverse(list);
        for (UrlMatch u : list) {
            String gadgetUrl = "http://jaap-y.appspot.com/servlet/gadget.xml?id=" + u.propertyID.toString();
            document.delete(new Range(u.start, u.end));
            Gadget gadget = new Gadget(gadgetUrl);
            document.insertElement(u.start, gadget);
        }
    }
