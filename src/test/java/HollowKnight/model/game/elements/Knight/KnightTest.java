package HollowKnight.model.game.elements.Knight;

import HollowKnight.model.dataStructs.Position;
import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.scene.Scene;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class KnightTest {
    private Knight knight;
    private KnightState knightState;
    private Scene scene;
    @BeforeEach
    void setup(){
        this.knightState = mock(KnightState.class);
        this.scene = mock(Scene.class);
        this.knight = new Knight(0,0,10, 1.5F,50);
        knight.setScene(scene);
        knight.setState(knightState);
        knight.setVelocity(new Vector(1, 1));
        knight.setFacingRight(false);
    }

    @Test
    void setHP() {
        int hp = 100;
        knight.setHP(hp);
        Assertions.assertEquals(knight.getHP(),hp);
    }

    @Test
    void setDamage() {
        float damage = 2.0F;
        knight.setDamage(damage);
        Assertions.assertEquals(knight.getDamage(), damage);
    }

    @Test
    void setEnergy() {
        int energy = 100;
        knight.setEnergy(energy);
        Assertions.assertEquals(knight.getEnergy(), energy);
    }

    @Test
    void setMaxVelocity() {
        Vector newVelocity = new Vector(5.0,5.0);
        knight.setMaxVelocity(newVelocity);
        Assertions.assertEquals(newVelocity, knight.getMaxVelocity());
    }


    @Test
    void setState() {

        knight.setState(new WalkingState(knight));
        Assertions.assertInstanceOf(WalkingState.class, knight.getState());
    }

    @Test
    void updateVelocity() {
        when(knightState.updateVelocity(any(Vector.class))).thenReturn(new Vector(3, 3));

        knight.updateVelocity();

        Mockito.verify(knightState).updateVelocity(any(Vector.class));

        assertEquals(new Vector(1, 1), knight.getVelocity());
    }

    @Test
    void updatePosition() {
        when(knightState.applyCollisions(any(Vector.class))).thenReturn(new Vector(1, 1));
        Position updatedPosition = knight.updatePosition();

        double X = knight.getPosition().x() + knight.getVelocity().x();
        double Y = knight.getPosition().y() + knight.getVelocity().y();

        assertEquals(new Position(X,Y), updatedPosition);
    }

    @Test
    void playerHit(){
        knight.setGotHit(false);
        knight.PlayerHit(5);

        Assertions.assertTrue(knight.isGotHit());
        Assertions.assertEquals(knight.getHP(), 5);
    }

    @Test
    void PlayerAlreadyHit(){
        knight.setGotHit(true);
        knight.PlayerHit(5);
        Assertions.assertTrue(knight.isGotHit());
        Assertions.assertEquals(knight.getHP(), 10);
    }


    @Test
    void resetValues() {
        knight.resetValues();

        assertTrue(knight.isFacingRight());

        assertInstanceOf(FallingState.class, knight.getState());
    }

    @Test
    void testAddOrbs() {
        knight.addOrbs();
        assertEquals(1, knight.getOrbs());
    }

}