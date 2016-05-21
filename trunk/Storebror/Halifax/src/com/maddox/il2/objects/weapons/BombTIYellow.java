package com.maddox.il2.objects.weapons;

public class BombTIYellow extends BombTiBase2 {

    protected Bomb spawnNewBomb() {
        return new BombTiYw();
    }
    
    static {
        initStatic(BombTIYellow.class);
    }
}
