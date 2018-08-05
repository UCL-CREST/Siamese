        void runTest(TestFile tf, boolean dryRunOnly) throws Exception {
            String cacheString = "";
            if (tf.cache == AbstractTest.PREDECODE) cacheString = "predecode"; else if (tf.cache == AbstractTest.PRELOAD) cacheString = "preload";
            if (!dryRunOnly) msg("  <test name='" + tf.testName + "' src='" + tf.testFile + "' cache='" + (cacheString == "" ? "no" : cacheString) + "'>");
            String cmd = "java -cp \"" + classpath + ";" + System.getProperty("java.class.path") + "\"";
            Iterator props = properties.entrySet().iterator();
            while (props.hasNext()) {
                Map.Entry entry = (Map.Entry) props.next();
                String n = (String) entry.getKey();
                String v = (String) entry.getValue();
                cmd += " -D" + n + "=" + v;
            }
            final String args = " " + driver + " " + tf.testFile + " " + (dryRunOnly ? "0" : tf.iterations) + " " + cacheString + " " + options;
            cmd += args;
            if (dryRunOnly) info("Dry run of " + name + " with " + tf.testName); else info("Testing " + name + " with " + tf.iterations + " iterations of " + tf.testName + ".");
            info("\targuments: " + args);
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String errors = "";
            String newLine;
            while ((newLine = stderr.readLine()) != null) {
                info("   " + newLine);
                errors += "   " + newLine + "\n";
            }
            if (!dryRunOnly && errors.length() > 0) {
                msg("   <!--\n" + errors + "   -->");
            }
            proc.waitFor();
            if (proc.exitValue() != 0) {
                info("process exited with non zero value - ignoring!");
                if (!dryRunOnly) msg("<error>Process exited with value " + proc.exitValue() + "</error>");
            } else {
                while (!dryRunOnly && (newLine = stdout.readLine()) != null) msg("   " + newLine);
            }
            if (!dryRunOnly) msg("  </test>");
        }
