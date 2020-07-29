    private void compileBootImage(String args[]) {
        String bi_args[] = new String[args.length - 1];
        String bi_name = args[args.length - 1];
        Class pub_cl;
        Object pub_obj;
        java.lang.reflect.Method pub_methods[];
        jdp_console.writeOutput("Compiling Boot Image for " + bi_name + " . . . ");
        for (int i = 0; i < bi_args.length; i++) {
            bi_args[i] = args[i + 1];
        }
        try {
            pub_cl = Class.forName(args[0]);
            pub_obj = pub_cl.newInstance();
            pub_methods = pub_cl.getMethods();
            for (int n = 0; n < pub_methods.length; n++) {
                if (pub_methods[n].getName().equals("main")) {
                    Object invoke_args[] = { bi_args };
                    pub_methods[n].invoke(pub_obj, invoke_args);
                    return;
                }
            }
        } catch (ClassNotFoundException e) {
            jdp_console.writeOutput("cannot compile, publicizing class loader not found: " + args[0]);
            System.exit(1);
        } catch (InstantiationException e1) {
            jdp_console.writeOutput("cannot compile, problem instantiating class");
            System.exit(1);
        } catch (IllegalAccessException e2) {
            jdp_console.writeOutput("cannot compile, illegal access to class");
            System.exit(1);
        } catch (InvocationTargetException e3) {
            jdp_console.writeOutput("cannot compile, Invocation Target Exception:");
            jdp_console.writeOutput(e3.getMessage());
            System.exit(1);
        }
    }
