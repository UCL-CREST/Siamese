    public static void parse(String path, boolean remote) {
        JavaClass jcl = new JavaClass();
        String text = null;
        try {
            text = readPage(path, remote);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        String classData = pullSection(text, "<!-- ======== START OF CLASS DATA ======== -->", false);
        if (classData.length() > 0) {
            int br = classData.indexOf("<BR>");
            String pkg = clearTags(classData.substring(0, br)).trim();
            int pre = classData.indexOf("</H2>", br);
            String title = clearTags(classData.substring(br, pre)).trim();
            String name = title.substring(title.indexOf(" ") + 1);
            if (name.indexOf("<") != -1) name = name.substring(0, name.indexOf("<"));
            jcl.setPackage(pkg);
            jcl.setName(name);
            if (packageLists.get(pkg) == null) {
                packageLists.put(pkg, new ArrayList<String>());
            }
            packageLists.get(pkg).add(name);
        }
        String fieldData = pullSection(text, "<!-- =========== FIELD SUMMARY =========== -->", true);
        if (fieldData.length() > 0) {
            Pattern p = Pattern.compile("<TR .*?>.*?</TR>");
            Matcher m = p.matcher(fieldData);
            while (m.find()) {
                String field = fieldData.substring(m.start(), m.end());
                if (field.indexOf("Deprecated.") != -1 || field.indexOf("Field Summary") != -1) continue;
                int br = field.indexOf("<BR>");
                String field_value = clearTags(field.substring(0, br)).trim();
                String field_desc = clearTags(field.substring(br, field.length())).trim();
                boolean is_static = false;
                if (field_value.startsWith("static ")) {
                    is_static = true;
                    field_value = field_value.substring(7);
                }
                String vis = "public";
                for (int i = 0; i < VISIBILITY.length; i++) {
                    if (field_value.startsWith(VISIBILITY[i] + " ")) {
                        vis = VISIBILITY[i];
                        field_value = field_value.substring(VISIBILITY[i].length() + 1);
                    }
                }
                int space = field_value.indexOf(" ");
                String field_type = field_value.substring(0, space);
                field_value = field_value.substring(space + 1);
                jcl.addField(field_value, field_type, field_desc, vis, is_static);
            }
        }
        String constructorData = pullSection(text, "<!-- ======== CONSTRUCTOR SUMMARY ======== -->", true);
        if (constructorData.length() > 0) {
            Pattern p = Pattern.compile("<TR .*?>.*?</TR>");
            Matcher m = p.matcher(constructorData);
            while (m.find()) {
                String con = constructorData.substring(m.start(), m.end());
                if (con.indexOf("Deprecated.") != -1 || con.indexOf("Constructor Summary") != -1) continue;
                int br = con.indexOf("<BR>");
                String con_value = clearTags(con.substring(0, br)).trim();
                String con_desc = clearTags(con.substring(br, con.length())).trim();
                jcl.addConstructor(con_value, con_desc);
            }
        }
        String methodData = pullSection(text, "<!-- ========== METHOD SUMMARY =========== -->", true);
        if (methodData.length() > 0) {
            Pattern p = Pattern.compile("<TR .*?>.*?</TR>");
            Matcher m = p.matcher(methodData);
            while (m.find()) {
                String method = methodData.substring(m.start(), m.end());
                if (method.indexOf("Deprecated.") != -1 || method.indexOf("Method Summary") != -1) continue;
                int br = method.indexOf("<BR>", method.indexOf("</FONT>"));
                if (br == -1) {
                    int end = methodData.indexOf("</TR>", m.end() + 1);
                    method = methodData.substring(m.start(), end);
                    br = method.indexOf("<BR>", method.indexOf("</FONT>"));
                }
                String method_value = clearTags(method.substring(0, br)).trim();
                String method_desc = clearTags(method.substring(br, method.length())).trim();
                boolean is_static = false;
                if (method_value.startsWith("static ")) {
                    method_value = method_value.substring(7);
                    is_static = true;
                }
                String vis = "public";
                for (int i = 0; i < VISIBILITY.length; i++) {
                    if (method_value.startsWith(VISIBILITY[i] + " ")) {
                        vis = VISIBILITY[i];
                        method_value = method_value.substring(VISIBILITY[i].length() + 1);
                    }
                }
                int space = method_value.lastIndexOf(" ", method_value.indexOf("("));
                String method_type = method_value.substring(0, space);
                method_value = method_value.substring(space + 1);
                jcl.addMethod(method_value, method_type, method_desc, vis, is_static);
            }
        }
        jcl.save();
    }
