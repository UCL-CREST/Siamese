    private void loadObject(URL url) throws IOException {
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        int linecounter = 0;
        try {
            String line;
            boolean firstpass = true;
            String[] coordstext;
            Material material = null;
            while (((line = br.readLine()) != null)) {
                linecounter++;
                line = line.trim();
                if (line.length() > 0) {
                    if (line.startsWith("mtllib")) {
                        String mtlfile = line.substring(6).trim();
                        loadMtlFile(new URL(url, mtlfile));
                    } else if (line.startsWith("usemtl")) {
                        String mtlname = line.substring(6).trim();
                        material = (Material) materials.get(mtlname);
                    } else if (line.charAt(0) == 'v' && line.charAt(1) == ' ') {
                        float[] coords = new float[4];
                        coordstext = line.split("\\s+");
                        for (int i = 1; i < coordstext.length; i++) {
                            coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
                        }
                        if (firstpass) {
                            rightpoint = coords[0];
                            leftpoint = coords[0];
                            toppoint = coords[1];
                            bottompoint = coords[1];
                            nearpoint = coords[2];
                            farpoint = coords[2];
                            firstpass = false;
                        }
                        if (coords[0] > rightpoint) {
                            rightpoint = coords[0];
                        }
                        if (coords[0] < leftpoint) {
                            leftpoint = coords[0];
                        }
                        if (coords[1] > toppoint) {
                            toppoint = coords[1];
                        }
                        if (coords[1] < bottompoint) {
                            bottompoint = coords[1];
                        }
                        if (coords[2] > nearpoint) {
                            nearpoint = coords[2];
                        }
                        if (coords[2] < farpoint) {
                            farpoint = coords[2];
                        }
                        vertexsets.add(coords);
                    } else if (line.charAt(0) == 'v' && line.charAt(1) == 't') {
                        float[] coords = new float[4];
                        coordstext = line.split("\\s+");
                        for (int i = 1; i < coordstext.length; i++) {
                            coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
                        }
                        vertexsetstexs.add(coords);
                    } else if (line.charAt(0) == 'v' && line.charAt(1) == 'n') {
                        float[] coords = new float[4];
                        coordstext = line.split("\\s+");
                        for (int i = 1; i < coordstext.length; i++) {
                            coords[i - 1] = Float.valueOf(coordstext[i]).floatValue();
                        }
                        vertexsetsnorms.add(coords);
                    } else if (line.charAt(0) == 'f' && line.charAt(1) == ' ') {
                        coordstext = line.split("\\s+");
                        int[] v = new int[coordstext.length - 1];
                        int[] vt = new int[coordstext.length - 1];
                        int[] vn = new int[coordstext.length - 1];
                        for (int i = 1; i < coordstext.length; i++) {
                            String fixstring = coordstext[i].replaceAll("//", "/0/");
                            String[] tempstring = fixstring.split("/");
                            v[i - 1] = Integer.valueOf(tempstring[0]).intValue();
                            if (tempstring.length > 1) {
                                vt[i - 1] = Integer.valueOf(tempstring[1]).intValue();
                            } else {
                                vt[i - 1] = 0;
                            }
                            if (tempstring.length > 2) {
                                vn[i - 1] = Integer.valueOf(tempstring[2]).intValue();
                            } else {
                                vn[i - 1] = 0;
                            }
                        }
                        Face face = new Face(v, vt, vn, material);
                        faces.add(face);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to read file: " + br.toString());
        } catch (NumberFormatException e) {
            System.out.println("Malformed OBJ (on line " + linecounter + "): " + br.toString() + "\r \r" + e.getMessage());
        }
    }
