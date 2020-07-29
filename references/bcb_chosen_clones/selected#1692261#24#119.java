    @Override
    public ReturnValue do_verify_parameters() {
        ReturnValue ret = new ReturnValue();
        if (this.getParameters().size() < 2) {
            StringBuffer syntax = new StringBuffer("To run bfast you must specify atleast one command. Usage:" + System.getProperty("line.separator"));
            syntax.append(get_syntax());
            ret.setStderr(syntax.toString());
            ret.setExitStatus(2);
            return ret;
        }
        try {
            String[] args = { this.getParameters().get(0), this.getParameters().get(1) };
            Process p = Runtime.getRuntime().exec(args);
            p.waitFor();
            ret.setExitStatus(p.exitValue());
            if (ret.getExitStatus() != 0) {
                StringBuffer syntax = new StringBuffer(this.getParameters().get(1) + " is not a valid command for bfast. Usage:" + System.getProperty("line.separator"));
                syntax.append(get_syntax());
                ret.setStderr(syntax.toString());
            }
        } catch (IOException e) {
            ret.setExitStatus(4);
            ret.setStderr(e.toString());
        } catch (InterruptedException e) {
            ret.setExitStatus(5);
            ret.setStderr(e.toString());
        }
        if (ret.getExitStatus() != 0) return ret;
        if (this.getStdoutFile() == null) {
            ret.setStderr("Bfast writes results to stdout, so you must redirect stdout to a file in order to use it. See the -o option for the seqware runner.");
            ret.setExitStatus(ReturnValue.STDOUTERR);
            return ret;
        }
        boolean valid = false;
        for (int i = 2; i < this.getParameters().size(); i++) {
            if (this.getParameters().get(i).compareTo("-f") == 0) {
                if (this.getParameters().get(i + 1).endsWith(".fa")) {
                    valid = true;
                    break;
                }
            }
        }
        if (valid == false) {
            ret.setExitStatus(3);
            ret.setStderr("Bfast always requires a '-f file.fa' argument");
            return ret;
        }
        if (this.getParameters().get(1).compareTo("match") == 0) {
            valid = false;
            for (int i = 2; i < this.getParameters().size(); i++) {
                if (this.getParameters().get(i).compareTo("-r") == 0) {
                    if (this.getParameters().get(i + 1).endsWith(".fastq")) {
                        valid = true;
                        break;
                    }
                }
            }
            if (valid == false) {
                ret.setExitStatus(3);
                ret.setStderr("Bfast match always requires a '-r file.fastq' argument");
                return ret;
            }
        } else if (this.getParameters().get(1).compareTo("localalign") == 0) {
            valid = false;
            for (int i = 2; i < this.getParameters().size(); i++) {
                if (this.getParameters().get(i).compareTo("-m") == 0) {
                    if (this.getParameters().get(i + 1).endsWith(".bmf")) {
                        valid = true;
                        break;
                    }
                }
            }
            if (valid == false) {
                ret.setExitStatus(3);
                ret.setStderr("Bfast localalign always requires a '-m file.bmf' argument");
                return ret;
            }
        } else if (this.getParameters().get(1).compareTo("postprocess") == 0) {
            valid = false;
            for (int i = 2; i < this.getParameters().size(); i++) {
                if (this.getParameters().get(i).compareTo("-i") == 0) {
                    if (this.getParameters().get(i + 1).endsWith(".baf")) {
                        valid = true;
                        break;
                    }
                }
            }
            if (valid == false) {
                ret.setExitStatus(3);
                ret.setStderr("Bfast postprocess always requires a '-i file.baf' argument");
                return ret;
            }
        }
        ret.setExitStatus(0);
        return ret;
    }
