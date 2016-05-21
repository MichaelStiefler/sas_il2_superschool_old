package com.maddox.il2.objects.weapons;

public class BombTIOrange extends BombTiBase2 {

    protected Bomb spawnNewBomb() {
        return new BombTiOn();
    }
    
    static {
        initStatic(BombTIOrange.class);
    }
}
