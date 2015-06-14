/* 4.10.1 class + TRACK IR mod */
package com.maddox.il2.engine.hotkey;

import java.io.BufferedReader;
import java.io.PrintWriter;
import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.HookRender;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.VisibilityChecker;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.MOSQUITO;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.effects.SpritesFog;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.Keyboard;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Time;
import com.maddox.rts.TrackIR;

public class HookPilot extends HookRender {
    private static final float[] af           = new float[7];
    private boolean              sight        = false;
    private boolean              usezoom;
    private boolean              usezoomc6;
    private boolean              mTypeBomber;
    private float                gfactor;
    private float                tfactor;
    private float                boh;
    private float                stepAzimut;
    private float                stepTangage;
    private float                maxAzimut;
    private float                maxTangage;
    private float                minTangage;
    private float                Azimut;
    private float                Tangage;
    private float                _Azimut;
    private float                _Tangage;
    private long                 rprevTime;
    private float                Px;
    private float                Py;
    private float                Pz;
    private float                azimPadlock;
    private float                tangPadlock;
    private float                timeKoof;
    private Orient               o;
    private Orient               op;
    private Loc                  le;
    private Point3d              pe;
    private Point3d              pEnemyAbs;
    private Vector3d             Ve;
    private Point3d              pAbs;
    private Orient               oAbs;
    private Actor                target;
    private Actor                target2;
    private Actor                enemy;
    private long                 stamp;
    private long                 prevTime;
    private long                 roolTime;
    private boolean              bUse;
    private boolean              bPadlock;
    private long                 tPadlockEnd;
    private long                 tPadlockEndLen;
    private boolean              bPadlockEnd;
    private boolean              bForward;
    private boolean              bAim;
    private boolean              bUp;
    private boolean              bVisibleEnemy;
    private boolean              bSimpleUse;
    private Point3d              pCenter;
    private Point3d              pAim;
    private Point3d              pUp;
    private static RangeRandom   rnd          = new RangeRandom();
    private static Point3d       P            = new Point3d();
    private static Vector3d      tmpA         = new Vector3d();
    private static Vector3d      tmpB         = new Vector3d();
    private static Vector3d      headShift    = new Vector3d();
    private static Vector3d      counterForce = new Vector3d();
    private static long          oldHeadTime  = 0L;
    private static double        oldWx        = 0.0;
    private static double        oldWy        = 0.0;
    private boolean              bUseMouse;
    private long                 timeViewSet;
    public static HookPilot      current;
    private static Orient        oTmp         = new Orient();
    private static Orient        oTmp2        = new Orient();
    private static float         shakeLVL;

    public void resetGame() {
        enemy = null;
        bUp = false;
    }

    private Point3d pCamera() {
        if (bAim)
            return pAim;
        if (bUp)
            return pUp;
        return pCenter;
    }

    public boolean isAim() {
        return bAim;
    }

    public void doAim(boolean bool) {
        sight = bool;
        if (bAim != bool)
            bAim = bool;
    }

    public void setSimpleUse(boolean bool) {
        bSimpleUse = bool;
    }

    public void setSimpleAimOrient(float f, float f_0_, float f_1_) {
        o.set(f, f_0_, f_1_);
        le.set(pCamera(), o);
    }

    public void setCenter(Point3d point3d) {
        pCenter.set(point3d);
        if (World.getPlayerAircraft() instanceof TypeBomber || World.getPlayerAircraft() instanceof MOSQUITO)
            mTypeBomber = true;
        else
            mTypeBomber = false;
    }

    public void setAim(Point3d point3d) {
        pAim.set(pCenter);
        if (point3d != null)
            pAim.set(point3d);
        setUp(point3d);
    }

    public void setUp(Point3d point3d) {
        pUp.set(pCenter);
        if (point3d != null)
            pUp.set(point3d);
    }

    public void setSteps(float f, float f_2_) {
        stepAzimut = f;
        stepTangage = f_2_;
    }

    public void setForward(boolean bool) {
        bForward = bool;
    }

    public void endPadlock() {
        bPadlockEnd = true;
    }

    private void _reset() {
        if (!AircraftHotKeys.bFirstHotCmd) {
            _Azimut = Azimut = 0.0F;
            _Tangage = Tangage = 0.0F;
            o.set(0.0F, 0.0F, 0.0F);
            le.set(pCamera(), o);
        }
        Px = Py = Pz = 0.0F;
        azimPadlock = 0.0F;
        tangPadlock = 0.0F;
        timeKoof = 1.0F;
        prevTime = -1L;
        roolTime = -1L;
        enemy = null;
        bPadlock = false;
        tPadlockEnd = -1L;
        tPadlockEndLen = 0L;
        bPadlockEnd = false;
        bForward = false;
        if (!Main3D.cur3D().isDemoPlaying())
            new MsgAction(64, 0.0) {
                public void doAction() {
                    HotKeyCmd.exec("misc", "target_");
                }
            };
        timeViewSet = -2000L;
        headShift.set(0.0, 0.0, 0.0);
        counterForce.set(0.0, 0.0, 0.0);
        oldHeadTime = -1L;
        oldWx = 0.0;
        oldWy = 0.0;
    }

    public void saveRecordedStates(PrintWriter printwriter) throws Exception {
        printwriter.println(Azimut);
        printwriter.println(_Azimut);
        printwriter.println(Tangage);
        printwriter.println(_Tangage);
        printwriter.println(o.azimut());
        printwriter.println(o.tangage());
    }

    public void loadRecordedStates(BufferedReader bufferedreader) throws Exception {
        Azimut = Float.parseFloat(bufferedreader.readLine());
        _Azimut = Float.parseFloat(bufferedreader.readLine());
        Tangage = Float.parseFloat(bufferedreader.readLine());
        _Tangage = Float.parseFloat(bufferedreader.readLine());
        o.set(Float.parseFloat(bufferedreader.readLine()), Float.parseFloat(bufferedreader.readLine()), 0.0F);
        le.set(pCamera(), o);
    }

    public void reset() {
        stamp = -1L;
        _reset();
    }

    private void setTimeKoof() {
        long l = Time.current();
        if (prevTime == -1L)
            timeKoof = 1.0F;
        else
            timeKoof = (float) (l - prevTime) / 30.0F;
        prevTime = l;
    }

    private void headRoll(Aircraft aircraft) {
        if (Keyboard.adapter().isPressed(106))
            M_showMenu6DOF();
        long l = roolTime - stamp;
        if (l < 0L || l >= 50L) {
            roolTime = stamp;
            // TODO: Modified by |ZUTI|: figure out AI shake level!
            // --------------------------------------------------------
            try {
                shakeLVL = ((RealFlightModel) aircraft.FM).shakeLevel;
            } catch (Exception ex) {
                shakeLVL = 0.0F;
            }
            // --------------------------------------------------------
            TrackIR.adapter().getAngles(af);
            float f;
            float f_4_;
            float f_5_;
            if (!sight) {
                f_4_ = (!mTypeBomber ? af[3] * 11.0F * 0.0090F / 180.0F : af[3] * 19.0F * 0.0090F / 180.0F);
                if (af[4] < 0.0F)
                    f_5_ = af[4] * 12.0F * 0.0090F / 180.0F;
                else
                    f_5_ = af[4] * 7.0F * 0.0090F / 180.0F;
                if (af[5] < 0.0F) {
                    if (af[5] < -90.0F) {
                        f = 0.24299999F;
                        if (usezoom) {
                            if (!usezoomc6) {
                                if (Main3D.FOVX != 30.0F && !(af[0] < -120.0F) && !(af[0] > 120.0F)) {
                                    float f_6_ = (70.0F + 40.0F * (af[5] + 90.0F) / 90.0F);
                                    if (f_6_ <= 30.0F)
                                        f_6_ = 29.99999F;
                                    CmdEnv.top().exec("fov " + f_6_);
                                }
                            } else if (Main3D.FOVX != 30.0F) {
                                if (!(af[0] < -120.0F) && !(af[0] > 120.0F)) {
                                    float f_7_ = (70.0F + 40.0F * (af[5] + 90.0F) / 90.0F);
                                    if (f_7_ <= 30.0F)
                                        f_7_ = 29.99999F;
                                    CmdEnv.top().exec("fov " + f_7_);
                                } else {
                                    float f_8_ = (70.0F + 20.0F * (-af[5] - 90.0F) / 90.0F);
                                    if (f_8_ >= 90.0F)
                                        f_8_ = 89.99999F;
                                    CmdEnv.top().exec("fov " + f_8_);
                                }
                            }
                        }
                    } else
                        f = -(af[5] * 27.0F * 0.0090F) / 90.0F;
                } else if (af[5] > 90.0F) {
                    f = -0.053999998F;
                    if (Main3D.FOVX != 90.0F && usezoom) {
                        float f_9_ = 70.0F + 20.0F * (af[5] - 90.0F) / 90.0F;
                        if (f_9_ >= 90.0F)
                            f_9_ = 89.99999F;
                        CmdEnv.top().exec("fov " + f_9_);
                    }
                } else
                    f = -(af[5] * 6.0F * 0.0090F) / 90.0F;
            } else
                f = f_4_ = f_5_ = 0.0F;
            if (World.cur().diffCur.Head_Shake) {
                long l_10_ = Time.current();
                if (oldHeadTime == -1L) {
                    oldHeadTime = Time.current();
                    oldWx = aircraft.FM.getW().x;
                    oldWy = aircraft.FM.getW().y;
                }
                long l_11_ = l_10_ - oldHeadTime;
                oldHeadTime = l_10_;
                if (l_11_ > 200L)
                    l_11_ = 200L;
                double d = 0.0030 * (double) l_11_;
                double d_12_ = aircraft.FM.getW().x - oldWx;
                double d_13_ = aircraft.FM.getW().y - oldWy;
                oldWx = aircraft.FM.getW().x;
                if (d < 0.0010)
                    d_12_ = 0.0;
                else
                    d_12_ /= d;
                oldWy = aircraft.FM.getW().y;
                if (d < 0.0010)
                    d_13_ = 0.0;
                else
                    d_13_ /= d;
                if (aircraft.FM.Gears.onGround()) {
                    tmpA.set(0.0, 0.0, 0.0);
                    headShift.scale(1.0 - d);
                    tmpA.scale(d);
                    headShift.add(tmpA);
                    f_4_ += (float) headShift.y * 0.0090F;
                    f += ((float) (headShift.x + (double) (0.03F * shakeLVL * (0.5F - rnd.nextFloat()))) * 0.0090F);
                    f_5_ += ((float) (headShift.z + (double) (1.2F * shakeLVL * (0.5F - rnd.nextFloat()))) * 0.0090F);
                } else {
                    tmpB.set(0.0, 0.0, 0.0);
                    tmpA.set(aircraft.FM.getAccel());
                    aircraft.FM.Or.transformInv(tmpA);
                    tmpA.scale(-0.6);
                    if (tmpA.z > 0.0)
                        tmpA.z *= 0.8;
                    tmpB.add(tmpA);
                    counterForce.scale(1.0 - 0.2 * d);
                    tmpA.scale(0.2 * d);
                    counterForce.add(tmpA);
                    tmpB.sub(counterForce);
                    counterForce.scale(1.0 - 0.05 * d);
                    if (counterForce.z > 0.0)
                        counterForce.z *= 1.0 - 0.08 * d;
                    tmpB.scale(0.08);
                    tmpA.set(-0.7 * d_13_, d_12_, 0.0);
                    tmpA.add(tmpB);
                    headShift.scale(1.0 - d);
                    tmpA.scale(d);
                    headShift.add(tmpA);
                    f_4_ += (float) headShift.y * 0.0090F * gfactor;
                    f += ((float) (headShift.x + (double) (0.3F * shakeLVL * (0.5F - rnd.nextFloat()))) * 0.0090F * gfactor);
                    f_5_ += ((float) (headShift.z + (double) (0.4F * shakeLVL * (0.5F - rnd.nextFloat()))) * 0.0090F * gfactor);
                }
            } else {
                f_4_ += 0.0F;
                f += 0.0F;
                f_5_ += 0.0F;
            }
            if (World.cur().diffCur.Wind_N_Turbulence) {
                float f_14_ = SpritesFog.dynamicFogAlpha;
                double d = aircraft.pos.getAbsPoint().z;
                if (f_14_ > 0.01F && d > 300.0 && d < 2500.0) {
                    float f_15_ = aircraft.FM.getSpeed();
                    if (f_15_ > 138.8889F)
                        f_15_ = 138.8889F;
                    f_15_ -= 55.55556F;
                    if (f_15_ < 0.0F)
                        f_15_ = 0.0F;
                    f_15_ /= 83.33334F;
                    f_4_ += (f_15_ * 0.05F * f_14_ * (0.5F - rnd.nextFloat()) * 0.0090F * tfactor);
                    f_5_ += (f_15_ * 0.3F * f_14_ * (0.5F - rnd.nextFloat()) * 0.0090F * tfactor);
                }
            }
            P.set((double) (Px += f - Px), (double) (Py += f_4_ - Py), (double) (Pz += f_5_ - Pz));
            oTmp.set((float) P.y, (float) P.z, (float) P.z);
            oTmp.increment(0.31F * rnd.nextFloat(-shakeLVL, shakeLVL), 0.31F * rnd.nextFloat(-shakeLVL, shakeLVL), 0.54F * rnd.nextFloat(-shakeLVL, shakeLVL));
        }
    }

    public void setMinMax(float f, float f_16_, float f_17_) {
        maxAzimut = 150.0F;
        minTangage = f_16_;
        maxTangage = 180.0F;
    }

    private void M_showMenu6DOF() {
        if (Keyboard.adapter().isPressed(106)) {
            TextScr.setFont("arial");
            if (boh >= 0.0F) {
                TextScr.setColor(new Color4f(boh, 0.0F, 0.0F, 1.0F));
                if (boh < 0.99F)
                    boh += 0.01F;
                else
                    boh = -0.98F;
            } else {
                TextScr.setColor(new Color4f(-boh, 0.0F, 0.0F, 1.0F));
                if (boh > -0.99F)
                    boh += 0.01F;
                else
                    boh = 0.98F;
            }
            TextScr.output(75, 275, "6DOF");
            TextScr.setColor(new Color4f(1.0F, 0.0F, 0.0F, 1.0F));
            TextScr.output(75, 225, "KP 1. Inverse Zoom on Check 6");
            TextScr.output(75, 250, "KP 0. Zoom View");
            TextScr.output(75, 200, "KP 2. Increase   G Force Factor");
            TextScr.output(75, 175, "KP 3. Decrease G Force Factor");
            TextScr.output(75, 150, "KP 4. Increase   Turbulence Factor");
            TextScr.output(75, 125, "KP 5. Decrease Turbulence Factor");
            if (boh >= 0.0F)
                TextScr.setColor(new Color4f(1.0F - boh, 1.0F - boh, 1.0F, 1.0F));
            else
                TextScr.setColor(new Color4f(1.0F + boh, 1.0F + boh, 1.0F, 1.0F));
            TextScr.output(380, 250, usezoom ? "On" : "Off");
            TextScr.output(380, 225, usezoomc6 ? "On" : "Off");
            TextScr.output(380, 187, gfactor + " ");
            TextScr.output(380, 137, tfactor + " ");
            if (Keyboard.adapter().isPressed(96)) {
                usezoom = !usezoom;
                if (!usezoom)
                    usezoomc6 = false;
                Keyboard.adapter().setRelease(Time.currentReal(), 96);
            } else if (Keyboard.adapter().isPressed(97)) {
                if (usezoom) {
                    usezoomc6 = !usezoomc6;
                    Keyboard.adapter().setRelease(Time.currentReal(), 97);
                }
            } else if (Keyboard.adapter().isPressed(98)) {
                if (gfactor < 9.0F) {
                    gfactor += 0.1F;
                    gfactor *= 10.0F;
                    gfactor = (float) Math.round(gfactor);
                    gfactor /= 10.0F;
                }
            } else if (Keyboard.adapter().isPressed(99)) {
                if (gfactor > 1.0F) {
                    gfactor -= 0.1F;
                    gfactor *= 10.0F;
                    gfactor = (float) Math.round(gfactor);
                    gfactor /= 10.0F;
                }
            } else if (Keyboard.adapter().isPressed(100)) {
                if (tfactor < 9.0F) {
                    tfactor += 0.1F;
                    tfactor *= 10.0F;
                    tfactor = (float) Math.round(tfactor);
                    tfactor /= 10.0F;
                }
            } else if (Keyboard.adapter().isPressed(101) && tfactor > 1.0F) {
                tfactor -= 0.1F;
                tfactor *= 10.0F;
                tfactor = (float) Math.round(tfactor);
                tfactor /= 10.0F;
            }
        }
    }

    public boolean isPadlock() {
        return bPadlock;
    }

    public Actor getEnemy() {
        return enemy;
    }

    public void stopPadlock() {
        if (bPadlock) {
            stamp = -1L;
            _reset();
        }
    }

    public boolean startPadlock(Actor actor) {
        if (!bUse || bSimpleUse)
            return false;
        if (!Actor.isValid(actor)) {
            bPadlock = false;
            return false;
        }
        Aircraft aircraft = World.getPlayerAircraft();
        if (!Actor.isValid(aircraft)) {
            bPadlock = false;
            return false;
        }
        enemy = actor;
        Azimut = _Azimut;
        Tangage = _Tangage;
        bPadlock = true;
        bPadlockEnd = false;
        bVisibleEnemy = true;
        aircraft.pos.getAbs(pAbs, oAbs);
        Camera3D camera3d = (Camera3D) target2;
        camera3d.pos.getAbs(o);
        o.sub(oAbs);
        azimPadlock = o.getAzimut();
        tangPadlock = o.getTangage();
        azimPadlock = (azimPadlock + 3600.0F) % 360.0F;
        if (azimPadlock > 180.0F)
            azimPadlock -= 360.0F;
        stamp = -1L;
        if (!Main3D.cur3D().isDemoPlaying())
            new MsgAction(64, 0.0) {
                public void doAction() {
                    HotKeyCmd.exec("misc", "target_");
                }
            };
        return true;
    }

    public boolean isUp() {
        return bUp;
    }

    public void doUp(boolean bool) {
        if (bUp != bool)
            bUp = bool;
    }

    private float bvalue(float f, float f_19_, long l) {
        float f_20_ = HookView.koofSpeed * (float) l / 30.0F;
        if (f == f_19_)
            return f;
        if (f > f_19_) {
            if (f < f_19_ + f_20_)
                return f;
            return f_19_ + f_20_;
        }
        if (f > f_19_ - f_20_)
            return f;
        return f_19_ - f_20_;
    }

    public boolean computeRenderPos(Actor actor, Loc loc, Loc loc_21_) {
        if (bUse) {
            if (bPadlock) {
                Aircraft aircraft = World.getPlayerAircraft();
                if (!Actor.isValid(aircraft)) {
                    reset();
                    loc_21_.add(le, loc);
                    return true;
                }
                long l = Time.current();
                if (l != stamp && enemy.pos != null && aircraft.pos != null) {
                    stamp = l;
                    setTimeKoof();
                    enemy.pos.getRender(pe);
                    pEnemyAbs.set(pe);
                    aircraft.pos.getRender(pAbs, oAbs);
                    Ve.sub(pe, pAbs);
                    o.setAT0(Ve);
                    headRoll(aircraft);
                    pe.add(pCamera(), P);
                    le.set(pe);
                    o.sub(oAbs);
                    padlockSet(o);
                    op.set(o);
                    op.add(oAbs);
                }
                loc_21_.add(le, loc);
                loc_21_.set(op);
                return true;
            }
            long l = Time.currentReal();
            if (l != rprevTime && !bSimpleUse) {
                long l_22_ = l - rprevTime;
                rprevTime = l;
                if (_Azimut != Azimut || _Tangage != Tangage) {
                    Azimut = bvalue(_Azimut, Azimut, l_22_);
                    Tangage = bvalue(_Tangage, Tangage, l_22_);
                    o.set(Azimut, Tangage, 0.0F);
                }
            }
            Aircraft aircraft = World.getPlayerAircraft();
            if (Actor.isValid(aircraft)) {
                long l_23_ = Time.current();
                if (l_23_ != stamp) {
                    stamp = l_23_;
                    headRoll(aircraft);
                }
            }
            pe.add(pCamera(), P);
            oTmp2.set(o);
            oTmp2.increment(oTmp);
            le.set(pe, oTmp2);
            loc_21_.add(le, loc);
        } else
            loc_21_.set(loc);
        return true;
    }

    public void computePos(Actor actor, Loc loc, Loc loc_24_) {
        if (bUse) {
            if (Time.isPaused() && !bPadlock) {
                if (World.cur().diffCur.Head_Shake) {
                    pe.add(pCamera(), P);
                    le.set(pe, o);
                } else
                    le.set(pCamera(), o);
                loc_24_.add(le, loc);
            } else {
                loc_24_.add(le, loc);
                if (bPadlock)
                    loc_24_.set(op);
            }
        } else
            loc_24_.set(loc);
    }

    private float avalue(float f, float f_25_) {
        if (f >= 0.0F) {
            if (f <= f_25_)
                return 0.0F;
            return f - f_25_;
        }
        if (f >= -f_25_)
            return 0.0F;
        return f + f_25_;
    }

    private float bvalue(float f, float f_26_) {
        float f_27_ = HookView.koofSpeed * 4.0F / 6.0F * timeKoof;
        if (f > f_26_) {
            if (f < f_26_ + f_27_)
                return f;
            return f_26_ + f_27_;
        }
        if (f > f_26_ - f_27_)
            return f;
        return f_26_ - f_27_;
    }

    private void padlockSet(Orient orient) {
        float f = orient.getAzimut();
        float f_28_ = orient.getTangage();
        if (bPadlockEnd || bForward) {
            f = f_28_ = 0.0F;
            tPadlockEnd = -1L;
        } else {
            Camera3D camera3d = (Camera3D) target2;
            float f_29_ = camera3d.FOV() * 0.3F;
            float f_30_ = f_29_ / camera3d.aspect();
            f = (f + 3600.0F) % 360.0F;
            if (f > 180.0F)
                f -= 360.0F;
            f = avalue(f, f_29_);
            f_28_ = avalue(f_28_, f_30_);
            boolean bool = false;
            if (f < -maxAzimut) {
                f = -maxAzimut;
                bool = true;
            }
            if (f > maxAzimut) {
                f = maxAzimut;
                bool = true;
            }
            if (f_28_ < minTangage) {
                f_28_ = minTangage;
                bool = true;
            }
            if (bool || !bVisibleEnemy || !Actor.isAlive(enemy)) {
                if (tPadlockEnd != -1L)
                    tPadlockEndLen += Time.current() - tPadlockEnd;
                tPadlockEnd = Time.current();
                if (tPadlockEndLen > 4000L) {
                    bPadlockEnd = true;
                    tPadlockEnd = -1L;
                    tPadlockEndLen = 0L;
                }
            } else {
                tPadlockEnd = -1L;
                tPadlockEndLen = 0L;
            }
        }
        f = bvalue(f, azimPadlock);
        f_28_ = bvalue(f_28_, tangPadlock);
        orient.set(f, f_28_, 0.0F);
        azimPadlock = f;
        tangPadlock = f_28_;
        if (bPadlockEnd && -1.0F < azimPadlock && azimPadlock < 1.0F && -1.0F < tangPadlock && tangPadlock < 1.0F) {
            stamp = -1L;
            _reset();
        }
    }

    public void checkPadlockState() {
        if (bPadlock && Actor.isAlive(enemy)) {
            VisibilityChecker.checkLandObstacle = true;
            VisibilityChecker.checkCabinObstacle = true;
            VisibilityChecker.checkPlaneObstacle = true;
            VisibilityChecker.checkObjObstacle = true;
            bVisibleEnemy = VisibilityChecker.computeVisibility(null, enemy) > 0.0F;
        }
    }

    public void setTarget(Actor actor) {
        target = actor;
    }

    public void setTarget2(Actor actor) {
        target2 = actor;
    }

    public boolean use(boolean bool) {
        boolean bool_31_ = bUse;
        bUse = bool;
        if (Actor.isValid(target))
            target.pos.inValidate(true);
        if (Actor.isValid(target2))
            target2.pos.inValidate(true);
        return bool_31_;
    }

    public boolean useMouse(boolean bool) {
        boolean bool_32_ = bUseMouse;
        bUseMouse = bool;
        return bool_32_;
    }

    public void mouseMove(int i, int i_33_, int i_34_) {
        if (bUse && !bPadlock && !bSimpleUse) {
            if (bUseMouse && Time.real() > timeViewSet + 1000L) {
                float f = (o.azimut() + (float) i * HookView.koofAzimut) % 360.0F;
                if (f > 180.0F)
                    f -= 360.0F;
                else if (f < -180.0F)
                    f += 360.0F;
                if (f < -maxAzimut) {
                    if (i <= 0)
                        f = -maxAzimut;
                    else
                        f = maxAzimut;
                } else if (f > maxAzimut) {
                    if (i >= 0)
                        f = maxAzimut;
                    else
                        f = -maxAzimut;
                }
                float f_35_ = ((o.tangage() + (float) i_33_ * HookView.koofTangage) % 360.0F);
                if (f_35_ > 180.0F)
                    f_35_ -= 360.0F;
                else if (f_35_ < -180.0F)
                    f_35_ += 360.0F;
                if (f_35_ < minTangage) {
                    if (i_33_ <= 0)
                        f_35_ = minTangage;
                    else
                        f_35_ = maxTangage;
                } else if (f_35_ > maxTangage) {
                    if (i_33_ >= 0)
                        f_35_ = maxTangage;
                    else
                        f_35_ = minTangage;
                }
                o.set(f, f_35_, 0.0F);
                if (Actor.isValid(target))
                    target.pos.inValidate(true);
                if (Actor.isValid(target2))
                    target2.pos.inValidate(true);
                Azimut = _Azimut;
                Tangage = _Tangage;
            }
        }
    }

    public void viewSet(float f, float f_36_) {
        if (bUse && !bPadlock && !bSimpleUse) {
            if (bUseMouse) {
                timeViewSet = Time.real();
                f %= 360.0F;
                if (f > 180.0F)
                    f -= 360.0F;
                else if (f < -180.0F)
                    f += 360.0F;
                f_36_ %= 360.0F;
                if (f_36_ > 180.0F)
                    f_36_ -= 360.0F;
                else if (f_36_ < -180.0F)
                    f_36_ += 360.0F;
                if (f < -maxAzimut)
                    f = -maxAzimut;
                else if (f > maxAzimut)
                    f = maxAzimut;
                if (f_36_ > maxTangage)
                    f_36_ = maxTangage;
                else if (f_36_ < minTangage)
                    f_36_ = minTangage;
                _Azimut = Azimut = f;
                _Tangage = Tangage = f_36_;
                if ((double) af[2] > 90.0)
                    af[2] = 90.0F;
                else if (af[2] < -90.0F)
                    af[2] = -90.0F;
                o.set(f, f_36_, -af[2]);
                if (Actor.isValid(target))
                    target.pos.inValidate(true);
                if (Actor.isValid(target2))
                    target2.pos.inValidate(true);
            }
        }
    }

    public void snapSet(float f, float f_37_) {
        if (bUse && !bPadlock && !bSimpleUse) {
            _Azimut = 45.0F * f;
            _Tangage = 44.0F * f_37_;
            Azimut = o.azimut() % 360.0F;
            if (Azimut > 180.0F)
                Azimut -= 360.0F;
            else if (Azimut < -180.0F)
                Azimut += 360.0F;
            Tangage = o.tangage() % 360.0F;
            if (Tangage > 180.0F)
                Tangage -= 360.0F;
            else if (Tangage < -180.0F)
                Tangage += 360.0F;
            if (Actor.isValid(target))
                target.pos.inValidate(true);
            if (Actor.isValid(target2))
                target2.pos.inValidate(true);
        }
    }

    public void panSet(int i, int i_38_) {
        if (bUse && !bPadlock && !bSimpleUse) {
            if (i == 0 && i_38_ == 0) {
                _Azimut = 0.0F;
                _Tangage = 0.0F;
            }
            if (_Azimut == -maxAzimut) {
                int i_39_ = (int) (_Azimut / stepAzimut);
                if (-_Azimut % stepAzimut > 0.01F * stepAzimut)
                    i_39_--;
                _Azimut = (float) i_39_ * stepAzimut;
            } else if (_Azimut == maxAzimut) {
                int i_40_ = (int) (_Azimut / stepAzimut);
                if (_Azimut % stepAzimut > 0.01F * stepAzimut)
                    i_40_++;
                _Azimut = (float) i_40_ * stepAzimut;
            }
            _Azimut = (float) i * stepAzimut + _Azimut;
            if (_Azimut < -maxAzimut)
                _Azimut = -maxAzimut;
            if (_Azimut > maxAzimut)
                _Azimut = maxAzimut;
            _Tangage = (float) i_38_ * stepTangage + _Tangage;
            if (_Tangage < minTangage)
                _Tangage = minTangage;
            if (_Tangage > maxTangage)
                _Tangage = maxTangage;
            Azimut = o.azimut() % 360.0F;
            if (Azimut > 180.0F)
                Azimut -= 360.0F;
            else if (Azimut < -180.0F)
                Azimut += 360.0F;
            Tangage = o.tangage() % 360.0F;
            if (Tangage > 180.0F)
                Tangage -= 360.0F;
            else if (Tangage < -180.0F)
                Tangage += 360.0F;
            if (Actor.isValid(target))
                target.pos.inValidate(true);
            if (Actor.isValid(target2))
                target2.pos.inValidate(true);
        }
    }

    private HookPilot() {
        usezoom = usezoomc6 = true;
        gfactor = tfactor = boh = 1.0F;
        stepAzimut = 45.0F;
        stepTangage = 30.0F;
        maxAzimut = 155.0F;
        maxTangage = 89.0F;
        minTangage = -60.0F;
        Azimut = 0.0F;
        Tangage = 0.0F;
        _Azimut = 0.0F;
        _Tangage = 0.0F;
        rprevTime = 0L;
        timeKoof = 1.0F;
        o = new Orient();
        op = new Orient();
        le = new Loc();
        pe = new Point3d();
        pEnemyAbs = new Point3d();
        Ve = new Vector3d();
        pAbs = new Point3d();
        oAbs = new Orient();
        target = null;
        target2 = null;
        enemy = null;
        stamp = -1L;
        prevTime = -1L;
        roolTime = -1L;
        bUse = false;
        bPadlock = true;
        tPadlockEnd = -1L;
        tPadlockEndLen = 0L;
        bPadlockEnd = false;
        bForward = false;
        bAim = false;
        bUp = false;
        bVisibleEnemy = true;
        bSimpleUse = false;
        pCenter = new Point3d();
        pAim = new Point3d();
        pUp = new Point3d();
        bUseMouse = true;
        timeViewSet = -2000L;
    }

    public static HookPilot New() {
        if (current == null)
            current = new HookPilot();
        return current;
    }

    public static HookPilot cur() {
        return New();
    }

}