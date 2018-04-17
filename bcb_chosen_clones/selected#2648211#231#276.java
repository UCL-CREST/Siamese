    private static void outputFile(File dstFile, Map<String, SortedMap<String, ZoneRules>> allBuiltZones, Set<String> allRegionIds, Set<ZoneRules> allRules) {
        try {
            JarOutputStream jos = new JarOutputStream(new FileOutputStream(dstFile));
            jos.putNextEntry(new ZipEntry("javax/time/calendar/zone/ZoneRules.dat"));
            DataOutputStream out = new DataOutputStream(jos);
            out.writeByte(1);
            out.writeUTF("TZDB");
            String[] versionArray = allBuiltZones.keySet().toArray(new String[allBuiltZones.size()]);
            out.writeShort(versionArray.length);
            for (String version : versionArray) {
                out.writeUTF(version);
            }
            String[] regionArray = allRegionIds.toArray(new String[allRegionIds.size()]);
            out.writeShort(regionArray.length);
            for (String regionId : regionArray) {
                out.writeUTF(regionId);
            }
            List<ZoneRules> rulesList = new ArrayList<ZoneRules>(allRules);
            for (String version : allBuiltZones.keySet()) {
                out.writeShort(allBuiltZones.get(version).size());
                for (Map.Entry<String, ZoneRules> entry : allBuiltZones.get(version).entrySet()) {
                    int regionIndex = Arrays.binarySearch(regionArray, entry.getKey());
                    int rulesIndex = rulesList.indexOf(entry.getValue());
                    out.writeShort(regionIndex);
                    out.writeShort(rulesIndex);
                }
            }
            out.writeShort(rulesList.size());
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            for (ZoneRules rules : rulesList) {
                baos.reset();
                DataOutputStream dataos = new DataOutputStream(baos);
                Ser.write(rules, dataos);
                dataos.close();
                byte[] bytes = baos.toByteArray();
                out.writeShort(bytes.length);
                out.write(bytes);
            }
            jos.closeEntry();
            out.close();
        } catch (Exception ex) {
            System.out.println("Failed: " + ex.toString());
            ex.printStackTrace();
            System.exit(1);
        }
    }
