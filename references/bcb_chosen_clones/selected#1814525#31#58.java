        @Override
        public Vector doInBackground() {
            try {
                Process p = Runtime.getRuntime().exec("android list targets");
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                Vector<String> targetList = new Vector<String>();
                try {
                    while (true) {
                        String line = in.readLine();
                        if (line == null) {
                            break;
                        }
                        if (line.startsWith("id:")) {
                            String id = line.substring(4, line.indexOf(' ', 4));
                            String name = line.substring(line.indexOf(' ', 4) + " or ".length());
                            name = name.replaceAll("\"", "");
                            targetList.add(id + " " + name);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return targetList;
            } catch (Exception e) {
                Util.showError(view, jEdit.getProperty("android.Error", "Error"), e.getMessage());
                return null;
            }
        }
