# indoorpositioning
Android app using mobile sensors for realizing indoor posistioning system

Princip řešení spočívá ve využití mobilních senzorů - především akcelerometr, magnetometr, barometr a gyroskop a okrajově bluetooth pro komunikaci s ostatními zařízeními.
 
Algoritmus k eliminaci odstředivé síly, vznikající při pohybu s telefonem po budově, která narušuje čistý vektor pohybu zařízení vzhledem k gravitačnímu a magnetickému poli. Za použití kvaternionů získaných v kombinaci s magnetometrem je možné takový vektor přepočítat na prostor reálného světa vzhledem ke světovým stranám. S pomocí tohoto vektoru je možné určit rychlost pohybu uživatele a směr, což v kombinaci s barometrem (na získání informace o aktuálně využívaném poschodí budovy) může dát poměrně přesnou informaci o pohybu uživatele v prostoru. Pohyb bude pak analyzován společně se siliou signálů z bluetooth beaconů rozmístěných po FIMu, čímž se uzavře možná množina cest pohybu.
