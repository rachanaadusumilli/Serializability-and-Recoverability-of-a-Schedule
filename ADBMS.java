/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ADBMS {

    /**
     * @param args the command line arguments
     */
    public static String[] GenerateTransaction(int TransactionId) {
        //Genarate a transaction
        char DataItems[] = {'a', 'b', 'c', 'd'};
        char BasicOperations[] = {'R', 'W'};
        char SpecialOperations[] = {'A', 'C'};
        Random Rand = new Random();
        //operations can be 4 to 8  in a single trasaction
        String T[] = new String[Rand.nextInt(4) + 4];
        for (int i = 0; i < T.length - 1; i++) {
            T[i] = BasicOperations[Rand.nextInt(BasicOperations.length)] + "" + TransactionId + DataItems[Rand.nextInt(DataItems.length)];
        }
        T[T.length - 1] = SpecialOperations[Rand.nextInt(SpecialOperations.length)] + "" + TransactionId;
        return T;
    }

    public static < E> void PrintArray(E[] inputArray) {
        // Display array elements
        for (E element : inputArray) {
            System.out.printf("%s ", element);
        }
        System.out.println();
    }

    public static String[] GenerateHistory() {

        String[] T1 = GenerateTransaction(1);
        String[] T2 = GenerateTransaction(2);
        String[] T3 = GenerateTransaction(3);
        String[] T4 = GenerateTransaction(4);
        int TotalLength = T1.length + T2.length + T3.length + T4.length;
        String[] H = new String[TotalLength];
        //copy the T1,T2,T3,T4 into H
        int Counter = 0;
        for (int i = 0; i < T1.length; i++) {
            H[Counter++] = T1[i];
        }
        for (int i = 0; i < T2.length; i++) {
            H[Counter++] = T2[i];
        }
        for (int i = 0; i < T3.length; i++) {
            H[Counter++] = T3[i];
        }
        for (int i = 0; i < T4.length; i++) {
            H[Counter++] = T4[i];
        }

        //shuffle H
        List<String> strList = Arrays.asList(H);
        Collections.shuffle(strList);
        H = strList.toArray(new String[strList.size()]);

        //place abort and commit in correct place for T1, T2, T3, T4
        for (int j = 1; j <= 4; j++) {
            String Find = j + "";
            int LastTransactionOperation = 0;
            int LastTransactionSOperation = 0;
            for (int i = 0; i < H.length; i++) {
                if (H[i].contains(Find)) {
                    if (H[i].contains("A") || H[i].contains("C")) {
                        LastTransactionSOperation = i;
                    } else {
                        LastTransactionOperation = i;
                    }
                }
            }
            if (LastTransactionOperation > LastTransactionSOperation) {
                //swap two of them
                String temp = H[LastTransactionOperation];
                H[LastTransactionOperation] = H[LastTransactionSOperation];
                H[LastTransactionSOperation] = temp;
            }
        }
        return H;
    }

    public static boolean isStrict(String H[]) {
        /*If in the given schedule, each transaction Tj neither reads nor
        writes any data item ‘x’ until the last transaction Ti
        that has written ‘X’ is committed or aborted then it is strict.*/
        boolean ACA = true;
        //get the data items written by T1 and commit or abort position
        for (int j = 1; j <= 4; j++) {
            String Find = j + "";
            int CALocation = 0;
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < H.length; i++) {
                if (H[i].contains(Find)) {

                    if (H[i].contains("A") || H[i].contains("C")) {
                        CALocation = i;
                        break;
                    } else {
                        if (H[i].contains("W")) {
                            list.add(H[i].charAt(2) + "");
                            //  System.out.println("Added element to list");
                        }
                    }
                }
            }
            //System.out.println(Arrays.toString(list.toArray()));
            for (int i = 0; i < CALocation; i++) {
                if (H[i].contains(Find) || H[i].length() == 2) {
                    continue;
                }

                String temp = H[i].charAt(2) + "";
                if (list.contains(temp)) {
                    ACA = false;
                    j = 10;
                    break;
                }

            }

        }
        return ACA;
    }

    public static boolean isACA(String H[]) {
        /*If in the given schedule, each transaction Tj doesnot read
        any data item ‘x’ until the last transaction Ti
        that has written ‘X’ is committed or aborted then it is strict. */
 /* very similar to Strict*/
        boolean Strict = true;
        //get the data items written by T1 and commit or abort position
        for (int j = 1; j <= 4; j++) {
            String Find = j + "";
            int CALocation = 0;
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < H.length; i++) {
                if (H[i].contains(Find)) {

                    if (H[i].contains("A") || H[i].contains("C")) {
                        CALocation = i;
                        break;
                    } else {
                        if (H[i].contains("W")) {
                            list.add(H[i].charAt(2) + "");
                            //  System.out.println("Added element to list");
                        }
                    }
                }
            }
            //System.out.println(Arrays.toString(list.toArray()));
            for (int i = 0; i < CALocation; i++) {
                if (H[i].contains(Find) || H[i].length() == 2) {
                    continue;
                }

                String temp = H[i].charAt(2) + "";

                if (list.contains(temp)) {
                    if (H[i].contains("R")) {
                        Strict = false;
                        j = 10;
                        break;
                    }
                }

            }

        }
        return Strict;
    }

    public static void main(String[] args) {
        // TODO code application logic here

//       String[] H = {"W1a", "W1b", "R2c", "W1d", "C2", "W2a", "R2b", "W2b", "C1"};
// for not ACA AND STRICT
//          String[] H ={"W2a", "W1b", "W1a", "R2b", "C1", "C2"};
         //String[] H = {"W1a", "W1b", "R2c", "C1", "C2"};
//         FOR RANDOM GENERATOR 
         String[] H = GenerateHistory();
        PrintArray(H);
        if (isACA(H)) {
            System.out.println("History is ACA");
        } else {
            System.out.println("Given History is not ACA");
        }
        if (isStrict(H)) {
            System.out.println("History is Strict");
        } else {
            System.out.println("Given History is not Strict");
        }
    }

}
