import java.util.*;

class Player {

    LinkedList<Pile> piles;


    public Player(LinkedList<Pile> piles) {
        this.piles = piles;
    }


}

class Pile {
    int[] numbers;
    LinkedList<Pile> nextPiles;
    Pile previous;
    boolean canContinue = true;

    public Pile(int[] numbers) {
        this.numbers = numbers;
        this.nextPiles = new LinkedList<>();
        this.previous = null;
    }

    public Pile(int[] numbers, Pile previous) {

        this.numbers = numbers;
        if (numbers.length == 0) canContinue = false;
        this.previous = previous;
        this.nextPiles = new LinkedList<>();
    }

    @Override
    public String toString() {
        return Arrays.toString(numbers) +
                "->" + nextPiles +

                '}';
    }
}

class methods {

    // part of the finding possible number arrays and make them to usable for the piles
    static int[] findAltNumbers(int number, int counter) {
        return new int[]{number - counter, counter};
    }

    static int[] mergeArrays(int[] new_array, int[] temp_array) {

        int[] mergedArray = new int[new_array.length + temp_array.length];

        for (int i = 0; i < mergedArray.length; i++) {
            if (i == 0 || i == 1) {
                mergedArray[i] = new_array[i];

            } else mergedArray[i] = temp_array[i - 2];

        }


        return mergedArray;
    }

    static LinkedList<int[]> possibleArrays(int number) {
        LinkedList<int[]> possibleArrays = new LinkedList<>();
        for (int i = 1; i < (double) number / 2; i++) {

            possibleArrays.addLast(findAltNumbers(number, i));

        }
        return possibleArrays;
    }

    static int[] copyArraytoAlt(int[] number, int index) {
        int[] temp_Alt = new int[number.length - 1];
        for (int i = 0, j = 0; i < number.length; i++) {
            if (i != index) {
                temp_Alt[j] = number[i];
                j++;
            }
        }
        return temp_Alt;
    }

    static LinkedList<int[]> makeNewArrays(int[] numbers, int index) {
        int[] temp = copyArraytoAlt(numbers, index);//6,5 6 5temp 5
        LinkedList<int[]> numList = possibleArrays(numbers[index]);
        for (int i = 0; i < numList.size(); i++) {
            numList.set(i, mergeArrays(numList.get(i), temp));
        }
        //to Print every Possible item
       /* numList.forEach((a) -> {
            for (int c : a
            ) {
                System.out.print(c);
            }
            System.out.println();
        });*/

        return numList;
    }
    // merges arrays to the required new Array by piles
    static LinkedList<int[]> mergeLinkedLists(int[] numbers) {
        LinkedList<LinkedList<int[]>> linkedList = new LinkedList<>();
        for (int i = 0; i < numbers.length; i++) {

            linkedList.addLast(makeNewArrays(numbers, i));
        }
        LinkedList<int[]> mergedLinkedList = new LinkedList<>();

        for (LinkedList<int[]> a : linkedList
        ) {
            mergedLinkedList.addAll(a);

        }
        return mergedLinkedList;
    }

    //Using created arrays to create new piles
    static void CreateNewPiles(Pile pile) {
        LinkedList<int[]> linkedList = mergeLinkedLists(pile.numbers);
        for (int[] a : linkedList
        ) {
            pile.nextPiles.add(new Pile(a, pile));
        }

    }

    // clone players playing every round according to their piles at the round
    static LinkedList<Pile> Game(Player player, LinkedList<Pile> piles) {
        Player copyPlayer;

        for (Pile a : player.piles
        ) {
            methods.CreateNewPiles(a);
        }

        for (int i = 0; i < player.piles.size(); i++) {

            copyPlayer = new Player(player.piles.get(i).nextPiles);
            if (player.piles.get(i).nextPiles.size() == 0) {

                piles.add(player.piles.get(i));
                //System.out.println(Arrays.toString(player.piles.get(i).numbers));

            }

            Game(copyPlayer, piles);
        }

        return piles;
    }
    //to explore path from end to start
    static void resolvePaths(Pile pile) {
        int counter = 0;

        while (pile.previous != null) {
            counter++;
            System.out.print(Arrays.toString(pile.numbers) + ",");
            pile = pile.previous;
        }
        counter++;

        System.out.println(Arrays.toString(pile.numbers));
        System.out.println(counter % 2 == 0 ? "B loses" : "A loses");
    }


    static LinkedList<int[]> savePaths(Pile pile) {
        LinkedList<int[]> path=new LinkedList<>();
        while (pile.previous != null) {

            path.addFirst(pile.numbers);

            pile = pile.previous;
        }

        path.addFirst(pile.numbers);

        return path;
    }

}

public class Tree {
    public static void main(String[] args) {

        int[] numbers = {7};// dont try it with over 15 because of the overload of computer
        // you can start with any number group such as {7,7} or {6,6,6,6,6,6,6} but dont try 6 one
        Pile pile = new Pile(numbers);
        Player player = new Player(pile.nextPiles);
        methods.CreateNewPiles(pile);
        LinkedList<Pile> paths = new LinkedList<>();
        methods.Game(player, paths);
        LinkedList<int[]> b;

        LinkedList<LinkedList<int[]>> PathA=new LinkedList();
        LinkedList<LinkedList<int[]>> PathB=new LinkedList();

        //Printing part of the paths
        for (Pile path : paths) {
           b= methods.savePaths(path);
           if(path.numbers.length%2==0){
               PathA.add(b);
           }
           else{
               PathB.add(b);
           }

        }
        System.out.println("B loses:");
        for (LinkedList<int[]> d:PathA
             ) {
            for (int[] p:d
                 ) {
                System.out.print(Arrays.toString(p));
            }
            System.out.println();
        }
        System.out.println("A loses:");
        for (LinkedList<int[]> d:PathB
        ) {
            for (int[] p:d
            ) {
                System.out.print(Arrays.toString(p));
            }
            System.out.println();
        }

    }
}

