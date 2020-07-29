    public void reademi(Vector<String> descriptions, Vector<String> links, String linkaddress, String idmap) {
        InputStream is = null;
        URL url;
        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<String> names = new ArrayList<String>();
        try {
            url = new URL(idmap);
            is = url.openStream();
            Scanner scanner = new Scanner(is);
            scanner.nextLine();
            String line = "";
            String id = "";
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                Scanner linescanner = new Scanner(line);
                linescanner.useDelimiter("\t");
                id = linescanner.next();
                id = id.substring(0, id.length() - 2);
                keys.add(id);
                linescanner.next();
                linescanner.next();
                linescanner.next();
                linescanner.useDelimiter("\n");
                names.add(linescanner.next());
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(linkaddress).openStream()));
            String link = "";
            String key = "";
            String name = "";
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("style=raw") != -1) {
                    int linkstart = line.indexOf("http://www.ebi.ac.uk/cgi-bin/dbfetch?db");
                    int idstart = line.indexOf("id=") + 3;
                    int linkend = line.substring(linkstart).indexOf("\"") + linkstart;
                    link = line.substring(linkstart, linkend);
                    key = line.substring(idstart, linkend);
                    if (keys.indexOf(key) != -1) {
                        name = names.get(keys.indexOf(key));
                        counter++;
                        descriptions.add(counter + " " + key + " " + name);
                        links.add(link);
                    }
                }
            }
        } catch (MalformedURLException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
