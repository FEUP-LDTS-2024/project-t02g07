package HollowKnight.model.game.elements.Collectables;

import HollowKnight.model.Vector;
import HollowKnight.model.game.elements.Knight.Knight;

public class SpeedOrb extends Collectables{
    private double speed_boost;
    public SpeedOrb(int x, int y, double boost){
        super(x,y,"Speed");
        this.speed_boost = boost;
    }

    @Override
    public void benefit(Knight knight){
        knight.setMaxVelocity(new Vector(knight.getMaxVelocity().x()*speed_boost,
                knight.getMaxVelocity().y()*speed_boost));
    }
}
