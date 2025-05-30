package entities;

public class Evaluacion { //LISTA
    private int userId;
    private int movieId;
    private double rating;
    private long timestamp;

    public Evaluacion(int userId, int movieId, double rating, long timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Evaluacion{" +
                "userId=" + userId +
                ", movieId=" + movieId +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}