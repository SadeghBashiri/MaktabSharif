package ir.maktab.onlineexam.testi;

import java.util.*;

public class Main {
    public static void main(String[] args) {
//        Animal a = new Horse();
//        System.out.println(a);
//        test(a);
//        a=?
//        System.out.println(test(a));
//        List<String> list = new ArrayList<>(Arrays.asList("salam", "1"));
//        System.out.println(contains("salam", list));

        HashSet set = new HashSet();
        TreeSet treeSet = new TreeSet();
        Animal animal1 = new Animal();
        Animal animal2 = new Animal();
        animal1.setName("a1");
        animal2.setName("a1");
        set.add(animal1);
        set.add(animal2);
        treeSet.add(animal1);
        treeSet.add(animal2);

        System.out.println(set.size());
        System.out.println(treeSet.size());
    }

    static boolean test(Animal animal) {
        animal = null;
        return false;
    }

    public static Boolean contains(String toFind, List<String> strings) {
        Boolean temp = false;
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).equals(toFind)) {
                temp = true;
            } else {
                temp = false;
            }
        }
        return temp;
    }
}
