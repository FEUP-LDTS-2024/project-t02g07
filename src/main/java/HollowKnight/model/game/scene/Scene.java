package HollowKnight.model.game.scene;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.game.elements.Collectables.Collectables;
import HollowKnight.model.game.elements.Element;
import HollowKnight.model.game.elements.Knight.Knight;
import HollowKnight.model.game.elements.Particle.Particle;
import HollowKnight.model.game.elements.Spike;
import HollowKnight.model.game.elements.Tree;
import HollowKnight.model.game.elements.enemies.Enemies;
import HollowKnight.model.game.elements.rocks.Rock;
import HollowKnight.model.game.elements.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final int width;
    private final int height;
    private final int sceneID;

    private Element[][] map;
    private Tile[][] tiles;
    private Spike[][] spikes;
    private Tree[][] trees;

    private Collectables[][] orbs;

    private Rock[][] rocks;

    private List<Enemies> monsters;

    private double gravity = 0.25;

    private Knight player;
    private List<Particle> particles;
    private List<Particle> doubleJumpParticles;
    private List<Particle> jumpParticles;
    private List<Particle> respawnParticles;
    private List<Particle> dashParticles;
    private Position EndPosition;
    private Position startPosition;

    public Scene(int width, int height, int sceneID) {
        this.width = width;
        this.height = height;
        this.sceneID = sceneID;

        this.doubleJumpParticles = new ArrayList<>();
        this.jumpParticles = new ArrayList<>();
        this.particles = new ArrayList<>();
        this.respawnParticles = new ArrayList<>();
        this.dashParticles = new ArrayList<>();

        this.map = new Element[height][width];
        this.tiles = new Tile[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Knight getPlayer() {
        return player;
    }

    public void setPlayer(Knight player) {
        this.player = player;
    }

    public int getSceneID() {
        return sceneID;
    }

    public void setMap(Element[][] map) {this.map = map;}

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Spike[][] getSpikes() {
        return spikes;
    }

    public void setSpikes(Spike[][] spikes) {
        this.spikes = spikes;
    }

    public void setTrees(Tree[][] trees) {
        this.trees = trees;
    }

    public Tree[][] getTrees() {
        return trees;
    }

    public void setRocks(Rock[][] rocks) {this.rocks = rocks;}
    public Rock[][] getRocks() {return rocks;}

    public Collectables[][] getOrbs() {
        return orbs;
    }

    public void setOrbs(Collectables[][] orbs) {
        this.orbs = orbs;
    }

    public List<Enemies> getMonsters() {return monsters;}

    public void setMonsters(List<Enemies> monsters) {this.monsters = monsters;}

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public double getGravity() {
        return gravity;
    }

    public void setEndPosition(Position EndPosition) {
        this.EndPosition = EndPosition;
    }

    private boolean isOutSideScene(double x1, double x2, double y1, double y2) {
        return x1 < 0 || x2 >= this.width || y1 < 0 || y2 >= this.height;
    }

    private boolean checkCollision(double x1, double x2, double y1, double y2, Element[][] layer) {
        if (isOutSideScene(x1, x2, y1, y2))
            return true;

        for (int tileY: List.of(((int)y1 / Tile.SIZE), ((int)y2 / Tile.SIZE))) {
            for (int tileX: List.of((int)x1 / Tile.SIZE, (int)x2 / Tile.SIZE)) {
                if (layer[tileY][tileX] != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collidesLeft(Position position, Position size) {
        double x = position.x(), y = position.y();
        return checkCollision(x, x + 1, y, y + size.y() - 1, map);
    }

    public boolean collidesRight(Position position, Position size) {
        double x = position.x(), y = position.y();
        return checkCollision(x + size.x() - 1, x + size.x() - 1, y, y + size.y() - 1, map);
    }

    public boolean collidesUp(Position position, Position size) {
        double x = position.x(), y = position.y();
        return checkCollision(x, x + size.x() - 1, y, y + 1, map);
    }

    public boolean collidesDown(Position position, Position size) {
        double x = position.x(), y = position.y();
        return checkCollision(x, x + size.x() - 1, y + size.y() - 2, y + size.y() - 1, map);
    }

    public List<Particle> getDoubleJumpParticles() {
        return doubleJumpParticles;
    }

    public void setDoubleJumpParticles(List<Particle> jumpParticles) {
        this.doubleJumpParticles = jumpParticles;
    }

    public List<Particle> getJumpParticles() {
        return jumpParticles;
    }

    public void setJumpParticles(List<Particle> jumpParticles) {
        this.jumpParticles = jumpParticles;
    }

    public List<Particle> getDashParticles() {
        return dashParticles;
    }

    public void setDashParticles(List<Particle> dashParticles) {
        this.dashParticles = dashParticles;
    }

    public void collectOrbs(Collectables[][] orbs){
        double x = getPlayer().getPosition().x();
        double y = getPlayer().getPosition().y();
        double width = player.getWidth(), height = player.getHeight();

        for (int tileY: List.of((int)y / Tile.SIZE, (int)(y + height - 1) / Tile.SIZE)) {
            for (int tileX: List.of((int)x / Tile.SIZE, (int)(x + width - 1) / Tile.SIZE)) {
                if (orbs[tileY][tileX] != null) {
                    orbs[tileY][tileX].benefit(getPlayer());
                    orbs[tileY][tileX] = null;
                    getPlayer().addOrbs();
                }
            }
        }
    }

    public void collideMonsters(List<Enemies> enemies) {
        // Loop through the list of enemies
        for (Enemies enemy : enemies) {
            if (checkCollision(getPlayer(), enemy)) {
                getPlayer().PlayerHit(enemy.getDamage());
            }
        }
    }

    public boolean collideSpike() {
        final int spikeHeightDiff = Tile.SIZE - Spike.SPIKE_HEIGHT;
        double x = player.getPosition().x(), y = player.getPosition().y();
        return checkCollision(x, x + player.getWidth() - 1, y, y + player.getHeight() - 1 - spikeHeightDiff, spikes);
    }

    /**
     * Helper method to check if the player collides with an enemy.
     */
    boolean checkCollision(Knight player, Enemies enemy) {
        double playerX = player.getPosition().x();
        double playerY = player.getPosition().y();
        double playerWidth = player.getWidth();
        double playerHeight = player.getHeight();

        double enemyX = enemy.getPosition().x();
        double enemyY = enemy.getPosition().y();
        double enemyWidth = enemy.getSize().x(); // Assuming Enemies has a getSize() method
        double enemyHeight = enemy.getSize().y();

        // Check for collision (simple AABB collision)
        return playerX < enemyX + enemyWidth &&
                playerX + playerWidth > enemyX &&
                playerY < enemyY + enemyHeight &&
                playerY + playerHeight > enemyY;
    }


    public boolean isAtEndPosition() {
        double x1 = player.getPosition().x();
        return x1 >= EndPosition.x();
    }

    public List<Particle> getRespawnParticles() {
        return respawnParticles;
    }

    public void setRespawnParticles(List<Particle> respawnParticles) {
        this.respawnParticles = respawnParticles;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

}
