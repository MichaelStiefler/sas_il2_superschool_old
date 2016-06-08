/*
 * UP3 RC4 Class with optional new 4.11+ TrackIR implementation added
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.rts;

import com.maddox.il2.engine.Config;

public final class TrackIRWin
    implements MsgTimeOutListener
{

    public final boolean isCreated()
    {
        return bCreated;
    }

    public final void create()
        throws MouseWinException
    {
        if(bCreated)
            return;
        if(RTSConf.cur.mainWindow.hWnd() == 0)
            throw new RuntimeException("TrackIR windows driver: main window not present");
        
        // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
        boolean createSuccess = false;
        if (useNewTrackIR)
            createSuccess = nCreateDT(RTSConf.cur.mainWindow.hWnd(), ProgramID);
        else
            nCreate(ProgramID);
        // if(!nCreate(ProgramID))
        if(!createSuccess)
        // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---
        {
            return;
        } else
        {
            ticker.post();
            bCreated = true;
            return;
        }
    }

    public final void destroy()
    {
        if(bCreated)
        {
         // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
            if (useNewTrackIR)
                nDestroyDT();
            else
                nDestroy();
            // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---
            RTSConf.cur.queueRealTime.remove(ticker);
            RTSConf.cur.queueRealTimeNextTick.remove(ticker);
            bCreated = false;
        }
    }

    public void msgTimeOut(Object obj)
    {
        if(bCreated)
        {
            // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
            
            // remove old bullshit code first
            
            // if(nGetAngles(angles))   -- lol?
            //     nGetAngles(angles);     ^^^
            // RTSConf.cur.trackIR.setAngles(angles[0], angles[1], angles[2], angles[3], angles[4], angles[5]); // unconditionally set angles? lol?
            
            if (useNewTrackIR) {
                if(nGetAnglesDT(nDTangles))
                    RTSConf.cur.trackIR.setAngles(angles[0], angles[1], angles[2], -angles[5], angles[3], angles[4]);
            } else {
                if(nGetAngles(angles))
                    RTSConf.cur.trackIR.setAngles(angles[0], angles[1], angles[2], angles[3], angles[4], angles[5]);
            }
           
            // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---
            ticker.post();
        }
    }

    protected TrackIRWin(int i, boolean flag)
    {
        // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
        
        // remove old bullshit code first
        // angles = new float[7]; ahh... what? re-instanciate a static member? lol?
        
        if (useNewTrackIR) nDTangles = new float[6];
        // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---
        bCreated = false;
        ticker = new MsgTimeOut(null);
        ticker.setTickPos(i);
        ticker.setNotCleanAfterSend();
        ticker.setFlags(88);
        ticker.setListener(this);
        if (Config.cur != null && Config.cur.ini != null) {
            useNewTrackIR = (Config.cur.ini.get("Mods", "NewTrackIR", 0) != 0);
            if (useNewTrackIR)
                System.out.println("  *** Using New 4.11+ style TrackIR from DT.dll *** ");
            else
                System.out.println("  *** Using old 4.10 style TrackIR from il2fb.exe *** ");
        }
        if(flag)
            create();
    }

    private static final native boolean nCreate(int i);

    private static final native void nDestroy();

    private static final native boolean nGetAngles(float af[]);

    private static int ProgramID = 1001;
    private boolean bCreated;
    private static float angles[] = new float[7];
    private MsgTimeOut ticker;
    
    // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
    private static final native boolean nCreateDT(int i, int j);

    private static final native void nDestroyDT();

    private static final native boolean nGetAnglesDT(float af[]);

    private static final native int nDTVersion();

    private static boolean useNewTrackIR = false;

    private static boolean libLoaded = false;

    private float nDTangles[];

    public static final void loadNative()
    {
        if(!libLoaded)
        {
            System.loadLibrary("DT");
            int i = nDTVersion();
            System.out.println("  *** Library **** DT ****  Loaded *** " + i);
            libLoaded = true;
        }
    }
    // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---

    static 
    {
        RTS.loadNative();
        loadNative();
    }
}
