package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitKI_49_II_NGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("Turret1A", 0.0F, -f, 0.0F);
        mesh.chunkSetAngles("Turret1B", 0.0F, f1+2.1F, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!isRealMode())
            return;
        if (!aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -35F)
            f = -35F;
        if (f > 35F)
            f = 35F;
        if (f1 > 25F)
            f1 = 25F;
        if (f1 < -45F)
            f1 = -45F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!isRealMode())
            return;
        if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        if (bGunFire) {
            if (iCocking > 0)
                iCocking = 0;
            else
                iCocking = 1;
        } else {
            iCocking = 0;
        }
        resetYPRmodifier();
        xyz[1] = -0.07F * (float) iCocking;
        ypr[1] = 0.0F;
        mesh.chunkSetLocate("Turret1C", xyz, ypr);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!isFocused()) {
            return;
        } else {
            aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    public void doGunFire(boolean flag) {
        if (!isRealMode())
            return;
        if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        else
            bGunFire = flag;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
    }

    public CockpitKI_49_II_NGunner() {
        super("3DO/Cockpit/Ki49-NGun/hier.him", "he111");
        iCocking = 0;
    }

    public void reflectCockpitState() {
    }

    private int iCocking;

    static {
        Class class1 = CockpitKI_49_II_NGunner.class;
        Property.set(class1, "aiTuretNum", 0);
        Property.set(class1, "weaponControlNum", 10);
        Property.set(class1, "astatePilotIndx", 2);
    }
}
