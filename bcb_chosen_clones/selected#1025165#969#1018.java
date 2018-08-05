        private void compileJarFile() {
            String javaFileName = jarFileName + ".java";
            try {
                File pluginWorkDirectory = new File(CastadivaModel.PLUGIN_WORKFOLDER);
                pluginWorkDirectory.mkdirs();
                File pluginsDir = new File(pluginWorkDirectory.getPath() + "/castadiva/Plugins");
                pluginsDir.mkdirs();
                BufferedWriter bout = new BufferedWriter(new FileWriter(pluginWorkDirectory.getPath() + "/castadiva/Plugins/" + javaFileName));
                bout.write("package castadiva.Plugins;\n");
                bout.write("import java.io.*;\n");
                bout.write("import java.util.zip.ZipEntry;\n");
                bout.write("import java.util.jar.JarFile;\n");
                bout.write("import lib.IPluginCastadiva;\n");
                bout.write("public class " + jarFileName + " implements IPluginCastadiva {\n");
                bout.write("    public String getBin() {\n");
                bout.write("        return \"" + binaryFilePath + "\"; \n    }\n");
                bout.write("    public String getFlags() {\n");
                bout.write("        return \"" + protocolFlags + "\"; \n    }\n");
                bout.write("    public String getPathConf() {\n");
                bout.write("        return \"" + configurationFilePath + "\"; \n    }\n");
                bout.write("    public String getConfContent(){\n");
                bout.write("        BufferedReader confFileReader;\n");
                bout.write("        try {\n");
                bout.write("            JarFile jar = new JarFile(\"" + CastadivaModel.PLUGIN_JAR_FOLDER + "/" + jarFileName + ".jar\");\n");
                bout.write("            ZipEntry entry = jar.getEntry(\"" + configurationFilename[configurationFilename.length - 1] + "\");\n");
                bout.write("            confFileReader = new BufferedReader(new InputStreamReader(jar.getInputStream(entry)));\n");
                bout.write("            String confFile = \"\";\n");
                bout.write("            String confFileLine;\n");
                bout.write("            while((confFileLine = confFileReader.readLine()) != null){\n");
                bout.write("                 confFile+=\"\\n\"+confFileLine;\n");
                bout.write("            }\n");
                bout.write("            return(confFile);\n");
                bout.write("        } catch (Exception ex) {\n");
                bout.write("            System.out.println(ex);\n");
                bout.write("        }\n");
                bout.write("        return(null);\n");
                bout.write("    }\n");
                bout.write("    public String getConf(){\n");
                bout.write("        return(\"" + configurationFilename[configurationFilename.length - 1] + "\");\n");
                bout.write("    }\n");
                bout.write("    public String getKillInstruction() {\n");
                bout.write("        return  \"killall " + binFileName[binFileName.length - 1] + " 2>/dev/null\"" + ";\n    }\n}");
                bout.close();
                BufferedWriter confFileWriter = new BufferedWriter(new FileWriter(CastadivaModel.PLUGIN_WORKFOLDER + "/" + configurationFilename[configurationFilename.length - 1]));
                confFileWriter.write(protocolConfiguration);
                confFileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(ProtocolsGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
