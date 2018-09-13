import java.util.concurrent.TimeUnit;

public class CallTestRun {

    public void m1() {
        synchronized (Object.class) {
            try {
                System.out.println("m1 start");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("m1 end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void m2() {
        synchronized (Object.class) {
            System.out.println("m2 start");
        }

    }

    public static void main(String[] args) {
        final CallTestRun r = new CallTestRun();
        new Thread(new Runnable() {

            @Override
            public void run() {
                r.m1();

            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(2);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    r.m2();

                }
            }).start();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
