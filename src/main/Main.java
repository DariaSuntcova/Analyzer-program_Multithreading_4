package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queueSearchA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueSearchB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueSearchC = new ArrayBlockingQueue<>(100);

    public static final char A = 'a';
    public static final char B = 'b';
    public static final char C = 'c';
    public static int countA = 0;
    public static int countB = 0;
    public static int countC = 0;


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countLetters(String text, char letter) {
        int count = 0;
        char[] chars = text.toCharArray();
        for (char c : chars) {
            if (c == letter) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threads = new ArrayList<>();

        Thread generateTextThread = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queueSearchA.put(text);
                    queueSearchB.put(text);
                    queueSearchC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        generateTextThread.start();
        threads.add(generateTextThread);


        Thread searchA = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String text = queueSearchA.take();
                    int count = countLetters(text, A);
                    if (count > countA) {
                        countA = count;
                        //System.out.println("Новое значение А = " + countA);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        searchA.start();
        threads.add(searchA);

        Thread searchB = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String text = queueSearchB.take();
                    int count = countLetters(text, B);
                    if (count > countB) {
                        countB = count;
                        //System.out.println("Новое значение B = " + countB);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        searchB.start();
        threads.add(searchB);

        Thread searchC = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String text = queueSearchC.take();
                    int count = countLetters(text, C);
                    if (count > countC) {
                        countC = count;
                        //System.out.println("Новое значение C = " + countC);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        searchC.start();
        threads.add(searchC);

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Максимальное количество букв А = " + countA);
        System.out.println("Максимальное количество букв B = " + countB);
        System.out.println("Максимальное количество букв C = " + countC);
    }

}
