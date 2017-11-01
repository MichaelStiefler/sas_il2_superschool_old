package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.effects.MiscEffects;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class RocketFlareBall extends Rocket {

    public boolean interpolateStep() {
        // Randomize Light Emission ("Flicker" Flare), take current Flare Size into account.
        float randomFactor = World.rnd().nextFloat();
        float timeFactor = 1.0F - Math.abs(((float) (Time.current() - this.timeStart) / (float) (RADIUS_PERIOD / 2)) - 1.0F);
        if (timeFactor < 0F) {
            timeFactor = 0F;
        }
        float endFactor = MiscEffects.cvt((this.timeStart + this.timeLife) - Time.current(), 0F, 1000F, 0F, 1F);
        Eff3DActor.setIntesity(this.eff3dactor, ((randomFactor * 4.75F) + 0.25F) * endFactor);
        if (this.lightpointactor != null) {
            this.lightPointIntensity = (randomFactor + 0.5F) * timeFactor * endFactor;
            this.lightpointactor.light.setEmit(this.lightPointIntensity, this.lightPointRange);
        }
//        this.light.light.setEmit(this.lightPointIntensity, this.lightPointRange);
        this.getSpeed(this.v);
        if (this.v.z < -15D) {
            this.v.z *= 0.9D;
            this.setSpeed(this.v);
        }

        // Relocate Flare Sound emitter position
        if ((this.draw != null) && (this.draw.sounds() != null)) {
            SoundFX soundfx = this.draw.sounds().get();
            soundfx.setPosition(this.pos.getAbsPoint());
        }
        return super.interpolateStep();
    }

    public RocketFlareBall(Actor actor, int i, Point3d point3d, Orient orient, float f) {
        this(actor, point3d, orient);
    }

    public RocketFlareBall(Actor actor, Point3d point3d, Orient orient) {
        this.speed = new Vector3d();
        this.pos.setAbs(point3d, orient);
        this.pos.reset();
        this.pos.setBase(actor, (Hook) null, true);
        this.collide(false);
        this.drawing(false);
        this.eff3dactor = null;
        this.lightpointactor = null;
        this.lightPointIntensity = 0F;
        this.lightPointRange = 0F;
    }

    public void start(float f, int i) {
//        System.out.println("RocketFlareBall start(" + f + ", " + i + ")");
        super.start(f, i);
        this.speed.normalize();
        this.speed.scale(110D);
        this.drawing(false);
        String flareEffect = Property.stringValue(this.getClass(), "effect");
        System.out.println("RocketFlareBall flareEffect=" + flareEffect);
        this.eff3dactor = Eff3DActor.New(this, null, new Loc(), 1.0F, flareEffect, f);
//        this.eff3dactor = Eff3DActor.New(this, null, new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareBall.eff", f);
        this.timeStart = Time.current();
        this.timeLife = (long) (f * 1000F);

        // Create Light Emission according to current darkness level
        if (World.Sun().ToSun.z < 0.1F) {
            this.lightPointIntensity = 0.5F + World.rnd().nextFloat();
            this.lightPointRange = MiscEffects.cvt(World.Sun().ToSun.z, -0.3F, 0.1F, 1000F, 100F);
            this.lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            Color3f lightColor = (Color3f)Property.value(this.getClass(), "lightColor");
            System.out.println("RocketFlareBall Color=" + lightColor.x + " : " + lightColor.y + " : " + lightColor.z);
            this.lightpointactor.light.setColor(lightColor);
//            this.lightpointactor.light.setColor(1.0F, 0.9F, 0.6F);
            this.lightpointactor.light.setEmit(this.lightPointIntensity, this.lightPointRange);
            ((Actor) (this.eff3dactor)).draw.lightMap().put("light", this.lightpointactor);
        }

        // Shoot Flare Gun, play sound
        SoundFX soundfx = this.newSound("weapon.rocketflare", true);
        if (soundfx.isInitialized()) {
            soundfx.setAcoustics(this.acoustics);
            if ((this.draw != null) && (this.draw.sounds() != null)) {
                soundfx.insert(this.draw.sounds(), true);
            }
            soundfx.play(this.pos.getAbsPoint());
//            System.out.println("soundfx=" + soundfx.hashCode());
        }
    }

    protected void doExplosion(Actor actor, String s) {
        this.destroy();
    }

    protected void doExplosionAir() {
    }

    public void destroy() {
//        System.out.println("RocketFlareBall destroy()");

        // Turn off flare illumination if there is any
        if (this.lightpointactor != null) {
            this.lightpointactor.destroy();
        }

        // Stop Flare Sound
        this.stopSounds();

        super.destroy();
    }

    private Vector3d          v             = new Vector3d();
    private Eff3DActor        eff3dactor;
    private LightPointActor   lightpointactor;
    private float             lightPointIntensity;
    private float             lightPointRange;
    private long              timeStart;
    private static final long RADIUS_PERIOD = 25000L;
    
    static void initCommon(Class theClass) {
        Property.set(theClass, "smoke", "3do/effects/rocket/RocketSmokeWhiteTile.eff");
        Property.set(theClass, "sprite", (Object) null);
        Property.set(theClass, "flame", (Object) null);
        Property.set(theClass, "emitLen", 1000.0F);
        Property.set(theClass, "emitMax", 1.0F);
        Property.set(theClass, "sound", (Object) null); // Don't set the sound preset here, it won't work.
        Property.set(theClass, "radius", 0.1F);
        Property.set(theClass, "timeLife", 40F);
        Property.set(theClass, "timeFire", 10F);
        Property.set(theClass, "force", 0.0F);
        Property.set(theClass, "power", 0.01485F);
        Property.set(theClass, "powerType", 0);
        Property.set(theClass, "kalibr", 0.001F);
        Property.set(theClass, "massa", 3F);
        Property.set(theClass, "massaEnd", 0.1F);
    }

    static {
        Class theClass = RocketFlareBall.class;
        initCommon(theClass);
        Property.set(theClass, "mesh", "3DO/Arms/RocketFlareBall/mono.sim");
        Property.set(theClass, "effect", "3DO/Effects/Fireworks/FlareBallWhite.eff");
        Property.set(theClass, "lightColor", new Color3f(1.0F, 0.9F, 0.6F));
        Property.set(theClass, "emitColor", new Color3f(0.8F, 0.8F, 1.0F));
    }
}
