    public void train() {
        String trainingDataPath = path + "/training/data.train";
        String templatePath = path + "/crfpp-templates/header.template";
        String modelPath = path + "/crfpp-models/model.crf";
        try {
            File f = new File(modelPath);
            f.renameTo(new File(modelPath + ".old"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String command = crfLearnPath + " " + templatePath + " " + trainingDataPath + " " + modelPath;
        try {
            Process child = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(child.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
            child.waitFor();
            System.out.println("Status: " + child.exitValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
