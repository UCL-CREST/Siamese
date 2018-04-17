    public List execute(ComClient comClient) throws Exception {
        ArrayList outStrings = new ArrayList();
        SearchResult sr = Util.getSearchResultByIDAndNum(SearchManager.getInstance(), qID, dwNum);
        for (int i = 0; i < checkerUrls.length; i++) {
            String parametrizedURL = checkerUrls[i];
            Iterator mtIter = sr.iterateMetatags();
            while (mtIter.hasNext()) {
                Map.Entry mt = (Map.Entry) mtIter.next();
                parametrizedURL = parametrizedURL.replaceAll("%%" + mt.getKey() + "%%", mt.getValue().toString());
                if (mt.getKey().equals("fake") && ((Boolean) mt.getValue()).booleanValue()) {
                    outStrings.add("it's a fake.");
                    return outStrings;
                }
            }
            parametrizedURL = parametrizedURL.replaceAll("%%fileid%%", sr.getFileHash().toString());
            System.out.println("parametrizedURL=" + parametrizedURL);
            try {
                URL url = new URL(parametrizedURL);
                URLConnection connection = url.openConnection();
                connection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String str = null;
                while ((str = br.readLine()) != null) {
                    System.out.println(str);
                    if (str.indexOf(fakeMarks[i]) != -1) {
                        System.out.println("FAKEFAKEFAKE");
                        sr.addMetatag("fake", Boolean.TRUE);
                        outStrings.add("it's a fake.");
                        break;
                    }
                }
            } catch (MalformedURLException murl_err) {
                murl_err.printStackTrace();
            } catch (IOException io_err) {
                io_err.printStackTrace();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        return outStrings;
    }
