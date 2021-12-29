package com.urise.webapp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MainConcurrencyPlus {

    public static final int THREADS_NUMBER = 10000;
    private int counter;
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println(Thread.currentThread().getName());

        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
                //далее этот ексепшен выведется только в лог и прога не остановится
                throw new IllegalStateException();
            }
        };
        thread0.start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
            }

            private void inc() {
                synchronized (this) {
//                    counter++;
                }
            }

        }).start();

        System.out.println(thread0.getState());

        final MainConcurrencyPlus mainConcurrencyPlus = new MainConcurrencyPlus();
        //сделаем замок с обратным отчетом(это один из синхронизаторов в пакете Конкаренс):
        CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
        // List<Thread> threads = new ArrayList<>(THREADS_NUMBER);

        //посмотрим на екзекуторы(распаралериватели, без использ потоков):
        //екзекутор сам создас потоки, посмотрев на кол-во ядер
        //получим екзекутор:
        ExecutorService executorService = Executors.newCachedThreadPool();

        //теперь сделаем оберточку с ексекутору, содерщащую блокирующую очередь:
        //все таски екзекутера складываются в эту очередь(напр в фьючи загружаем картинки
        // а completionService будет брать первую загруженную таску)
        CompletionService completionService = new ExecutorCompletionService(executorService);

        //екзекутор узнает сколько у нас процессоров:
        //ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        //здесь цикл ждет завершения предидуще-созданных потоков
        //сделаем цикл через екзекутор(отдадим ему эту задачу):
        //submit() с раннабал-не надо возвращать результат,а с коллабл -надо
        //те если в лямбде дописать return 5; -> изменится на коллабл
        //и сабмит в этом случае возвратит фьючу:
        //это отложенный результат- те когда нам фьючу возвратиили, мы не знаем-
        // исполнился код до конца или еще нет
        //у фиючи можно спросить-isDone-выполняется ли еще, сделать ей кэнсал

        for (int i = 0; i < THREADS_NUMBER; i++) {

            Future<Integer> future =    //Ctrl+Alt+V присвоить к чемуто возврат
            //засабмитим задачу екзекутору:
            executorService.submit(() -> {//екзекутору не надо говорить старт
                for (int j = 0; j < 100; j++) {
                    mainConcurrencyPlus.inc();
                }

                latch.countDown();
                return 5;
//                Thread thread = new Thread(() -> {
//                    for (int j = 0; j < 100; j++) {
//                        mainConcurrencyPlus.inc();
//                    }//после выполнения кода в итерации цикла делаем countDown-механизм ожидающий выполнения всех потоков:
//                latch.countDown();//замок делает обратный отчет.-это вместо join() в forEach
//                });//когда latch станет=0 продолжится программа(то заджоинили)
//                thread.start();
                // threads.add(thread);
            });
            completionService.poll();//берем первую выполненную таску

            System.out.println(future.isDone());//выполняется ли еще фьюча
            System.out.println(future.get());//делает вейт пока ждет результата(пока фьюча не готова) и потом берет результат
        }

            //здесь подключаемся ко всем потокам и ждем пока они закончатся
//            threads.forEach(t -> {
//                try {
//                    t.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
            //скажем замочку- ожидай 10 сек, а потом отваливайся:
            //без ожидания какие то потоки в екзекуторе могут не успеть запуститься
            latch.await(10, TimeUnit.SECONDS);
            executorService.shutdown();//выключим екзекутор, чтобы параллельно с прогой далее не работал
            System.out.println(mainConcurrencyPlus.counter);


            //для демонстрации деадЛока сделаем два лока-два объекта(ресурса)
        //на которых можно лочится. пусть это будут стринги
            final String lock1 = "lock1";
            final String lock2 = "lock2";
            //и lock1, lock2 меняем местами чтобы lock1 захватил блокировку
        //по первому стрингу и ждал вторую
        //а lock2 наоботот, захватит блокировку по второму стрингу и будет ждать первую
            deadLock(lock1, lock2);
            deadLock(lock2, lock1);

        }//закрыли майн

    //здесь поток захватывает сначала один ресурс, потом второй
        private static void deadLock (Object lock1, Object lock2){
           //созд поток с пом ранабл
            new Thread(() -> {
                //выводим состояние потока
                System.out.println("Waiting " + lock1);
                //захватывает этот поток(первый ресурс)
                synchronized (lock1) {
                    //выводим состояние-захватили
                    System.out.println("Holding " + lock1);
                    try {
                        //чтобы был деад лок в этом моменте надо
                        //подождать, чтобы второй поток стартовал
                        //и захватил вторую стрингу(ресурс lock2)
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Waiting " + lock2);
                    //захватывает этот поток(второй ресурс)
                    synchronized (lock2) {
                        //выводим состояние-захватили
                        System.out.println("Holding " + lock2);
                    }
                }
            }).start();
        }
        //Waiting lock1
        //Holding lock1 захватил первый поток
        //Waiting lock2
        //Holding lock2 захватил второй поток
        //Waiting lock2 оба потока в ожидании друг друга
        //Waiting lock1 оба потока в ожидании друг друга 1-й ждет 2-ого , а 2-ой 1-ого
        //программа никогда не завершится сама

        private synchronized void inc () {
//        synchronized (this) {
//        synchronized (MainConcurrency.class) {
            counter++;
//                wait();
//                readFile
//                ...
//        }
        }
    }


