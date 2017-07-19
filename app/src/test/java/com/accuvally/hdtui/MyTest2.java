package com.accuvally.hdtui;

/**
 * Created by MBENBEN on 2017/7/18.
 */
public class MyTest2 {

    static int m=0;
    public static void main(String[] args)
    {
        System.out.println("Hello World!");


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println(""+(m++));
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){

                    }
                }

            }
        },"mythread").start();
    }


}
