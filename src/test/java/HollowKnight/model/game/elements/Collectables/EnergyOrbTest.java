package HollowKnight.model.game.elements.Collectables;

import HollowKnight.model.game.elements.Knight.Knight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnergyOrbTest {
    private EnergyOrb energyorb;
    private Knight knight;
    @BeforeEach
    void setUp() {
        this.knight = new Knight(0,0,10,1,50);
        this.energyorb = new EnergyOrb(0,0,5, 'e');
    }

    @Test
    void setEnergyorb(){
        int new_energy = 10;
        energyorb.setEnergy(new_energy);
        Assertions.assertEquals(energyorb.getEnergy(),new_energy);
    }

    @Test
    void benefit() {
        energyorb.benefit(knight);
        Assertions.assertEquals(knight.getEnergy(), 55);
    }

    @Test
    void testGetChar() {
        Assertions.assertEquals(energyorb.getChar(), 'e');
    }
}