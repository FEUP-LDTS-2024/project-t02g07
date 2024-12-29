package HollowKnight.model.game.elements.Collectables;

import HollowKnight.model.dataStructs.Vector;
import HollowKnight.model.game.elements.Knight.Knight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpeedOrbTest {
    private SpeedOrb speedOrb;
    private Knight knight;
    @BeforeEach
    void setUp() {
        this.knight = new Knight(0,0,10,1,50);
        knight.setMaxVelocity(new Vector(1,1));
        this.speedOrb = new SpeedOrb(0,0,1.1, 's');
    }

    @Test
    void setSpeed_boost() {
        double newspeed = 1.4;
        speedOrb.setSpeed_boost(newspeed);
        Assertions.assertEquals(newspeed, speedOrb.getSpeed_boost());
    }

    @Test
    void benefit() {
        Vector expected = new Vector(knight.getMaxVelocity().x()*speedOrb.getSpeed_boost(),
                        knight.getMaxVelocity().y());
        speedOrb.benefit(knight);
        Assertions.assertEquals(expected, knight.getMaxVelocity());
    }

    @Test
    void testGetChar() {
        Assertions.assertEquals(speedOrb.getChar(), 's');
    }
}