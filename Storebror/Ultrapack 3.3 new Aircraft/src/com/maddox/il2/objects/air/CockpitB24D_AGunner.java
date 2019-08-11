package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB24D_AGunner extends CockpitGunner
{

    public void reflectWorldToInstruments(float f)
    {
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
        this.mesh.chunkSetAngles("zColumn", controlYaw, 0.0F, 0.0F);
        f4 = Math.abs(f2 / f3);
        if(Float.isNaN(f4))
            f4 = 0.0F;
        f5 = 1.0F;
        if(f2 < 0.0F)
            f5 = -1F;
        controlTan = 0.9F * controlTan + 0.1F * cvt(f4, 0.0F, tanVelMax, 0.0F, 30F * f5);
        this.mesh.chunkSetAngles("zGrips", 0.0F, -controlTan, 0.0F);
        prevYaw = yaw;
        prevTan = tan;
        prevMoveTime = Time.current();
        B_24D140CO b_24d140co = (B_24D140CO)aircraft();
        resetYPRmodifier();
        if(this.bGunFire && World.cur().diffCur.Limited_Ammo)
        {
            BulletEmitter abulletemitter[] = this.fm.CT.Weapons[weaponControlNum()];
            if(!b_24d140co.rearLeftGunJammed)
            {
                int i = abulletemitter[0].countBullets();
                for(int l = 1; l <= 16; l++)
                    this.mesh.chunkVisible("AmmoL" + l, i > 19 - l);

            }
            if(!b_24d140co.rearRightGunJammed)
            {
                int j = abulletemitter[1].countBullets();
                for(int i1 = 1; i1 <= 16; i1++)
                    this.mesh.chunkVisible("AmmoR" + i1, j > 19 - i1);

            }
        }
        float f6 = tan;
        for(int k = 1; k <= 16; k++)
        {
            Aircraft.xyz[1] = 0.0F;
            float f7 = (f6 / 13F) * (float)(k - 3);
            if(k >= 3)
            {
                this.mesh.chunkSetAngles("AmmoR" + k, 0.0F, 0.0F, -f7);
                this.mesh.chunkSetAngles("AmmoL" + k, 0.0F, 0.0F, -f7);
            }
            if(!b_24d140co.rearRightGunJammed)
            {
                Aircraft.xyz[2] = rightBeltAnimCoords[k - 1][1] * ammoBeltAnimLoop * 0.05F;
                Aircraft.xyz[0] = rightBeltAnimCoords[k - 1][0] * ammoBeltAnimLoop * 0.05F;
            }
            if(k >= 3)
                if(f6 > 0.0F)
                {
                    Aircraft.xyz[1] = f7 * 0.0002F;
                    Aircraft.xyz[2] += f7 * -0.0021F;
                } else
                {
                    Aircraft.xyz[1] = f7 * -0.0011F;
                    Aircraft.xyz[2] += f7 * -0.0016F;
                }
            this.mesh.chunkSetLocate("AmmoR" + k + "B", Aircraft.xyz, Aircraft.ypr);
            if(!b_24d140co.rearLeftGunJammed)
            {
                Aircraft.xyz[2] = leftBeltAnimCoords[k - 1][1] * ammoBeltAnimLoop * 0.05F;
                Aircraft.xyz[0] = leftBeltAnimCoords[k - 1][0] * ammoBeltAnimLoop * 0.05F;
            }
            if(k >= 3)
                if(f6 > 0.0F)
                {
                    Aircraft.xyz[1] = f7 * -0.0016F;
                    Aircraft.xyz[2] += f7 * 0.0013F;
                } else
                {
                    Aircraft.xyz[1] = f7 * -0.001F;
                    Aircraft.xyz[2] += f7 * 0.0024F;
                }
            this.mesh.chunkSetLocate("AmmoL" + k + "B", Aircraft.xyz, Aircraft.ypr);
        }

        this.mesh.chunkVisible("AmmoL16", false);
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        if(ammoBeltAnim < 20F)
        {
            Aircraft.xyz[1] = ammoBeltAnim * 0.03F;
            if(!b_24d140co.rearLeftGunJammed)
                this.mesh.chunkSetLocate("50CalStripL_1", Aircraft.xyz, Aircraft.ypr);
            if(!b_24d140co.rearRightGunJammed)
                this.mesh.chunkSetLocate("50CalStripR_1", Aircraft.xyz, Aircraft.ypr);
        } else
        if(ammoBeltAnim < 40F)
        {
            Aircraft.xyz[1] = (ammoBeltAnim - 20F) * 0.03F;
            if(!b_24d140co.rearLeftGunJammed)
                this.mesh.chunkSetLocate("50CalStripL_2", Aircraft.xyz, Aircraft.ypr);
            if(!b_24d140co.rearRightGunJammed)
                this.mesh.chunkSetLocate("50CalStripR_2", Aircraft.xyz, Aircraft.ypr);
        } else
        {
            Aircraft.xyz[1] = (ammoBeltAnim - 40F) * 0.03F;
            if(!b_24d140co.rearLeftGunJammed)
                this.mesh.chunkSetLocate("50CalStripL_3", Aircraft.xyz, Aircraft.ypr);
            if(!b_24d140co.rearRightGunJammed)
                this.mesh.chunkSetLocate("50CalStripR_3", Aircraft.xyz, Aircraft.ypr);
        }
    }

    protected void reflectPlaneMats()
    {
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 8) != 0)
            this.mesh.chunkVisible("GlassA_Dmg", true);
        if((this.fm.AS.astateCockpitState & 0x20) != 0)
        {
            this.mesh.chunkVisible("Glass_Dmg", true);
            this.mesh.chunkVisible("Hull_Dmg", true);
        }
    }

    protected void reflectPlaneToModel()
    {
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Main", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("zGuns", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("zSghtBar", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("zSghtLnksRods", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("zElShft", 0.0F, orient.getTangage(), 0.0F);
        yaw = orient.getYaw();
        tan = orient.getTangage();
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if(!isRealMode())
        {
            prevA0 = f;
            prevA1 = f;
            return;
        }
        if(!aiTurret().bIsOperable)
        {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        if(f < -60F)
            f = -60F;
        if(f > 60F)
            f = 60F;
        if(f1 > 71F)
            f1 = 71F;
        if(f1 < -29F)
            f1 = -29F;
        if(bNeedSetUp)
        {
            prevTime = Time.current() - 1L;
            bNeedSetUp = false;
            reflectPlaneMats();
        }
        if(f < -yawVelMax && prevA0 > yawVelMax)
            f += 360F;
        else
        if(f > yawVelMax && prevA0 < -yawVelMax)
            prevA0 += 360F;
        float f2 = f - prevA0;
        float f3 = 0.001F * (float)(Time.current() - prevTime);
        float f4 = Math.abs(f2 / f3);
        if(f4 > yawVelMax)
            if(f > prevA0)
                f = prevA0 + yawVelMax * f3;
            else
            if(f < prevA0)
                f = prevA0 - yawVelMax * f3;
        f2 = f1 - prevA1;
        f3 = 0.001F * (float)(Time.current() - prevTime);
        f4 = Math.abs(f2 / f3);
        if(f4 > tanVelMax)
            if(f1 > prevA1)
                f1 = prevA1 + tanVelMax * f3;
            else
            if(f1 < prevA1)
                f1 = prevA1 - tanVelMax * f3;
        prevTime = Time.current();
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        prevA0 = f;
        prevA1 = f1;
    }

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        if(this.bGunFire)
        {
            ammoBeltAnimLoop += 0.25F;
            ammoBeltAnim += 0.25F;
            if(ammoBeltAnimLoop > 1.0F)
                ammoBeltAnimLoop = 0.0F;
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN03");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN03");
            if(hook2 == null)
                hook2 = new HookNamed(aircraft(), "_MGUN04");
            doHitMasterAircraft(aircraft(), hook2, "_MGUN04");
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

    public CockpitB24D_AGunner()
    {
        super("3DO/Cockpit/B-24D-AGun/hier.him", "bf109");
        bNeedSetUp = true;
        yawVelMax = 120F;
        tanVelMax = 120F;
        yaw = 0.0F;
        tan = 0.0F;
        prevTan = 0.0F;
        prevYaw = 0.0F;
        prevMoveTime = -1L;
        controlYaw = 0.0F;
        controlTan = 0.0F;
        ammoBeltAnimLoop = 0.0F;
        ammoBeltAnim = 0.0F;
        prevTime = -1L;
        prevA0 = 0.0F;
        prevA1 = 0.0F;
        hook1 = null;
        hook2 = null;
        this.cockpitNightMats = (new String[] {
            "Crew", "Needles"
        });
        setNightMats(false);
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
    private float ammoBeltAnimLoop;
    private float ammoBeltAnim;
    private final float leftBeltAnimCoords[][] = {
        {
            -0.602F, 0.0F
        }, {
            -0.608F, -0.008F
        }, {
            -0.612F, -0.036F
        }, {
            -0.587F, -0.021F
        }, {
            -0.583F, -0.042F
        }, {
            -0.602F, -0.025F
        }, {
            -0.608F, -0.043F
        }, {
            -0.612F, 0.0F
        }, {
            -0.587F, 0.042F
        }, {
            -0.598F, 0.053F
        }, {
            -0.602F, 0.054F
        }, {
            -0.608F, 0.086F
        }, {
            -0.612F, 0.063F
        }, {
            -0.587F, 0.062F
        }, {
            -0.581F, 0.044F
        }, {
            -0.602F, 0.0F
        }
    };
    private final float rightBeltAnimCoords[][] = {
        {
            0.581F, -0.044F
        }, {
            0.587F, -0.062F
        }, {
            0.612F, -0.063F
        }, {
            0.608F, -0.086F
        }, {
            0.602F, -0.054F
        }, {
            0.598F, -0.053F
        }, {
            0.587F, -0.042F
        }, {
            0.612F, 0.055F
        }, {
            0.608F, 0.048F
        }, {
            0.602F, 0.084F
        }, {
            0.583F, 0.063F
        }, {
            0.587F, 0.097F
        }, {
            0.612F, 0.095F
        }, {
            0.608F, 0.1F
        }, {
            0.602F, 0.083F
        }, {
            0.6F, 0.0F
        }
    };
    private long prevTime;
    private float prevA0;
    private float prevA1;
    private Hook hook1;
    private Hook hook2;

    static 
    {
        Property.set(CockpitB24D_AGunner.class, "aiTuretNum", 5);
        Property.set(CockpitB24D_AGunner.class, "weaponControlNum", 15);
        Property.set(CockpitB24D_AGunner.class, "astatePilotIndx", 7);
        Property.set(CockpitB24D_AGunner.class, "normZN", 1.88F);
    }
}
