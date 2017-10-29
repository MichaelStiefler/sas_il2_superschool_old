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

public class RocketFlareBall extends Rocket
{

    public boolean interpolateStep()
    {
        // Randomize Light Emission ("Flicker" Flare), take current Flare Size into account.
        float randomFactor = World.rnd().nextFloat();
        float timeFactor = 1.0F - Math.abs((float)(Time.current() - this.timeStart) / (float)(RADIUS_PERIOD / 2) - 1.0F);
        if (timeFactor < 0F) timeFactor = 0F;
        float endFactor = MiscEffects.cvt((float)(this.timeStart + this.timeLife - Time.current()), 0F, 1000F, 0F, 1F);
        Eff3DActor.setIntesity(eff3dactor, (randomFactor * 4.75F + 0.25F) * endFactor);
        if (this.lightpointactor != null) {
            lightPointIntensity = (randomFactor + 0.5F) * timeFactor * endFactor;
            this.lightpointactor.light.setEmit(lightPointIntensity, lightPointRange);
        }
        this.getSpeed(this.v);
        if (this.v.z < -15D) {
            this.v.z *= 0.9D;
            this.setSpeed(this.v);
        }
        
        // Relocate Flare Sound emitter position
        if (draw != null && draw.sounds() != null) {
            SoundFX soundfx = draw.sounds().get();
            soundfx.setPosition(this.pos.getAbsPoint());
        }
        return super.interpolateStep();
    }

    public RocketFlareBall(Actor actor, int i, Point3d point3d, Orient orient, float f)
    {
        this(actor, point3d, orient);
    }

    public RocketFlareBall(Actor actor, Point3d point3d, Orient orient)
    {
        speed = new Vector3d();
        pos.setAbs(point3d, orient);
        pos.reset();
        pos.setBase(actor, (Hook)null, true);
        collide(false);
        drawing(false);
        eff3dactor = null;
        lightpointactor = null;
        lightPointIntensity = 0F;
        lightPointRange = 0F;
    }

    public void start(float f, int i)
    {
//        System.out.println("RocketFlareBall start(" + f + ", " + i + ")");
        super.start(f, i);
        speed.normalize();
        speed.scale(110D);
        drawing(false);
        eff3dactor = Eff3DActor.New(this, null, new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareBall.eff", f);
        this.timeStart = Time.current();
        this.timeLife = (long)(f*1000F);
        
        // Create Light Emission according to current darkness level
        if (World.Sun().ToSun.z < 0.1F) {
            lightPointIntensity = 0.5F + World.rnd().nextFloat();
            lightPointRange = MiscEffects.cvt(World.Sun().ToSun.z, -0.3F, 0.1F, 1000F, 100F);
            lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.9F, 0.6F);
            lightpointactor.light.setEmit(lightPointIntensity, lightPointRange);
            ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
        }
        
        // Shoot Flare Gun, play sound
        SoundFX soundfx = newSound("weapon.rocketflare", true);
        if(soundfx.isInitialized()) {
            soundfx.setAcoustics(acoustics);
            if (draw != null && draw.sounds() != null)
                soundfx.insert(draw.sounds(), true);
            soundfx.play(this.pos.getAbsPoint());
//            System.out.println("soundfx=" + soundfx.hashCode());
        }
    }

    protected void doExplosion(Actor actor, String s)
    {
        destroy();
    }

    protected void doExplosionAir()
    {
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

    private Vector3d v = new Vector3d();
    private Eff3DActor eff3dactor;
    private LightPointActor lightpointactor;
    private float lightPointIntensity;
    private float lightPointRange;
    private long timeStart;
    private static final long RADIUS_PERIOD = 25000L;

    static 
    {
        Class class1 = RocketFlareBall.class;
        Property.set(class1, "mesh", "3DO/Arms/RocketFlareBall/mono.sim");
        Property.set(class1, "sprite", (Object)null);
        Property.set(class1, "flame", (Object)null);
        Property.set(class1, "smoke", "3do/effects/rocket/RocketSmokeWhiteTile.eff");
        Property.set(class1, "emitColor", new Color3f(0.8F, 0.8F, 1.0F));
        Property.set(class1, "emitLen", 0.0F);
        Property.set(class1, "emitMax", 0.0F);
        Property.set(class1, "sound", (Object)null); // Don't set the sound preset here, it won't work.
        Property.set(class1, "radius", 0.1F);
        Property.set(class1, "timeLife", 40F);
        Property.set(class1, "timeFire", 10F);
        Property.set(class1, "force", 0.0F);
        Property.set(class1, "power", 0.01485F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.001F);
        Property.set(class1, "massa", 3F);
        Property.set(class1, "massaEnd", 0.1F);
    }
}
