package HollowKnight.model.game.scene;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.game.elements.Collectables.Collectables;
import HollowKnight.model.game.elements.Collectables.SpeedOrb;
import HollowKnight.model.game.elements.Knight.Knight;
import HollowKnight.model.game.elements.Particle.DoubleJumpParticle;
import HollowKnight.model.game.elements.Particle.JumpParticle;
import HollowKnight.model.game.elements.enemies.Enemies;
import HollowKnight.model.game.elements.enemies.GhostMonster;
import HollowKnight.model.game.elements.enemies.PurpleMonster;
import HollowKnight.model.game.elements.enemies.SwordMonster;
import HollowKnight.model.game.elements.tile.Tile;
import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.Key;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class SceneTest {
    private Scene scene;
    private SceneLoader sceneLoader;
    private Knight player;
    private Position playerSize;

    @BeforeEach
    public void setup() throws IOException {
        this.scene = new Scene(20, 16, 0);
        this.sceneLoader = new SceneLoader(0);
        this.player = new Knight(0,0, 60,0,0);
        this.scene.setPlayer(player);
        this.playerSize = new Position(player.getWidth(), player.getHeight());
    }

    @Test
    public void CheckGravity() {
        assertEquals(0.25, scene.getGravity());
    }

    @Nested
    class SceneTestCollisions {
        @BeforeEach
        public void setup() {
            scene = new Scene(5, 5, 0);
            Tile[][] tileSet = {{null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, new Tile(16,16, 'G'), null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null}};
            scene.setTiles(tileSet);
            player = new Knight(0,0, 50,0,0);
            scene.setPlayer(player);
        }

        @Test
        public void checkCollisionLeft() {
            Position playerPosition1 = new Position(31, 20);
            Position playerPosition2 = new Position(24, 20);
            Position playerPosition3 = new Position(23, 20);
            Position playerPosition4 = new Position(19, 20);

            assertTrue(scene.collidesLeft(playerPosition1, playerSize));
            assertTrue(scene.collidesLeft(playerPosition2, playerSize));
            assertTrue(scene.collidesLeft(playerPosition3, playerSize));
            assertTrue(scene.collidesLeft(playerPosition4, playerSize));
        }

        @Test
        public void checkCollisionRight() {
            Position playerPosition1 = new Position(1, 12);
            Position playerPosition2 = new Position(10, 12);
            Position playerPosition3 = new Position(11, 12);
            Position playerPosition4 = new Position(15, 12);

            assertTrue(scene.collidesRight(playerPosition1, playerSize));
            assertTrue(scene.collidesRight(playerPosition2, playerSize));
            assertTrue(scene.collidesRight(playerPosition3, playerSize));
            assertTrue(scene.collidesRight(playerPosition4, playerSize));
        }

        @Test
        public void checkCollisionUp() {
            Position playerPosition1 = new Position(20, 31);
            Position playerPosition2 = new Position(20, 24);
            Position playerPosition3 = new Position(20, 23);
            Position playerPosition4 = new Position(20, 18);

            assertTrue(scene.collidesUp(playerPosition1, playerSize));
            assertTrue(scene.collidesUp(playerPosition2, playerSize));
            assertTrue(scene.collidesUp(playerPosition3, playerSize));
            assertTrue(scene.collidesUp(playerPosition4, playerSize));
        }

        @Test
        public void checkCollisionDown() {
            Position playerPosition1 = new Position(12, 1);
            Position playerPosition2 = new Position(12, 8);
            Position playerPosition3 = new Position(12, 9);
            Position playerPosition4 = new Position(12, 14);

            assertTrue(scene.collidesDown(playerPosition1, playerSize));
            assertTrue(scene.collidesDown(playerPosition2, playerSize));
            assertTrue(scene.collidesDown(playerPosition3, playerSize));
            assertTrue(scene.collidesDown(playerPosition4, playerSize));
        }
    }

    @Nested
    class SceneTestOrbs{
        @BeforeEach
        public void setup() {
            scene = new Scene(5, 5, 0);
            Collectables[][] orbs = {{null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, new SpeedOrb(8,8,1.1,'x'), null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null}};
            scene.setOrbs(orbs);
            player = new Knight(0,0, 50,0,0);
            scene.setPlayer(player);
        }

        @Test
        public void collectOrb(){
            player.setPosition(new Position(31,16));
            scene.collectOrbs(scene.getOrbs());

            assertNotNull(scene.getOrbs()[2][2]);
        }
    }

    @Test
    void testCheckCollisionBetweenPlayerAndEnemy() {
        Knight player = new Knight(5, 5, 30, 0, 0);
        Enemies enemy1 = new SwordMonster(4,4,10, scene, 10, new Position(8, 8), 'E'); // Overlapping
        Enemies enemy2 = new GhostMonster(50, 50, 1, scene, 2, new Position(2, 2), 'm'); // No collision

        // Player colliding with enemy1
        assertTrue(scene.checkCollision(player, enemy1));

        // Player not colliding with enemy2
        assertFalse(scene.checkCollision(player, enemy2));
    }

    @Test
    void testIsAtEndPosition() {
        scene.setEndPosition(new Position(100, 0)); // Set the end position to (100, 0)

        Knight player = new Knight(0, 0, 30, 0, 0);
        scene.setPlayer(player);

        // Player not at end position
        player.setPosition(new Position(99, 0));
        assertFalse(scene.isAtEndPosition());

        // Player exactly at end position
        player.setPosition(new Position(100, 0));
        assertTrue(scene.isAtEndPosition());

        // Player past the end position
        player.setPosition(new Position(101, 0));
        assertTrue(scene.isAtEndPosition());
    }

    @Test
    void testCollectOrbsWithNoOrbsToCollect() {
        Collectables[][] orbs = new Collectables[5][5]; // No orbs
        scene.setOrbs(orbs);

        Knight player = new Knight(0, 0, 30, 0, 0);
        scene.setPlayer(player);

        scene.collectOrbs(orbs); // Nothing should happen
        for (Collectables[] row : orbs) {
            for (Collectables orb : row) {
                assertNull(orb);
            }
        }
    }

    @Test
    void testCollectOrbsWithOrbsToCollect() {
        Collectables[][] orbs = new Collectables[5][5];
        orbs[1][1] = new SpeedOrb(0, 0, 1.1, 's');
        scene.setOrbs(orbs);

        Knight player = new Knight(0, 0, 30, 0, 0);
        scene.setPlayer(player);

        scene.collectOrbs(orbs);

        // Assert the orb is collected
        assertEquals(1, player.getOrbs());
    }

    @Test
    void testCollideMonstersNoCollision() {
        Knight player = new Knight(0, 0, 100, 0, 0);
        scene.setPlayer(player);

        List<Enemies> enemies = List.of(
                new SwordMonster(14,4,10, scene, 10, new Position(8, 8), 'E'),
                new SwordMonster(10,10,10, scene, 10, new Position(8, 8), 'E')
        );

        scene.collideMonsters(enemies);

        // Assert player not hit
        assertEquals(100, player.getHP()); // Assuming 100 is initial health
    }

    @Test
    void testCollideMonstersWithCollision() {
        Knight player = new Knight(0, 0, 30, 0, 0);
        scene.setPlayer(player);

        SwordMonster enemy = new SwordMonster(1,1,10, scene, 10, new Position(8, 8), 'E');

        player.setPosition(new Position(15, 15)); // Overlapping with enemy

        List<Enemies> enemies = List.of(enemy);
        scene.collideMonsters(enemies);

        // Assert player was hit
        assertEquals(30, player.getHP());
    }

    @Test
    void testSceneSize() {
        assertEquals(sceneLoader.getHeight(), 15);
        assertEquals(sceneLoader.getWidth(), 30);
    }

    @Test
    void testSetParticles() {
        JumpParticle jumpParticle = new JumpParticle(0, 0,
                new Position(1, 2), new TextColor.RGB(25, 25,25));
        scene.setJumpParticles(List.of(jumpParticle));
        DoubleJumpParticle doubleJumpParticle = new DoubleJumpParticle(0, 0,
                new Position(1, 2), new TextColor.RGB(25, 25,25));
        scene.setDoubleJumpParticles(List.of(doubleJumpParticle));

        assertEquals(scene.getJumpParticles().size(), 1);
        assertEquals(scene.getDoubleJumpParticles().size(), 1);

    }
}