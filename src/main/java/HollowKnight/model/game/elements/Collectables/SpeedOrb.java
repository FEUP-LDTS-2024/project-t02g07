package HollowKnight.model.game.elements.Collectables;

import HollowKnight.model.game.elements.Knight.Knight;

public class SpeedOrb extends Collectables{
    private double speed_boost;
    public SpeedOrb(int x, int y, double boost){
        super(x,y,"Health");
        this.speed_boost = boost;
    }

    @Override
    public void benefit(Knight knight){
        knight.setVelocity(knight.getVelocity()*this.speed_boost);
    }
}