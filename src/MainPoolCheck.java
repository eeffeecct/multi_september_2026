public class MainPoolCheck {
    public static void main(String[] args) {
        MyPool myPool = new MyPool(5,2);
        myPool.start();
        myPool.setRejectPolicy(MyPool.DROP);

        myPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("kek3");
            }
        });

        myPool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("kek2");
            }
        });

        myPool.shutdown();

    }
}
