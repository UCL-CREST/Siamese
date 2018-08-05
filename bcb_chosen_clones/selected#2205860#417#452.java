    public static Shader loadShader(String vspath, String fspath, int textureUnits, boolean separateCam, boolean fog) throws ShaderProgramProcessException {
        if (vspath == "" || fspath == "") return null;
        BufferedReader in;
        String vert = "", frag = "";
        try {
            URL v_url = Graphics.class.getClass().getResource("/eu/cherrytree/paj/graphics/shaders/" + vspath);
            String v_path = AppDefinition.getDefaultDataPackagePath() + "/shaders/" + vspath;
            if (v_url != null) in = new BufferedReader(new InputStreamReader(v_url.openStream())); else in = new BufferedReader(new InputStreamReader(new FileReader(v_path).getInputStream()));
            boolean run = true;
            String str;
            while (run) {
                str = in.readLine();
                if (str != null) vert += str + "\n"; else run = false;
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Couldn't read in vertex shader \"" + vspath + "\".");
            throw new ShaderNotLoadedException(vspath, fspath);
        }
        try {
            URL f_url = Graphics.class.getClass().getResource("/eu/cherrytree/paj/graphics/shaders/" + fspath);
            String f_path = AppDefinition.getDefaultDataPackagePath() + "/shaders/" + fspath;
            if (f_url != null) in = new BufferedReader(new InputStreamReader(f_url.openStream())); else in = new BufferedReader(new InputStreamReader(new FileReader(f_path).getInputStream()));
            boolean run = true;
            String str;
            while (run) {
                str = in.readLine();
                if (str != null) frag += str + "\n"; else run = false;
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Couldn't read in fragment shader \"" + fspath + "\".");
            throw new ShaderNotLoadedException(vspath, fspath);
        }
        return loadShaderFromSource(vert, frag, textureUnits, separateCam, fog);
    }
