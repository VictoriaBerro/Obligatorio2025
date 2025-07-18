package entities;

public class Actor {
    private int cast_id;
    private String character;
    private String credit_id;
    private int gender;
    private String id;
    private String name;
    private int order;
    private String profile_path;
    private int[] evaluacionesMensuales;

    public Actor( int gender, String id, String name) {
        this.evaluacionesMensuales = evaluacionesMensuales;
        this.cast_id = cast_id;
        this.character = character;
        this.credit_id = credit_id;
        this.gender = gender;
        this.id = id;
        this.name = name;
        this.order = order;
        this.profile_path = profile_path;
    }


    public int getCast_id() {
        return cast_id;
    }

    public void setCast_id(int cast_id) {
        this.cast_id = cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    @Override
    public String toString() {
        return name + " como " + character;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int[] getEvaluacionesMensuales() {
        return evaluacionesMensuales;
    }

    public void setEvaluacionesMensuales(int[] evaluacionesMensuales) {
        this.evaluacionesMensuales = evaluacionesMensuales;
    }
}