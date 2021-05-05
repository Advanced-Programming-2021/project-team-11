package model;

public class Player {
    private final User user;
    private int health = 8000;

    public Player(User user) {
        this.user = user;
    }

    public int getHealth() {
        return health;
    }

    public void increaseHealth(int delta) {
        this.health += delta;
    }

    public void decreaseHealth(int delta) {
        this.health -= delta;
    }

    public User getUser() {
        return user;
    }
}
