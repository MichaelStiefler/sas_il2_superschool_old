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
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookGunner;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;
import com.maddox.sound.SoundPreset;

public class CockpitGunner extends Cockpit implements com.maddox.il2.engine.hotkey.HookGunner.Move {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitGunner.this.fm != null) {
                CockpitGunner.this.interpTick();
                if (CockpitGunner.this.fm.isTick(4, 0)) {
                    CockpitGunner.this.updateTurretSounds();
                }
            }
            if ((CockpitGunner.this.emitter != null) && CockpitGunner.this.emitter.haveBullets()) {
                CockpitGunner.this.ammoLeft = CockpitGunner.this.emitter.countBullets();
                if (CockpitGunner.this.ammoLeft != CockpitGunner.this.prevAmmoLeft) {
                    CockpitGunner.this.areGunsReloading();
                    if (CockpitGunner.this.magRotate >= 0.0F) {
                        CockpitGunner.this.magRotate += 7.42268F;
                    }
                    CockpitGunner.this.prevAmmoLeft = CockpitGunner.this.ammoLeft;
                    CockpitGunner.this.bHandleMoved = true;
                } else {
                    CockpitGunner.this.bHandleMoved = false;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    public boolean isEnableHotKeysOnOutsideView() {
        return false;
    }

    public String[] getHotKeyEnvs() {
        return this.hotKeyEnvs;
    }

    protected void initTurretSounds(Turret turret, Point3d point3d) {
        this.turretSfx = turret;
        if (turretSfxPreset == null) {
            turretSfxPreset = new SoundPreset("aircraft.turret");
        }
        this.turretSounds = new SoundFX[5];
        if (point3d != null) {
            this.turretSfxPos = point3d;
        }
    }

    protected void turretSfxStart(int i) {
        if ((this.turretSounds != null) && (this.turretSounds.length > i)) {
            SoundFX soundfx = this.turretSounds[i];
            if (soundfx == null) {
                soundfx = this.aircraft().newSound(turretSfxPreset, false, false);
                if (soundfx == null) {
                    return;
                }
                soundfx.setParent(this.aircraft().getRootFX());
                this.turretSounds[i] = soundfx;
                soundfx.setUsrFlag(i);
            }
            soundfx.setVolume(1.0F);
            soundfx.play(this.turretSfxPos);
        }
    }

    protected void turretSfxStop(int i) {
        if ((this.turretSounds != null) && (this.turretSounds.length > i) && (this.turretSounds[i] != null)) {
            this.turretSounds[i].setPlay(false);
            this.turretSounds[i].setVolume(0.0F);
        }
    }

    private void updateTurretSounds() {
        if ((this.turretSfx != null) && this.isFocused()) {
            float f = this.turretSfx.tu[0];
            float f1 = this.turretSfx.tu[1];
            float f2 = Math.abs(this.prevAzi - f);
            float f3 = Math.abs(this.prevTan - f1);
            if (f2 > 0.0F) {
                if (!this.playTurretSfxAzi) {
                    if (this.playElectricTurretSounds) {
                        this.turretSfxStart(1);
                    } else if (this.playHydraulicTurretSounds) {
                        this.turretSfxStart(3);
                    }
                    this.playTurretSfxAzi = true;
                }
            } else if (this.playTurretSfxAzi) {
                if (this.playElectricTurretSounds) {
                    this.turretSfxStop(1);
                } else if (this.playHydraulicTurretSounds) {
                    this.turretSfxStop(3);
                }
                this.playTurretSfxAzi = false;
            }
            if (f3 > 0.0F) {
                if (!this.playTurretSfxTan) {
                    if (this.playElectricTurretSounds) {
                        this.turretSfxStart(2);
                    } else if (this.playHydraulicTurretSounds) {
                        this.turretSfxStart(4);
                    }
                    this.playTurretSfxTan = true;
                }
            } else if (this.playTurretSfxTan) {
                if (this.playElectricTurretSounds) {
                    this.turretSfxStop(2);
                } else if (this.playHydraulicTurretSounds) {
                    this.turretSfxStop(4);
                }
                this.playTurretSfxTan = false;
            }
            this.prevAzi = f;
            this.prevTan = f1;
        }
    }

    private void ZNear(float f) {
        if (f < 0.0F) {
            return;
        } else {
            Camera3D camera3d = Main3D.cur3D().camera3D;
            camera3d.ZNear = f;
            return;
        }
    }

    public void resetZnear() {
        Main3D.cur3D().camera3D.ZNear = 1.2F;
        Main3D.cur3D().cameraCockpit.ZNear = 0.05F;
    }

    public HookGunner hookGunner() {
        return this.hookGunner;
    }

    public Turret aiTurret() {
        if (this._aiTuretNum == -1) {
            this._aiTuretNum = Property.intValue(this.getClass(), "aiTuretNum", 0);
        }
        if (this._aiTuretNum < 0) {
            return null;
        } else {
            return this.fm.turret[this._aiTuretNum];
        }
    }

    public int weaponControlNum() {
        if (this._weaponControlNum == -1) {
            this._weaponControlNum = Property.intValue(this.getClass(), "weaponControlNum", 10);
        }
        return this._weaponControlNum;
    }

    public boolean isRealMode() {
        return !this.aiTurret().bIsAIControlled;
    }

    public void setRealMode(boolean flag) {
        if (this.aiTurret().bIsAIControlled != flag) {
            return;
        }
        this.aiTurret().bIsAIControlled = !flag;
        this.aiTurret().target = null;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = false;
        this.bGunFire = false;
        if (flag) {
            this.hookGunner().resetMove(0.0F, 0.0F);
        } else {
            this.aiTurret().tu[0] = 0.0F;
            this.aiTurret().tu[1] = 0.0F;
        }
    }

    public boolean isNetMirror() {
        return this.bNetMirror;
    }

    public void setNetMirror(boolean flag) {
        this.bNetMirror = flag;
    }

    public void moveGun(Orient orient) {
        if (this.isRealMode()) {
            this.aiTurret().tu[0] = orient.getAzimut();
            this.aiTurret().tu[1] = orient.getTangage();
        }
    }

    public void clipAnglesGun(Orient orient) {
    }

    public Hook getHookCameraGun() {
        if (!this.isRealMode()) {
            this._tmpOrient.set(this.aiTurret().tu[0], this.aiTurret().tu[1], 0.0F);
            this.moveGun(this._tmpOrient);
        }
        return this.cameraHook;
    }

    public void doGunFire(boolean flag) {
        if ((this.emitter != null) && !this.emitter.haveBullets()) {
            this.bGunFire = false;
        } else {
            this.bGunFire = flag;
        }
    }

    protected void doHitMasterAircraft(Aircraft aircraft, Hook hook, String s) {
        _tmpLoc1.set(0.10000000000000001D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        aircraft.pos.getAbs(_tmpLoc2);
        hook.computePos(aircraft, _tmpLoc2, _tmpLoc1);
        _tmpLoc1.get(_tmpP1);
        _tmpLoc1.set(48.899999999999999D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hook.computePos(aircraft, _tmpLoc2, _tmpLoc1);
        _tmpLoc1.get(_tmpP2);
        int i = aircraft.hierMesh().detectCollisionLineMulti(_tmpLoc2, _tmpP1, _tmpP2);
        if (i > 0) {
            Gun gun = aircraft.getGunByHookName(s);
            if (gun.getTickShot()) {
                _tmpShot.powerType = 0;
                _tmpShot.mass = gun.bulletMassa();
                aircraft.hierMesh();
                _tmpShot.p.interpolate(_tmpP1, _tmpP2, Mesh.collisionDistMulti(0));
                _tmpShot.v.x = (float) (_tmpP2.x - _tmpP1.x);
                _tmpShot.v.y = (float) (_tmpP2.y - _tmpP1.y);
                _tmpShot.v.z = (float) (_tmpP2.z - _tmpP1.z);
                _tmpShot.v.normalize();
                _tmpShot.v.scale(gun.bulletSpeed());
                aircraft.getSpeed(_tmpV1);
                _tmpShot.v.x += (float) _tmpV1.x;
                _tmpShot.v.y += (float) _tmpV1.y;
                _tmpShot.v.z += (float) _tmpV1.z;
                _tmpShot.tickOffset = 1.0D;
                _tmpShot.chunkName = "CF_D0";
                _tmpShot.initiator = this.fm.actor;
                aircraft.msgShot(_tmpShot);
            }
        }
    }

    protected void _setNullShow(boolean flag) {
    }

    protected void _setEnableRendering(boolean flag) {
    }

    protected void interpTick() {
    }

    protected boolean doFocusEnter() {
        HookGunner hookgunner = this.hookGunner();
        Aircraft aircraft = this.aircraft();
        Main3D main3d = Main3D.cur3D();
        Main3D.cur3D().cameraCockpit.ZNear = 0.005F;
        hookgunner.setMover(this);
        hookgunner.reset();
        hookgunner.use(true);
        aircraft.setAcoustics(this.acoustics);
        if (this.acoustics != null) {
            aircraft.enableDoorSnd(true);
            if (this.acoustics.getEnvNum() == 2) {
                aircraft.setDoorSnd(1.0F);
            }
        }
        main3d.camera3D.pos.setRel(new Point3d(), new Orient());
        main3d.camera3D.pos.setBase(aircraft, hookgunner, false);
        main3d.camera3D.pos.resetAsBase();
        this.pos.resetAsBase();
        main3d.cameraCockpit.pos.setRel(new Point3d(), new Orient());
        main3d.cameraCockpit.pos.setBase(this, hookgunner, false);
        main3d.cameraCockpit.pos.resetAsBase();
        main3d.overLoad.setShow(true);
        main3d.renderCockpit.setShow(true);
        aircraft.drawing(!this.isNullShow());
        if (this.hidePilot) {
            try {
                HierMesh hiermesh = this.aircraft().hierMesh();
                int i = hiermesh.materialFind("Pilot1");
                if (i != -1) {
                    Mat mat = hiermesh.material(i);
                    mat.setLayer(0);
                    mat.set((byte) 0, 15F);
                }
                i = hiermesh.materialFind("Pilot2");
                if (i != -1) {
                    Mat mat1 = hiermesh.material(i);
                    mat1.setLayer(0);
                    mat1.set((byte) 0, 15F);
                }
            } catch (Exception exception) {
                System.out.println(exception);
            }
        }
        if (HookPilot.current.isAim()) {
            this.ZNear(this.gsZN);
        } else {
            this.ZNear(this.normZN);
        }
        return true;
    }

    protected void doFocusLeave() {
        HookGunner hookgunner = this.hookGunner();
        Aircraft aircraft = this.aircraft();
        Main3D main3d = Main3D.cur3D();
        hookgunner.use(false);
        main3d.camera3D.pos.setRel(new Point3d(), new Orient());
        main3d.camera3D.pos.setBase(null, null, false);
        main3d.cameraCockpit.pos.setRel(new Point3d(), new Orient());
        main3d.cameraCockpit.pos.setBase(null, null, false);
        main3d.overLoad.setShow(false);
        main3d.renderCockpit.setShow(false);
        if (Actor.isValid(aircraft)) {
            aircraft.drawing(true);
        }
        if (aircraft != null) {
// if ((double)aircraft.FM.CT.getCockpitDoor() < 0.5D)
// aircraft.setDoorSnd(0.0F);
// else
// aircraft.setDoorSnd(1.0F);
            aircraft.setAcoustics(null);
        }
        aircraft.enableDoorSnd(false);
        this.resetZnear();
        if (this.hidePilot) {
            try {
                HierMesh hiermesh = this.aircraft().hierMesh();
                int i = hiermesh.materialFind("Pilot1");
                if (i != -1) {
                    Mat mat = hiermesh.material(i);
                    mat.setLayer(0);
                    mat.set((byte) 0, 0.0F);
                }
                i = hiermesh.materialFind("Pilot2");
                if (i != -1) {
                    Mat mat1 = hiermesh.material(i);
                    mat1.setLayer(0);
                    mat1.set((byte) 0, 0.0F);
                }
            } catch (Exception exception) {
            }
        }
        if (this.playTurretSfxAzi) {
            if (this.playElectricTurretSounds) {
                this.turretSfxStop(1);
            } else if (this.playHydraulicTurretSounds) {
                this.turretSfxStop(3);
            }
            this.playTurretSfxAzi = false;
        }
        if (this.playTurretSfxTan) {
            if (this.playElectricTurretSounds) {
                this.turretSfxStop(2);
            } else if (this.playHydraulicTurretSounds) {
                this.turretSfxStop(4);
            }
            this.playTurretSfxTan = false;
        }
    }

    public CockpitGunner(String s, String s1) {
        super(s, s1);
        this.turretSounds = null;
        this.turretSfx = null;
        this.turretSfxPos = new Point3d(1.0D, 0.0D, 0.0D);
        this.magazines = -1;
        this.spareMagName = "";
        this.bHandleMoved = false;
        this.ammoLeft = 0;
        this.prevAmmoLeft = 0;
        this.magRotate = -1F;
        this.gunLeverMoveAxis = 1;
        this.bGunFire = false;
        this._tmpOrient = new Orient();
        this.bNetMirror = false;
        this.normZN = 1.2F;
        this.gsZN = 1.2F;
        this._aiTuretNum = -1;
        this._weaponControlNum = -1;
        this.prevAzi = 0.0F;
        this.prevTan = 0.0F;
        this.playTurretSfxAzi = false;
        this.playTurretSfxTan = false;
        this.playElectricTurretSounds = false;
        this.playHydraulicTurretSounds = false;
        this.bReloading = false;
        this.cameraHook = new HookNamed(this.mesh, "CAMERA");
        this.pos.setBase(this.aircraft(), new Cockpit.HookOnlyOrient(), false);
        this.hookGunner = new HookGunner(Main3D.cur3D().cameraCockpit, Main3D.cur3D().camera3D, this.weaponControlNum());
        this.hookGunner().setMover(this);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        Aircraft aircraft = this.aircraft();
        BulletEmitter abulletemitter[] = aircraft.FM.CT.Weapons[this.weaponControlNum()];
        if (abulletemitter != null) {
            this.emitter = abulletemitter[0];
        }
        this.normZN = Property.floatValue(this.getClass(), "normZN", -1F);
        this.gsZN = Property.floatValue(this.getClass(), "gsZN", -1F);
    }

    public boolean useMultiFunction() {
        return false;
    }

    public void doMultiFunction(boolean flag) {
    }

    public void doGunReload(boolean flag) {
        this.bReloading = flag;
    }

    public boolean areGunsReloading() {
        return Main3D.cur3D().aircraftHotKeys.areGunsReloading(this);
    }

    public void reflectWorldToInstruments(float f) {
        if ((this.emitter != null) && (this.emitter.getBulletsPerMag() > 0)) {
            if (this.magazines == -1) {
                this.magazines = this.emitter.getMagazines();
            }
            if (this.magazines > 0) {
                int i = ((this.emitter.countBullets() - 1) / this.emitter.getBulletsPerMag()) + 1;
                for (int k = 1; k < this.magazines; k++) {
                    if (k < i) {
                        this.mesh.chunkVisible(this.spareMagName + k, true);
                    } else {
                        this.mesh.chunkVisible(this.spareMagName + k, false);
                    }
                }

            }
        } else {
            for (int j = 1; j < this.magazines; j++) {
                this.mesh.chunkVisible(this.spareMagName + j, false);
            }

        }
        if (this.bReloading) {
            this.mesh.chunkVisible("cur_magazine", false);
            if (this.magRotate >= 0.0F) {
                this.mesh.chunkVisible("cur_magazineRot", false);
            }
            this.areGunsReloading();
        } else {
            this.mesh.chunkVisible("cur_magazine", true);
            if (this.magRotate >= 0.0F) {
                this.mesh.chunkVisible("cur_magazineRot", true);
                this.mesh.chunkSetAngles("cur_magazineRot", this.magRotate, 0.0F, 0.0F);
            }
            this.resetYPRmodifier();
            if (this.gunLeverMoveAxis >= 0) {
                if (this.bHandleMoved) {
                    xyz[this.gunLeverMoveAxis] = 0.0F;
                } else {
                    xyz[this.gunLeverMoveAxis] = 0.03F;
                }
                this.mesh.chunkSetLocate("CockingLever", xyz, ypr);
            }
        }
    }

    public void setCameraZForTest(String s, boolean flag) {
        float f = 0.005F;
        if (s.equals("normZN")) {
            if (flag) {
                this.normZN = this.normZN + f;
            } else {
                this.normZN = this.normZN - f;
            }
            if (this.normZN < 0.0F) {
                this.normZN = 0.0F;
            }
            HUD.log("normZN: " + this.normZN);
        } else if (s.equals("gsZN")) {
            if (flag) {
                this.gsZN = this.gsZN + f;
            } else {
                this.gsZN = this.gsZN - f;
            }
            if (this.gsZN < 0.0F) {
                this.gsZN = 0.0F;
            }
            HUD.log("gsZN: " + this.gsZN);
        }
    }

    public String[] getHotKeyEnvsAll() {
        return this.hotKeyEnvsAll;
    }

    public void setHotKeyEnvsAll(String hotKeyEnvsAll[]) {
        this.hotKeyEnvsAll = hotKeyEnvsAll;
    }

    private String             hotKeyEnvs[]                   = { "gunner" };
    private String             hotKeyEnvsAll[]                = { "gunner", "pilot", "move" };

    public static final int    SNDTURRET_RAISE_LOWER          = 0;
    public static final int    SNDTURRET_ELECTRIC_ROTATE_YAW  = 1;
    public static final int    SNDTURRET_ELECTRIC_ROTATE_TAN  = 2;
    public static final int    SNDTURRET_HYDRAULIC_ROTATE_YAW = 3;
    public static final int    SNDTURRET_HYDRAULIC_ROTATE_TAN = 4;
    public static final int    SNDTURRET_COUNT                = 5;
    private static SoundPreset turretSfxPreset                = null;
    private SoundFX            turretSounds[];
    private Turret             turretSfx;
    private Point3d            turretSfxPos;
    protected int              magazines;
    protected String           spareMagName;
    protected boolean          bHandleMoved;
    private int                ammoLeft;
    private int                prevAmmoLeft;
    protected float            magRotate;
    protected int              gunLeverMoveAxis;
    protected Hook             cameraHook;
    protected boolean          bGunFire;
    protected BulletEmitter    emitter;
    private Orient             _tmpOrient;
    private HookGunner         hookGunner;
    private boolean            bNetMirror;
    protected float            normZN;
    protected float            gsZN;
    protected int              _aiTuretNum;
    protected int              _weaponControlNum;
    private float              prevAzi;
    private float              prevTan;
    private boolean            playTurretSfxAzi;
    private boolean            playTurretSfxTan;
    public boolean             playElectricTurretSounds;
    public boolean             playHydraulicTurretSounds;
    private static Loc         _tmpLoc1                       = new Loc();
    private static Loc         _tmpLoc2                       = new Loc();
    private static Point3d     _tmpP1                         = new Point3d();
    private static Point3d     _tmpP2                         = new Point3d();
    private static Vector3d    _tmpV1                         = new Vector3d();
    private static Shot        _tmpShot                       = new Shot();
    protected boolean          bReloading;

    static {
        Property.set(CockpitGunner.class, "aiTuretNum", 0);
        Property.set(CockpitGunner.class, "weaponControlNum", 10);
        Property.set(CockpitGunner.class, "astatePilotIndx", 1);
    }
}
