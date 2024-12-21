package HollowKnight.model.game.elements.enemies;

import HollowKnight.model.Position;
import HollowKnight.model.Vector;
import HollowKnight.model.game.elements.Element;
import HollowKnight.model.game.scene.Scene;

public abstract class Enemies extends Element {
    private int HP;
    private Scene scene;
    private int damage;
    private Vector velocity;

    private Position size;
    public Enemies(int x, int y,int HP, Scene scene, int damage, Position size) {
        super(x,y);
        this.HP = HP;
        this.scene = scene;
        this.damage = damage;
        this.velocity = new Vector(1.5,1.5);
        this.size = size;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public boolean isAlive(){
        return this.HP >0;
    }

    public int getHP() {
        return HP;
    }

    public Scene getScene() {
        return scene;
    }

    public int getDamage() {
        return damage;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public abstract Position updatePosition();

    public void setHP(int HP) {
        this.HP = HP;
    }

    protected abstract Vector applyCollisions(Vector velocity);

    public abstract Position moveMonster();
    public Position getSize() {
        return size;
    }
}
