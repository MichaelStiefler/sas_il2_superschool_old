******************************************************************************************************************
*                                   Installation Instructions:                                                   *
******************************************************************************************************************

1.) Extract to contents of this mod to the "#SAS" folder of your Modact 5.x game.

2.) Open air.ini (in Modact 5 you find this file at "<Your Game Folder>\#SAS\STD\com\maddox\il2\objects\air.ini").
    Find the following lines:

P-47D-10          air.P_47D10 1                                 usa01 DESERT 
P-47D-22          air.P_47D22 1                         NOINFO  usa01 DESERT 
P-47D-27          air.P_47D27 1                                 usa01 DESERT 
P-47D             air.P_47D 1                           NOINFO  usa01 DESERT

3.) In Front of the above mentioned lines from step 1.) add the following lines:

P-35              air.P_35 1                            NOINFO  usa01 SUMMER
P-47B-1           air.P_47B1 1                          NOINFO  usa01 SUMMER
P-47B-15          air.P_47B15 1                         NOINFO  usa01 SUMMER
P-47B-DT          air.P_47BDT 1                         NOINFO  usa01 SUMMER
P-47C-5           air.P_47C5 1                                  usa01 DESERT

4.) Behind the above mentioned lines from step 1.) add the following lines:

P-47D-30          air.P_47D30 1                         NOINFO  usa01 DESERT 
P-47D-40          air.P_47D40 1                         NOINFO  usa01 DESERT
P-47M             air.P_47M 1                           NOINFO  usa01 DESERT
P-47N-15          air.P_47N15 1                                 usa01 DESERT 

5.) Eventually the Jugs block of your air.ini should look like this (check and confirm!):

P-35              air.P_35 1                            NOINFO  usa01 SUMMER
P-47B-1           air.P_47B1 1                          NOINFO  usa01 SUMMER
P-47B-15          air.P_47B15 1                         NOINFO  usa01 SUMMER
P-47B-DT          air.P_47BDT 1                         NOINFO  usa01 SUMMER
P-47C-5           air.P_47C5 1                                  usa01 DESERT
P-47D-10          air.P_47D10 1                                 usa01 DESERT 
P-47D-22          air.P_47D22 1                         NOINFO  usa01 DESERT 
P-47D-27          air.P_47D27 1                                 usa01 DESERT 
P-47D             air.P_47D 1                           NOINFO  usa01 DESERT
P-47D-30          air.P_47D30 1                         NOINFO  usa01 DESERT 
P-47D-40          air.P_47D40 1                         NOINFO  usa01 DESERT
P-47M             air.P_47M 1                           NOINFO  usa01 DESERT
P-47N-15          air.P_47N15 1                                 usa01 DESERT 

6.) Scroll down for further optional .properties file modifications.










******************************************************************************************************************
*                                                  Contents:                                                     *
******************************************************************************************************************

This mod gives you more than 1000 ordnance options, including droptanks of various
sizes, historically correct bomb loadouts (1000lb bombs on wings only, center line
is limited to 500lb bombs), Napalm tanks and Fragmentation bombs.

This mod includes enhanced Stock aircraft (D-10, 22, 27 and 27 late), mods which
have been around standalone before (B-1, B-15, C-5, D-30, D-40, N-15) and new
aircraft (P-47B "Double Twister").

This mod includes enhancements like "better wheels" and "improved engines and
cowlings" which are derived from dedicated mods available before.

You will find the new ordnance options below the stock ones marked with an asterisk
"*" in front of the ordnance description for stock planes D-10, 22, 27 and 27 late.
(this is to avoid incompatibilities with stock maps, missions and campaigns).

******************************************************************************************************************
*                                                  Credits:                                                      *
******************************************************************************************************************

Credits:
- Original 3D & code by 1C/Maddox.
- Cockpits by Poncho, Taurus, Freddy and others
- Gunsight reticles by NonWonderdog.
- P-47B-1
- P-47D-30 and P-47D-40 based on dedicated mods by Kumpel
- P-47M based on original mod by barrett_g
- P-47N-15 based on dedicated mod by 101tfs
- P-47N-15 FM edit and some weapons hooks by crazyflak
- P-47C-5 based on dedicated mod by Spitwulf with assistance by PA_Jeronimo
- P-47B-1 and B-15 based on dedicated mods by SAS~Riken
- Other Modders involved based on dedicated Mod's readme's:
  VH-Rock, Bombsaway, Friction, CirX, Loku, Sani, Pavlac, Jaypack44, Red_Fox90,
  Birdman, Jafa, Docholiday, Nightshifter and many, many others
  (sorry to say but there's so much content which made it into this pack,
  it's simply impossible to grab all members involved. Give a call if we forgot
  to mention you here and bear with us, we didn't mean it bad in any way)
- Java Code and new ordnance arrangement by barrett_g and SAS~Storebror
- P-47B "Double Twister" based on P-47B-15 by SAS~Storebror





















******************************************************************************************************************
*                                  Optional properties modifications:                                            *
******************************************************************************************************************

1.) Open plane.properties (Modact 5, can be found at "<Your Game Folder>\#SAS\STD\i18n\plane.properties")
    or plane_ru.properties (other game versions, officially unsupported).

2.) At the end of the file, add the following lines:
   
P-35                 P-35, 1937
P-47B-1              P-47B-1, 1941
P-47B-15             P-47B-15, 1942
P-47B-DT             P-47B Double Twister, 1942
P-47C-5              P-47C-5, 1942
P-47D-30             P-47D-30, 1944
P-47D-40             P-47D-40, 1944
P-47M                P-47M, 1944
P-47N-15             P-47N-15, 1945

3.) Open weapons.properties (Modact 5, can be found at "<Your Game Folder>\#SAS\STD\i18n\weapons.properties")
    or weapons_ru.properties (other game versions, officially unsupported).
    
4.) At the end of the file, add the following lines (yes, it's 1264 lines to be added, seriously. We know
    that this is roughly 1/4th of the original stock weapons.properties file size. Trust us: It's worth it.):

#####################################################################
# P-35
#####################################################################
P-35.default                           Default
P-35.2x100lbs_Bombs                    2x100 lb. Bombs
P-35.none                              Empty

#####################################################################
# P-47B-1
#####################################################################
P-47B-1.default                        Default
P-47B-1.overload                       Extra Ammunition
P-47B-1.none                           Empty

#####################################################################
# P-47B-15
#####################################################################
P-47B-15.default                       Default
P-47B-15.overload                      Extra Ammunition
P-47B-15.none                          Empty

#####################################################################
# P-47B-DT
#####################################################################
P-47B-DT.default                       Default
P-47B-DT.overload                      Extra Ammunition
P-47B-DT.none                          Empty

#####################################################################
# P-47C-5
#####################################################################
P-47C-5.default                        Default
P-47C-5.overload                       Extra Ammunition
P-47C-5.6x45                           6x4.5" Rockets
P-47C-5.6x45_overload                  6x4.5"+Ammo
P-47C-5.c_tank075                      75gal Droptank
P-47C-5.c_tank075_overload             75gal.DT+Ammo
P-47C-5.c_tank075_6x45                 75gal.DT+6x4.5"
P-47C-5.c_tank075_6x45_overload        75gal.DT+6x4.5"+Ammo
P-47C-5.c_tank108                      108gal Droptank
P-47C-5.c_tank108_overload             108gal.DT+Ammo
P-47C-5.c_tank108_6x45                 108gal.DT+6x4.5"
P-47C-5.c_tank108_6x45_overload        108gal.DT+6x4.5"+Ammo
P-47C-5.c_tank110                      110gal Droptank
P-47C-5.c_tank110_overload             110gal.DT+Ammo
P-47C-5.c_tank110_6x45                 110gal.DT+6x4.5"
P-47C-5.c_tank110_6x45_overload        110gal.DT+6x4.5"+Ammo
P-47C-5.c_tank200                      200gal Droptank
P-47C-5.c_tank200_overload             200gal.DT+Ammo
P-47C-5.c_tank200_6x45                 200gal.DT+6x4.5"
P-47C-5.c_tank200_6x45_overload        200gal.DT+6x4.5"+Ammo
P-47C-5.c_500lbs                       1x500 lb. Bomb
P-47C-5.c_500lbs_overload              500lb+Ammo
P-47C-5.c_500lbs_6x45                  500lb+6x4.5"
P-47C-5.c_500lbs_6x45_overload         500lb+6x4.5"+Ammo
P-47C-5.c_M17                          1xM17 Inc. Cluster Bomb
P-47C-5.c_M17_overload                 M17+Ammo
P-47C-5.c_M17_6x45                     M17+6x4.5"
P-47C-5.c_M17_6x45_overload            M17+6x4.5"+Ammo
P-47C-5.c_M26                          1xM26 Cluster Bomb
P-47C-5.c_M26_overload                 M26+Ammo
P-47C-5.c_M26_6x45                     M26+6x4.5"
P-47C-5.c_M26_6x45_overload            M26+6x4.5"+Ammo
P-47C-5.c_Nap110                       1x110gal Napalm Tank Bomb
P-47C-5.c_Nap110_overload              110Nap+Ammo
P-47C-5.c_Nap110_6x45                  110Nap+6x4.5"
P-47C-5.c_Nap110_6x45_overload         110Nap+6x4.5"+Ammo
P-47C-5.none                           Empty

#####################################################################
# P-47D-10
#####################################################################
P-47D-10.*default                      *Default
P-47D-10.*overload                     *Extra Ammunition
P-47D-10.*6x45                         *6x4.5" Rockets
P-47D-10.*6x45_overload                *6x4.5"+Ammo
P-47D-10.*c_tank075                    *75gal Droptank
P-47D-10.*c_tank075_overload           *75gal.DT+Ammo
P-47D-10.*c_tank075_6x45               *75gal.DT+6x4.5"
P-47D-10.*c_tank075_6x45_overload      *75gal.DT+6x4.5"+Ammo
P-47D-10.*c_tank108                    *108gal Droptank
P-47D-10.*c_tank108_overload           *108gal.DT+Ammo
P-47D-10.*c_tank108_6x45               *108gal.DT+6x4.5"
P-47D-10.*c_tank108_6x45_overload      *108gal.DT+6x4.5"+Ammo
P-47D-10.*c_tank110                    *110gal Droptank
P-47D-10.*c_tank110_overload           *110gal.DT+Ammo
P-47D-10.*c_tank110_6x45               *110gal.DT+6x4.5"
P-47D-10.*c_tank110_6x45_overload      *110gal.DT+6x4.5"+Ammo
P-47D-10.*c_tank200                    *200gal Droptank
P-47D-10.*c_tank200_overload           *200gal.DT+Ammo
P-47D-10.*c_tank200_6x45               *200gal.DT+6x4.5"
P-47D-10.*c_tank200_6x45_overload      *200gal.DT+6x4.5"+Ammo
P-47D-10.*c_500lbs                     *1x500 lb. Bomb
P-47D-10.*c_500lbs_overload            *500lb+Ammo
P-47D-10.*c_500lbs_6x45                *500lb+6x4.5"
P-47D-10.*c_500lbs_6x45_overload       *500lb+6x4.5"+Ammo
P-47D-10.*c_M17                        *1xM17 Inc. Cluster Bomb
P-47D-10.*c_M17_overload               *M17+Ammo
P-47D-10.*c_M17_6x45                   *M17+6x4.5"
P-47D-10.*c_M17_6x45_overload          *M17+6x4.5"+Ammo
P-47D-10.*c_M26                        *1xM26 Cluster Bomb
P-47D-10.*c_M26_overload               *M26+Ammo
P-47D-10.*c_M26_6x45                   *M26+6x4.5"
P-47D-10.*c_M26_6x45_overload          *M26+6x4.5"+Ammo
P-47D-10.*c_Nap110                     *1x110gal Napalm Tank Bomb
P-47D-10.*c_Nap110_overload            *110Nap+Ammo
P-47D-10.*c_Nap110_6x45                *110Nap+6x4.5"
P-47D-10.*c_Nap110_6x45_overload       *110Nap+6x4.5"+Ammo

#####################################################################
# P-47D-22
#####################################################################
P-47D-22.*default                      *Default
P-47D-22.*overload                     *Extra Ammunition
P-47D-22.*6x45                         *6x4.5" Rockets
P-47D-22.*6x45_overload                *6x4.5"+Ammo
P-47D-22.*w_tank108                    *2x108gal. Droptanks
P-47D-22.*w_tank108_6x45               *2x108gal.DT+6x4.5" Rockets
P-47D-22.*w_tank110                    *2x110gal. Droptanks
P-47D-22.*w_tank110_6x45               *2x110gal.DT+6x4.5" Rockets
P-47D-22.*w_tank165                    *2x165gal. Droptanks
P-47D-22.*w_tank165_6x45               *2x165gal.DT+6x4.5" Rockets
P-47D-22.*w_500lbs                     *2x500 lb. Bombs
P-47D-22.*w_500lbs_6x45                *2x500lb+6x4.5" Rockets
P-47D-22.*w_1000lbs                    *2x1000 lb. Bombs
P-47D-22.*w_1000lbs_6x45               *2x1000lb+6x4.5" Rockets
P-47D-22.*w_M17                        *2xM17 Inc. Cluster Bombs
P-47D-22.*w_M17_6x45                   *2xM17+6x4.5" Rockets
P-47D-22.*w_M26                        *2xM26 Cluster Bombs
P-47D-22.*w_M26_6x45                   *2xM26+6x4.5" Rockets
P-47D-22.*w_Nap110                     *2x110gal Napalm Tank Bombs
P-47D-22.*w_Nap110_6x45                *2x110Nap+6x4.5" Rockets
P-47D-22.*c_tank075                    *75gal Droptank
P-47D-22.*c_tank075_overload           *75gal.DT+Ammo
P-47D-22.*c_tank075_6x45               *75gal.DT+6x4.5"
P-47D-22.*c_tank075_6x45_overload      *75gal.DT+6x4.5"+Ammo
P-47D-22.*c_tank075_w_tank108          *75gal+2x108gal.DT
P-47D-22.*c_tank075_w_tank108_6x45     *75gal+2x108gal+6x4.5"
P-47D-22.*c_tank075_w_tank110          *75gal+2x110gal.DT
P-47D-22.*c_tank075_w_tank110_6x45     *75gal+2x110gal+6x4.5"
P-47D-22.*c_tank075_w_tank165          *75gal+2x165gal.DT
P-47D-22.*c_tank075_w_tank165_6x45     *75gal+2x165gal+6x4.5"
P-47D-22.*c_tank075_w_500lbs           *75gal+2x500lb
P-47D-22.*c_tank075_w_500lbs_6x45      *75gal+2x500lb+6x4.5"
P-47D-22.*c_tank075_w_1000lbs          *75gal+2x1000lb
P-47D-22.*c_tank075_w_1000lbs_6x45     *75gal+2x1000lb+6x4.5"
P-47D-22.*c_tank075_w_M17              *75gal+2xM17
P-47D-22.*c_tank075_w_M17_6x45         *75gal+2xM17+6x4.5"
P-47D-22.*c_tank075_w_M26              *75gal+2xM26
P-47D-22.*c_tank075_w_M26_6x45         *75gal+2xM26+6x4.5"
P-47D-22.*c_tank075_w_Nap110           *75gal+2x110Nap
P-47D-22.*c_tank075_w_Nap110_6x45      *75gal+2x110Nap+6x4.5"
P-47D-22.*c_tank108                    *108gal Droptank
P-47D-22.*c_tank108_overload           *108gal.DT+Ammo
P-47D-22.*c_tank108_6x45               *108gal.DT+6x4.5"
P-47D-22.*c_tank108_6x45_overload      *108gal.DT+6x4.5"+Ammo
P-47D-22.*c_tank108_w_tank108          *3x108gal Droptank
P-47D-22.*c_tank108_w_tank108_6x45     *3x108gal+6x4.5"
P-47D-22.*c_tank108_w_tank110          *108gal+2x110gal.DT
P-47D-22.*c_tank108_w_tank110_6x45     *108gal+2x110gal+6x4.5"
P-47D-22.*c_tank108_w_tank165          *108gal+2x165gal.DT
P-47D-22.*c_tank108_w_tank165_6x45     *108gal+2x165gal+6x4.5"
P-47D-22.*c_tank108_w_500lbs           *108gal+2x500lb
P-47D-22.*c_tank108_w_500lbs_6x45      *108gal+2x500lb+6x4.5"
P-47D-22.*c_tank108_w_1000lbs          *108gal+2x1000lb
P-47D-22.*c_tank108_w_1000lbs_6x45     *108gal+2x1000lb+6x4.5"
P-47D-22.*c_tank108_w_M17              *108gal+2xM17
P-47D-22.*c_tank108_w_M17_6x45         *108gal+2xM17+6x4.5"
P-47D-22.*c_tank108_w_M26              *108gal+2xM26
P-47D-22.*c_tank108_w_M26_6x45         *108gal+2xM26+6x4.5"
P-47D-22.*c_tank108_w_Nap110           *108gal+2x110Nap
P-47D-22.*c_tank108_w_Nap110_6x45      *108gal+2x110Nap+6x4.5"
P-47D-22.*c_tank110                    *110gal Droptank
P-47D-22.*c_tank110_overload           *110gal.DT+Ammo
P-47D-22.*c_tank110_6x45               *110gal.DT+6x4.5"
P-47D-22.*c_tank110_6x45_overload      *110gal.DT+6x4.5"+Ammo
P-47D-22.*c_tank110_w_tank108          *110gal+2x108gal.DT
P-47D-22.*c_tank110_w_tank108_6x45     *110gal+2x108gal+6x4.5"
P-47D-22.*c_tank110_w_tank110          *3x110gal Droptank
P-47D-22.*c_tank110_w_tank110_6x45     *3x110gal+6x4.5"
P-47D-22.*c_tank110_w_tank165          *110gal+2x165gal.DT
P-47D-22.*c_tank110_w_tank165_6x45     *110gal+2x165gal+6x4.5"
P-47D-22.*c_tank110_w_500lbs           *110gal+2x500lb
P-47D-22.*c_tank110_w_500lbs_6x45      *110gal+2x500lb+6x4.5"
P-47D-22.*c_tank110_w_1000lbs          *110gal+2x1000lb
P-47D-22.*c_tank110_w_1000lbs_6x45     *110gal+2x1000lb+6x4.5"
P-47D-22.*c_tank110_w_M17              *110gal+2xM17
P-47D-22.*c_tank110_w_M17_6x45         *110gal+2xM17+6x4.5"
P-47D-22.*c_tank110_w_M26              *110gal+2xM26
P-47D-22.*c_tank110_w_M26_6x45         *110gal+2xM26+6x4.5"
P-47D-22.*c_tank110_w_Nap110           *110gal+2x110Nap
P-47D-22.*c_tank110_w_Nap110_6x45      *110gal+2x110Nap+6x4.5"
P-47D-22.*c_tank200                    *200gal Droptank
P-47D-22.*c_tank200_overload           *200gal.DT+Ammo
P-47D-22.*c_tank200_6x45               *200gal.DT+6x4.5"
P-47D-22.*c_tank200_6x45_overload      *200gal.DT+6x4.5"+Ammo
P-47D-22.*c_tank200_w_tank108          *200gal+2x108gal.DT
P-47D-22.*c_tank200_w_tank108_6x45     *200gal+2x108gal+6x4.5"
P-47D-22.*c_tank200_w_tank110          *200gal+2x110gal.DT
P-47D-22.*c_tank200_w_tank110_6x45     *200gal+2x110gal+6x4.5"
P-47D-22.*c_tank200_w_tank165          *200gal+2x165gal.DT
P-47D-22.*c_tank200_w_tank165_6x45     *200gal+2x165gal+6x4.5"
P-47D-22.*c_tank200_w_500lbs           *200gal+2x500lb
P-47D-22.*c_tank200_w_500lbs_6x45      *200gal+2x500lb+6x4.5"
P-47D-22.*c_tank200_w_1000lbs          *200gal+2x1000lb
P-47D-22.*c_tank200_w_1000lbs_6x45     *200gal+2x1000lb+6x4.5"
P-47D-22.*c_tank200_w_M17              *200gal+2xM17
P-47D-22.*c_tank200_w_M17_6x45         *200gal+2xM17+6x4.5"
P-47D-22.*c_tank200_w_M26              *200gal+2xM26
P-47D-22.*c_tank200_w_M26_6x45         *200gal+2xM26+6x4.5"
P-47D-22.*c_tank200_w_Nap110           *200gal+2x110Nap
P-47D-22.*c_tank200_w_Nap110_6x45      *200gal+2x110Nap+6x4.5"
P-47D-22.*c_500lbs                     *1x 500 lb. Bomb
P-47D-22.*c_500lbs_overload            *500lb+Ammo
P-47D-22.*c_500lbs_6x45                *500lb+6x4.5"
P-47D-22.*c_500lbs_6x45_overload       *500lb+6x4.5"+Ammo
P-47D-22.*c_500lbs_w_tank108           *500lb+2x108gal.DT
P-47D-22.*c_500lbs_w_tank108_6x45      *500lb+2x108gal+6x4.5"
P-47D-22.*c_500lbs_w_tank110           *500lb+2x110gal.DT
P-47D-22.*c_500lbs_w_tank110_6x45      *500lb+2x110gal+6x4.5"
P-47D-22.*c_500lbs_w_tank165           *500lb+2x165gal.DT
P-47D-22.*c_500lbs_w_tank165_6x45      *500lb+2x165gal+6x4.5"
P-47D-22.*c_500lbs_w_500lbs            *3x 500 lb. Bomb
P-47D-22.*c_500lbs_w_500lbs_6x45       *3x500lb+6x4.5"
P-47D-22.*c_500lbs_w_1000lbs           *500lb+2x1000lb
P-47D-22.*c_500lbs_w_1000lbs_6x45      *500lb+2x1000lb+6x4.5"
P-47D-22.*c_500lbs_w_M17               *500lb+2xM17
P-47D-22.*c_500lbs_w_M17_6x45          *500lb+2xM17+6x4.5"
P-47D-22.*c_500lbs_w_M26               *500lb+2xM26
P-47D-22.*c_500lbs_w_M26_6x45          *500lb+2xM26+6x4.5"
P-47D-22.*c_500lbs_w_Nap110            *500lb+2x110Nap
P-47D-22.*c_500lbs_w_Nap110_6x45       *500lb+2x110Nap+6x4.5"
P-47D-22.*c_M17                        *1xM17 Inc. Cluster Bomb
P-47D-22.*c_M17_overload               *M17+Ammo
P-47D-22.*c_M17_6x45                   *M17+6x4.5"
P-47D-22.*c_M17_6x45_overload          *M17+6x4.5"+Ammo
P-47D-22.*c_M17_w_tank108              *M17+2x108gal.DT
P-47D-22.*c_M17_w_tank108_6x45         *M17+2x108gal+6x4.5"
P-47D-22.*c_M17_w_tank110              *M17+2x110gal.DT
P-47D-22.*c_M17_w_tank110_6x45         *M17+2x110gal+6x4.5"
P-47D-22.*c_M17_w_tank165              *M17+2x165gal.DT
P-47D-22.*c_M17_w_tank165_6x45         *M17+2x165gal+6x4.5"
P-47D-22.*c_M17_w_500lbs               *M17+2x500lb
P-47D-22.*c_M17_w_500lbs_6x45          *M17+2x500lb+6x4.5"
P-47D-22.*c_M17_w_1000lbs              *M17+2x1000lb
P-47D-22.*c_M17_w_1000lbs_6x45         *M17+2x1000lb+6x4.5"
P-47D-22.*c_M17_w_M17                  *3xM17 Inc. Cluster Bomb
P-47D-22.*c_M17_w_M17_6x45             *3xM17+6x4.5"
P-47D-22.*c_M17_w_M26                  *M17+2xM26
P-47D-22.*c_M17_w_M26_6x45             *M17+2xM26+6x4.5"
P-47D-22.*c_M17_w_Nap110               *M17+2x110Nap
P-47D-22.*c_M17_w_Nap110_6x45          *M17+2x110Nap+6x4.5"
P-47D-22.*c_M26                        *1xM26 Cluster Bomb
P-47D-22.*c_M26_overload               *M26+Ammo
P-47D-22.*c_M26_6x45                   *M26+6x4.5"
P-47D-22.*c_M26_6x45_overload          *M26+6x4.5"+Ammo
P-47D-22.*c_M26_w_tank108              *M26+2x108gal.DT
P-47D-22.*c_M26_w_tank108_6x45         *M26+2x108gal+6x4.5"
P-47D-22.*c_M26_w_tank110              *M26+2x110gal.DT
P-47D-22.*c_M26_w_tank110_6x45         *M26+2x110gal+6x4.5"
P-47D-22.*c_M26_w_tank165              *M26+2x165gal.DT
P-47D-22.*c_M26_w_tank165_6x45         *M26+2x165gal+6x4.5"
P-47D-22.*c_M26_w_500lbs               *M26+2x500lb
P-47D-22.*c_M26_w_500lbs_6x45          *M26+2x500lb+6x4.5"
P-47D-22.*c_M26_w_1000lbs              *M26+2x1000lb
P-47D-22.*c_M26_w_1000lbs_6x45         *M26+2x1000lb+6x4.5"
P-47D-22.*c_M26_w_M17                  *M26+2xM17
P-47D-22.*c_M26_w_M17_6x45             *M26+2xM17+6x4.5"
P-47D-22.*c_M26_w_M26                  *3xM26 Cluster Bomb
P-47D-22.*c_M26_w_M26_6x45             *3xM26+6x4.5"
P-47D-22.*c_M26_w_Nap110               *M26+2x110Nap
P-47D-22.*c_M26_w_Nap110_6x45          *M26+2x110Nap+6x4.5"
P-47D-22.*c_Nap110                     *1x110gal Napalm Tank Bomb
P-47D-22.*c_Nap110_overload            *110Nap+Ammo
P-47D-22.*c_Nap110_6x45                *110Nap+6x4.5"
P-47D-22.*c_Nap110_6x45_overload       *110Nap+6x4.5"+Ammo
P-47D-22.*c_Nap110_w_tank108           *110Nap+2x108gal.DT
P-47D-22.*c_Nap110_w_tank108_6x45      *110Nap+2x108gal+6x4.5"
P-47D-22.*c_Nap110_w_tank110           *110Nap+2x110gal.DT
P-47D-22.*c_Nap110_w_tank110_6x45      *110Nap+2x110gal+6x4.5"
P-47D-22.*c_Nap110_w_tank165           *110Nap+2x165gal.DT
P-47D-22.*c_Nap110_w_tank165_6x45      *110Nap+2x165gal+6x4.5"
P-47D-22.*c_Nap110_w_500lbs            *110Nap+2x500lb
P-47D-22.*c_Nap110_w_500lbs_6x45       *110Nap+2x500lb+6x4.5"
P-47D-22.*c_Nap110_w_1000lbs           *110Nap+2x1000lb
P-47D-22.*c_Nap110_w_1000lbs_6x45      *110Nap+2x1000lb+6x4.5"
P-47D-22.*c_Nap110_w_M17               *110Nap+2xM17
P-47D-22.*c_Nap110_w_M17_6x45          *110Nap+2xM17+6x4.5"
P-47D-22.*c_Nap110_w_M26               *110Nap+2xM26
P-47D-22.*c_Nap110_w_M26_6x45          *110Nap+2xM26+6x4.5"
P-47D-22.*c_Nap110_w_Nap110            *3x110gal Napalm Tank Bomb
P-47D-22.*c_Nap110_w_Nap110_6x45       *3x110Nap+6x4.5"

#####################################################################
# P-47D-27
#####################################################################
P-47D-27.*default                      *Default
P-47D-27.*overload                     *Extra Ammunition
P-47D-27.*6x45                         *6x4.5" Rockets
P-47D-27.*6x45_overload                *6x4.5"+Ammo
P-47D-27.*w_tank108                    *2x108gal. Droptanks
P-47D-27.*w_tank108_6x45               *2x108gal.DT+6x4.5" Rockets
P-47D-27.*w_tank110                    *2x110gal. Droptanks
P-47D-27.*w_tank110_6x45               *2x110gal.DT+6x4.5" Rockets
P-47D-27.*w_tank165                    *2x165gal. Droptanks
P-47D-27.*w_tank165_6x45               *2x165gal.DT+6x4.5" Rockets
P-47D-27.*w_500lbs                     *2x500 lb. Bombs
P-47D-27.*w_500lbs_6x45                *2x500lb+6x4.5" Rockets
P-47D-27.*w_1000lbs                    *2x1000 lb. Bombs
P-47D-27.*w_1000lbs_6x45               *2x1000lb+6x4.5" Rockets
P-47D-27.*w_M17                        *2xM17 Inc. Cluster Bombs
P-47D-27.*w_M17_6x45                   *2xM17+6x4.5" Rockets
P-47D-27.*w_M26                        *2xM26 Cluster Bombs
P-47D-27.*w_M26_6x45                   *2xM26+6x4.5" Rockets
P-47D-27.*w_Nap110                     *2x110gal Napalm Tank Bombs
P-47D-27.*w_Nap110_6x45                *2x110Nap+6x4.5" Rockets
P-47D-27.*c_tank075                    *75gal Droptank
P-47D-27.*c_tank075_overload           *75gal.DT+Ammo
P-47D-27.*c_tank075_6x45               *75gal.DT+6x4.5"
P-47D-27.*c_tank075_6x45_overload      *75gal.DT+6x4.5"+Ammo
P-47D-27.*c_tank075_w_tank108          *75gal+2x108gal.DT
P-47D-27.*c_tank075_w_tank108_6x45     *75gal+2x108gal+6x4.5"
P-47D-27.*c_tank075_w_tank110          *75gal+2x110gal.DT
P-47D-27.*c_tank075_w_tank110_6x45     *75gal+2x110gal+6x4.5"
P-47D-27.*c_tank075_w_tank165          *75gal+2x165gal.DT
P-47D-27.*c_tank075_w_tank165_6x45     *75gal+2x165gal+6x4.5"
P-47D-27.*c_tank075_w_500lbs           *75gal+2x500lb
P-47D-27.*c_tank075_w_500lbs_6x45      *75gal+2x500lb+6x4.5"
P-47D-27.*c_tank075_w_1000lbs          *75gal+2x1000lb
P-47D-27.*c_tank075_w_1000lbs_6x45     *75gal+2x1000lb+6x4.5"
P-47D-27.*c_tank075_w_M17              *75gal+2xM17
P-47D-27.*c_tank075_w_M17_6x45         *75gal+2xM17+6x4.5"
P-47D-27.*c_tank075_w_M26              *75gal+2xM26
P-47D-27.*c_tank075_w_M26_6x45         *75gal+2xM26+6x4.5"
P-47D-27.*c_tank075_w_Nap110           *75gal+2x110Nap
P-47D-27.*c_tank075_w_Nap110_6x45      *75gal+2x110Nap+6x4.5"
P-47D-27.*c_tank108                    *108gal Droptank
P-47D-27.*c_tank108_overload           *108gal.DT+Ammo
P-47D-27.*c_tank108_6x45               *108gal.DT+6x4.5"
P-47D-27.*c_tank108_6x45_overload      *108gal.DT+6x4.5"+Ammo
P-47D-27.*c_tank108_w_tank108          *3x108gal Droptank
P-47D-27.*c_tank108_w_tank108_6x45     *3x108gal+6x4.5"
P-47D-27.*c_tank108_w_tank110          *108gal+2x110gal.DT
P-47D-27.*c_tank108_w_tank110_6x45     *108gal+2x110gal+6x4.5"
P-47D-27.*c_tank108_w_tank165          *108gal+2x165gal.DT
P-47D-27.*c_tank108_w_tank165_6x45     *108gal+2x165gal+6x4.5"
P-47D-27.*c_tank108_w_500lbs           *108gal+2x500lb
P-47D-27.*c_tank108_w_500lbs_6x45      *108gal+2x500lb+6x4.5"
P-47D-27.*c_tank108_w_1000lbs          *108gal+2x1000lb
P-47D-27.*c_tank108_w_1000lbs_6x45     *108gal+2x1000lb+6x4.5"
P-47D-27.*c_tank108_w_M17              *108gal+2xM17
P-47D-27.*c_tank108_w_M17_6x45         *108gal+2xM17+6x4.5"
P-47D-27.*c_tank108_w_M26              *108gal+2xM26
P-47D-27.*c_tank108_w_M26_6x45         *108gal+2xM26+6x4.5"
P-47D-27.*c_tank108_w_Nap110           *108gal+2x110Nap
P-47D-27.*c_tank108_w_Nap110_6x45      *108gal+2x110Nap+6x4.5"
P-47D-27.*c_tank110                    *110gal Droptank
P-47D-27.*c_tank110_overload           *110gal.DT+Ammo
P-47D-27.*c_tank110_6x45               *110gal.DT+6x4.5"
P-47D-27.*c_tank110_6x45_overload      *110gal.DT+6x4.5"+Ammo
P-47D-27.*c_tank110_w_tank108          *110gal+2x108gal.DT
P-47D-27.*c_tank110_w_tank108_6x45     *110gal+2x108gal+6x4.5"
P-47D-27.*c_tank110_w_tank110          *3x110gal Droptank
P-47D-27.*c_tank110_w_tank110_6x45     *3x110gal+6x4.5"
P-47D-27.*c_tank110_w_tank165          *110gal+2x165gal.DT
P-47D-27.*c_tank110_w_tank165_6x45     *110gal+2x165gal+6x4.5"
P-47D-27.*c_tank110_w_500lbs           *110gal+2x500lb
P-47D-27.*c_tank110_w_500lbs_6x45      *110gal+2x500lb+6x4.5"
P-47D-27.*c_tank110_w_1000lbs          *110gal+2x1000lb
P-47D-27.*c_tank110_w_1000lbs_6x45     *110gal+2x1000lb+6x4.5"
P-47D-27.*c_tank110_w_M17              *110gal+2xM17
P-47D-27.*c_tank110_w_M17_6x45         *110gal+2xM17+6x4.5"
P-47D-27.*c_tank110_w_M26              *110gal+2xM26
P-47D-27.*c_tank110_w_M26_6x45         *110gal+2xM26+6x4.5"
P-47D-27.*c_tank110_w_Nap110           *110gal+2x110Nap
P-47D-27.*c_tank110_w_Nap110_6x45      *110gal+2x110Nap+6x4.5"
P-47D-27.*c_tank200                    *200gal Droptank
P-47D-27.*c_tank200_overload           *200gal.DT+Ammo
P-47D-27.*c_tank200_6x45               *200gal.DT+6x4.5"
P-47D-27.*c_tank200_6x45_overload      *200gal.DT+6x4.5"+Ammo
P-47D-27.*c_tank200_w_tank108          *200gal+2x108gal.DT
P-47D-27.*c_tank200_w_tank108_6x45     *200gal+2x108gal+6x4.5"
P-47D-27.*c_tank200_w_tank110          *200gal+2x110gal.DT
P-47D-27.*c_tank200_w_tank110_6x45     *200gal+2x110gal+6x4.5"
P-47D-27.*c_tank200_w_tank165          *200gal+2x165gal.DT
P-47D-27.*c_tank200_w_tank165_6x45     *200gal+2x165gal+6x4.5"
P-47D-27.*c_tank200_w_500lbs           *200gal+2x500lb
P-47D-27.*c_tank200_w_500lbs_6x45      *200gal+2x500lb+6x4.5"
P-47D-27.*c_tank200_w_1000lbs          *200gal+2x1000lb
P-47D-27.*c_tank200_w_1000lbs_6x45     *200gal+2x1000lb+6x4.5"
P-47D-27.*c_tank200_w_M17              *200gal+2xM17
P-47D-27.*c_tank200_w_M17_6x45         *200gal+2xM17+6x4.5"
P-47D-27.*c_tank200_w_M26              *200gal+2xM26
P-47D-27.*c_tank200_w_M26_6x45         *200gal+2xM26+6x4.5"
P-47D-27.*c_tank200_w_Nap110           *200gal+2x110Nap
P-47D-27.*c_tank200_w_Nap110_6x45      *200gal+2x110Nap+6x4.5"
P-47D-27.*c_500lbs                     *1x 500 lb. Bomb
P-47D-27.*c_500lbs_overload            *500lb+Ammo
P-47D-27.*c_500lbs_6x45                *500lb+6x4.5"
P-47D-27.*c_500lbs_6x45_overload       *500lb+6x4.5"+Ammo
P-47D-27.*c_500lbs_w_tank108           *500lb+2x108gal.DT
P-47D-27.*c_500lbs_w_tank108_6x45      *500lb+2x108gal+6x4.5"
P-47D-27.*c_500lbs_w_tank110           *500lb+2x110gal.DT
P-47D-27.*c_500lbs_w_tank110_6x45      *500lb+2x110gal+6x4.5"
P-47D-27.*c_500lbs_w_tank165           *500lb+2x165gal.DT
P-47D-27.*c_500lbs_w_tank165_6x45      *500lb+2x165gal+6x4.5"
P-47D-27.*c_500lbs_w_500lbs            *3x 500 lb. Bomb
P-47D-27.*c_500lbs_w_500lbs_6x45       *3x500lb+6x4.5"
P-47D-27.*c_500lbs_w_1000lbs           *500lb+2x1000lb
P-47D-27.*c_500lbs_w_1000lbs_6x45      *500lb+2x1000lb+6x4.5"
P-47D-27.*c_500lbs_w_M17               *500lb+2xM17
P-47D-27.*c_500lbs_w_M17_6x45          *500lb+2xM17+6x4.5"
P-47D-27.*c_500lbs_w_M26               *500lb+2xM26
P-47D-27.*c_500lbs_w_M26_6x45          *500lb+2xM26+6x4.5"
P-47D-27.*c_500lbs_w_Nap110            *500lb+2x110Nap
P-47D-27.*c_500lbs_w_Nap110_6x45       *500lb+2x110Nap+6x4.5"
P-47D-27.*c_M17                        *1xM17 Inc. Cluster Bomb
P-47D-27.*c_M17_overload               *M17+Ammo
P-47D-27.*c_M17_6x45                   *M17+6x4.5"
P-47D-27.*c_M17_6x45_overload          *M17+6x4.5"+Ammo
P-47D-27.*c_M17_w_tank108              *M17+2x108gal.DT
P-47D-27.*c_M17_w_tank108_6x45         *M17+2x108gal+6x4.5"
P-47D-27.*c_M17_w_tank110              *M17+2x110gal.DT
P-47D-27.*c_M17_w_tank110_6x45         *M17+2x110gal+6x4.5"
P-47D-27.*c_M17_w_tank165              *M17+2x165gal.DT
P-47D-27.*c_M17_w_tank165_6x45         *M17+2x165gal+6x4.5"
P-47D-27.*c_M17_w_500lbs               *M17+2x500lb
P-47D-27.*c_M17_w_500lbs_6x45          *M17+2x500lb+6x4.5"
P-47D-27.*c_M17_w_1000lbs              *M17+2x1000lb
P-47D-27.*c_M17_w_1000lbs_6x45         *M17+2x1000lb+6x4.5"
P-47D-27.*c_M17_w_M17                  *3xM17 Inc. Cluster Bomb
P-47D-27.*c_M17_w_M17_6x45             *3xM17+6x4.5"
P-47D-27.*c_M17_w_M26                  *M17+2xM26
P-47D-27.*c_M17_w_M26_6x45             *M17+2xM26+6x4.5"
P-47D-27.*c_M17_w_Nap110               *M17+2x110Nap
P-47D-27.*c_M17_w_Nap110_6x45          *M17+2x110Nap+6x4.5"
P-47D-27.*c_M26                        *1xM26 Cluster Bomb
P-47D-27.*c_M26_overload               *M26+Ammo
P-47D-27.*c_M26_6x45                   *M26+6x4.5"
P-47D-27.*c_M26_6x45_overload          *M26+6x4.5"+Ammo
P-47D-27.*c_M26_w_tank108              *M26+2x108gal.DT
P-47D-27.*c_M26_w_tank108_6x45         *M26+2x108gal+6x4.5"
P-47D-27.*c_M26_w_tank110              *M26+2x110gal.DT
P-47D-27.*c_M26_w_tank110_6x45         *M26+2x110gal+6x4.5"
P-47D-27.*c_M26_w_tank165              *M26+2x165gal.DT
P-47D-27.*c_M26_w_tank165_6x45         *M26+2x165gal+6x4.5"
P-47D-27.*c_M26_w_500lbs               *M26+2x500lb
P-47D-27.*c_M26_w_500lbs_6x45          *M26+2x500lb+6x4.5"
P-47D-27.*c_M26_w_1000lbs              *M26+2x1000lb
P-47D-27.*c_M26_w_1000lbs_6x45         *M26+2x1000lb+6x4.5"
P-47D-27.*c_M26_w_M17                  *M26+2xM17
P-47D-27.*c_M26_w_M17_6x45             *M26+2xM17+6x4.5"
P-47D-27.*c_M26_w_M26                  *3xM26 Cluster Bomb
P-47D-27.*c_M26_w_M26_6x45             *3xM26+6x4.5"
P-47D-27.*c_M26_w_Nap110               *M26+2x110Nap
P-47D-27.*c_M26_w_Nap110_6x45          *M26+2x110Nap+6x4.5"
P-47D-27.*c_Nap110                     *1x110gal Napalm Tank Bomb
P-47D-27.*c_Nap110_overload            *110Nap+Ammo
P-47D-27.*c_Nap110_6x45                *110Nap+6x4.5"
P-47D-27.*c_Nap110_6x45_overload       *110Nap+6x4.5"+Ammo
P-47D-27.*c_Nap110_w_tank108           *110Nap+2x108gal.DT
P-47D-27.*c_Nap110_w_tank108_6x45      *110Nap+2x108gal+6x4.5"
P-47D-27.*c_Nap110_w_tank110           *110Nap+2x110gal.DT
P-47D-27.*c_Nap110_w_tank110_6x45      *110Nap+2x110gal+6x4.5"
P-47D-27.*c_Nap110_w_tank165           *110Nap+2x165gal.DT
P-47D-27.*c_Nap110_w_tank165_6x45      *110Nap+2x165gal+6x4.5"
P-47D-27.*c_Nap110_w_500lbs            *110Nap+2x500lb
P-47D-27.*c_Nap110_w_500lbs_6x45       *110Nap+2x500lb+6x4.5"
P-47D-27.*c_Nap110_w_1000lbs           *110Nap+2x1000lb
P-47D-27.*c_Nap110_w_1000lbs_6x45      *110Nap+2x1000lb+6x4.5"
P-47D-27.*c_Nap110_w_M17               *110Nap+2xM17
P-47D-27.*c_Nap110_w_M17_6x45          *110Nap+2xM17+6x4.5"
P-47D-27.*c_Nap110_w_M26               *110Nap+2xM26
P-47D-27.*c_Nap110_w_M26_6x45          *110Nap+2xM26+6x4.5"
P-47D-27.*c_Nap110_w_Nap110            *3x110gal Napalm Tank Bomb
P-47D-27.*c_Nap110_w_Nap110_6x45       *3x110Nap+6x4.5"

#####################################################################
# P-47D
#####################################################################
P-47D.*default                         *Default
P-47D.*overload                        *Extra Ammunition
P-47D.*6x45                            *6x4.5" Rockets
P-47D.*6x45_overload                   *6x4.5"+Ammo
P-47D.*w_tank108                       *2x108gal. Droptanks
P-47D.*w_tank108_6x45                  *2x108gal.DT+6x4.5" Rockets
P-47D.*w_tank110                       *2x110gal. Droptanks
P-47D.*w_tank110_6x45                  *2x110gal.DT+6x4.5" Rockets
P-47D.*w_tank165                       *2x165gal. Droptanks
P-47D.*w_tank165_6x45                  *2x165gal.DT+6x4.5" Rockets
P-47D.*w_500lbs                        *2x500 lb. Bombs
P-47D.*w_500lbs_6x45                   *2x500lb+6x4.5" Rockets
P-47D.*w_1000lbs                       *2x1000 lb. Bombs
P-47D.*w_1000lbs_6x45                  *2x1000lb+6x4.5" Rockets
P-47D.*w_M17                           *2xM17 Inc. Cluster Bombs
P-47D.*w_M17_6x45                      *2xM17+6x4.5" Rockets
P-47D.*w_M26                           *2xM26 Cluster Bombs
P-47D.*w_M26_6x45                      *2xM26+6x4.5" Rockets
P-47D.*w_Nap110                        *2x110gal Napalm Tank Bombs
P-47D.*w_Nap110_6x45                   *2x110Nap+6x4.5" Rockets
P-47D.*c_tank075                       *75gal Droptank
P-47D.*c_tank075_overload              *75gal.DT+Ammo
P-47D.*c_tank075_6x45                  *75gal.DT+6x4.5"
P-47D.*c_tank075_6x45_overload         *75gal.DT+6x4.5"+Ammo
P-47D.*c_tank075_w_tank108             *75gal+2x108gal.DT
P-47D.*c_tank075_w_tank108_6x45        *75gal+2x108gal+6x4.5"
P-47D.*c_tank075_w_tank110             *75gal+2x110gal.DT
P-47D.*c_tank075_w_tank110_6x45        *75gal+2x110gal+6x4.5"
P-47D.*c_tank075_w_tank165             *75gal+2x165gal.DT
P-47D.*c_tank075_w_tank165_6x45        *75gal+2x165gal+6x4.5"
P-47D.*c_tank075_w_500lbs              *75gal+2x500lb
P-47D.*c_tank075_w_500lbs_6x45         *75gal+2x500lb+6x4.5"
P-47D.*c_tank075_w_1000lbs             *75gal+2x1000lb
P-47D.*c_tank075_w_1000lbs_6x45        *75gal+2x1000lb+6x4.5"
P-47D.*c_tank075_w_M17                 *75gal+2xM17
P-47D.*c_tank075_w_M17_6x45            *75gal+2xM17+6x4.5"
P-47D.*c_tank075_w_M26                 *75gal+2xM26
P-47D.*c_tank075_w_M26_6x45            *75gal+2xM26+6x4.5"
P-47D.*c_tank075_w_Nap110              *75gal+2x110Nap
P-47D.*c_tank075_w_Nap110_6x45         *75gal+2x110Nap+6x4.5"
P-47D.*c_tank108                       *108gal Droptank
P-47D.*c_tank108_overload              *108gal.DT+Ammo
P-47D.*c_tank108_6x45                  *108gal.DT+6x4.5"
P-47D.*c_tank108_6x45_overload         *108gal.DT+6x4.5"+Ammo
P-47D.*c_tank108_w_tank108             *3x108gal Droptank
P-47D.*c_tank108_w_tank108_6x45        *3x108gal+6x4.5"
P-47D.*c_tank108_w_tank110             *108gal+2x110gal.DT
P-47D.*c_tank108_w_tank110_6x45        *108gal+2x110gal+6x4.5"
P-47D.*c_tank108_w_tank165             *108gal+2x165gal.DT
P-47D.*c_tank108_w_tank165_6x45        *108gal+2x165gal+6x4.5"
P-47D.*c_tank108_w_500lbs              *108gal+2x500lb
P-47D.*c_tank108_w_500lbs_6x45         *108gal+2x500lb+6x4.5"
P-47D.*c_tank108_w_1000lbs             *108gal+2x1000lb
P-47D.*c_tank108_w_1000lbs_6x45        *108gal+2x1000lb+6x4.5"
P-47D.*c_tank108_w_M17                 *108gal+2xM17
P-47D.*c_tank108_w_M17_6x45            *108gal+2xM17+6x4.5"
P-47D.*c_tank108_w_M26                 *108gal+2xM26
P-47D.*c_tank108_w_M26_6x45            *108gal+2xM26+6x4.5"
P-47D.*c_tank108_w_Nap110              *108gal+2x110Nap
P-47D.*c_tank108_w_Nap110_6x45         *108gal+2x110Nap+6x4.5"
P-47D.*c_tank110                       *110gal Droptank
P-47D.*c_tank110_overload              *110gal.DT+Ammo
P-47D.*c_tank110_6x45                  *110gal.DT+6x4.5"
P-47D.*c_tank110_6x45_overload         *110gal.DT+6x4.5"+Ammo
P-47D.*c_tank110_w_tank108             *110gal+2x108gal.DT
P-47D.*c_tank110_w_tank108_6x45        *110gal+2x108gal+6x4.5"
P-47D.*c_tank110_w_tank110             *3x110gal Droptank
P-47D.*c_tank110_w_tank110_6x45        *3x110gal+6x4.5"
P-47D.*c_tank110_w_tank165             *110gal+2x165gal.DT
P-47D.*c_tank110_w_tank165_6x45        *110gal+2x165gal+6x4.5"
P-47D.*c_tank110_w_500lbs              *110gal+2x500lb
P-47D.*c_tank110_w_500lbs_6x45         *110gal+2x500lb+6x4.5"
P-47D.*c_tank110_w_1000lbs             *110gal+2x1000lb
P-47D.*c_tank110_w_1000lbs_6x45        *110gal+2x1000lb+6x4.5"
P-47D.*c_tank110_w_M17                 *110gal+2xM17
P-47D.*c_tank110_w_M17_6x45            *110gal+2xM17+6x4.5"
P-47D.*c_tank110_w_M26                 *110gal+2xM26
P-47D.*c_tank110_w_M26_6x45            *110gal+2xM26+6x4.5"
P-47D.*c_tank110_w_Nap110              *110gal+2x110Nap
P-47D.*c_tank110_w_Nap110_6x45         *110gal+2x110Nap+6x4.5"
P-47D.*c_tank200                       *200gal Droptank
P-47D.*c_tank200_overload              *200gal.DT+Ammo
P-47D.*c_tank200_6x45                  *200gal.DT+6x4.5"
P-47D.*c_tank200_6x45_overload         *200gal.DT+6x4.5"+Ammo
P-47D.*c_tank200_w_tank108             *200gal+2x108gal.DT
P-47D.*c_tank200_w_tank108_6x45        *200gal+2x108gal+6x4.5"
P-47D.*c_tank200_w_tank110             *200gal+2x110gal.DT
P-47D.*c_tank200_w_tank110_6x45        *200gal+2x110gal+6x4.5"
P-47D.*c_tank200_w_tank165             *200gal+2x165gal.DT
P-47D.*c_tank200_w_tank165_6x45        *200gal+2x165gal+6x4.5"
P-47D.*c_tank200_w_500lbs              *200gal+2x500lb
P-47D.*c_tank200_w_500lbs_6x45         *200gal+2x500lb+6x4.5"
P-47D.*c_tank200_w_1000lbs             *200gal+2x1000lb
P-47D.*c_tank200_w_1000lbs_6x45        *200gal+2x1000lb+6x4.5"
P-47D.*c_tank200_w_M17                 *200gal+2xM17
P-47D.*c_tank200_w_M17_6x45            *200gal+2xM17+6x4.5"
P-47D.*c_tank200_w_M26                 *200gal+2xM26
P-47D.*c_tank200_w_M26_6x45            *200gal+2xM26+6x4.5"
P-47D.*c_tank200_w_Nap110              *200gal+2x110Nap
P-47D.*c_tank200_w_Nap110_6x45         *200gal+2x110Nap+6x4.5"
P-47D.*c_500lbs                        *1x 500 lb. Bomb
P-47D.*c_500lbs_overload               *500lb+Ammo
P-47D.*c_500lbs_6x45                   *500lb+6x4.5"
P-47D.*c_500lbs_6x45_overload          *500lb+6x4.5"+Ammo
P-47D.*c_500lbs_w_tank108              *500lb+2x108gal.DT
P-47D.*c_500lbs_w_tank108_6x45         *500lb+2x108gal+6x4.5"
P-47D.*c_500lbs_w_tank110              *500lb+2x110gal.DT
P-47D.*c_500lbs_w_tank110_6x45         *500lb+2x110gal+6x4.5"
P-47D.*c_500lbs_w_tank165              *500lb+2x165gal.DT
P-47D.*c_500lbs_w_tank165_6x45         *500lb+2x165gal+6x4.5"
P-47D.*c_500lbs_w_500lbs               *3x 500 lb. Bomb
P-47D.*c_500lbs_w_500lbs_6x45          *3x500lb+6x4.5"
P-47D.*c_500lbs_w_1000lbs              *500lb+2x1000lb
P-47D.*c_500lbs_w_1000lbs_6x45         *500lb+2x1000lb+6x4.5"
P-47D.*c_500lbs_w_M17                  *500lb+2xM17
P-47D.*c_500lbs_w_M17_6x45             *500lb+2xM17+6x4.5"
P-47D.*c_500lbs_w_M26                  *500lb+2xM26
P-47D.*c_500lbs_w_M26_6x45             *500lb+2xM26+6x4.5"
P-47D.*c_500lbs_w_Nap110               *500lb+2x110Nap
P-47D.*c_500lbs_w_Nap110_6x45          *500lb+2x110Nap+6x4.5"
P-47D.*c_M17                           *1xM17 Inc. Cluster Bomb
P-47D.*c_M17_overload                  *M17+Ammo
P-47D.*c_M17_6x45                      *M17+6x4.5"
P-47D.*c_M17_6x45_overload             *M17+6x4.5"+Ammo
P-47D.*c_M17_w_tank108                 *M17+2x108gal.DT
P-47D.*c_M17_w_tank108_6x45            *M17+2x108gal+6x4.5"
P-47D.*c_M17_w_tank110                 *M17+2x110gal.DT
P-47D.*c_M17_w_tank110_6x45            *M17+2x110gal+6x4.5"
P-47D.*c_M17_w_tank165                 *M17+2x165gal.DT
P-47D.*c_M17_w_tank165_6x45            *M17+2x165gal+6x4.5"
P-47D.*c_M17_w_500lbs                  *M17+2x500lb
P-47D.*c_M17_w_500lbs_6x45             *M17+2x500lb+6x4.5"
P-47D.*c_M17_w_1000lbs                 *M17+2x1000lb
P-47D.*c_M17_w_1000lbs_6x45            *M17+2x1000lb+6x4.5"
P-47D.*c_M17_w_M17                     *3xM17 Inc. Cluster Bomb
P-47D.*c_M17_w_M17_6x45                *3xM17+6x4.5"
P-47D.*c_M17_w_M26                     *M17+2xM26
P-47D.*c_M17_w_M26_6x45                *M17+2xM26+6x4.5"
P-47D.*c_M17_w_Nap110                  *M17+2x110Nap
P-47D.*c_M17_w_Nap110_6x45             *M17+2x110Nap+6x4.5"
P-47D.*c_M26                           *1xM26 Cluster Bomb
P-47D.*c_M26_overload                  *M26+Ammo
P-47D.*c_M26_6x45                      *M26+6x4.5"
P-47D.*c_M26_6x45_overload             *M26+6x4.5"+Ammo
P-47D.*c_M26_w_tank108                 *M26+2x108gal.DT
P-47D.*c_M26_w_tank108_6x45            *M26+2x108gal+6x4.5"
P-47D.*c_M26_w_tank110                 *M26+2x110gal.DT
P-47D.*c_M26_w_tank110_6x45            *M26+2x110gal+6x4.5"
P-47D.*c_M26_w_tank165                 *M26+2x165gal.DT
P-47D.*c_M26_w_tank165_6x45            *M26+2x165gal+6x4.5"
P-47D.*c_M26_w_500lbs                  *M26+2x500lb
P-47D.*c_M26_w_500lbs_6x45             *M26+2x500lb+6x4.5"
P-47D.*c_M26_w_1000lbs                 *M26+2x1000lb
P-47D.*c_M26_w_1000lbs_6x45            *M26+2x1000lb+6x4.5"
P-47D.*c_M26_w_M17                     *M26+2xM17
P-47D.*c_M26_w_M17_6x45                *M26+2xM17+6x4.5"
P-47D.*c_M26_w_M26                     *3xM26 Cluster Bomb
P-47D.*c_M26_w_M26_6x45                *3xM26+6x4.5"
P-47D.*c_M26_w_Nap110                  *M26+2x110Nap
P-47D.*c_M26_w_Nap110_6x45             *M26+2x110Nap+6x4.5"
P-47D.*c_Nap110                        *1x110gal Napalm Tank Bomb
P-47D.*c_Nap110_overload               *110Nap+Ammo
P-47D.*c_Nap110_6x45                   *110Nap+6x4.5"
P-47D.*c_Nap110_6x45_overload          *110Nap+6x4.5"+Ammo
P-47D.*c_Nap110_w_tank108              *110Nap+2x108gal.DT
P-47D.*c_Nap110_w_tank108_6x45         *110Nap+2x108gal+6x4.5"
P-47D.*c_Nap110_w_tank110              *110Nap+2x110gal.DT
P-47D.*c_Nap110_w_tank110_6x45         *110Nap+2x110gal+6x4.5"
P-47D.*c_Nap110_w_tank165              *110Nap+2x165gal.DT
P-47D.*c_Nap110_w_tank165_6x45         *110Nap+2x165gal+6x4.5"
P-47D.*c_Nap110_w_500lbs               *110Nap+2x500lb
P-47D.*c_Nap110_w_500lbs_6x45          *110Nap+2x500lb+6x4.5"
P-47D.*c_Nap110_w_1000lbs              *110Nap+2x1000lb
P-47D.*c_Nap110_w_1000lbs_6x45         *110Nap+2x1000lb+6x4.5"
P-47D.*c_Nap110_w_M17                  *110Nap+2xM17
P-47D.*c_Nap110_w_M17_6x45             *110Nap+2xM17+6x4.5"
P-47D.*c_Nap110_w_M26                  *110Nap+2xM26
P-47D.*c_Nap110_w_M26_6x45             *110Nap+2xM26+6x4.5"
P-47D.*c_Nap110_w_Nap110               *3x110gal Napalm Tank Bomb
P-47D.*c_Nap110_w_Nap110_6x45          *3x110Nap+6x4.5"

#####################################################################
# P-47D-30
#####################################################################
P-47D-30.default                        Default
P-47D-30.overload                       Extra Ammunition
P-47D-30.6x45                           6x4.5" Rockets
P-47D-30.6x45_overload                  6x4.5"+Ammo
P-47D-30.w_tank108                      2x108gal. Droptanks
P-47D-30.w_tank108_6x45                 2x108gal.DT+6x4.5" Rockets
P-47D-30.w_tank110                      2x110gal. Droptanks
P-47D-30.w_tank110_6x45                 2x110gal.DT+6x4.5" Rockets
P-47D-30.w_tank165                      2x165gal. Droptanks
P-47D-30.w_tank165_6x45                 2x165gal.DT+6x4.5" Rockets
P-47D-30.w_500lbs                       2x500 lb. Bombs
P-47D-30.w_500lbs_6x45                  2x500lb+6x4.5" Rockets
P-47D-30.w_1000lbs                      2x1000 lb. Bombs
P-47D-30.w_1000lbs_6x45                 2x1000lb+6x4.5" Rockets
P-47D-30.w_M17                          2xM17 Inc. Cluster Bombs
P-47D-30.w_M17_6x45                     2xM17+6x4.5" Rockets
P-47D-30.w_M26                          2xM26 Cluster Bombs
P-47D-30.w_M26_6x45                     2xM26+6x4.5" Rockets
P-47D-30.w_Nap110                       2x110gal Napalm Tank Bombs
P-47D-30.w_Nap110_6x45                  2x110Nap+6x4.5" Rockets
P-47D-30.c_tank075                      75gal Droptank
P-47D-30.c_tank075_overload             75gal.DT+Ammo
P-47D-30.c_tank075_6x45                 75gal.DT+6x4.5"
P-47D-30.c_tank075_6x45_overload        75gal.DT+6x4.5"+Ammo
P-47D-30.c_tank075_w_tank108            75gal+2x108gal.DT
P-47D-30.c_tank075_w_tank108_6x45       75gal+2x108gal+6x4.5"
P-47D-30.c_tank075_w_tank110            75gal+2x110gal.DT
P-47D-30.c_tank075_w_tank110_6x45       75gal+2x110gal+6x4.5"
P-47D-30.c_tank075_w_tank165            75gal+2x165gal.DT
P-47D-30.c_tank075_w_tank165_6x45       75gal+2x165gal+6x4.5"
P-47D-30.c_tank075_w_500lbs             75gal+2x500lb
P-47D-30.c_tank075_w_500lbs_6x45        75gal+2x500lb+6x4.5"
P-47D-30.c_tank075_w_1000lbs            75gal+2x1000lb
P-47D-30.c_tank075_w_1000lbs_6x45       75gal+2x1000lb+6x4.5"
P-47D-30.c_tank075_w_M17                75gal+2xM17
P-47D-30.c_tank075_w_M17_6x45           75gal+2xM17+6x4.5"
P-47D-30.c_tank075_w_M26                75gal+2xM26
P-47D-30.c_tank075_w_M26_6x45           75gal+2xM26+6x4.5"
P-47D-30.c_tank075_w_Nap110             75gal+2x110Nap
P-47D-30.c_tank075_w_Nap110_6x45        75gal+2x110Nap+6x4.5"
P-47D-30.c_tank108                      108gal Droptank
P-47D-30.c_tank108_overload             108gal.DT+Ammo
P-47D-30.c_tank108_6x45                 108gal.DT+6x4.5"
P-47D-30.c_tank108_6x45_overload        108gal.DT+6x4.5"+Ammo
P-47D-30.c_tank108_w_tank108            3x108gal Droptank
P-47D-30.c_tank108_w_tank108_6x45       3x108gal+6x4.5"
P-47D-30.c_tank108_w_tank110            108gal+2x110gal.DT
P-47D-30.c_tank108_w_tank110_6x45       108gal+2x110gal+6x4.5"
P-47D-30.c_tank108_w_tank165            108gal+2x165gal.DT
P-47D-30.c_tank108_w_tank165_6x45       108gal+2x165gal+6x4.5"
P-47D-30.c_tank108_w_500lbs             108gal+2x500lb
P-47D-30.c_tank108_w_500lbs_6x45        108gal+2x500lb+6x4.5"
P-47D-30.c_tank108_w_1000lbs            108gal+2x1000lb
P-47D-30.c_tank108_w_1000lbs_6x45       108gal+2x1000lb+6x4.5"
P-47D-30.c_tank108_w_M17                108gal+2xM17
P-47D-30.c_tank108_w_M17_6x45           108gal+2xM17+6x4.5"
P-47D-30.c_tank108_w_M26                108gal+2xM26
P-47D-30.c_tank108_w_M26_6x45           108gal+2xM26+6x4.5"
P-47D-30.c_tank108_w_Nap110             108gal+2x110Nap
P-47D-30.c_tank108_w_Nap110_6x45        108gal+2x110Nap+6x4.5"
P-47D-30.c_tank110                      110gal Droptank
P-47D-30.c_tank110_overload             110gal.DT+Ammo
P-47D-30.c_tank110_6x45                 110gal.DT+6x4.5"
P-47D-30.c_tank110_6x45_overload        110gal.DT+6x4.5"+Ammo
P-47D-30.c_tank110_w_tank108            110gal+2x108gal.DT
P-47D-30.c_tank110_w_tank108_6x45       110gal+2x108gal+6x4.5"
P-47D-30.c_tank110_w_tank110            3x110gal Droptank
P-47D-30.c_tank110_w_tank110_6x45       3x110gal+6x4.5"
P-47D-30.c_tank110_w_tank165            110gal+2x165gal.DT
P-47D-30.c_tank110_w_tank165_6x45       110gal+2x165gal+6x4.5"
P-47D-30.c_tank110_w_500lbs             110gal+2x500lb
P-47D-30.c_tank110_w_500lbs_6x45        110gal+2x500lb+6x4.5"
P-47D-30.c_tank110_w_1000lbs            110gal+2x1000lb
P-47D-30.c_tank110_w_1000lbs_6x45       110gal+2x1000lb+6x4.5"
P-47D-30.c_tank110_w_M17                110gal+2xM17
P-47D-30.c_tank110_w_M17_6x45           110gal+2xM17+6x4.5"
P-47D-30.c_tank110_w_M26                110gal+2xM26
P-47D-30.c_tank110_w_M26_6x45           110gal+2xM26+6x4.5"
P-47D-30.c_tank110_w_Nap110             110gal+2x110Nap
P-47D-30.c_tank110_w_Nap110_6x45        110gal+2x110Nap+6x4.5"
P-47D-30.c_tank200                      200gal Droptank
P-47D-30.c_tank200_overload             200gal.DT+Ammo
P-47D-30.c_tank200_6x45                 200gal.DT+6x4.5"
P-47D-30.c_tank200_6x45_overload        200gal.DT+6x4.5"+Ammo
P-47D-30.c_tank200_w_tank108            200gal+2x108gal.DT
P-47D-30.c_tank200_w_tank108_6x45       200gal+2x108gal+6x4.5"
P-47D-30.c_tank200_w_tank110            200gal+2x110gal.DT
P-47D-30.c_tank200_w_tank110_6x45       200gal+2x110gal+6x4.5"
P-47D-30.c_tank200_w_tank165            200gal+2x165gal.DT
P-47D-30.c_tank200_w_tank165_6x45       200gal+2x165gal+6x4.5"
P-47D-30.c_tank200_w_500lbs             200gal+2x500lb
P-47D-30.c_tank200_w_500lbs_6x45        200gal+2x500lb+6x4.5"
P-47D-30.c_tank200_w_1000lbs            200gal+2x1000lb
P-47D-30.c_tank200_w_1000lbs_6x45       200gal+2x1000lb+6x4.5"
P-47D-30.c_tank200_w_M17                200gal+2xM17
P-47D-30.c_tank200_w_M17_6x45           200gal+2xM17+6x4.5"
P-47D-30.c_tank200_w_M26                200gal+2xM26
P-47D-30.c_tank200_w_M26_6x45           200gal+2xM26+6x4.5"
P-47D-30.c_tank200_w_Nap110             200gal+2x110Nap
P-47D-30.c_tank200_w_Nap110_6x45        200gal+2x110Nap+6x4.5"
P-47D-30.c_500lbs                       1x 500 lb. Bomb
P-47D-30.c_500lbs_overload              500lb+Ammo
P-47D-30.c_500lbs_6x45                  500lb+6x4.5"
P-47D-30.c_500lbs_6x45_overload         500lb+6x4.5"+Ammo
P-47D-30.c_500lbs_w_tank108             500lb+2x108gal.DT
P-47D-30.c_500lbs_w_tank108_6x45        500lb+2x108gal+6x4.5"
P-47D-30.c_500lbs_w_tank110             500lb+2x110gal.DT
P-47D-30.c_500lbs_w_tank110_6x45        500lb+2x110gal+6x4.5"
P-47D-30.c_500lbs_w_tank165             500lb+2x165gal.DT
P-47D-30.c_500lbs_w_tank165_6x45        500lb+2x165gal+6x4.5"
P-47D-30.c_500lbs_w_500lbs              3x 500 lb. Bomb
P-47D-30.c_500lbs_w_500lbs_6x45         3x500lb+6x4.5"
P-47D-30.c_500lbs_w_1000lbs             500lb+2x1000lb
P-47D-30.c_500lbs_w_1000lbs_6x45        500lb+2x1000lb+6x4.5"
P-47D-30.c_500lbs_w_M17                 500lb+2xM17
P-47D-30.c_500lbs_w_M17_6x45            500lb+2xM17+6x4.5"
P-47D-30.c_500lbs_w_M26                 500lb+2xM26
P-47D-30.c_500lbs_w_M26_6x45            500lb+2xM26+6x4.5"
P-47D-30.c_500lbs_w_Nap110              500lb+2x110Nap
P-47D-30.c_500lbs_w_Nap110_6x45         500lb+2x110Nap+6x4.5"
P-47D-30.c_M17                          1xM17 Inc. Cluster Bomb
P-47D-30.c_M17_overload                 M17+Ammo
P-47D-30.c_M17_6x45                     M17+6x4.5"
P-47D-30.c_M17_6x45_overload            M17+6x4.5"+Ammo
P-47D-30.c_M17_w_tank108                M17+2x108gal.DT
P-47D-30.c_M17_w_tank108_6x45           M17+2x108gal+6x4.5"
P-47D-30.c_M17_w_tank110                M17+2x110gal.DT
P-47D-30.c_M17_w_tank110_6x45           M17+2x110gal+6x4.5"
P-47D-30.c_M17_w_tank165                M17+2x165gal.DT
P-47D-30.c_M17_w_tank165_6x45           M17+2x165gal+6x4.5"
P-47D-30.c_M17_w_500lbs                 M17+2x500lb
P-47D-30.c_M17_w_500lbs_6x45            M17+2x500lb+6x4.5"
P-47D-30.c_M17_w_1000lbs                M17+2x1000lb
P-47D-30.c_M17_w_1000lbs_6x45           M17+2x1000lb+6x4.5"
P-47D-30.c_M17_w_M17                    3xM17 Inc. Cluster Bomb
P-47D-30.c_M17_w_M17_6x45               3xM17+6x4.5"
P-47D-30.c_M17_w_M26                    M17+2xM26
P-47D-30.c_M17_w_M26_6x45               M17+2xM26+6x4.5"
P-47D-30.c_M17_w_Nap110                 M17+2x110Nap
P-47D-30.c_M17_w_Nap110_6x45            M17+2x110Nap+6x4.5"
P-47D-30.c_M26                          1xM26 Cluster Bomb
P-47D-30.c_M26_overload                 M26+Ammo
P-47D-30.c_M26_6x45                     M26+6x4.5"
P-47D-30.c_M26_6x45_overload            M26+6x4.5"+Ammo
P-47D-30.c_M26_w_tank108                M26+2x108gal.DT
P-47D-30.c_M26_w_tank108_6x45           M26+2x108gal+6x4.5"
P-47D-30.c_M26_w_tank110                M26+2x110gal.DT
P-47D-30.c_M26_w_tank110_6x45           M26+2x110gal+6x4.5"
P-47D-30.c_M26_w_tank165                M26+2x165gal.DT
P-47D-30.c_M26_w_tank165_6x45           M26+2x165gal+6x4.5"
P-47D-30.c_M26_w_500lbs                 M26+2x500lb
P-47D-30.c_M26_w_500lbs_6x45            M26+2x500lb+6x4.5"
P-47D-30.c_M26_w_1000lbs                M26+2x1000lb
P-47D-30.c_M26_w_1000lbs_6x45           M26+2x1000lb+6x4.5"
P-47D-30.c_M26_w_M17                    M26+2xM17
P-47D-30.c_M26_w_M17_6x45               M26+2xM17+6x4.5"
P-47D-30.c_M26_w_M26                    3xM26 Cluster Bomb
P-47D-30.c_M26_w_M26_6x45               3xM26+6x4.5"
P-47D-30.c_M26_w_Nap110                 M26+2x110Nap
P-47D-30.c_M26_w_Nap110_6x45            M26+2x110Nap+6x4.5"
P-47D-30.c_Nap110                       1x110gal Napalm Tank Bomb
P-47D-30.c_Nap110_overload              110Nap+Ammo
P-47D-30.c_Nap110_6x45                  110Nap+6x4.5"
P-47D-30.c_Nap110_6x45_overload         110Nap+6x4.5"+Ammo
P-47D-30.c_Nap110_w_tank108             110Nap+2x108gal.DT
P-47D-30.c_Nap110_w_tank108_6x45        110Nap+2x108gal+6x4.5"
P-47D-30.c_Nap110_w_tank110             110Nap+2x110gal.DT
P-47D-30.c_Nap110_w_tank110_6x45        110Nap+2x110gal+6x4.5"
P-47D-30.c_Nap110_w_tank165             110Nap+2x165gal.DT
P-47D-30.c_Nap110_w_tank165_6x45        110Nap+2x165gal+6x4.5"
P-47D-30.c_Nap110_w_500lbs              110Nap+2x500lb
P-47D-30.c_Nap110_w_500lbs_6x45         110Nap+2x500lb+6x4.5"
P-47D-30.c_Nap110_w_1000lbs             110Nap+2x1000lb
P-47D-30.c_Nap110_w_1000lbs_6x45        110Nap+2x1000lb+6x4.5"
P-47D-30.c_Nap110_w_M17                 110Nap+2xM17
P-47D-30.c_Nap110_w_M17_6x45            110Nap+2xM17+6x4.5"
P-47D-30.c_Nap110_w_M26                 110Nap+2xM26
P-47D-30.c_Nap110_w_M26_6x45            110Nap+2xM26+6x4.5"
P-47D-30.c_Nap110_w_Nap110              3x110gal Napalm Tank Bomb
P-47D-30.c_Nap110_w_Nap110_6x45         3x110Nap+6x4.5"
P-47D-30.none                           Empty

#####################################################################
# P-47D-40
#####################################################################
P-47D-40.default                        Default
P-47D-40.overload                       Extra Ammunition
P-47D-40.8xHVAR                         8x5" HVAR Rockets
P-47D-40.8xHVAR_overload                8xHVAR+Ammo
P-47D-40.w_tank108                      2x108gal. Droptanks
P-47D-40.w_tank108_8xHVAR               2x108gal.DT+8xHVAR Rockets
P-47D-40.w_tank110                      2x110gal. Droptanks
P-47D-40.w_tank110_8xHVAR               2x110gal.DT+8xHVAR Rockets
P-47D-40.w_tank165                      2x165gal. Droptanks
P-47D-40.w_tank165_8xHVAR               2x165gal.DT+8xHVAR Rockets
P-47D-40.w_500lbs                       2x500 lb. Bombs
P-47D-40.w_500lbs_8xHVAR                2x500lb+8xHVAR Rockets
P-47D-40.w_1000lbs                      2x1000 lb. Bombs
P-47D-40.w_1000lbs_8xHVAR               2x1000lb+8xHVAR Rockets
P-47D-40.w_M17                          2xM17 Inc. Cluster Bombs
P-47D-40.w_M17_8xHVAR                   2xM17+8xHVAR Rockets
P-47D-40.w_M26                          2xM26 Cluster Bombs
P-47D-40.w_M26_8xHVAR                   2xM26+8xHVAR Rockets
P-47D-40.w_Nap110                       2x110gal Napalm Tank Bombs
P-47D-40.w_Nap110_8xHVAR                2x110Nap+8xHVAR Rockets
P-47D-40.c_tank075                      75gal Droptank
P-47D-40.c_tank075_overload             75gal.DT+Ammo
P-47D-40.c_tank075_8xHVAR               75gal.DT+8xHVAR
P-47D-40.c_tank075_8xHVAR_overload      75gal.DT+8xHVAR+Ammo
P-47D-40.c_tank075_w_tank108            75gal+2x108gal.DT
P-47D-40.c_tank075_w_tank108_8xHVAR     75gal+2x108gal+8xHVAR
P-47D-40.c_tank075_w_tank110            75gal+2x110gal.DT
P-47D-40.c_tank075_w_tank110_8xHVAR     75gal+2x110gal+8xHVAR
P-47D-40.c_tank075_w_tank165            75gal+2x165gal.DT
P-47D-40.c_tank075_w_tank165_8xHVAR     75gal+2x165gal+8xHVAR
P-47D-40.c_tank075_w_500lbs             75gal+2x500lb
P-47D-40.c_tank075_w_500lbs_8xHVAR      75gal+2x500lb+8xHVAR
P-47D-40.c_tank075_w_1000lbs            75gal+2x1000lb
P-47D-40.c_tank075_w_1000lbs_8xHVAR     75gal+2x1000lb+8xHVAR
P-47D-40.c_tank075_w_M17                75gal+2xM17
P-47D-40.c_tank075_w_M17_8xHVAR         75gal+2xM17+8xHVAR
P-47D-40.c_tank075_w_M26                75gal+2xM26
P-47D-40.c_tank075_w_M26_8xHVAR         75gal+2xM26+8xHVAR
P-47D-40.c_tank075_w_Nap110             75gal+2x110Nap
P-47D-40.c_tank075_w_Nap110_8xHVAR      75gal+2x110Nap+8xHVAR
P-47D-40.c_tank108                      108gal Droptank
P-47D-40.c_tank108_overload             108gal.DT+Ammo
P-47D-40.c_tank108_8xHVAR               108gal.DT+8xHVAR
P-47D-40.c_tank108_8xHVAR_overload      108gal.DT+8xHVAR+Ammo
P-47D-40.c_tank108_w_tank108            3x108gal Droptank
P-47D-40.c_tank108_w_tank108_8xHVAR     3x108gal+8xHVAR
P-47D-40.c_tank108_w_tank110            108gal+2x110gal.DT
P-47D-40.c_tank108_w_tank110_8xHVAR     108gal+2x110gal+8xHVAR
P-47D-40.c_tank108_w_tank165            108gal+2x165gal.DT
P-47D-40.c_tank108_w_tank165_8xHVAR     108gal+2x165gal+8xHVAR
P-47D-40.c_tank108_w_500lbs             108gal+2x500lb
P-47D-40.c_tank108_w_500lbs_8xHVAR      108gal+2x500lb+8xHVAR
P-47D-40.c_tank108_w_1000lbs            108gal+2x1000lb
P-47D-40.c_tank108_w_1000lbs_8xHVAR     108gal+2x1000lb+8xHVAR
P-47D-40.c_tank108_w_M17                108gal+2xM17
P-47D-40.c_tank108_w_M17_8xHVAR         108gal+2xM17+8xHVAR
P-47D-40.c_tank108_w_M26                108gal+2xM26
P-47D-40.c_tank108_w_M26_8xHVAR         108gal+2xM26+8xHVAR
P-47D-40.c_tank108_w_Nap110             108gal+2x110Nap
P-47D-40.c_tank108_w_Nap110_8xHVAR      108gal+2x110Nap+8xHVAR
P-47D-40.c_tank110                      110gal Droptank
P-47D-40.c_tank110_overload             110gal.DT+Ammo
P-47D-40.c_tank110_8xHVAR               110gal.DT+8xHVAR
P-47D-40.c_tank110_8xHVAR_overload      110gal.DT+8xHVAR+Ammo
P-47D-40.c_tank110_w_tank108            110gal+2x108gal.DT
P-47D-40.c_tank110_w_tank108_8xHVAR     110gal+2x108gal+8xHVAR
P-47D-40.c_tank110_w_tank110            3x110gal Droptank
P-47D-40.c_tank110_w_tank110_8xHVAR     3x110gal+8xHVAR
P-47D-40.c_tank110_w_tank165            110gal+2x165gal.DT
P-47D-40.c_tank110_w_tank165_8xHVAR     110gal+2x165gal+8xHVAR
P-47D-40.c_tank110_w_500lbs             110gal+2x500lb
P-47D-40.c_tank110_w_500lbs_8xHVAR      110gal+2x500lb+8xHVAR
P-47D-40.c_tank110_w_1000lbs            110gal+2x1000lb
P-47D-40.c_tank110_w_1000lbs_8xHVAR     110gal+2x1000lb+8xHVAR
P-47D-40.c_tank110_w_M17                110gal+2xM17
P-47D-40.c_tank110_w_M17_8xHVAR         110gal+2xM17+8xHVAR
P-47D-40.c_tank110_w_M26                110gal+2xM26
P-47D-40.c_tank110_w_M26_8xHVAR         110gal+2xM26+8xHVAR
P-47D-40.c_tank110_w_Nap110             110gal+2x110Nap
P-47D-40.c_tank110_w_Nap110_8xHVAR      110gal+2x110Nap+8xHVAR
P-47D-40.c_tank200                      200gal Droptank
P-47D-40.c_tank200_overload             200gal.DT+Ammo
P-47D-40.c_tank200_8xHVAR               200gal.DT+8xHVAR
P-47D-40.c_tank200_8xHVAR_overload      200gal.DT+8xHVAR+Ammo
P-47D-40.c_tank200_w_tank108            200gal+2x108gal.DT
P-47D-40.c_tank200_w_tank108_8xHVAR     200gal+2x108gal+8xHVAR
P-47D-40.c_tank200_w_tank110            200gal+2x110gal.DT
P-47D-40.c_tank200_w_tank110_8xHVAR     200gal+2x110gal+8xHVAR
P-47D-40.c_tank200_w_tank165            200gal+2x165gal.DT
P-47D-40.c_tank200_w_tank165_8xHVAR     200gal+2x165gal+8xHVAR
P-47D-40.c_tank200_w_500lbs             200gal+2x500lb
P-47D-40.c_tank200_w_500lbs_8xHVAR      200gal+2x500lb+8xHVAR
P-47D-40.c_tank200_w_1000lbs            200gal+2x1000lb
P-47D-40.c_tank200_w_1000lbs_8xHVAR     200gal+2x1000lb+8xHVAR
P-47D-40.c_tank200_w_M17                200gal+2xM17
P-47D-40.c_tank200_w_M17_8xHVAR         200gal+2xM17+8xHVAR
P-47D-40.c_tank200_w_M26                200gal+2xM26
P-47D-40.c_tank200_w_M26_8xHVAR         200gal+2xM26+8xHVAR
P-47D-40.c_tank200_w_Nap110             200gal+2x110Nap
P-47D-40.c_tank200_w_Nap110_8xHVAR      200gal+2x110Nap+8xHVAR
P-47D-40.c_500lbs                       1x 500 lb. Bomb
P-47D-40.c_500lbs_overload              500lb+Ammo
P-47D-40.c_500lbs_8xHVAR                500lb+8xHVAR
P-47D-40.c_500lbs_8xHVAR_overload       500lb+8xHVAR+Ammo
P-47D-40.c_500lbs_w_tank108             500lb+2x108gal.DT
P-47D-40.c_500lbs_w_tank108_8xHVAR      500lb+2x108gal+8xHVAR
P-47D-40.c_500lbs_w_tank110             500lb+2x110gal.DT
P-47D-40.c_500lbs_w_tank110_8xHVAR      500lb+2x110gal+8xHVAR
P-47D-40.c_500lbs_w_tank165             500lb+2x165gal.DT
P-47D-40.c_500lbs_w_tank165_8xHVAR      500lb+2x165gal+8xHVAR
P-47D-40.c_500lbs_w_500lbs              3x 500 lb. Bomb
P-47D-40.c_500lbs_w_500lbs_8xHVAR       3x500lb+8xHVAR
P-47D-40.c_500lbs_w_1000lbs             500lb+2x1000lb
P-47D-40.c_500lbs_w_1000lbs_8xHVAR      500lb+2x1000lb+8xHVAR
P-47D-40.c_500lbs_w_M17                 500lb+2xM17
P-47D-40.c_500lbs_w_M17_8xHVAR          500lb+2xM17+8xHVAR
P-47D-40.c_500lbs_w_M26                 500lb+2xM26
P-47D-40.c_500lbs_w_M26_8xHVAR          500lb+2xM26+8xHVAR
P-47D-40.c_500lbs_w_Nap110              500lb+2x110Nap
P-47D-40.c_500lbs_w_Nap110_8xHVAR       500lb+2x110Nap+8xHVAR
P-47D-40.c_M17                          1xM17 Inc. Cluster Bomb
P-47D-40.c_M17_overload                 M17+Ammo
P-47D-40.c_M17_8xHVAR                   M17+8xHVAR
P-47D-40.c_M17_8xHVAR_overload          M17+8xHVAR+Ammo
P-47D-40.c_M17_w_tank108                M17+2x108gal.DT
P-47D-40.c_M17_w_tank108_8xHVAR         M17+2x108gal+8xHVAR
P-47D-40.c_M17_w_tank110                M17+2x110gal.DT
P-47D-40.c_M17_w_tank110_8xHVAR         M17+2x110gal+8xHVAR
P-47D-40.c_M17_w_tank165                M17+2x165gal.DT
P-47D-40.c_M17_w_tank165_8xHVAR         M17+2x165gal+8xHVAR
P-47D-40.c_M17_w_500lbs                 M17+2x500lb
P-47D-40.c_M17_w_500lbs_8xHVAR          M17+2x500lb+8xHVAR
P-47D-40.c_M17_w_1000lbs                M17+2x1000lb
P-47D-40.c_M17_w_1000lbs_8xHVAR         M17+2x1000lb+8xHVAR
P-47D-40.c_M17_w_M17                    3xM17 Inc. Cluster Bomb
P-47D-40.c_M17_w_M17_8xHVAR             3xM17+8xHVAR
P-47D-40.c_M17_w_M26                    M17+2xM26
P-47D-40.c_M17_w_M26_8xHVAR             M17+2xM26+8xHVAR
P-47D-40.c_M17_w_Nap110                 M17+2x110Nap
P-47D-40.c_M17_w_Nap110_8xHVAR          M17+2x110Nap+8xHVAR
P-47D-40.c_M26                          1xM26 Cluster Bomb
P-47D-40.c_M26_overload                 M26+Ammo
P-47D-40.c_M26_8xHVAR                   M26+8xHVAR
P-47D-40.c_M26_8xHVAR_overload          M26+8xHVAR+Ammo
P-47D-40.c_M26_w_tank108                M26+2x108gal.DT
P-47D-40.c_M26_w_tank108_8xHVAR         M26+2x108gal+8xHVAR
P-47D-40.c_M26_w_tank110                M26+2x110gal.DT
P-47D-40.c_M26_w_tank110_8xHVAR         M26+2x110gal+8xHVAR
P-47D-40.c_M26_w_tank165                M26+2x165gal.DT
P-47D-40.c_M26_w_tank165_8xHVAR         M26+2x165gal+8xHVAR
P-47D-40.c_M26_w_500lbs                 M26+2x500lb
P-47D-40.c_M26_w_500lbs_8xHVAR          M26+2x500lb+8xHVAR
P-47D-40.c_M26_w_1000lbs                M26+2x1000lb
P-47D-40.c_M26_w_1000lbs_8xHVAR         M26+2x1000lb+8xHVAR
P-47D-40.c_M26_w_M17                    M26+2xM17
P-47D-40.c_M26_w_M17_8xHVAR             M26+2xM17+8xHVAR
P-47D-40.c_M26_w_M26                    3xM26 Cluster Bomb
P-47D-40.c_M26_w_M26_8xHVAR             3xM26+8xHVAR
P-47D-40.c_M26_w_Nap110                 M26+2x110Nap
P-47D-40.c_M26_w_Nap110_8xHVAR          M26+2x110Nap+8xHVAR
P-47D-40.c_Nap110                       1x110gal Napalm Tank Bomb
P-47D-40.c_Nap110_overload              110Nap+Ammo
P-47D-40.c_Nap110_8xHVAR                110Nap+8xHVAR
P-47D-40.c_Nap110_8xHVAR_overload       110Nap+8xHVAR+Ammo
P-47D-40.c_Nap110_w_tank108             110Nap+2x108gal.DT
P-47D-40.c_Nap110_w_tank108_8xHVAR      110Nap+2x108gal+8xHVAR
P-47D-40.c_Nap110_w_tank110             110Nap+2x110gal.DT
P-47D-40.c_Nap110_w_tank110_8xHVAR      110Nap+2x110gal+8xHVAR
P-47D-40.c_Nap110_w_tank165             110Nap+2x165gal.DT
P-47D-40.c_Nap110_w_tank165_8xHVAR      110Nap+2x165gal+8xHVAR
P-47D-40.c_Nap110_w_500lbs              110Nap+2x500lb
P-47D-40.c_Nap110_w_500lbs_8xHVAR       110Nap+2x500lb+8xHVAR
P-47D-40.c_Nap110_w_1000lbs             110Nap+2x1000lb
P-47D-40.c_Nap110_w_1000lbs_8xHVAR      110Nap+2x1000lb+8xHVAR
P-47D-40.c_Nap110_w_M17                 110Nap+2xM17
P-47D-40.c_Nap110_w_M17_8xHVAR          110Nap+2xM17+8xHVAR
P-47D-40.c_Nap110_w_M26                 110Nap+2xM26
P-47D-40.c_Nap110_w_M26_8xHVAR          110Nap+2xM26+8xHVAR
P-47D-40.c_Nap110_w_Nap110              3x110gal Napalm Tank Bomb
P-47D-40.c_Nap110_w_Nap110_8xHVAR       3x110Nap+8xHVAR
P-47D-40.none                           Empty

#####################################################################
# P-47M
#####################################################################
P-47M.default                           Default
P-47M.overload                          Extra Ammunition
P-47M.c_tank075                         75gal Droptank
P-47M.c_tank075_overload                75gal.DT+Ammo
P-47M.c_tank108                         108gal Droptank
P-47M.c_tank108_overload                108gal.DT+Ammo
P-47M.c_tank110                         110gal Droptank
P-47M.c_tank110_overload                110gal.DT+Ammo
P-47M.c_tank200                         200gal Droptank
P-47M.c_tank200_overload                200gal.DT+Ammo
P-47M.c_500lbs                          1x500 lb. Bomb
P-47M.c_500lbs_overload                 500lb+Ammo
P-47M.c_M17                             1xM17 Inc. Cluster Bomb
P-47M.c_M17_overload                    M17+Ammo
P-47M.c_M26                             1xM26 Cluster Bomb
P-47M.c_M26_overload                    M26+Ammo
P-47M.c_Nap110                          1x110gal Napalm Tank Bomb
P-47M.c_Nap110_overload                 110Nap+Ammo
P-47M.none                              Empty

#####################################################################
# P-47N-15
#####################################################################
P-47N-15.default                       Default
P-47N-15.overload                      Extra Ammunition
P-47N-15.10xHVAR                       10x5" HVAR Rockets
P-47N-15.10xHVAR_overload              10xHVAR+Ammo
P-47N-15.w_tank108                     2x108gal. Droptanks
P-47N-15.w_tank108_10xHVAR             2x108gal.DT+10xHVAR Rockets
P-47N-15.w_tank110                     2x110gal. Droptanks
P-47N-15.w_tank110_10xHVAR             2x110gal.DT+10xHVAR Rockets
P-47N-15.w_tank165                     2x165gal. Droptanks
P-47N-15.w_tank165_10xHVAR             2x165gal.DT+10xHVAR Rockets
P-47N-15.w_500lbs                      2x500 lb. Bombs
P-47N-15.w_500lbs_10xHVAR              2x500lb+10xHVAR Rockets
P-47N-15.w_1000lbs                     2x1000 lb. Bombs
P-47N-15.w_1000lbs_10xHVAR             2x1000lb+10xHVAR Rockets
P-47N-15.w_M17                         2xM17 Inc. Cluster Bombs
P-47N-15.w_M17_10xHVAR                 2xM17+10xHVAR Rockets
P-47N-15.w_M26                         2xM26 Cluster Bombs
P-47N-15.w_M26_10xHVAR                 2xM26+10xHVAR Rockets
P-47N-15.w_Nap110                      2x110gal Napalm Tank Bombs
P-47N-15.w_Nap110_10xHVAR              2x110Nap+10xHVAR Rockets
P-47N-15.w_16xHVAR                     16x5" HVAR Rockets
P-47N-15.c_tank075                     75gal Droptank
P-47N-15.c_tank075_overload            75gal.DT+Ammo
P-47N-15.c_tank075_10xHVAR             75gal.DT+10xHVAR
P-47N-15.c_tank075_10xHVAR_overload    75gal.DT+10xHVAR+Ammo
P-47N-15.c_tank075_w_tank108           75gal+2x108gal.DT
P-47N-15.c_tank075_w_tank108_10xHVAR   75gal+2x108gal+10xHVAR
P-47N-15.c_tank075_w_tank110           75gal+2x110gal.DT
P-47N-15.c_tank075_w_tank110_10xHVAR   75gal+2x110gal+10xHVAR
P-47N-15.c_tank075_w_tank165           75gal+2x165gal.DT
P-47N-15.c_tank075_w_tank165_10xHVAR   75gal+2x165gal+10xHVAR
P-47N-15.c_tank075_w_500lbs            75gal+2x500lb
P-47N-15.c_tank075_w_500lbs_10xHVAR    75gal+2x500lb+10xHVAR
P-47N-15.c_tank075_w_1000lbs           75gal+2x1000lb
P-47N-15.c_tank075_w_1000lbs_10xHVAR   75gal+2x1000lb+10xHVAR
P-47N-15.c_tank075_w_M17               75gal+2xM17
P-47N-15.c_tank075_w_M17_10xHVAR       75gal+2xM17+10xHVAR
P-47N-15.c_tank075_w_M26               75gal+2xM26
P-47N-15.c_tank075_w_M26_10xHVAR       75gal+2xM26+10xHVAR
P-47N-15.c_tank075_w_Nap110            75gal+2x110Nap
P-47N-15.c_tank075_w_Nap110_10xHVAR    75gal+2x110Nap+10xHVAR
P-47N-15.c_tank075_w_16xHVAR           75gal+16x5" HVAR Rockets
P-47N-15.c_tank108                     108gal Droptank
P-47N-15.c_tank108_overload            108gal.DT+Ammo
P-47N-15.c_tank108_10xHVAR             108gal.DT+10xHVAR
P-47N-15.c_tank108_10xHVAR_overload    108gal.DT+10xHVAR+Ammo
P-47N-15.c_tank108_w_tank108           3x108gal Droptank
P-47N-15.c_tank108_w_tank108_10xHVAR   3x108gal+10xHVAR
P-47N-15.c_tank108_w_tank110           108gal+2x110gal.DT
P-47N-15.c_tank108_w_tank110_10xHVAR   108gal+2x110gal+10xHVAR
P-47N-15.c_tank108_w_tank165           108gal+2x165gal.DT
P-47N-15.c_tank108_w_tank165_10xHVAR   108gal+2x165gal+10xHVAR
P-47N-15.c_tank108_w_500lbs            108gal+2x500lb
P-47N-15.c_tank108_w_500lbs_10xHVAR    108gal+2x500lb+10xHVAR
P-47N-15.c_tank108_w_1000lbs           108gal+2x1000lb
P-47N-15.c_tank108_w_1000lbs_10xHVAR   108gal+2x1000lb+10xHVAR
P-47N-15.c_tank108_w_M17               108gal+2xM17
P-47N-15.c_tank108_w_M17_10xHVAR       108gal+2xM17+10xHVAR
P-47N-15.c_tank108_w_M26               108gal+2xM26
P-47N-15.c_tank108_w_M26_10xHVAR       108gal+2xM26+10xHVAR
P-47N-15.c_tank108_w_Nap110            108gal+2x110Nap
P-47N-15.c_tank108_w_Nap110_10xHVAR    108gal+2x110Nap+10xHVAR
P-47N-15.c_tank108_w_16xHVAR           108gal+16x5" HVAR Rockets
P-47N-15.c_tank110                     110gal Droptank
P-47N-15.c_tank110_overload            110gal.DT+Ammo
P-47N-15.c_tank110_10xHVAR             110gal.DT+10xHVAR
P-47N-15.c_tank110_10xHVAR_overload    110gal.DT+10xHVAR+Ammo
P-47N-15.c_tank110_w_tank108           110gal+2x108gal.DT
P-47N-15.c_tank110_w_tank108_10xHVAR   110gal+2x108gal+10xHVAR
P-47N-15.c_tank110_w_tank110           3x110gal Droptank
P-47N-15.c_tank110_w_tank110_10xHVAR   3x110gal+10xHVAR
P-47N-15.c_tank110_w_tank165           110gal+2x165gal.DT
P-47N-15.c_tank110_w_tank165_10xHVAR   110gal+2x165gal+10xHVAR
P-47N-15.c_tank110_w_500lbs            110gal+2x500lb
P-47N-15.c_tank110_w_500lbs_10xHVAR    110gal+2x500lb+10xHVAR
P-47N-15.c_tank110_w_1000lbs           110gal+2x1000lb
P-47N-15.c_tank110_w_1000lbs_10xHVAR   110gal+2x1000lb+10xHVAR
P-47N-15.c_tank110_w_M17               110gal+2xM17
P-47N-15.c_tank110_w_M17_10xHVAR       110gal+2xM17+10xHVAR
P-47N-15.c_tank110_w_M26               110gal+2xM26
P-47N-15.c_tank110_w_M26_10xHVAR       110gal+2xM26+10xHVAR
P-47N-15.c_tank110_w_Nap110            110gal+2x110Nap
P-47N-15.c_tank110_w_Nap110_10xHVAR    110gal+2x110Nap+10xHVAR
P-47N-15.c_tank110_w_16xHVAR           110gal+16x5" HVAR Rockets
P-47N-15.c_tank200                     200gal Droptank
P-47N-15.c_tank200_overload            200gal.DT+Ammo
P-47N-15.c_tank200_10xHVAR             200gal.DT+10xHVAR
P-47N-15.c_tank200_10xHVAR_overload    200gal.DT+10xHVAR+Ammo
P-47N-15.c_tank200_w_tank108           200gal+2x108gal.DT
P-47N-15.c_tank200_w_tank108_10xHVAR   200gal+2x108gal+10xHVAR
P-47N-15.c_tank200_w_tank110           200gal+2x110gal.DT
P-47N-15.c_tank200_w_tank110_10xHVAR   200gal+2x110gal+10xHVAR
P-47N-15.c_tank200_w_tank165           200gal+2x165gal.DT
P-47N-15.c_tank200_w_tank165_10xHVAR   200gal+2x165gal+10xHVAR
P-47N-15.c_tank200_w_500lbs            200gal+2x500lb
P-47N-15.c_tank200_w_500lbs_10xHVAR    200gal+2x500lb+10xHVAR
P-47N-15.c_tank200_w_1000lbs           200gal+2x1000lb
P-47N-15.c_tank200_w_1000lbs_10xHVAR   200gal+2x1000lb+10xHVAR
P-47N-15.c_tank200_w_M17               200gal+2xM17
P-47N-15.c_tank200_w_M17_10xHVAR       200gal+2xM17+10xHVAR
P-47N-15.c_tank200_w_M26               200gal+2xM26
P-47N-15.c_tank200_w_M26_10xHVAR       200gal+2xM26+10xHVAR
P-47N-15.c_tank200_w_Nap110            200gal+2x110Nap
P-47N-15.c_tank200_w_Nap110_10xHVAR    200gal+2x110Nap+10xHVAR
P-47N-15.c_tank200_w_16xHVAR           200gal+16x5" HVAR Rockets
P-47N-15.c_500lbs                      1x 500 lb. Bomb
P-47N-15.c_500lbs_overload             500lb+Ammo
P-47N-15.c_500lbs_10xHVAR              500lb+10xHVAR
P-47N-15.c_500lbs_10xHVAR_overload     500lb+10xHVAR+Ammo
P-47N-15.c_500lbs_w_tank108            500lb+2x108gal.DT
P-47N-15.c_500lbs_w_tank108_10xHVAR    500lb+2x108gal+10xHVAR
P-47N-15.c_500lbs_w_tank110            500lb+2x110gal.DT
P-47N-15.c_500lbs_w_tank110_10xHVAR    500lb+2x110gal+10xHVAR
P-47N-15.c_500lbs_w_tank165            500lb+2x165gal.DT
P-47N-15.c_500lbs_w_tank165_10xHVAR    500lb+2x165gal+10xHVAR
P-47N-15.c_500lbs_w_500lbs             3x 500 lb. Bomb
P-47N-15.c_500lbs_w_500lbs_10xHVAR     3x500lb+10xHVAR
P-47N-15.c_500lbs_w_1000lbs            500lb+2x1000lb
P-47N-15.c_500lbs_w_1000lbs_10xHVAR    500lb+2x1000lb+10xHVAR
P-47N-15.c_500lbs_w_M17                500lb+2xM17
P-47N-15.c_500lbs_w_M17_10xHVAR        500lb+2xM17+10xHVAR
P-47N-15.c_500lbs_w_M26                500lb+2xM26
P-47N-15.c_500lbs_w_M26_10xHVAR        500lb+2xM26+10xHVAR
P-47N-15.c_500lbs_w_Nap110             500lb+2x110Nap
P-47N-15.c_500lbs_w_Nap110_10xHVAR     500lb+2x110Nap+10xHVAR
P-47N-15.c_500lbs_w_16xHVAR            500lb+16x5" HVAR Rockets
P-47N-15.c_M17                         1xM17 Inc. Cluster Bomb
P-47N-15.c_M17_overload                M17+Ammo
P-47N-15.c_M17_10xHVAR                 M17+10xHVAR
P-47N-15.c_M17_10xHVAR_overload        M17+10xHVAR+Ammo
P-47N-15.c_M17_w_tank108               M17+2x108gal.DT
P-47N-15.c_M17_w_tank108_10xHVAR       M17+2x108gal+10xHVAR
P-47N-15.c_M17_w_tank110               M17+2x110gal.DT
P-47N-15.c_M17_w_tank110_10xHVAR       M17+2x110gal+10xHVAR
P-47N-15.c_M17_w_tank165               M17+2x165gal.DT
P-47N-15.c_M17_w_tank165_10xHVAR       M17+2x165gal+10xHVAR
P-47N-15.c_M17_w_500lbs                M17+2x500lb
P-47N-15.c_M17_w_500lbs_10xHVAR        M17+2x500lb+10xHVAR
P-47N-15.c_M17_w_1000lbs               M17+2x1000lb
P-47N-15.c_M17_w_1000lbs_10xHVAR       M17+2x1000lb+10xHVAR
P-47N-15.c_M17_w_M17                   3xM17 Inc. Cluster Bomb
P-47N-15.c_M17_w_M17_10xHVAR           3xM17+10xHVAR
P-47N-15.c_M17_w_M26                   M17+2xM26
P-47N-15.c_M17_w_M26_10xHVAR           M17+2xM26+10xHVAR
P-47N-15.c_M17_w_Nap110                M17+2x110Nap
P-47N-15.c_M17_w_Nap110_10xHVAR        M17+2x110Nap+10xHVAR
P-47N-15.c_M17_w_16xHVAR               M17+16x5" HVAR Rockets
P-47N-15.c_M26                         1xM26 Cluster Bomb
P-47N-15.c_M26_overload                M26+Ammo
P-47N-15.c_M26_10xHVAR                 M26+10xHVAR
P-47N-15.c_M26_10xHVAR_overload        M26+10xHVAR+Ammo
P-47N-15.c_M26_w_tank108               M26+2x108gal.DT
P-47N-15.c_M26_w_tank108_10xHVAR       M26+2x108gal+10xHVAR
P-47N-15.c_M26_w_tank110               M26+2x110gal.DT
P-47N-15.c_M26_w_tank110_10xHVAR       M26+2x110gal+10xHVAR
P-47N-15.c_M26_w_tank165               M26+2x165gal.DT
P-47N-15.c_M26_w_tank165_10xHVAR       M26+2x165gal+10xHVAR
P-47N-15.c_M26_w_500lbs                M26+2x500lb
P-47N-15.c_M26_w_500lbs_10xHVAR        M26+2x500lb+10xHVAR
P-47N-15.c_M26_w_1000lbs               M26+2x1000lb
P-47N-15.c_M26_w_1000lbs_10xHVAR       M26+2x1000lb+10xHVAR
P-47N-15.c_M26_w_M17                   M26+2xM17
P-47N-15.c_M26_w_M17_10xHVAR           M26+2xM17+10xHVAR
P-47N-15.c_M26_w_M26                   3xM26 Cluster Bomb
P-47N-15.c_M26_w_M26_10xHVAR           3xM26+10xHVAR
P-47N-15.c_M26_w_Nap110                M26+2x110Nap
P-47N-15.c_M26_w_Nap110_10xHVAR        M26+2x110Nap+10xHVAR
P-47N-15.c_M26_w_16xHVAR               M26+16x5" HVAR Rockets
P-47N-15.c_Nap110                      1x110gal Napalm Tank Bomb
P-47N-15.c_Nap110_overload             110Nap+Ammo
P-47N-15.c_Nap110_10xHVAR              110Nap+10xHVAR
P-47N-15.c_Nap110_10xHVAR_overload     110Nap+10xHVAR+Ammo
P-47N-15.c_Nap110_w_tank108            110Nap+2x108gal.DT
P-47N-15.c_Nap110_w_tank108_10xHVAR    110Nap+2x108gal+10xHVAR
P-47N-15.c_Nap110_w_tank110            110Nap+2x110gal.DT
P-47N-15.c_Nap110_w_tank110_10xHVAR    110Nap+2x110gal+10xHVAR
P-47N-15.c_Nap110_w_tank165            110Nap+2x165gal.DT
P-47N-15.c_Nap110_w_tank165_10xHVAR    110Nap+2x165gal+10xHVAR
P-47N-15.c_Nap110_w_500lbs             110Nap+2x500lb
P-47N-15.c_Nap110_w_500lbs_10xHVAR     110Nap+2x500lb+10xHVAR
P-47N-15.c_Nap110_w_1000lbs            110Nap+2x1000lb
P-47N-15.c_Nap110_w_1000lbs_10xHVAR    110Nap+2x1000lb+10xHVAR
P-47N-15.c_Nap110_w_M17                110Nap+2xM17
P-47N-15.c_Nap110_w_M17_10xHVAR        110Nap+2xM17+10xHVAR
P-47N-15.c_Nap110_w_M26                110Nap+2xM26
P-47N-15.c_Nap110_w_M26_10xHVAR        110Nap+2xM26+10xHVAR
P-47N-15.c_Nap110_w_Nap110             3x110gal Napalm Tank Bomb
P-47N-15.c_Nap110_w_Nap110_10xHVAR     3x110Nap+10xHVAR
P-47N-15.c_Nap110_w_16xHVAR            110Nap+16x5" HVAR Rockets
P-47N-15.none                          Empty
