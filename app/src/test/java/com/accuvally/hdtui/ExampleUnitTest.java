package com.accuvally.hdtui;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {





    @Test
    public void httpTest() throws Exception {
//        String url="https://api.huodongxing.com/v3/utils/qret?qid=b9bbeda85548459f9b134d07bef05258&type=1";
//
//        URL realUrl = new URL(url);
//
//        System.out.println(realUrl.getPath());
//        System.out.println(realUrl.getQuery());

       /* HttpCilents httpCilents=new HttpCilents(AccuApplication.getInstance());
        final List<NameValuePair> params2=new ArrayList<NameValuePair>();

        httpCilents.postA(url,params2,null);*/

        String s="http://www.huodongxing.com/news/770644596518";
        int index= s.indexOf("huodongxing.com/news");

        System.out.print(index);

       String S1[]= s.split("huodongxing.com/news/");

        System.out.print(index);
    }

    @Test
    public void parseUrl(){
//        https://api.huodongxing.com/v3/utils/qret?qid=b9bbeda85548459f9b134d07bef05258&type=1
        String url="https://api.huodongxing.com/v3/utils/qret?qid=b9bbeda85548459f9b134d07bef05258&type=1";
        String url2="";
        List<NameValuePair> params2=new ArrayList<>();
        String[] strings=url.split("\\?");
        url2=strings[0];
        System.out.println(strings[0]);
//        System.out.println(strings[1]);

        String[] strings1=strings[1].split("&");
        for(String s:strings1){
            System.out.println(s);
            String[] strings2=s.split("=");
            params2.add(new BasicNameValuePair(strings2[0],strings2[1]));

        }

        System.out.println(strings[0]);


    }

    @Test
    public void addition_isCorrect() throws Exception {

        String s="var qServerUrl = \"http://rush.huodongxing.com/rush/f9ad2da871\";" ;
        String s2="http://rush.huodongxing.com/rush/f0db853fde\"";

        String s1 ="325363798255889578\n";
        System.out.println(s1.substring(0, s1.length() - 1));


        System.out.println("1050".length());

        System.out.println("1050\n".trim());
        System.out.println("  1050\n   ".trim());
        System.out.print("1050\n".trim());
        System.out.print("xxxx");
        assertEquals(4, 2 + 2);

       /* java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#0.00");
        System.out.println(df.format(3.4));
        System.out.println(df.format(0.4));

        String s=" <Form max = \"3\"><Version>1</Version><Items><Item><Sort>1</Sort><Type>textarea</Type><Title>学校</Title><Description>报名用户大学名称</Description></Item><Item><Sort>2</Sort><Type>textarea</Type><Title>专业</Title><Description>报名用户专业</Description></Item><Item><Sort>3</Sort><Type>textarea</Type><Title>班级</Title><Description>报名用户班级</Description></Item></Items></Form>\n";

        String sinceStartTime="";String sinceEndTime="";
        int limit=0;int offset=0;
        String queryString="http://180.150.178.179:25679/hdx/pass_follow/_search?q="+
                URLEncoder.encode("target_type:2" + " AND update_date:[" + sinceStartTime + " TO " + sinceEndTime + "]", "utf-8")
                +"&from=" + offset + "&size=" + limit + "&pretty";*/
    }

  /*  class Account {
        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        ThreadLocal<String> name=new ThreadLocal<>();

    }

    class MyThread extends Thread{
        public MyThread(String threadName, Account account) {
            super(threadName);
            this.account = account;
        }

        Account account;

        @Override
        public void run() {
            super.run();

            for(int i=0;i<10;i++){
                System.out.println(account.getName()+"  "+getName()+"  " +i);

                Integer a;
                if(i==5){
                    account.setName(getName());
                }

            }

        }
    }



    @Test
    public void loaclTest() throws Exception {

        Account account=new Account();
        System.out.println(account.getName() + "  " + Thread.currentThread().getName() + "  ");
        new MyThread("aaa",account).start();
        new MyThread("bbb",account).start();

        AtomicInteger atomicInteger;

        AtomicReference atomicReference;
    }

    class Spinlock{
        AtomicReference<Thread> atomicReference=new AtomicReference<>();

        public void lock(){//不可重入？？？
            Thread thread =Thread.currentThread();
            while (!atomicReference.compareAndSet(null,thread));
        }


        public void unlock(){

            Thread thread =Thread.currentThread();
            if(thread==atomicReference.get()){
                atomicReference.set(null);
            }

        }


    }

    class RTSpinlock{
        AtomicReference<Thread> atomicReference=new AtomicReference<>();
        private volatile int count=0;

        public void lock(){
            Thread thread =Thread.currentThread();
            if(thread==atomicReference.get()){
                count++;
                return;
            }

            while (!atomicReference.compareAndSet(null,thread));
        }


        public void unlock(){
            Thread thread =Thread.currentThread();

            if(thread==atomicReference.get()){
                if(count==0){
                    atomicReference.set(null);
                }else {
                    count--;
                }

            }



        }


    }





    public synchronized void lock1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    System.out.println("lock1");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("lock1  InterruptedException");
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }



    public static synchronized void staticlock1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    System.out.println("staticlock1");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("staticlock1  InterruptedException");
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }



    @Test
    public void loaclTest1() throws Exception {


        ReadWriteLock readWriteLock;
        ReentrantLock reentrantReadWriteLock;
        lock1();
        staticlock1();

        while (Thread.activeCount()>0){
            Thread.yield();
        }
    }*/

}