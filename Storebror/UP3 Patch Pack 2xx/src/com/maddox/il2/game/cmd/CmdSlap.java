/*
 * New "slap" command by SAS~Storebror
 */

package com.maddox.il2.game.cmd;

import com.maddox.il2.game.Main;
import com.maddox.il2.net.NetUser;
import com.maddox.rts.*;
import java.util.*;

public class CmdSlap extends Cmd {

    public Object exec(CmdEnv cmdenv, Map map) {
        if (Main.cur().netServerParams == null)
            return null;
        ArrayList arraylist = new ArrayList();
        fillUsers(map, arraylist);
        for (int i = 0; i < arraylist.size(); i++) {
            NetUser netuser = (NetUser) arraylist.get(i);
            if (!netuser.isMaster())
                ((NetUser) NetEnv.host()).slap(netuser);
        }

        return CmdEnv.RETURN_OK;
    }

    private void fillUsers(Map map, List list) {
        ArrayList arraylist = new ArrayList();
        HashMap hashmap = new HashMap();
        if (map.containsKey("_$$")) {
            int i = nargs(map, "_$$");
            for (int j = 0; j < i; j++) {
                String s = arg(map, "_$$", j);
                for (int k = 0; k < NetEnv.hosts().size(); k++) {
                    NetUser netuser = (NetUser) NetEnv.hosts().get(k);
                    String s1 = netuser.uniqueName();
                    if (s.equals(s1)) {
                        if (hashmap.containsKey(netuser))
                            break;
                        hashmap.put(netuser, null);
                        arraylist.add(netuser);
                        break;
                    }
                }
            }

        }
        list.addAll(arraylist);
    }

    public CmdSlap() {
        _properties.put("NAME", "slap");
        _levelAccess = 1;
    }
}
