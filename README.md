# indoorpositioning
Android app using mobile sensors for realizing indoor posistioning system

Princip řešení spočívá ve využití mobilních senzorů - především akcelerometr, magnetometr, barometr a gyroskop a okrajově bluetooth pro komunikaci s ostatními zařízeními.
 
Algoritmus k eliminaci odstředivé síly, vznikající při pohybu s telefonem po budově, která narušuje čistý vektor pohybu zařízení vzhledem k gravitačnímu a magnetickému poli. Za použití kvaternionů získaných v kombinaci s magnetometrem je možné takový vektor přepočítat na prostor reálného světa vzhledem ke světovým stranám. S pomocí tohoto vektoru je možné určit rychlost pohybu uživatele a směr, což v kombinaci s barometrem (na získání informace o aktuálně využívaném poschodí budovy) může dát poměrně přesnou informaci o pohybu uživatele v prostoru budovy. Pohyb je následně analyzován společně se silou signálů z bluetooth beaconů rozmístěných po FIMu, čímž se uzavře možná množina cest pohybu. Pro zpřesnění výsledků jsou data podrobena realtime statistickým výpočtům, eliminující chybné určení pohybu.


Zdroje vědecké články a navázání na seminární práce na SMAP:

[1]	Motion Sensors. Developers Android [online]. [cit. 2018-01-28]. Dostupné z: https://developer.android.com/guide/topics/sensors/sensors_motion.html. 

[2]	DİRİCAN, Ahmet Cengizhan a Selim AKSOY. STEP COUNTING USING SMARTPHONE ACCELEROMETER AND FAST FOURIER TRANSFORM [online]. Kabul, 2016 [cit. 2018-01-28]. Research Article. Gebze Technical Universit.
http://apps.webofknowledge.com/full_record.do?product=WOS&search_mode=GeneralSearch&qid=25&SID=E5HgieUlJcZoz47Xwap&page=1&doc=1

[3]	Indoor Integrated Navigation and Synchronous Data Acquisition Method for Android Smartphone By:Hu, CS (Hu, Chunsheng) ; Wei, WJ (Wei, Wenjian) ; Qin, SQ (Qin, Shiqiao) ; Wang, XS (Wang, Xingshu) ; Habib, A (Habib, Ayman) ; Wang, RS (Wang, Ruisheng)[cit. 2018-01-28]. [online]
http://apps.webofknowledge.com/full_record.do?product=WOS&search_mode=GeneralSearch&qid=2&SID=D45kzucMbWSFBSvgrVY&page=1&doc=3

[4]	JEDLIČKA, Martin. Určování pozice v budovách pomocí Wi-Fi sítí. [cit. 2018-01-28].

[5]	KULAWIAK Marcin, WYCINKA Witold. DYNAMIC SIGNAL STRENGTH MAPPING AND ANALYSIS BY MEANS OF MOBILE GEOGRAPHIC INFORMATION SYSTEM [online]. Gdansk, 2017 [cit. 2018-01-28]. Research Article. Gdansk Univ Technol.[online]
http://apps.webofknowledge.com/full_record.do?product=WOS&search_mode=GeneralSearch&qid=18&SID=E5HgieUlJcZoz47Xwap&page=1&doc=1

[6]	Tan, JQ (Tan, Jieqing); Xing, Y (Xing, Yan); Fan, W (Fan, Wen); Hong, PL (Hong, Peilin); Smooth orientation interpolation using parametric quintic-polynomial-based quaternion spline curve [cit. 2018-01-28].[online] http://apps.webofknowledge.com/full_record.do?product=WOS&search_mode=GeneralSearch&qid=21&SID=E5HgieUlJcZoz47Xwap&page=1&doc=9

[7]	Indoor Integrated Navigation and Synchronous Data Acquisition Method for Android Smartphone By:Hu, CS (Hu, Chunsheng) ; Wei, WJ (Wei, Wenjian) ; Qin, SQ (Qin, Shiqiao) ; Wang, XS (Wang, Xingshu) ; Habib, A (Habib, Ayman) ; Wang, RS (Wang, Ruisheng) [online] [cit. 2018-01-28].
http://apps.webofknowledge.com/full_record.do?product=WOS&search_mode=GeneralSearch&qid=2&SID=D45kzucMbWSFBSvgrVY&page=1&doc=3


