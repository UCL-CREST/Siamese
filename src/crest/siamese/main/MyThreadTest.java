package crest.siamese.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MyThreadTest {

    public static class WorkerThread implements Callable<String> {

        private String command;

        public WorkerThread(String s) {
            this.command = s;
        }

        private void processCommand() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString(){
            return this.command;
        }

        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
            processCommand();
            System.out.println(Thread.currentThread().getName()+" End.");
            return this.command;
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<String>> output = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Callable worker = new WorkerThread("" + i);
            Future<String> future = executor.submit(worker);
            output.add(future);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
        for (Future<String> o: output) {
            try {
                System.out.println(o.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
