# indoorpositioning
Android app using mobile sensors for realizing indoor posistioning system

Princip řešení spočívá ve využití mobilních senzorů - především akcelerometr, magnetometr, barometr a gyroskop a okrajově bluetooth pro komunikaci s ostatními zařízeními.
 
Algoritmus k eliminaci odstředivé síly, vznikající při pohybu s telefonem po budově, která narušuje čistý vektor pohybu zařízení vzhledem k gravitačnímu a magnetickému poli. Za použití kvaternionů získaných v kombinaci s magnetometrem je možné takový vektor přepočítat na prostor reálného světa vzhledem ke světovým stranám. S pomocí tohoto vektoru je možné určit rychlost pohybu uživatele a směr, což v kombinaci s barometrem (na získání informace o aktuálně využívaném poschodí budovy) může dát poměrně přesnou informaci o pohybu uživatele v prostoru budovy. Pohyb je následně analyzován společně se silou signálů z bluetooth beaconů rozmístěných po FIMu, čímž se uzavře možná množina cest pohybu. Pro zpřesnění výsledků jsou data podrobena realtime statistickým výpočtům, eliminující chybné určení pohybu.


Zdroje vědecké články:
[1] Rigorous Performance Evaluation of Smartphone GNSS/IMU Sensors for ITS Applications
By:Gikas, V (Gikas, Vassilis) ; Perakis, H (Perakis, Harris) [dostupné online]
http://apps.webofknowledge.com/full_record.do?product=WOS&search_mode=GeneralSearch&qid=4&SID=D45kzucMbWSFBSvgrVY&page=1&doc=1

[2] Indoor Integrated Navigation and Synchronous Data Acquisition Method for Android Smartphone
By:Hu, CS (Hu, Chunsheng) ; Wei, WJ (Wei, Wenjian) ; Qin, SQ (Qin, Shiqiao) ; Wang, XS (Wang, Xingshu) ; Habib, A (Habib, Ayman) ; Wang, RS (Wang, Ruisheng)
http://apps.webofknowledge.com/full_record.do?product=WOS&search_mode=GeneralSearch&qid=2&SID=D45kzucMbWSFBSvgrVY&page=1&doc=3

Zdroje práce studentů a ak.pracovníků podílejících se na výzkumu indoor lokalizace na UHK:
[3] Ing. Kateřina Hanušová - Gamifikace sběru rádiových fingerprintů (diplomová práce)

Navázání na seminární práce na SMAP:
[4] Jedlička Martin - Určování pozice v budovách pomocí Wi-Fi sítí
[5] Brůha RAdek - UHK Localization
