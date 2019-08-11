package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB24D_TGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Turret2B_D0", aircraft().hierMesh().isChunkVisible("Turret2A_D0"));
        super.doFocusLeave();
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Tmain", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Cradle", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("CradleU", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("CradleRods", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("GunPull", 0.0F, orient.getTangage(), 0.0F);
        yaw = orient.getYaw();
        tan = orient.getTangage();
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        for(; f < -180F; f += 360F);
        for(; f > 180F; f -= 360F);
        for(; prevA0 < -180F; prevA0 += 360F);
        for(; prevA0 > 180F; prevA0 -= 360F);
        if(!isRealMode())
        {
            prevA0 = f;
            prevA1 = f1;
            return;
        }
        if(f < -yawVelMax && prevA0 > yawVelMax)
            f += 360F;
        else
        if(f > yawVelMax && prevA0 < -yawVelMax)
            prevA0 += 360F;
        float f3 = f - prevA0;
        float f4 = 0.001F * (float)(Time.current() - prevTime);
        float f5 = Math.abs(f3 / f4);
        if(f5 > yawVelMax)
            if(f > prevA0)
                f = prevA0 + yawVelMax * f4;
            else
            if(f < prevA0)
                f = prevA0 - yawVelMax * f4;
        if(f1 > 85F)
            f1 = 85F;
        if(f1 < -6.5F)
            f1 = -6.5F;
        f3 = f1 - prevA1;
        f4 = 0.001F * (float)(Time.current() - prevTime);
        f5 = Math.abs(f3 / f4);
        if(f5 > tanVelMax)
            if(f1 > prevA1)
                f1 = prevA1 + tanVelMax * f4;
            else
            if(f1 < prevA1)
                f1 = prevA1 - tanVelMax * f4;
        prevTime = Time.current();
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        prevA0 = f;
        prevA1 = f1;
    }

    protected void interpTick()
    {
        if(bNeedSetUp)
        {
            prevTime = Time.current() - 1L;
            bNeedSetUp = false;
            reflectPlaneMats();
        }
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        if(this.bGunFire)
        {
            ammoBeltAnim += 0.25F;
            if(ammoBeltAnim > 1.0F)
                ammoBeltAnim = 0.0F;
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN05");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN05");
            if(hook2 == null)
                hook2 = new HookNamed(aircraft(), "_MGUN06");
            doHitMasterAircraft(aircraft(), hook2, "_MGUN06");
        }
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        else
            this.bGunFire = flag;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            prevTime = Time.current() - 1L;
            bNeedSetUp = false;
            reflectPlaneMats();
        }
        float f1 = prevYaw - yaw;
        float f2 = prevTan - tan;
        float f3 = 0.001F * (float)(Time.current() - prevMoveTime);
        float f4 = Math.abs(f1 / f3);
        if(Float.isNaN(f4))
            f4 = 0.0F;
        float f5 = 1.0F;
        if(f1 < 0.0F)
            f5 = -1F;
        controlYaw = 0.9F * controlYaw + 0.1F * cvt(f4, 0.0F, yawVelMax, 0.0F, 30F * f5);
        this.mesh.chunkSetAngles("TContrl", 0.0F, controlYaw, 0.0F);
        f4 = Math.abs(f2 / f3);
        if(Float.isNaN(f4))
            f4 = 0.0F;
        f5 = 1.0F;
        if(f2 < 0.0F)
            f5 = -1F;
        controlTan = 0.9F * controlTan + 0.1F * cvt(f4, 0.0F, tanVelMax, 0.0F, 30F * f5);
        this.mesh.chunkSetAngles("Handles", 0.0F, controlTan, 0.0F);
        prevYaw = yaw;
        prevTan = tan;
        prevMoveTime = Time.current();
        B_24D140CO b_24d140co = (B_24D140CO)aircraft();
        resetYPRmodifier();
        if(this.bGunFire)
        {
            BulletEmitter abulletemitter[] = this.fm.CT.Weapons[weaponControlNum()];
            if(!b_24d140co.topLeftGunJammed)
            {
                this.mesh.chunkSetAngles("TBoostr2L", 0.0F, ammoBeltAnim * -40F, 0.0F);
                if(World.cur().diffCur.Limited_Ammo)
                {
                    int i = abulletemitter[0].countBullets();
                    for(int l = 1; l <= 11; l++)
                        this.mesh.chunkVisible("Ammo" + l + "L", i > 13 - l);

                }
            }
            if(!b_24d140co.topRightGunJammed)
            {
                this.mesh.chunkSetAngles("TBoostr2R", 0.0F, ammoBeltAnim * -40F, 0.0F);
                if(World.cur().diffCur.Limited_Ammo)
                {
                    int j = abulletemitter[1].countBullets();
                    for(int i1 = 1; i1 <= 11; i1++)
                        this.mesh.chunkVisible("Ammo" + i1 + "R", j > 13 - i1);

                }
            }
        }
        float f6 = tan;
        if(f6 < 0.0F)
            f6 = 0.0F;
        for(int k = 1; k <= 11; k++)
        {
            Aircraft.xyz[1] = f6 * (float)(11 - k) * -5E-005F;
            if(!b_24d140co.topRightGunJammed)
            {
                Aircraft.xyz[2] = beltAnimCoords[k - 1][1] * ammoBeltAnim * 0.05F;
                Aircraft.xyz[0] = beltAnimCoords[k - 1][0] * ammoBeltAnim * 0.05F;
                Aircraft.ypr[1] = beltAnimRot[k - 1] * -ammoBeltAnim;
            }
            this.mesh.chunkSetLocate("Ammo" + k + "R", Aircraft.xyz, Aircraft.ypr);
            if(!b_24d140co.topLeftGunJammed)
            {
                Aircraft.xyz[2] = beltAnimCoords[k - 1][1] * ammoBeltAnim * 0.05F;
                Aircraft.xyz[0] = beltAnimCoords[k - 1][0] * ammoBeltAnim * -0.05F;
                Aircraft.ypr[1] = beltAnimRot[k - 1] * ammoBeltAnim;
            }
            this.mesh.chunkSetLocate("Ammo" + k + "L", Aircraft.xyz, Aircraft.ypr);
        }

    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 0x80) != 0)
            this.mesh.chunkVisible("Glass_Dmg", true);
    }

    public CockpitB24D_TGunner()
    {
        super("3DO/Cockpit/B-24D-TGun/hier.him", "bf109");
        bNeedSetUp = true;
        yawVelMax = 43F;
        tanVelMax = 30F;
        yaw = 0.0F;
        tan = 0.0F;
        prevTan = 0.0F;
        prevYaw = 0.0F;
        prevMoveTime = -1L;
        controlYaw = 0.0F;
        controlTan = 0.0F;
        ammoBeltAnim = 0.0F;
        prevTime = -1L;
        prevA0 = 0.0F;
        prevA1 = 0.0F;
        hook1 = null;
        hook2 = null;
        this.cockpitNightMats = (new String[] {
            "Tcrwgear"
        });
        setNightMats(false);
        if(this.emitter == null)
        {
            for(int i = 1; i <= 11; i++)
            {
                this.mesh.chunkVisible("Ammo" + i + "L", false);
                this.mesh.chunkVisible("Ammo" + i + "R", false);
            }

        }
    }

    private boolean bNeedSetUp;
    private float yawVelMax;
    private float tanVelMax;
    private float yaw;
    private float tan;
    private float prevTan;
    private float prevYaw;
    private long prevMoveTime;
    private float controlYaw;
    private float controlTan;
    private float ammoBeltAnim;
    private final float beltAnimCoords[][] = {
        {
            0.019F, 0.622F
        }, {
            0.078F, 0.561F
        }, {
            0.431F, 0.377F
        }, {
            0.549F, -0.016F
        }, {
            0.529F, -0.212F
        }, {
            0.506F, -0.053F
        }, {
            0.512F, -0.051F
        }, {
            0.487F, 0.115F
        }, {
            0.445F, 0.257F
        }, {
            0.491F, 0.152F
        }, {
            0.491F, 0.0F
        }
    };
    private final float beltAnimRot[] = {
        0.0F, 42F, 38F, 18F, -12F, 0.0F, -15F, -15F, 10F, 10F, 
        0.0F
    };
    private long prevTime;
    private float prevA0;
    private float prevA1;
    private Hook hook1;
    private Hook hook2;

    static 
    {
        Property.set(CockpitB24D_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitB24D_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitB24D_TGunner.class, "astatePilotIndx", 3);
        Property.set(CockpitB24D_TGunner.class, "normZN", 1.2F);
    }
}
