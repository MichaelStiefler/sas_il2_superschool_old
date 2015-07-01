package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookGunner;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitGunner extends Cockpit
    implements com.maddox.il2.engine.hotkey.HookGunner.Move
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
                interpTick();
            return true;
        }

        Interpolater()
        {
        }
    }


    public boolean isEnableHotKeysOnOutsideView()
    {
        return false;
    }

    public String[] getHotKeyEnvs()
    {
        return hotKeyEnvs;
    }

    private void ZNear(float f)
    {
        if(f < 0.0F)
        {
            return;
        } else
        {
            Camera3D camera3d = Main3D.cur3D().camera3D;
            camera3d.ZNear = f;
            return;
        }
    }

    public void resetZnear()
    {
        Main3D.cur3D().camera3D.ZNear = 1.2F;
        Main3D.cur3D().cameraCockpit.ZNear = 0.05F;
    }

    public HookGunner hookGunner()
    {
        return hookGunner;
    }

    public Turret aiTurret()
    {
        if(_aiTuretNum == -1)
            _aiTuretNum = Property.intValue(getClass(), "aiTuretNum", 0);
        return fm.turret[_aiTuretNum];
    }

    public int weaponControlNum()
    {
        if(_weaponControlNum == -1)
            _weaponControlNum = Property.intValue(getClass(), "weaponControlNum", 10);
        return _weaponControlNum;
    }

    public boolean isRealMode()
    {
        return !aiTurret().bIsAIControlled;
    }

    public void setRealMode(boolean flag)
    {
        if(aiTurret().bIsAIControlled != flag)
            return;
        aiTurret().bIsAIControlled = !flag;
        aiTurret().target = null;
        fm.CT.WeaponControl[weaponControlNum()] = false;
        bGunFire = false;
        if(flag)
        {
            hookGunner().resetMove(0.0F, 0.0F);
        } else
        {
            aiTurret().tu[0] = 0.0F;
            aiTurret().tu[1] = 0.0F;
        }
    }

    public boolean isNetMirror()
    {
        return bNetMirror;
    }

    public void setNetMirror(boolean flag)
    {
        bNetMirror = flag;
    }

    public void moveGun(Orient orient)
    {
        if(isRealMode())
        {
            aiTurret().tu[0] = orient.getAzimut();
            aiTurret().tu[1] = orient.getTangage();
        }
    }

    public void clipAnglesGun(Orient orient)
    {
    }

    public Hook getHookCameraGun()
    {
        if(!isRealMode())
        {
            _tmpOrient.set(aiTurret().tu[0], aiTurret().tu[1], 0.0F);
            moveGun(_tmpOrient);
        }
        return cameraHook;
    }

    public void doGunFire(boolean flag)
    {
        if(emitter != null && !emitter.haveBullets())
            bGunFire = false;
        else
            bGunFire = flag;
    }

    protected void doHitMasterAircraft(Aircraft aircraft, Hook hook, String s)
    {
        _tmpLoc1.set(0.10000000000000001D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        aircraft.pos.getAbs(_tmpLoc2);
        hook.computePos(aircraft, _tmpLoc2, _tmpLoc1);
        _tmpLoc1.get(_tmpP1);
        _tmpLoc1.set(48.899999999999999D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hook.computePos(aircraft, _tmpLoc2, _tmpLoc1);
        _tmpLoc1.get(_tmpP2);
        int i = aircraft.hierMesh().detectCollisionLineMulti(_tmpLoc2, _tmpP1, _tmpP2);
        if(i > 0)
        {
            Gun gun = aircraft.getGunByHookName(s);
            if(gun.getTickShot())
            {
                _tmpShot.powerType = 0;
                _tmpShot.mass = gun.bulletMassa();
                aircraft.hierMesh();
				_tmpShot.p.interpolate(_tmpP1, _tmpP2, HierMesh.collisionDistMulti(0));
                _tmpShot.v.x = (float)(_tmpP2.x - _tmpP1.x);
                _tmpShot.v.y = (float)(_tmpP2.y - _tmpP1.y);
                _tmpShot.v.z = (float)(_tmpP2.z - _tmpP1.z);
                _tmpShot.v.normalize();
                _tmpShot.v.scale(gun.bulletSpeed());
                aircraft.getSpeed(_tmpV1);
                _tmpShot.v.x += (float)_tmpV1.x;
                _tmpShot.v.y += (float)_tmpV1.y;
                _tmpShot.v.z += (float)_tmpV1.z;
                _tmpShot.tickOffset = 1.0D;
                _tmpShot.chunkName = "CF_D0";
                _tmpShot.initiator = fm.actor;
                aircraft.msgShot(_tmpShot);
            }
        }
    }

    protected void _setNullShow(boolean flag)
    {
    }

    protected void _setEnableRendering(boolean flag)
    {
    }

    protected void interpTick()
    {
    }

    protected boolean doFocusEnter()
    {
        HookGunner hookgunner = hookGunner();
        Aircraft aircraft = aircraft();
        Main3D main3d = Main3D.cur3D();
        Main3D.cur3D().cameraCockpit.ZNear = 0.005F;
        hookgunner.setMover(this);
        hookgunner.reset();
        hookgunner.use(true);
        aircraft.setAcoustics(acoustics);
        if(acoustics != null)
        {
            aircraft.enableDoorSnd(true);
            if(acoustics.getEnvNum() == 2)
                aircraft.setDoorSnd(1.0F);
        }
        main3d.camera3D.pos.setRel(new Point3d(), new Orient());
        main3d.camera3D.pos.setBase(aircraft, hookgunner, false);
        main3d.camera3D.pos.resetAsBase();
        pos.resetAsBase();
        main3d.cameraCockpit.pos.setRel(new Point3d(), new Orient());
        main3d.cameraCockpit.pos.setBase(this, hookgunner, false);
        main3d.cameraCockpit.pos.resetAsBase();
        main3d.overLoad.setShow(true);
        main3d.renderCockpit.setShow(true);
        aircraft.drawing(!isNullShow());
        if(hidePilot)
            try
            {
                HierMesh hiermesh = aircraft().hierMesh();
                int i = hiermesh.materialFind("Pilot1");
                if(i != -1)
                {
                    Mat mat = hiermesh.material(i);
                    mat.setLayer(0);
                    mat.set((byte)0, 15F);
                }
                i = hiermesh.materialFind("Pilot2");
                if(i != -1)
                {
                    Mat mat1 = hiermesh.material(i);
                    mat1.setLayer(0);
                    mat1.set((byte)0, 15F);
                }
            }
            catch(Exception exception) { }
        if(HookPilot.current.isAim())
            ZNear(gsZN);
        else
            ZNear(normZN);
        return true;
    }

    protected void doFocusLeave()
    {
        HookGunner hookgunner = hookGunner();
        Aircraft aircraft = aircraft();
        Main3D main3d = Main3D.cur3D();
        hookgunner.use(false);
        main3d.camera3D.pos.setRel(new Point3d(), new Orient());
        main3d.camera3D.pos.setBase(null, null, false);
        main3d.cameraCockpit.pos.setRel(new Point3d(), new Orient());
        main3d.cameraCockpit.pos.setBase(null, null, false);
        main3d.overLoad.setShow(false);
        main3d.renderCockpit.setShow(false);
        if(Actor.isValid(aircraft))
            aircraft.drawing(true);
        if(aircraft != null) {
            if((double)aircraft.FM.CT.getCockpitDoor() < 0.5D)
                aircraft.setDoorSnd(0.0F);
            else
                aircraft.setDoorSnd(1.0F);
            aircraft.setAcoustics(null);
        }
        aircraft.enableDoorSnd(false);
        resetZnear();
        if(hidePilot)
            try
            {
                HierMesh hiermesh = aircraft().hierMesh();
                int i = hiermesh.materialFind("Pilot1");
                if(i != -1)
                {
                    Mat mat = hiermesh.material(i);
                    mat.setLayer(0);
                    mat.set((byte)0, 0.0F);
                }
                i = hiermesh.materialFind("Pilot2");
                if(i != -1)
                {
                    Mat mat1 = hiermesh.material(i);
                    mat1.setLayer(0);
                    mat1.set((byte)0, 0.0F);
                }
            }
            catch(Exception exception) { }
    }

    public CockpitGunner(String s, String s1)
    {
        super(s, s1);
        bGunFire = false;
        _tmpOrient = new Orient();
        bNetMirror = false;
        normZN = 1.2F;
        gsZN = 1.2F;
        _aiTuretNum = -1;
        _weaponControlNum = -1;
        cameraHook = new HookNamed(mesh, "CAMERA");
        pos.setBase(aircraft(), new Cockpit.HookOnlyOrient(), false);
        hookGunner = new HookGunner(Main3D.cur3D().cameraCockpit, Main3D.cur3D().camera3D, weaponControlNum());
        hookGunner().setMover(this);
        interpPut(new Interpolater(), null, Time.current(), null);
        Aircraft aircraft = aircraft();
        BulletEmitter abulletemitter[] = aircraft.FM.CT.Weapons[weaponControlNum()];
        if(abulletemitter != null)
            emitter = abulletemitter[0];
        normZN = Property.floatValue(getClass(), "normZN", -1F);
        gsZN = Property.floatValue(getClass(), "gsZN", -1F);
    }

    public String[] getHotKeyEnvsAll() {
		return hotKeyEnvsAll;
	}

	public void setHotKeyEnvsAll(String hotKeyEnvsAll[]) {
		this.hotKeyEnvsAll = hotKeyEnvsAll;
	}

	private String hotKeyEnvs[] = {
        "gunner"
    };
    private String hotKeyEnvsAll[] = {
        "gunner", "pilot", "move"
    };
    protected Hook cameraHook;
    protected boolean bGunFire;
    protected BulletEmitter emitter;
    private Orient _tmpOrient;
    private HookGunner hookGunner;
    private boolean bNetMirror;
    protected float normZN;
    protected float gsZN;
    protected int _aiTuretNum;
    protected int _weaponControlNum;
    private static Loc _tmpLoc1 = new Loc();
    private static Loc _tmpLoc2 = new Loc();
    private static Point3d _tmpP1 = new Point3d();
    private static Point3d _tmpP2 = new Point3d();
    private static Vector3d _tmpV1 = new Vector3d();
    private static Shot _tmpShot = new Shot();

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitGunner.class, "aiTuretNum", 0);
        Property.set(com.maddox.il2.objects.air.CockpitGunner.class, "weaponControlNum", 10);
        Property.set(com.maddox.il2.objects.air.CockpitGunner.class, "astatePilotIndx", 1);
    }
}
