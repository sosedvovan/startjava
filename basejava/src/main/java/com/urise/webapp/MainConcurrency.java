package com.urise.webapp;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MainConcurrency {

    // потоки будут инкрементить счетчики
    private static int counter1;
    private static int counter2;
    private static int counter3;
    private static int counter4;
    private static int counter5;

    //читобы обойтись без синхронизид и локов можно исп атомики для счетчиков:
    //в своей реализации в цикле атомик пытается добавить 1 до тех пор пока
    //ожидаемое значение, что у нас есть, не совпало со значением в ячейке памяти
    //процессор эту инструкцию делает атомарно
    private static final AtomicInteger atomicCounter = new AtomicInteger();
    //и в статик методе inc() оставим инкрементацию только с этим атомиком

    private static final Object LOCK = new Object();//объект блокировки

    //в 5 Ява появились классы - локи(интерфейс с готов реализацией) - кот вместо синхронизид
    //его можно исп как монитор для очереди потоков
    //те в статик методе inc() уберем слово синхрониззед(см внизу)
    private static final Lock lock = new ReentrantLock();
    //еще есть рид-врайт локи(когда надо блокировать при записи а причтении не обязательно)
    private static ReadWriteLock reentrantReadWriteLock;
    private static final Lock READ_LOCK = reentrantReadWriteLock.readLock();

    //еще есть форматоры Дат
    private static final SimpleDateFormat sdf = new SimpleDateFormat();
    //для работы с ним положим его с средЛокал:
    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue(){
            return new SimpleDateFormat();//задали начальное значение
        }
    };
    //в результате у каждого Дате будет свой SimpleDateFormat
    //и они пересекаться не будут

    public static void main(String[] args) throws InterruptedException {
        //выведем имя и статус(енум) главного потока майн
        System.out.println("Name main cancurrent :" + Thread.currentThread().getName() + " ," + Thread.currentThread().getState());

        //дадим имя объекту потока(но не самому потоку)
        //запустили поток переопределив внутренний метод run() класса Thread
        Thread thread0 = new Thread() {
            @Override
            public void run() {
                //внутри Thread можно сказать просто getName()(есть доступ ко всем методам Thread)
                System.out.println("Name dop concurrent :" + getName() + " " + getState());
            }
        };
        thread0.start();
        thread0.join();//текущий поток (майн) будет ждать пока не завершится этот поток. то можно и без синхронизации обойтись
        //вместо sleep(500), потому, что кол-во милисекунд точно не угадать
        System.out.println(thread0.getName() + " ," + thread0.getState());


        //запустили поток переопределив метод run() анон класса имплементищего Runnable
        //так предпочтительней(через переменную тк ее можно куда нибудь передавать + можно множественное наследование)
        new Thread(new Runnable() {
            @Override
            public void run() {
                //внутри Runnable  нельзя сказать просто getName()(там нет такого доступа)
                System.out.println("Name Runnable concurrent :" + Thread.currentThread().getName() + " ," + Thread.currentThread().getState());
            }
        }).start();


        //Runnable через лямбду
        new Thread(() -> {
            //внутри Runnable  нельзя сказать просто getName()(там нет такого доступа)
            System.out.println("Name Runnable concurrent :" + Thread.currentThread().getName() + " ," + Thread.currentThread().getState());
        }).start();




        //созадим 10000 штук потоков чтобы они толкались в один счетчик(зашаренный ресурс)
        //те не параллел(когда каждый поток со своим объектом работает) получается а канкарент

        //используем майн поток
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 100; j++) {
                counter1++;//миллион получится
            }
        }
        System.out.println("main-thread, counter1 :" + counter1);


        //используем потоки
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    counter2++;//миллион не получится без синхронизации
                }
            }).start();
        }
        System.out.println("NO sinhronise, counter2 :" + counter2);


        //используем статик синхроник метод для инкрементации счетчика
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    System.out.println(threadLocal.get().format(new Date()));
                    inc();
                    inc2();
                }
            }).start();
        }
        //миллион все равно не получится даже с синхрон методом тк sout сработает до окончания работы потоков
        //след попробуем усыпить майн поток на ... секунды, чтобы другие успели выполнится
        Thread.sleep(1500);//теперь миллион получится
        System.out.println("counter3 " + counter3);
        System.out.println("counter4 " + counter4);
        System.out.println("counter5 " + counter5);
        System.out.println("LOCK" + LOCK);
        System.out.println("атомик" + atomicCounter.get());

        MainConcurrency mainConcurrency = new MainConcurrency();
        mainConcurrency.inc3();
        System.out.println("mainConcurrency" + mainConcurrency);//



        //создаем 10000 потоков, каждый поток инкрементирует счетчик 100 раз
        //и добавляется в Аррайлист
        MainConcurrency mainConcurrency2 = new MainConcurrency();
        List<Thread> threads = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(() ->{//создается очередной поток (1-н из 10000)
            for (int j = 0; j < 100; j++) {//этот поток 100 запустит inc3() на объекте-мониторе этого класса в этом переопред методе runnable
                mainConcurrency2.inc3();//inc3() synchronized
            }
            });
            thread.start();//стартуем то, что выше в переопределенном runnable, в {...}
            threads.add(thread);//каждый поток(по одному за внешний цикл for) добавится в Аррайлист

            //в forEach идем по Аррайлисту и будем ожидать, пока не закончится каждый поток
            threads.forEach(t -> {//при первой итерации в Аррай 1 поток, на второй итерации-2-а потока... и все они
                try {                //становятся на join(заставляют другие потоки подождать своего окончания)
                    t.join();       //и ничего страшного что на join ставятся не запущенные потоки, кот уже отработали в предидущ. итерациях
                } catch (InterruptedException e) {//главное, что на join текущий поток станет
                    e.printStackTrace();
                }
            });

        }//закрыли внешний for


    }//майн

    //synchronized- значит ставим в очередь к объекту
    //и главное смотреть- к какому объекту-монитору мы встаем в очередь- тогда все ок с синхронизацией

    //при synchronized в этот метод имеет право заходить только единственный поток
    private static /*synchronized через lock()*/ void inc() {//в таком случае синхронизируемся на объекте этого класса

        //инкрементим атомик вместо счетчика
        atomicCounter.incrementAndGet();
        /**lock.lock();
        try {
            counter3++;
        }finally {
            lock.unlock();
        }
        //руками дописываем finally тк если будет ексепшен в try, то поток никогда не закроется
        */
         /**В СЛУЧАЕ СТАТИК МЕТОДА СИНХРОНИЗИРУЕМСЯ НА ОБЪЕКТЕ ЭТОГО КЛАССА
         * если убрать synchronized из объявления метода то синхронизируемся на объекте этого класса:
         * private static void inc(){
         *  synchronized(MainConcurrency.class){//MainConcurrency.class это тож объект, кот загружается класслоудэром и хранится в стеке
         *               counter3++;
         *    }
         *    }
         */
    }


    //можно еще так, если в методе присутствуют еще другие вычисления:
    private static void inc2() {
        double a = Math.sin(13.);
        // Object lock = new Object();
        synchronized (LOCK) {//синхронизируем на объекте блокировке ,но миллион не получили
            counter4++;//каждому из 10000 потоков будет создан объект lock и каждый поток встанет
            //в очередь к своему объекту и будет там единственным в очереди
            //след никакой синхронизации му ними не получится
            //а чтобы получилось - lock надо создавать не здесь, а сюда передавать и по нему блокироваться
            //тогда все потоки встанут в одну очередь только к одному объекту.Как мы и сделали.

        }
    }

    /**
     * В СЛУЧАЕ НЕ СТАТИК МЕТОДА СИНХРОНИЗИРУЕМСЯ НА ОБЪЕКТЕ, КОТ ЭТОТ МЕТОД ВЫЗВАЛ
     * если метод был бы не статический, и мы его вызывали через объект этого
     * класса, то это бы означало что синхронайз блочился на этом объекте
     * <p>
     * MainConcurrency mainConcurrency = new MainConcurrency();
     * mainConcurrency.inc();//если у объекта вызываем какой-то метод, то this внутри этого метода- это ссылка на этот конкретный объект
     * <p>
     * private  void inc(){
     * synchronized(this){ // this - если inc() вызываем через объект этого класса
     * counter3++; //те все 10000 потоков встанет в очередь к этому монитору
     * //и будут выполняться последовательно
     * }
     * ниже демонстрирую в коде:
     */

    private void inc3() {
        synchronized (this) {
            counter5++;
            System.out.println("this" + this);//this == mainConcurrency. те this это объект этого класса mainConcurrency на кот вызывается этот поток

        }
    }

}//класс

/**
 * методы в классе Обжект для синхронизации устаревшие:
 * wait() и др. можем вызывато только внутри синхронизированного блока
 * напр внутри метода:
 * synchronized(LOCK){
 * counter5++;
 * wait()//отдает управление и снимает блокировку и прекращает выполнение блока до поры пока другой поток не встанет в wait
 * или не скжет notify или notifyAll.
 * далее в коде ждем ридфайл например, а другие потоки ждут,
 * а wait() снимает блокировку (встает в положение wait) и отдает управлен другим потокам пока опять не запустится
 * //а notify() или notifyAll(), если написать вместо wait(), пробуждает другой(ие все) поток(и) (которые выходят из состояния wait)
 * только после получения ридфайла(выполнения блока до конца)
 * }
 */

