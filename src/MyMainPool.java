public class MyMainPool {

    public static void main(String[] args) {
        MyNicePool myNicePool = new MyNicePool(30, 10);
        myNicePool.setRejectPolicy(MyNicePool.REJECT);
        myNicePool.start();

        myNicePool.setBeforeRun(new Runnable() {
            @Override
            public void run() {
                System.out.println("before run " + Thread.currentThread().getName());
            }
        });

        myNicePool.setAfterRun(new Runnable() {
            @Override
            public void run() {
                System.out.println("after run " + Thread.currentThread().getName());
            }
        });

        for (int i = 0; i < 10; i++) {
            myNicePool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("kek" + Thread.currentThread().getName());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }


//        myNicePool.shutdown();

    }


}
