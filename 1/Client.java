import MSCP;

public class Client {
    public static void main(String[] args) {
        MSCP carpark = new MSCP("Block 123 MSCP", "Boon Lay St 987", 5);
        System.out.println(carpark.getName());
        carpark.name = "Mama shop";
    }
}
