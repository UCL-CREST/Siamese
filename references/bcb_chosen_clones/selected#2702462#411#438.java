        protected Void doInBackground() {
            try {
                List<String> cmd = new ArrayList<String>();
                cmd.add(path + "/wendy");
                cmd.add("temp.owfn");
                cmd.add("--verbose");
                cmd.add("--correctness=livelock");
                cmd.add("--sa");
                ProcessBuilder builder = new ProcessBuilder(cmd);
                builder.directory(new File(path));
                builder.redirectErrorStream(true);
                Process generate = builder.start();
                StringWriter out = new StringWriter(32);
                InputStream is = generate.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                char[] buffer = new char[32];
                int count;
                while ((count = isr.read(buffer)) > 0) {
                    out.write(buffer, 0, count);
                    publish(out.toString());
                    if (msgDialog.isCancelled()) break;
                }
                isr.close();
            } catch (IOException ioe) {
                publish("IOException reading Wendy output.");
            }
            return null;
        }
