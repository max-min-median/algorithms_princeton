public class MSCP {
    
    public String name;
    private String location;
    private int numStorey;

    public MSCP(String name, String location, int numStorey) {
        this.name = name;
        this.location = location;
        this.numStorey = numStorey;
    }

    public String getName() { return this.name; }
    public void setName(String newname) { this.name = newname; }


    // unit testing (not graded)
    public static void main(String[] args) {
        // MSCP carpark = new MSCP("Block 123 MSCP", "Boon Lay St 987", 5);
        // System.out.println(carpark.name);
    }

}
