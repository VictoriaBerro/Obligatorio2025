public class Miembro {
    private String credit_id;
    private String department;
    private int gender;
    private int id;
    private String job;
    private String name;
    private String profile_path;

    public Miembro(String credit_id, String department, int gender, int id, String job, String name, String profile_path) {
        this.credit_id = credit_id;
        this.department = department;
        this.gender = gender;
        this.id = id;
        this.job = job;
        this.name = name;
        this.profile_path = profile_path;
    }

    @Override
    public String toString() {
        return job + " - " + name + " (" + department + ")";
    }
}
