package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetGunner;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunAircraftGeneric

public class MGunYakB extends MGunAircraftGeneric
{

    public MGunYakB()
    {
    }
    
    public void doStartBullet(double paramDouble) {   
    	super.doStartBullet(paramDouble);
    	Actor localActor = getOwner();
    	localActor.getSpeed(v);
    	v1.add(v);
    }
    
    private static Vector3d v = new Vector3d();
    private static Vector3d v1 = new Vector3d();

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = false;
        gunproperties.fireMesh = "3DO/Effects/GunFire/7mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/7mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.sound = "weapon.MiniGun";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 5F;
        gunproperties.aimMaxDist = 3000F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.1F;
        gunproperties.shotFreq = 40F;
        gunproperties.traceFreq = 1;
        gunproperties.bullets = 1470;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.045F;
        gunproperties.bullet[0].kalibr = 0.0001209675F;
        gunproperties.bullet[0].speed = 810F;
        gunproperties.bullet[0].power = 0.0208F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/7mmGreen/mono.sim";
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0xd200ff00;
        gunproperties.bullet[0].timeLife = 3F;
        gunproperties.bullet[1].massa = 0.045F;
        gunproperties.bullet[1].kalibr = 0.0001209675F;
        gunproperties.bullet[1].speed = 810F;
        gunproperties.bullet[1].power = 0.01F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.05F;
        gunproperties.bullet[1].traceMesh = "3do/effects/tracers/7mmGreen/mono.sim";
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0xd200ff00;
        gunproperties.bullet[1].timeLife = 3F;
        return gunproperties;
    }
}