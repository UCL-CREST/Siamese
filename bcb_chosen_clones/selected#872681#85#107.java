    private static File expandCommandLine(final String cmd) {
        final Pattern vars = Pattern.compile("[$]\\{(\\S+)\\}");
        final Matcher m = vars.matcher(cmd.trim());
        final StringBuffer sb = new StringBuffer(cmd.length());
        int lastMatchEnd = 0;
        while (m.find()) {
            sb.append(cmd.substring(lastMatchEnd, m.start()));
            final String envVar = m.group(1);
            String envVal = System.getenv(envVar);
            if (envVal == null) {
                File defPath = null;
                if ("mobac-prog".equalsIgnoreCase(envVar)) defPath = programDir; else if ("home".equalsIgnoreCase(envVar)) defPath = userHomeDir; else if ("XDG_CONFIG_HOME".equalsIgnoreCase(envVar)) defPath = new File(userHomeDir, ".config"); else if ("XDG_CACHE_HOME".equalsIgnoreCase(envVar)) defPath = new File(userHomeDir, ".cache"); else if ("XDG_DATA_HOME".equalsIgnoreCase(envVar)) {
                    File localDataDir = new File(userHomeDir, ".local");
                    defPath = new File(localDataDir, "share");
                }
                if (defPath != null) envVal = defPath.getAbsolutePath();
            }
            if (envVal == null) sb.append(cmd.substring(m.start(), m.end())); else sb.append(envVal);
            lastMatchEnd = m.end();
        }
        sb.append(cmd.substring(lastMatchEnd));
        return new File(sb.toString());
    }
