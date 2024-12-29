package HollowKnight.model.game.elements.Collectables;

import HollowKnight.model.game.elements.Knight.Knight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HealthOrbTest {
    private HealthOrb healthOrb;
    private Knight knight;
    @BeforeEach
    void setUp() {
        this.knight = new Knight(0,0,10,1,50);
        this.healthOrb = new HealthOrb(0,0,10, 'h');
    }
    @Test
    void setHealthOrb(){
        int new_health = 20;
        healthOrb.setHealth(new_health);
        Assertions.assertEquals(new_health, healthOrb.getHealth());
    }

    @Test
    void benefit() {
        healthOrb.benefit(knight);
        Assertions.assertEquals(knight.getHP(), 20);
    }

    @Test
    void testGetChar() {
        Assertions.assertEquals(healthOrb.getChar(), 'h');
    }
}