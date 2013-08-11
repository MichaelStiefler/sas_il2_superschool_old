Installation Instructions:

Just extract to contents of this mod to the "#SAS" folder of your Modact 5.x game.

This mod gives you a total of up to 140 additional ordnance options (P-47D-47 late,
-47 and -22, for the P-47D-10 you get 16 additional ordnance options), including
droptanks of various sizes, corrected bomb loadouts (1000lb bombs on wings only,
center line is limited to 500lb bombs), Napalm tanks and Fragmentation bombs.

You will find the new ordnance options below the stock ones
(this is to avoid incompatibilities with stock maps, missions and campaigns).

***********************************************************************************

Credits:
- Original 3D & code by 1C/Maddox.
- Cockpits (Freddy IIRC) and Droptanks (source: ?) from DBW 1.71
- Java Code and new ordnance arrangement by barrett_g and SAS~Storebror

***********************************************************************************

Optional Installation Step:

If you wish to have plain text readable names for the loadout options, you have to
edit your "weapons.properties" file , which can be found in the "#SAS\STD\i18n"
subfolder of your Modact 5.x game. In this file, please remove all current loadout
descriptions for P-47D-10, P-47D-22, P-47D-27 and P-47D and replace them
by the following content:

#####################################################################
# P-47D-10
#####################################################################
P-47D-10.default                               Default
P-47D-10.overload                              Extra Ammunition
P-47D-10.tank                                  1x75 gal. Droptank
P-47D-10.tank6x45                              1x75gal.Droptank+6x4.5"Rockets
P-47D-10.6x45                                  6x4.5" Rockets
P-47D-10.1x1000                                1x1000 lb. Bomb
P-47D-10.1x10006x45                            1x1000lb.Bomb+6x4.5"Rockets
P-47D-10.*default                              *Default
P-47D-10.*c_tank075                            *75gal Droptank
P-47D-10.*c_tank108                            *108gal Droptank
P-47D-10.*c_tank110                            *110gal Droptank
P-47D-10.*c_tank200                            *200gal Droptank
P-47D-10.*c_tank075_6x45                       *75gal.DT+6x4.5"Rockets
P-47D-10.*c_tank108_6x45                       *108gal.DT+6x4.5"Rockets
P-47D-10.*c_tank110_6x45                       *110gal.DT+6x4.5"Rockets
P-47D-10.*c_tank200_6x45                       *200gal.DT+6x4.5"Rockets
P-47D-10.*6x45                                 *6x4.5" Rockets
P-47D-10.*c_1x500lbs                           *1x500 lb. Bomb
P-47D-10.*c_1x500lbs_6x45                      *500lb Bomb+6x4.5" Rockets
P-47D-10.*c_1xCluster                          *Fragmentation Bombs
P-47D-10.*c_1xCluster_6x45                     *Frag Bombs+6x4.5" Rockets
P-47D-10.*c_1x154Napalm                        *154gal. Napalm
P-47D-10.*c_1x154Napalm_6x45                   *154Napalm+6x4.5" Rockets
P-47D-10.none                                  Empty

#####################################################################
# P-47D-22
#####################################################################
P-47D-22.default                               Default
P-47D-22.overload                              Extra Ammunition
P-47D-22.tank                                  1x75 gal. Droptank
P-47D-22.tank2x500                             1x75 gal.+2x500 lb.Bombs
P-47D-22.tank6x45                              1x75gal.Droptank+6x4.5"Rockets
P-47D-22.tank2x5006x45                         1x75gal.+2x500lb.B.+6x4.5"R.
P-47D-22.6x45                                  6x4.5" Rockets
P-47D-22.2x500                                 2x500 lb. Bombs
P-47D-22.2x5006x45                             2x500lb.Bombs+6x4.5"Rockets
P-47D-22.1x1000                                1x1000 lb. Bomb
P-47D-22.1x10002x500                           1x1000lb.Bomb+2x500lb.Bombs
P-47D-22.1x10006x45                            1x1000lb.Bomb+6x4.5"Rockets
P-47D-22.1x10002x5006x45                       1x1000lb.B.+2x500lb.B.+6x4.5"R.
P-47D-22.*default                              *Default
P-47D-22.*c_tank075                            *75gal Droptank
P-47D-22.*c_tank108                            *108gal Droptank
P-47D-22.*c_tank110                            *110gal Droptank
P-47D-22.*c_tank200                            *200gal Droptank
P-47D-22.*w_2xtank108                          *2x108gal Droptank
P-47D-22.*w_2xtank110                          *2x110gal Droptank
P-47D-22.*w_2xtank165                          *2x165gal Droptank
P-47D-22.*c_tank075_w_2xtank108                *75+2x108gal Droptank
P-47D-22.*c_tank075_w_2xtank110                *75+2x110gal Droptank
P-47D-22.*c_tank075_w_2xtank165                *75+2x165gal Droptank
P-47D-22.*c_tank108_w_2xtank108                *108+2x108gal Droptank
P-47D-22.*c_tank108_w_2xtank110                *108+2x110gal Droptank
P-47D-22.*c_tank108_w_2xtank165                *108+2x165gal Droptank
P-47D-22.*c_tank110_w_2xtank108                *110+2x108gal Droptank
P-47D-22.*c_tank110_w_2xtank110                *110+2x110gal Droptank
P-47D-22.*c_tank110_w_2xtank165                *110+2x165gal Droptank
P-47D-22.*c_tank200_w_2xtank108                *200+2x108gal Droptank
P-47D-22.*c_tank200_w_2xtank110                *200+2x110gal Droptank
P-47D-22.*c_tank200_w_2xtank165                *200+2x165gal Droptank
P-47D-22.*c_tank075_w_2x500lbs                 *75gal.DT+2x500lb Bombs
P-47D-22.*c_tank075_6x45                       *75gal.DT+6x4.5"Rockets
P-47D-22.*c_tank075_w_2xtank108_6x45           *75+2x108gal+6x4.5"Rockets
P-47D-22.*c_tank075_w_2xtank110_6x45           *75+2x110gal+6x4.5"Rockets
P-47D-22.*c_tank075_w_2xtank165_6x45           *75+2x165gal+6x4.5"Rockets
P-47D-22.*c_tank075_w_2x500lbs_6x45            *75gal+2x500lb+6x4.5"Rockets
P-47D-22.*c_tank108_w_2x500lbs                 *108gal.DT+2x500lb Bombs
P-47D-22.*c_tank108_6x45                       *108gal.DT+6x4.5"Rockets
P-47D-22.*c_tank108_w_2xtank108_6x45           *108+2x108gal+6x4.5"Rockets
P-47D-22.*c_tank108_w_2xtank110_6x45           *108+2x110gal+6x4.5"Rockets
P-47D-22.*c_tank108_w_2xtank165_6x45           *108+2x165gal+6x4.5"Rockets
P-47D-22.*c_tank108_w_2x500lbs_6x45            *108gal+2x500lb+6x4.5"Rockets
P-47D-22.*c_tank110_w_2x500lbs                 *110gal.DT+2x500lb Bombs
P-47D-22.*c_tank110_6x45                       *110gal.DT+6x4.5"Rockets
P-47D-22.*c_tank110_w_2xtank108_6x45           *110+2x108gal+6x4.5"Rockets
P-47D-22.*c_tank110_w_2xtank110_6x45           *110+2x110gal+6x4.5"Rockets
P-47D-22.*c_tank110_w_2xtank165_6x45           *110+2x165gal+6x4.5"Rockets
P-47D-22.*c_tank110_w_2x500lbs_6x45            *110gal+2x500lb+6x4.5"Rockets
P-47D-22.*c_tank200_w_2x500lbs                 *200gal.DT+2x500lb Bombs
P-47D-22.*c_tank200_6x45                       *200gal.DT+6x4.5"Rockets
P-47D-22.*c_tank200_w_2xtank108_6x45           *200+2x108gal+6x4.5"Rockets
P-47D-22.*c_tank200_w_2xtank110_6x45           *200+2x110gal+6x4.5"Rockets
P-47D-22.*c_tank200_w_2xtank165_6x45           *200+2x165gal+6x4.5"Rockets
P-47D-22.*c_tank200_w_2x500lbs_6x45            *200gal+2x500lb+6x4.5"Rockets
P-47D-22.*6x45                                 *6x4.5" Rockets
P-47D-22.*w_2xtank108_6x45                     *2x108gal+6x4.5" Rockets
P-47D-22.*w_2xtank110_6x45                     *2x110gal+6x4.5" Rockets
P-47D-22.*w_2xtank165_6x45                     *2x165gal+6x4.5" Rockets
P-47D-22.*w_2x500lbs                           *2x500 lb. Bombs
P-47D-22.*w_2x500lbs_6x45                      *2x500lb.Bombs+6x4.5"Rockets
P-47D-22.*c_1x500lbs                           *1x500 lb. Bomb
P-47D-22.*c_1x500lbs_w_2xtank108               *1x500lb Bomb+2x108gal DT
P-47D-22.*c_1x500lbs_w_2xtank110               *1x500lb Bomb+2x110gal DT
P-47D-22.*c_1x500lbs_w_2xtank165               *1x500lb Bomb+2x165gal DT
P-47D-22.*c_1x500lbs_w_2x500lbs                *3x500 lb. Bombs
P-47D-22.*c_1x500lbs_6x45                      *500lb Bomb+6x4.5" Rockets
P-47D-22.*c_1x500lbs_w_2xtank108_6x45          *500lb+2x108gal+6x4.5"Rockets
P-47D-22.*c_1x500lbs_w_2xtank110_6x45          *500lb+2x110gal+6x4.5"Rockets
P-47D-22.*c_1x500lbs_w_2xtank165_6x45          *500lb+2x165gal+6x4.5"Rockets
P-47D-22.*c_1x500lbs_w_2x500lbs_6x45           *3x500lb+6x4.5"Rockets
P-47D-22.*w_2x1000lbs                          *2x1000 lb. Bombs
P-47D-22.*c_tank075_w_2x1000lbs                *75gal.DT+2x1000lb.Bombs
P-47D-22.*c_tank108_w_2x1000lbs                *108gal.DT+2x1000lb.Bombs
P-47D-22.*c_tank110_w_2x1000lbs                *110gal.DT+2x1000lb.Bombs
P-47D-22.*c_tank200_w_2x1000lbs                *200gal.DT+2x1000lb.Bombs
P-47D-22.*c_1x500lbs_w_2x1000lbs               *500lb.+2x1000lb.Bombs
P-47D-22.*w_2x1000lbs_6x45                     *2x1000lb+6x4.5"Rockets
P-47D-22.*c_tank075_w_2x1000lbs_6x45           *75gal+2x1000lb+6x4.5"Rockets
P-47D-22.*c_tank108_w_2x1000lbs_6x45           *108gal+2x1000lb+6x4.5"Rockets
P-47D-22.*c_tank110_w_2x1000lbs_6x45           *110gal+2x1000lb+6x4.5"Rockets
P-47D-22.*c_tank200_w_2x1000lbs_6x45           *200gal+2x1000lb+6x4.5"Rockets
P-47D-22.*c_1x500lbs_w_2x1000lbs_6x45          *500lb+2x1000lb+6x4.5"Rockets
P-47D-22.*c_1xCluster                          *Fragmentation Bombs
P-47D-22.*c_1xCluster_w_2xtank108              *Frag+2x108gal DT
P-47D-22.*c_1xCluster_w_2xtank110              *Frag+2x110gal DT
P-47D-22.*c_1xCluster_w_2xtank165              *Frag+2x165gal DT
P-47D-22.*c_1xCluster_w_2x500lbs               *Frag+2x500lb Bombs
P-47D-22.*c_1xCluster_6x45                     *Frag+6x4.5"Rockets
P-47D-22.*c_1xCluster_w_2xtank108_6x45         *Frag+2x108gal+6x4.5"Rockets
P-47D-22.*c_1xCluster_w_2xtank110_6x45         *Frag+2x110gal+6x4.5"Rockets
P-47D-22.*c_1xCluster_w_2xtank165_6x45         *Frag+2x165gal+6x4.5"Rockets
P-47D-22.*c_1xCluster_w_2x500lbs_6x45          *Frag+2x500lb+6x4.5"Rockets
P-47D-22.*c_1xCluster_w_2x1000lbs              *Frag+2x1000lb.Bombs
P-47D-22.*c_1xCluster_w_2x1000lbs_6x45         *Frag+2x1000lb+6x4.5"Rockets
P-47D-22.*w_2xCluster                          *2xFragmentation Bombs
P-47D-22.*c_1xCluster_w_2xCluster              *3xFragmentation Bombs
P-47D-22.*c_1xCluster_w_2xCluster_6x45         *3xFrag+6x4.5"Rockets
P-47D-22.*c_tank075_w_2xCluster                *75gal+2xFrag
P-47D-22.*c_tank108_w_2xCluster                *108gal+2xFrag
P-47D-22.*c_tank110_w_2xCluster                *110gal+2xFrag
P-47D-22.*c_tank200_w_2xCluster                *200gal+2xFrag
P-47D-22.*c_1x500lbs_w_2xCluster               *500lb+2xFrag
P-47D-22.*w_2xCluster_6x45                     *2xFrag+6x4.5"Rockets
P-47D-22.*c_tank075_w_2xCluster_6x45           *75gal+2xFrag+6x4.5"Rockets
P-47D-22.*c_tank108_w_2xCluster_6x45           *108gal+2xFrag+6x4.5"Rockets
P-47D-22.*c_tank110_w_2xCluster_6x45           *110gal+2xFrag+6x4.5"Rockets
P-47D-22.*c_tank200_w_2xCluster_6x45           *200gal+2xFrag+6x4.5"Rockets
P-47D-22.*c_1x500lbs_w_2xCluster_6x45          *500lb+2xFrag+6x4.5"Rockets
P-47D-22.*c_1x154Napalm                        *154gal. Napalm
P-47D-22.*c_1x154Napalm_w_2xtank108            *154Napalm+2x108gal
P-47D-22.*c_1x154Napalm_w_2xtank110            *154Napalm+2x110gal
P-47D-22.*c_1x154Napalm_w_2xtank165            *154Napalm+2x165gal
P-47D-22.*c_1x154Napalm_w_2x500lbs             *154Napalm+2x500lb
P-47D-22.*c_1x154Napalm_w_2x1000lbs            *154Napalm+2x1000lb
P-47D-22.*c_1x154Napalm_w_2xCluster            *154Napalm+2xFrag
P-47D-22.*c_1x154Napalm_6x45                   *154Napalm+6x4.5"Rockets
P-47D-22.*c_1x154Napalm_w_2xtank108_6x45       *154Napalm+2x108gal+6x4.5"Rockets
P-47D-22.*c_1x154Napalm_w_2xtank110_6x45       *154Napalm+2x110gal+6x4.5"Rockets
P-47D-22.*c_1x154Napalm_w_2xtank165_6x45       *154Napalm+2x165gal+6x4.5"Rockets
P-47D-22.*c_1x154Napalm_w_2x500lbs_6x45        *154Napalm+2x500lb+6x4.5"Rockets
P-47D-22.*c_1x154Napalm_w_2x1000lbs_6x45       *154Napalm+2x1000lb+6x4.5"Rockets
P-47D-22.*c_1x154Napalm_w_2xCluster_6x45       *154Napalm+2xFrag+6x4.5"Rockets
P-47D-22.*c_1x154Napalm_w_2x154Napalm          *3x154Napalm
P-47D-22.*c_1x154Napalm_w_2x175Napalm          *154Napalm+2x175Napalm
P-47D-22.*c_1xCluster_w_2x154Napalm            *Frag+2x154Napalm
P-47D-22.*c_1x500lbs_w_2x154Napalm             *500lb+2x154Napalm
P-47D-22.*c_tank075_w_2x154Napalm              *75gal+2x154Napalm
P-47D-22.*c_tank108_w_2x154Napalm              *108gal+2x154Napalm
P-47D-22.*c_tank110_w_2x154Napalm              *110gal+2x154Napalm
P-47D-22.*c_tank200_w_2x154Napalm              *200gal+2x154Napalm
P-47D-22.*c_1x154Napalm_w_2x154Napalm_6x45     *3x154Napalm+2x154Napalm+6x4.5"Rockets
P-47D-22.*c_1xCluster_w_2x154Napalm_6x45       *Frag+2x154Napalm+6x4.5"Rockets
P-47D-22.*c_1x500lbs_w_2x154Napalm_6x45        *500lb+2x154Napalm+6x4.5"Rockets
P-47D-22.*c_tank075_w_2x154Napalm_6x45         *75gal+2x154Napalm+6x4.5"Rockets
P-47D-22.*c_tank108_w_2x154Napalm_6x45         *108gal+2x154Napalm+6x4.5"Rockets
P-47D-22.*c_tank110_w_2x154Napalm_6x45         *110gal+2x154Napalm+6x4.5"Rockets
P-47D-22.*c_tank200_w_2x154Napalm_6x45         *200gal+2x154Napalm+6x4.5"Rockets
P-47D-22.*c_1xCluster_w_2x175Napalm            *Frag+2x175Napalm
P-47D-22.*c_1x500lbs_w_2x175Napalm             *500lb+2x175Napalm
P-47D-22.*c_tank075_w_2x175Napalm              *75gal+2x175Napalm
P-47D-22.*c_tank108_w_2x175Napalm              *108gal+2x175Napalm
P-47D-22.*c_tank110_w_2x175Napalm              *110gal+2x175Napalm
P-47D-22.*c_tank200_w_2x175Napalm              *200gal+2x175Napalm
P-47D-22.*c_1x154Napalm_w_2x175Napalm_6x45     *154Napalm+2x175Napalm+6x4.5"Rockets
P-47D-22.*c_1xCluster_w_2x175Napalm_6x45       *Frag+2x175Napalm+6x4.5"Rockets
P-47D-22.*c_1x500lbs_w_2x175Napalm_6x45        *500lb+2x175Napalm+6x4.5"Rockets
P-47D-22.*c_tank075_w_2x175Napalm_6x45         *75gal+2x175Napalm+6x4.5"Rockets
P-47D-22.*c_tank108_w_2x175Napalm_6x45         *108gal+2x175Napalm+6x4.5"Rockets
P-47D-22.*c_tank110_w_2x175Napalm_6x45         *110gal+2x175Napalm+6x4.5"Rockets
P-47D-22.*c_tank200_w_2x175Napalm_6x45         *200gal+2x175Napalm+6x4.5"Rockets
P-47D-22.none                                  Empty

#####################################################################
# P-47D-27
#####################################################################
P-47D-27.default                               Default
P-47D-27.overload                              Extra Ammunition
P-47D-27.tank                                  1x75 gal. Droptank
P-47D-27.tank2x500                             1x75 gal.+2x500 lb.Bombs
P-47D-27.tank6x45                              1x75gal.Droptank+6x4.5"Rockets
P-47D-27.tank2x5006x45                         1x75gal.+2x500lb.B.+6x4.5"R.
P-47D-27.6x45                                  6x4.5" Rockets
P-47D-27.2x500                                 2x500 lb. Bombs
P-47D-27.2x5006x45                             2x500lb.Bombs+6x4.5"Rockets
P-47D-27.1x1000                                1x1000 lb. Bomb
P-47D-27.1x10002x500                           1x1000lb.Bomb+2x500lb.Bombs
P-47D-27.1x10006x45                            1x1000lb.Bomb+6x4.5"Rockets
P-47D-27.1x10002x5006x45                       1x1000lb.B.+2x500lb.B.+6x4.5"R.
P-47D-27.*default                              *Default
P-47D-27.*c_tank075                            *75gal Droptank
P-47D-27.*c_tank108                            *108gal Droptank
P-47D-27.*c_tank110                            *110gal Droptank
P-47D-27.*c_tank200                            *200gal Droptank
P-47D-27.*w_2xtank108                          *2x108gal Droptank
P-47D-27.*w_2xtank110                          *2x110gal Droptank
P-47D-27.*w_2xtank165                          *2x165gal Droptank
P-47D-27.*c_tank075_w_2xtank108                *75+2x108gal Droptank
P-47D-27.*c_tank075_w_2xtank110                *75+2x110gal Droptank
P-47D-27.*c_tank075_w_2xtank165                *75+2x165gal Droptank
P-47D-27.*c_tank108_w_2xtank108                *108+2x108gal Droptank
P-47D-27.*c_tank108_w_2xtank110                *108+2x110gal Droptank
P-47D-27.*c_tank108_w_2xtank165                *108+2x165gal Droptank
P-47D-27.*c_tank110_w_2xtank108                *110+2x108gal Droptank
P-47D-27.*c_tank110_w_2xtank110                *110+2x110gal Droptank
P-47D-27.*c_tank110_w_2xtank165                *110+2x165gal Droptank
P-47D-27.*c_tank200_w_2xtank108                *200+2x108gal Droptank
P-47D-27.*c_tank200_w_2xtank110                *200+2x110gal Droptank
P-47D-27.*c_tank200_w_2xtank165                *200+2x165gal Droptank
P-47D-27.*c_tank075_w_2x500lbs                 *75gal.DT+2x500lb Bombs
P-47D-27.*c_tank075_6x45                       *75gal.DT+6x4.5"Rockets
P-47D-27.*c_tank075_w_2xtank108_6x45           *75+2x108gal+6x4.5"Rockets
P-47D-27.*c_tank075_w_2xtank110_6x45           *75+2x110gal+6x4.5"Rockets
P-47D-27.*c_tank075_w_2xtank165_6x45           *75+2x165gal+6x4.5"Rockets
P-47D-27.*c_tank075_w_2x500lbs_6x45            *75gal+2x500lb+6x4.5"Rockets
P-47D-27.*c_tank108_w_2x500lbs                 *108gal.DT+2x500lb Bombs
P-47D-27.*c_tank108_6x45                       *108gal.DT+6x4.5"Rockets
P-47D-27.*c_tank108_w_2xtank108_6x45           *108+2x108gal+6x4.5"Rockets
P-47D-27.*c_tank108_w_2xtank110_6x45           *108+2x110gal+6x4.5"Rockets
P-47D-27.*c_tank108_w_2xtank165_6x45           *108+2x165gal+6x4.5"Rockets
P-47D-27.*c_tank108_w_2x500lbs_6x45            *108gal+2x500lb+6x4.5"Rockets
P-47D-27.*c_tank110_w_2x500lbs                 *110gal.DT+2x500lb Bombs
P-47D-27.*c_tank110_6x45                       *110gal.DT+6x4.5"Rockets
P-47D-27.*c_tank110_w_2xtank108_6x45           *110+2x108gal+6x4.5"Rockets
P-47D-27.*c_tank110_w_2xtank110_6x45           *110+2x110gal+6x4.5"Rockets
P-47D-27.*c_tank110_w_2xtank165_6x45           *110+2x165gal+6x4.5"Rockets
P-47D-27.*c_tank110_w_2x500lbs_6x45            *110gal+2x500lb+6x4.5"Rockets
P-47D-27.*c_tank200_w_2x500lbs                 *200gal.DT+2x500lb Bombs
P-47D-27.*c_tank200_6x45                       *200gal.DT+6x4.5"Rockets
P-47D-27.*c_tank200_w_2xtank108_6x45           *200+2x108gal+6x4.5"Rockets
P-47D-27.*c_tank200_w_2xtank110_6x45           *200+2x110gal+6x4.5"Rockets
P-47D-27.*c_tank200_w_2xtank165_6x45           *200+2x165gal+6x4.5"Rockets
P-47D-27.*c_tank200_w_2x500lbs_6x45            *200gal+2x500lb+6x4.5"Rockets
P-47D-27.*6x45                                 *6x4.5" Rockets
P-47D-27.*w_2xtank108_6x45                     *2x108gal+6x4.5" Rockets
P-47D-27.*w_2xtank110_6x45                     *2x110gal+6x4.5" Rockets
P-47D-27.*w_2xtank165_6x45                     *2x165gal+6x4.5" Rockets
P-47D-27.*w_2x500lbs                           *2x500 lb. Bombs
P-47D-27.*w_2x500lbs_6x45                      *2x500lb.Bombs+6x4.5"Rockets
P-47D-27.*c_1x500lbs                           *1x500 lb. Bomb
P-47D-27.*c_1x500lbs_w_2xtank108               *1x500lb Bomb+2x108gal DT
P-47D-27.*c_1x500lbs_w_2xtank110               *1x500lb Bomb+2x110gal DT
P-47D-27.*c_1x500lbs_w_2xtank165               *1x500lb Bomb+2x165gal DT
P-47D-27.*c_1x500lbs_w_2x500lbs                *3x500 lb. Bombs
P-47D-27.*c_1x500lbs_6x45                      *500lb Bomb+6x4.5" Rockets
P-47D-27.*c_1x500lbs_w_2xtank108_6x45          *500lb+2x108gal+6x4.5"Rockets
P-47D-27.*c_1x500lbs_w_2xtank110_6x45          *500lb+2x110gal+6x4.5"Rockets
P-47D-27.*c_1x500lbs_w_2xtank165_6x45          *500lb+2x165gal+6x4.5"Rockets
P-47D-27.*c_1x500lbs_w_2x500lbs_6x45           *3x500lb+6x4.5"Rockets
P-47D-27.*w_2x1000lbs                          *2x1000 lb. Bombs
P-47D-27.*c_tank075_w_2x1000lbs                *75gal.DT+2x1000lb.Bombs
P-47D-27.*c_tank108_w_2x1000lbs                *108gal.DT+2x1000lb.Bombs
P-47D-27.*c_tank110_w_2x1000lbs                *110gal.DT+2x1000lb.Bombs
P-47D-27.*c_tank200_w_2x1000lbs                *200gal.DT+2x1000lb.Bombs
P-47D-27.*c_1x500lbs_w_2x1000lbs               *500lb.+2x1000lb.Bombs
P-47D-27.*w_2x1000lbs_6x45                     *2x1000lb+6x4.5"Rockets
P-47D-27.*c_tank075_w_2x1000lbs_6x45           *75gal+2x1000lb+6x4.5"Rockets
P-47D-27.*c_tank108_w_2x1000lbs_6x45           *108gal+2x1000lb+6x4.5"Rockets
P-47D-27.*c_tank110_w_2x1000lbs_6x45           *110gal+2x1000lb+6x4.5"Rockets
P-47D-27.*c_tank200_w_2x1000lbs_6x45           *200gal+2x1000lb+6x4.5"Rockets
P-47D-27.*c_1x500lbs_w_2x1000lbs_6x45          *500lb+2x1000lb+6x4.5"Rockets
P-47D-27.*c_1xCluster                          *Fragmentation Bombs
P-47D-27.*c_1xCluster_w_2xtank108              *Frag+2x108gal DT
P-47D-27.*c_1xCluster_w_2xtank110              *Frag+2x110gal DT
P-47D-27.*c_1xCluster_w_2xtank165              *Frag+2x165gal DT
P-47D-27.*c_1xCluster_w_2x500lbs               *Frag+2x500lb Bombs
P-47D-27.*c_1xCluster_6x45                     *Frag+6x4.5"Rockets
P-47D-27.*c_1xCluster_w_2xtank108_6x45         *Frag+2x108gal+6x4.5"Rockets
P-47D-27.*c_1xCluster_w_2xtank110_6x45         *Frag+2x110gal+6x4.5"Rockets
P-47D-27.*c_1xCluster_w_2xtank165_6x45         *Frag+2x165gal+6x4.5"Rockets
P-47D-27.*c_1xCluster_w_2x500lbs_6x45          *Frag+2x500lb+6x4.5"Rockets
P-47D-27.*c_1xCluster_w_2x1000lbs              *Frag+2x1000lb.Bombs
P-47D-27.*c_1xCluster_w_2x1000lbs_6x45         *Frag+2x1000lb+6x4.5"Rockets
P-47D-27.*w_2xCluster                          *2xFragmentation Bombs
P-47D-27.*c_1xCluster_w_2xCluster              *3xFragmentation Bombs
P-47D-27.*c_1xCluster_w_2xCluster_6x45         *3xFrag+6x4.5"Rockets
P-47D-27.*c_tank075_w_2xCluster                *75gal+2xFrag
P-47D-27.*c_tank108_w_2xCluster                *108gal+2xFrag
P-47D-27.*c_tank110_w_2xCluster                *110gal+2xFrag
P-47D-27.*c_tank200_w_2xCluster                *200gal+2xFrag
P-47D-27.*c_1x500lbs_w_2xCluster               *500lb+2xFrag
P-47D-27.*w_2xCluster_6x45                     *2xFrag+6x4.5"Rockets
P-47D-27.*c_tank075_w_2xCluster_6x45           *75gal+2xFrag+6x4.5"Rockets
P-47D-27.*c_tank108_w_2xCluster_6x45           *108gal+2xFrag+6x4.5"Rockets
P-47D-27.*c_tank110_w_2xCluster_6x45           *110gal+2xFrag+6x4.5"Rockets
P-47D-27.*c_tank200_w_2xCluster_6x45           *200gal+2xFrag+6x4.5"Rockets
P-47D-27.*c_1x500lbs_w_2xCluster_6x45          *500lb+2xFrag+6x4.5"Rockets
P-47D-27.*c_1x154Napalm                        *154gal. Napalm
P-47D-27.*c_1x154Napalm_w_2xtank108            *154Napalm+2x108gal
P-47D-27.*c_1x154Napalm_w_2xtank110            *154Napalm+2x110gal
P-47D-27.*c_1x154Napalm_w_2xtank165            *154Napalm+2x165gal
P-47D-27.*c_1x154Napalm_w_2x500lbs             *154Napalm+2x500lb
P-47D-27.*c_1x154Napalm_w_2x1000lbs            *154Napalm+2x1000lb
P-47D-27.*c_1x154Napalm_w_2xCluster            *154Napalm+2xFrag
P-47D-27.*c_1x154Napalm_6x45                   *154Napalm+6x4.5"Rockets
P-47D-27.*c_1x154Napalm_w_2xtank108_6x45       *154Napalm+2x108gal+6x4.5"Rockets
P-47D-27.*c_1x154Napalm_w_2xtank110_6x45       *154Napalm+2x110gal+6x4.5"Rockets
P-47D-27.*c_1x154Napalm_w_2xtank165_6x45       *154Napalm+2x165gal+6x4.5"Rockets
P-47D-27.*c_1x154Napalm_w_2x500lbs_6x45        *154Napalm+2x500lb+6x4.5"Rockets
P-47D-27.*c_1x154Napalm_w_2x1000lbs_6x45       *154Napalm+2x1000lb+6x4.5"Rockets
P-47D-27.*c_1x154Napalm_w_2xCluster_6x45       *154Napalm+2xFrag+6x4.5"Rockets
P-47D-27.*c_1x154Napalm_w_2x154Napalm          *3x154Napalm
P-47D-27.*c_1x154Napalm_w_2x175Napalm          *154Napalm+2x175Napalm
P-47D-27.*c_1xCluster_w_2x154Napalm            *Frag+2x154Napalm
P-47D-27.*c_1x500lbs_w_2x154Napalm             *500lb+2x154Napalm
P-47D-27.*c_tank075_w_2x154Napalm              *75gal+2x154Napalm
P-47D-27.*c_tank108_w_2x154Napalm              *108gal+2x154Napalm
P-47D-27.*c_tank110_w_2x154Napalm              *110gal+2x154Napalm
P-47D-27.*c_tank200_w_2x154Napalm              *200gal+2x154Napalm
P-47D-27.*c_1x154Napalm_w_2x154Napalm_6x45     *3x154Napalm+2x154Napalm+6x4.5"Rockets
P-47D-27.*c_1xCluster_w_2x154Napalm_6x45       *Frag+2x154Napalm+6x4.5"Rockets
P-47D-27.*c_1x500lbs_w_2x154Napalm_6x45        *500lb+2x154Napalm+6x4.5"Rockets
P-47D-27.*c_tank075_w_2x154Napalm_6x45         *75gal+2x154Napalm+6x4.5"Rockets
P-47D-27.*c_tank108_w_2x154Napalm_6x45         *108gal+2x154Napalm+6x4.5"Rockets
P-47D-27.*c_tank110_w_2x154Napalm_6x45         *110gal+2x154Napalm+6x4.5"Rockets
P-47D-27.*c_tank200_w_2x154Napalm_6x45         *200gal+2x154Napalm+6x4.5"Rockets
P-47D-27.*c_1xCluster_w_2x175Napalm            *Frag+2x175Napalm
P-47D-27.*c_1x500lbs_w_2x175Napalm             *500lb+2x175Napalm
P-47D-27.*c_tank075_w_2x175Napalm              *75gal+2x175Napalm
P-47D-27.*c_tank108_w_2x175Napalm              *108gal+2x175Napalm
P-47D-27.*c_tank110_w_2x175Napalm              *110gal+2x175Napalm
P-47D-27.*c_tank200_w_2x175Napalm              *200gal+2x175Napalm
P-47D-27.*c_1x154Napalm_w_2x175Napalm_6x45     *154Napalm+2x175Napalm+6x4.5"Rockets
P-47D-27.*c_1xCluster_w_2x175Napalm_6x45       *Frag+2x175Napalm+6x4.5"Rockets
P-47D-27.*c_1x500lbs_w_2x175Napalm_6x45        *500lb+2x175Napalm+6x4.5"Rockets
P-47D-27.*c_tank075_w_2x175Napalm_6x45         *75gal+2x175Napalm+6x4.5"Rockets
P-47D-27.*c_tank108_w_2x175Napalm_6x45         *108gal+2x175Napalm+6x4.5"Rockets
P-47D-27.*c_tank110_w_2x175Napalm_6x45         *110gal+2x175Napalm+6x4.5"Rockets
P-47D-27.*c_tank200_w_2x175Napalm_6x45         *200gal+2x175Napalm+6x4.5"Rockets
P-47D-27.none                                  Empty

#####################################################################
# P-47D
#####################################################################
P-47D.default                                  Default
P-47D.overload                                 Extra Ammunition
P-47D.tank                                     1x75 gal. Droptank
P-47D.tank2x500                                1x75 gal.+2x500 lb.Bombs
P-47D.tank6x45                                 1x75gal.Droptank+6x4.5"Rockets
P-47D.tank2x5006x45                            1x75gal.+2x500lb.B.+6x4.5"R.
P-47D.6x45                                     6x4.5" Rockets
P-47D.2x500                                    2x500 lb. Bombs
P-47D.2x5006x45                                2x500lb.Bombs+6x4.5"Rockets
P-47D.1x1000                                   1x1000 lb. Bomb
P-47D.1x10002x500                              1x1000lb.Bomb+2x500lb.Bombs
P-47D.1x10006x45                               1x1000lb.Bomb+6x4.5"Rockets
P-47D.1x10002x5006x45                          1x1000lb.B.+2x500lb.B.+6x4.5"R.
P-47D.*default                                 *Default
P-47D.*c_tank075                               *75gal Droptank
P-47D.*c_tank108                               *108gal Droptank
P-47D.*c_tank110                               *110gal Droptank
P-47D.*c_tank200                               *200gal Droptank
P-47D.*w_2xtank108                             *2x108gal Droptank
P-47D.*w_2xtank110                             *2x110gal Droptank
P-47D.*w_2xtank165                             *2x165gal Droptank
P-47D.*c_tank075_w_2xtank108                   *75+2x108gal Droptank
P-47D.*c_tank075_w_2xtank110                   *75+2x110gal Droptank
P-47D.*c_tank075_w_2xtank165                   *75+2x165gal Droptank
P-47D.*c_tank108_w_2xtank108                   *108+2x108gal Droptank
P-47D.*c_tank108_w_2xtank110                   *108+2x110gal Droptank
P-47D.*c_tank108_w_2xtank165                   *108+2x165gal Droptank
P-47D.*c_tank110_w_2xtank108                   *110+2x108gal Droptank
P-47D.*c_tank110_w_2xtank110                   *110+2x110gal Droptank
P-47D.*c_tank110_w_2xtank165                   *110+2x165gal Droptank
P-47D.*c_tank200_w_2xtank108                   *200+2x108gal Droptank
P-47D.*c_tank200_w_2xtank110                   *200+2x110gal Droptank
P-47D.*c_tank200_w_2xtank165                   *200+2x165gal Droptank
P-47D.*c_tank075_w_2x500lbs                    *75gal.DT+2x500lb Bombs
P-47D.*c_tank075_6x45                          *75gal.DT+6x4.5"Rockets
P-47D.*c_tank075_w_2xtank108_6x45              *75+2x108gal+6x4.5"Rockets
P-47D.*c_tank075_w_2xtank110_6x45              *75+2x110gal+6x4.5"Rockets
P-47D.*c_tank075_w_2xtank165_6x45              *75+2x165gal+6x4.5"Rockets
P-47D.*c_tank075_w_2x500lbs_6x45               *75gal+2x500lb+6x4.5"Rockets
P-47D.*c_tank108_w_2x500lbs                    *108gal.DT+2x500lb Bombs
P-47D.*c_tank108_6x45                          *108gal.DT+6x4.5"Rockets
P-47D.*c_tank108_w_2xtank108_6x45              *108+2x108gal+6x4.5"Rockets
P-47D.*c_tank108_w_2xtank110_6x45              *108+2x110gal+6x4.5"Rockets
P-47D.*c_tank108_w_2xtank165_6x45              *108+2x165gal+6x4.5"Rockets
P-47D.*c_tank108_w_2x500lbs_6x45               *108gal+2x500lb+6x4.5"Rockets
P-47D.*c_tank110_w_2x500lbs                    *110gal.DT+2x500lb Bombs
P-47D.*c_tank110_6x45                          *110gal.DT+6x4.5"Rockets
P-47D.*c_tank110_w_2xtank108_6x45              *110+2x108gal+6x4.5"Rockets
P-47D.*c_tank110_w_2xtank110_6x45              *110+2x110gal+6x4.5"Rockets
P-47D.*c_tank110_w_2xtank165_6x45              *110+2x165gal+6x4.5"Rockets
P-47D.*c_tank110_w_2x500lbs_6x45               *110gal+2x500lb+6x4.5"Rockets
P-47D.*c_tank200_w_2x500lbs                    *200gal.DT+2x500lb Bombs
P-47D.*c_tank200_6x45                          *200gal.DT+6x4.5"Rockets
P-47D.*c_tank200_w_2xtank108_6x45              *200+2x108gal+6x4.5"Rockets
P-47D.*c_tank200_w_2xtank110_6x45              *200+2x110gal+6x4.5"Rockets
P-47D.*c_tank200_w_2xtank165_6x45              *200+2x165gal+6x4.5"Rockets
P-47D.*c_tank200_w_2x500lbs_6x45               *200gal+2x500lb+6x4.5"Rockets
P-47D.*6x45                                    *6x4.5" Rockets
P-47D.*w_2xtank108_6x45                        *2x108gal+6x4.5" Rockets
P-47D.*w_2xtank110_6x45                        *2x110gal+6x4.5" Rockets
P-47D.*w_2xtank165_6x45                        *2x165gal+6x4.5" Rockets
P-47D.*w_2x500lbs                              *2x500 lb. Bombs
P-47D.*w_2x500lbs_6x45                         *2x500lb.Bombs+6x4.5"Rockets
P-47D.*c_1x500lbs                              *1x500 lb. Bomb
P-47D.*c_1x500lbs_w_2xtank108                  *1x500lb Bomb+2x108gal DT
P-47D.*c_1x500lbs_w_2xtank110                  *1x500lb Bomb+2x110gal DT
P-47D.*c_1x500lbs_w_2xtank165                  *1x500lb Bomb+2x165gal DT
P-47D.*c_1x500lbs_w_2x500lbs                   *3x500 lb. Bombs
P-47D.*c_1x500lbs_6x45                         *500lb Bomb+6x4.5" Rockets
P-47D.*c_1x500lbs_w_2xtank108_6x45             *500lb+2x108gal+6x4.5"Rockets
P-47D.*c_1x500lbs_w_2xtank110_6x45             *500lb+2x110gal+6x4.5"Rockets
P-47D.*c_1x500lbs_w_2xtank165_6x45             *500lb+2x165gal+6x4.5"Rockets
P-47D.*c_1x500lbs_w_2x500lbs_6x45              *3x500lb+6x4.5"Rockets
P-47D.*w_2x1000lbs                             *2x1000 lb. Bombs
P-47D.*c_tank075_w_2x1000lbs                   *75gal.DT+2x1000lb.Bombs
P-47D.*c_tank108_w_2x1000lbs                   *108gal.DT+2x1000lb.Bombs
P-47D.*c_tank110_w_2x1000lbs                   *110gal.DT+2x1000lb.Bombs
P-47D.*c_tank200_w_2x1000lbs                   *200gal.DT+2x1000lb.Bombs
P-47D.*c_1x500lbs_w_2x1000lbs                  *500lb.+2x1000lb.Bombs
P-47D.*w_2x1000lbs_6x45                        *2x1000lb+6x4.5"Rockets
P-47D.*c_tank075_w_2x1000lbs_6x45              *75gal+2x1000lb+6x4.5"Rockets
P-47D.*c_tank108_w_2x1000lbs_6x45              *108gal+2x1000lb+6x4.5"Rockets
P-47D.*c_tank110_w_2x1000lbs_6x45              *110gal+2x1000lb+6x4.5"Rockets
P-47D.*c_tank200_w_2x1000lbs_6x45              *200gal+2x1000lb+6x4.5"Rockets
P-47D.*c_1x500lbs_w_2x1000lbs_6x45             *500lb+2x1000lb+6x4.5"Rockets
P-47D.*c_1xCluster                             *Fragmentation Bombs
P-47D.*c_1xCluster_w_2xtank108                 *Frag+2x108gal DT
P-47D.*c_1xCluster_w_2xtank110                 *Frag+2x110gal DT
P-47D.*c_1xCluster_w_2xtank165                 *Frag+2x165gal DT
P-47D.*c_1xCluster_w_2x500lbs                  *Frag+2x500lb Bombs
P-47D.*c_1xCluster_6x45                        *Frag+6x4.5"Rockets
P-47D.*c_1xCluster_w_2xtank108_6x45            *Frag+2x108gal+6x4.5"Rockets
P-47D.*c_1xCluster_w_2xtank110_6x45            *Frag+2x110gal+6x4.5"Rockets
P-47D.*c_1xCluster_w_2xtank165_6x45            *Frag+2x165gal+6x4.5"Rockets
P-47D.*c_1xCluster_w_2x500lbs_6x45             *Frag+2x500lb+6x4.5"Rockets
P-47D.*c_1xCluster_w_2x1000lbs                 *Frag+2x1000lb.Bombs
P-47D.*c_1xCluster_w_2x1000lbs_6x45            *Frag+2x1000lb+6x4.5"Rockets
P-47D.*w_2xCluster                             *2xFragmentation Bombs
P-47D.*c_1xCluster_w_2xCluster                 *3xFragmentation Bombs
P-47D.*c_1xCluster_w_2xCluster_6x45            *3xFrag+6x4.5"Rockets
P-47D.*c_tank075_w_2xCluster                   *75gal+2xFrag
P-47D.*c_tank108_w_2xCluster                   *108gal+2xFrag
P-47D.*c_tank110_w_2xCluster                   *110gal+2xFrag
P-47D.*c_tank200_w_2xCluster                   *200gal+2xFrag
P-47D.*c_1x500lbs_w_2xCluster                  *500lb+2xFrag
P-47D.*w_2xCluster_6x45                        *2xFrag+6x4.5"Rockets
P-47D.*c_tank075_w_2xCluster_6x45              *75gal+2xFrag+6x4.5"Rockets
P-47D.*c_tank108_w_2xCluster_6x45              *108gal+2xFrag+6x4.5"Rockets
P-47D.*c_tank110_w_2xCluster_6x45              *110gal+2xFrag+6x4.5"Rockets
P-47D.*c_tank200_w_2xCluster_6x45              *200gal+2xFrag+6x4.5"Rockets
P-47D.*c_1x500lbs_w_2xCluster_6x45             *500lb+2xFrag+6x4.5"Rockets
P-47D.*c_1x154Napalm                           *154gal. Napalm
P-47D.*c_1x154Napalm_w_2xtank108               *154Napalm+2x108gal
P-47D.*c_1x154Napalm_w_2xtank110               *154Napalm+2x110gal
P-47D.*c_1x154Napalm_w_2xtank165               *154Napalm+2x165gal
P-47D.*c_1x154Napalm_w_2x500lbs                *154Napalm+2x500lb
P-47D.*c_1x154Napalm_w_2x1000lbs               *154Napalm+2x1000lb
P-47D.*c_1x154Napalm_w_2xCluster               *154Napalm+2xFrag
P-47D.*c_1x154Napalm_6x45                      *154Napalm+6x4.5"Rockets
P-47D.*c_1x154Napalm_w_2xtank108_6x45          *154Napalm+2x108gal+6x4.5"Rockets
P-47D.*c_1x154Napalm_w_2xtank110_6x45          *154Napalm+2x110gal+6x4.5"Rockets
P-47D.*c_1x154Napalm_w_2xtank165_6x45          *154Napalm+2x165gal+6x4.5"Rockets
P-47D.*c_1x154Napalm_w_2x500lbs_6x45           *154Napalm+2x500lb+6x4.5"Rockets
P-47D.*c_1x154Napalm_w_2x1000lbs_6x45          *154Napalm+2x1000lb+6x4.5"Rockets
P-47D.*c_1x154Napalm_w_2xCluster_6x45          *154Napalm+2xFrag+6x4.5"Rockets
P-47D.*c_1x154Napalm_w_2x154Napalm             *3x154Napalm
P-47D.*c_1x154Napalm_w_2x175Napalm             *154Napalm+2x175Napalm
P-47D.*c_1xCluster_w_2x154Napalm               *Frag+2x154Napalm
P-47D.*c_1x500lbs_w_2x154Napalm                *500lb+2x154Napalm
P-47D.*c_tank075_w_2x154Napalm                 *75gal+2x154Napalm
P-47D.*c_tank108_w_2x154Napalm                 *108gal+2x154Napalm
P-47D.*c_tank110_w_2x154Napalm                 *110gal+2x154Napalm
P-47D.*c_tank200_w_2x154Napalm                 *200gal+2x154Napalm
P-47D.*c_1x154Napalm_w_2x154Napalm_6x45        *3x154Napalm+2x154Napalm+6x4.5"Rockets
P-47D.*c_1xCluster_w_2x154Napalm_6x45          *Frag+2x154Napalm+6x4.5"Rockets
P-47D.*c_1x500lbs_w_2x154Napalm_6x45           *500lb+2x154Napalm+6x4.5"Rockets
P-47D.*c_tank075_w_2x154Napalm_6x45            *75gal+2x154Napalm+6x4.5"Rockets
P-47D.*c_tank108_w_2x154Napalm_6x45            *108gal+2x154Napalm+6x4.5"Rockets
P-47D.*c_tank110_w_2x154Napalm_6x45            *110gal+2x154Napalm+6x4.5"Rockets
P-47D.*c_tank200_w_2x154Napalm_6x45            *200gal+2x154Napalm+6x4.5"Rockets
P-47D.*c_1xCluster_w_2x175Napalm               *Frag+2x175Napalm
P-47D.*c_1x500lbs_w_2x175Napalm                *500lb+2x175Napalm
P-47D.*c_tank075_w_2x175Napalm                 *75gal+2x175Napalm
P-47D.*c_tank108_w_2x175Napalm                 *108gal+2x175Napalm
P-47D.*c_tank110_w_2x175Napalm                 *110gal+2x175Napalm
P-47D.*c_tank200_w_2x175Napalm                 *200gal+2x175Napalm
P-47D.*c_1x154Napalm_w_2x175Napalm_6x45        *154Napalm+2x175Napalm+6x4.5"Rockets
P-47D.*c_1xCluster_w_2x175Napalm_6x45          *Frag+2x175Napalm+6x4.5"Rockets
P-47D.*c_1x500lbs_w_2x175Napalm_6x45           *500lb+2x175Napalm+6x4.5"Rockets
P-47D.*c_tank075_w_2x175Napalm_6x45            *75gal+2x175Napalm+6x4.5"Rockets
P-47D.*c_tank108_w_2x175Napalm_6x45            *108gal+2x175Napalm+6x4.5"Rockets
P-47D.*c_tank110_w_2x175Napalm_6x45            *110gal+2x175Napalm+6x4.5"Rockets
P-47D.*c_tank200_w_2x175Napalm_6x45            *200gal+2x175Napalm+6x4.5"Rockets
P-47D.none                                     Empty
