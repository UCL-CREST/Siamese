    public static SubstanceSkin.ColorSchemes getColorSchemes(URL url) {
        List<SubstanceColorScheme> schemes = new ArrayList<SubstanceColorScheme>();
        BufferedReader reader = null;
        Color ultraLight = null;
        Color extraLight = null;
        Color light = null;
        Color mid = null;
        Color dark = null;
        Color ultraDark = null;
        Color foreground = null;
        String name = null;
        ColorSchemeKind kind = null;
        boolean inColorSchemeBlock = false;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.length() == 0) continue;
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.indexOf("{") >= 0) {
                    if (inColorSchemeBlock) {
                        throw new IllegalArgumentException("Already in color scheme definition");
                    }
                    inColorSchemeBlock = true;
                    name = line.substring(0, line.indexOf("{")).trim();
                    continue;
                }
                if (line.indexOf("}") >= 0) {
                    if (!inColorSchemeBlock) {
                        throw new IllegalArgumentException("Not in color scheme definition");
                    }
                    inColorSchemeBlock = false;
                    if ((name == null) || (kind == null) || (ultraLight == null) || (extraLight == null) || (light == null) || (mid == null) || (dark == null) || (ultraDark == null) || (foreground == null)) {
                        throw new IllegalArgumentException("Incomplete specification");
                    }
                    Color[] colors = new Color[] { ultraLight, extraLight, light, mid, dark, ultraDark, foreground };
                    if (kind == ColorSchemeKind.LIGHT) {
                        schemes.add(getLightColorScheme(name, colors));
                    } else {
                        schemes.add(getDarkColorScheme(name, colors));
                    }
                    name = null;
                    kind = null;
                    ultraLight = null;
                    extraLight = null;
                    light = null;
                    mid = null;
                    dark = null;
                    ultraDark = null;
                    foreground = null;
                    continue;
                }
                String[] split = line.split("=");
                if (split.length != 2) {
                    throw new IllegalArgumentException("Unsupported format in line " + line);
                }
                String key = split[0].trim();
                String value = split[1].trim();
                if ("kind".equals(key)) {
                    if (kind == null) {
                        if ("Light".equals(value)) {
                            kind = ColorSchemeKind.LIGHT;
                            continue;
                        }
                        if ("Dark".equals(value)) {
                            kind = ColorSchemeKind.DARK;
                            continue;
                        }
                        throw new IllegalArgumentException("Unsupported format in line " + line);
                    }
                    throw new IllegalArgumentException("'kind' should only be defined once");
                }
                if ("colorUltraLight".equals(key)) {
                    if (ultraLight == null) {
                        ultraLight = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'ultraLight' should only be defined once");
                }
                if ("colorExtraLight".equals(key)) {
                    if (extraLight == null) {
                        extraLight = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'extraLight' should only be defined once");
                }
                if ("colorLight".equals(key)) {
                    if (light == null) {
                        light = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'light' should only be defined once");
                }
                if ("colorMid".equals(key)) {
                    if (mid == null) {
                        mid = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'mid' should only be defined once");
                }
                if ("colorDark".equals(key)) {
                    if (dark == null) {
                        dark = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'dark' should only be defined once");
                }
                if ("colorUltraDark".equals(key)) {
                    if (ultraDark == null) {
                        ultraDark = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'ultraDark' should only be defined once");
                }
                if ("colorForeground".equals(key)) {
                    if (foreground == null) {
                        foreground = Color.decode(value);
                        continue;
                    }
                    throw new IllegalArgumentException("'foreground' should only be defined once");
                }
                throw new IllegalArgumentException("Unsupported format in line " + line);
            }
            ;
        } catch (IOException ioe) {
            throw new IllegalArgumentException(ioe);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                }
            }
        }
        return new SubstanceSkin.ColorSchemes(schemes);
    }
