    public static void main(String arg[]) {
        try {
            URL url = new URL(tempurl);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setDoInput(true);
            connect.setDoOutput(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream(), "gb2312"));
            String line = null;
            StringBuffer content = new StringBuffer();
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();
            url = null;
            String msg = content.toString();
            Matcher m = p.matcher(msg);
            while (m.find()) {
                System.out.println(m.group(1) + "---" + m.group(2) + "---" + m.group(3) + "---" + m.group(4) + "---" + m.group(5) + "---");
            }
        } catch (Exception e) {
            System.out.println("Error:");
            System.out.println(e.getStackTrace());
        }
    }
