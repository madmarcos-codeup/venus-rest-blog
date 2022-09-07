package docrob.venusrestblog;

public class FakeDogFactGateway {

    public static String fetchDogFactById(long id) {
        // TODO: fetch the particular dog fact with matching id from database
        switch((int) id) {
            case 1:
                return "Dogs don’t feel Guilty";
            case 2:
                return "Their sense of Smell is really Powerful";
            default:
                return "Unknown fact id!";
        }
    }
}
