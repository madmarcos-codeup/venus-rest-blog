package docrob.venusrestblog.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DogTest {
    private static List<Dog> dogs;

    public static void main(String[] args) {
        testWithDogWithoutOptional();
        testWithoutDogWithoutOptional();
        testWithDogWithOptional();
        testWithoutDogWithOptional();
    }

    private static void testWithDogWithoutOptional() {
        dogs = new ArrayList<>();
        // maybe add a starting dog
        dogs.add(new Dog("Spot"));

        Dog aDog = fetchDog();
        System.out.println(aDog.getName());
    }

    private static void testWithDogWithOptional() {
        dogs = new ArrayList<>();
        // maybe add a starting dog
        dogs.add(new Dog("Spot"));

        Optional<Dog> aDog = fetchDogOptional();
        System.out.println(aDog.get().getName());
    }

    private static void testWithoutDogWithoutOptional() {
        dogs = new ArrayList<>();
        // maybe add a starting dog
//        dogs.add(new Dog("Spot"));

        Dog aDog = fetchDog();
        // have to manually add null check to avoid an error
        if(aDog == null) {
            System.out.println("no dog found");
        } else {
            System.out.println(aDog.getName());
        }
    }
    private static void testWithoutDogWithOptional() {
        dogs = new ArrayList<>();
        // maybe add a starting dog
//        dogs.add(new Dog("Spot"));

        Optional<Dog> aDog = fetchDogOptional();
//        if(aDog.isEmpty()) {
//            System.out.println("no dog found");
//        } else {
            System.out.println(aDog.get().getName());
//        }
    }

    public static Dog fetchDog() {
        if(dogs.size() == 0)
            return null;
        return dogs.get(0);
    }

    public static Optional<Dog> fetchDogOptional() {
        return Optional.ofNullable(fetchDog());
    }
}
