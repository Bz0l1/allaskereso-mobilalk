## Pontozási segédlet

Készítettem egy pontozási segédletet, hogy gyorsabban menjen az ellenőrzés:  

1. __Fordítási hiba nincs:__ nem tapasztaltam ilyen hibát, de majd figyeld te is. 
 
2. __Futtatási hiba nincs:__ nem tapasztaltam ilyen hibát, de majd figyeld te is.

3. __Firebase autentikáció meg van valósítva::__ Be lehet jelentkezni és regisztrálni

4. __Adatmodell definiálása (class vagy interfész formájában):__ a moddelek a `Models` mappában megtalálhatod.

5. __Legalább 3 különböző activity vagy fragmens használata:__ 3 Activity van, ezeket az ``Activity``mappában találod. ``LoginActivity``, ``MainActivity``, ``RegisterActivity``.

6. __Beviteli mezők beviteli típusa megfelelő (jelszó kicsillagozva, email-nél megfelelő billentyűzet jelenik meg stb.):__ Az input mezőket helyesen állítottam be.
 
7. __ConstraintLayout és még egy másik layout típus használata:__ A ``ConstraintLayout`` használatára több helyen is találhatsz példát, illetve használtam a ``LinearLayout``-ot is.

8. __Reszponzív:__ Több eszközzel próbáltam, az UI elemek jól jelennek meg.

9. __Legalább 2 különböző animáció használata:__ 2 animációt használtam az állásajánlatok betöltésekor.

10. __"Intentek használata: navigáció meg van valósítva az activityk/fragmensek között (minden activity/fragmens elérhető)":__ ez meg van valósítva.

11. __Legalább egy Lifecycle Hook használata a teljes projektben:__ ``onPause()``-t használog a Login és Register activity-ben.

12. __Legalább egy olyan androidos erőforrás használata, amihez kell android permission":__ nem használtam

13. __Legalább egy notification vagy alam manager vagy job scheduler használata__ nem használtam

14. __CRUD műveletek mindegyike megvalósult és az adatbázis műveletek a konvenciónak megfelelően külön szálon történnek":__
      - **Create**: ``add`` - jelentkezés hozzáadása
      - **Read:** ``selectOffers`` - ajánlatok lekérdezése
      - **Delete:** ``delete`` - jelentkezés törlése

15: __Legalább 2 komplex Firestore lekérdezés megvalósítása, amely indexet igényel (ide tartoznak: where feltétel, rendezés, léptetés, limitálás)":__ A ``SelectOffers`` és ``delete`` használatakor történik komplex lekérdezés
