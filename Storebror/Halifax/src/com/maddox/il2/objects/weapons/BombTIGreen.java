package com.maddox.il2.objects.weapons;

public class BombTIGreen extends BombTiBase2 {

    protected Bomb spawnNewBomb() {
        return new BombTiGr();
    }
    
    static {
        initStatic(BombTIGreen.class);
    }
}
