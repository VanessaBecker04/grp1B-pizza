# Pizza Suez [![Build Status](https://travis-ci.org/swenib/grp1B-pizza.svg?branch=master)](https://travis-ci.org/swenib/grp1B-pizza) [![Codacy Badge](https://api.codacy.com/project/badge/Coverage/b94b30622e2d49a9b194287064d25cdc)](https://www.codacy.com/app/maximilianoe/grp1B-pizza?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=swenib/grp1B-pizza&amp;utm_campaign=Badge_Coverage) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/b94b30622e2d49a9b194287064d25cdc)](https://www.codacy.com/app/maximilianoe/grp1B-pizza?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=swenib/grp1B-pizza&amp;utm_campaign=Badge_Grade)

Die Website ist über [Heroku](http://grp1b-pizza.herokuapp.com/) erreichbar. Empfehlenswert ist die Nutzung der Website über den Firefox-Browser, um Darstellungsfehler zu vermeiden.

Voreingestellte Nutzer zum Anmelden:

* Mitarbeiter Padrone mit Email: *padrone@suez.de* und Passwort: *Suez82346*
* Kunde Emil mit Email: *emil@gmx.de* und Passwort: *Susanne82343*


**Benutzungshinweise:**

* Ein manueller Aufruf von firmeninternen Funktionen über die Adresszeile des Browsers ist ohne vorherige Anmeldung als Mitarbeiter nicht möglich und führt zu einer Fehlermeldung, dass nicht die nötigen Zugriffsrechte vorhanden sind.

* Das Registrieren ist nur möglich, wenn der Nutzer im Liefergebiet wohnt und eine ungenutzte Email angibt. Auch hier wird auf entsprechende Fehlermeldungen weitergeleitet.

* Das Bestellen ist nur im eingeloggten Zustand möglich.


**Organisation/Aufgaben:**

|    Person             | Sprint 1 | Sprint 2 |
| --------------------- | :-------- | :-------- |
| Maximilian Öttl       | <ul><li>Rolleneinteilung![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Wechsel von H2 zu PostgreSQL![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Heroku, Travis & Codacy Einrichtung![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Warenkorb hinzufügen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Bestellung im uneingeloggten Zustand speichern![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Kategorien-Verwaltung (+ dynamische Anpassung der Benutzeroberfläche je nach Anzahl)![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Bestellung mehrerer Produkte derselben Kategorie![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Cookie bzw. Session Login![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Cookie bzw. Session Data Handling für Registrierung/Warenkorb (Eliminierung von activeUser)![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Cookie bzw. Session Data Handling für OrderHistory (Eliminierung von OrderProcess)![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Codeoptimierung (setOrder, Trennung Produkt-/Kategorieverwaltung, OrderBill entfernt, Bestellablauf, Bestellstatus)![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Fehlerbehebung (Login, Mitarbeiter fügt Nutzer hinzu, UpdateCategory aktualisiert nicht alle Einträge, Registrieren außerhalb Liefergebiet, nötige Änderungen an Zugriffsschutz nach Switch zu Sessions, Lieferzeitberechnung in Service verschoben, ...)![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li></ul> | <ul><li>Rolleneinteilung![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>UserController testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>OrderController testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>UserService testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>OrderService testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>UserDao testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>OrderDao testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Checkstyle Fehler beheben (u.a. Wildcard Imports entfernt)![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>JavaDocs korrigieren + Notationsänderung zu ScalaDocs![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Unnötige Dateien (Rest, JavaScript) und Leerzeilen entfernen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Play auf v2.3.10, Scala auf v2.11.11 und Dependencies aktualisieren![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li></ul>
| Hasibullah Faroq      | <ul><li>Kategorie-Verwaltung hinzufügen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Code optimieren (OrderProcess eleminiert und auf Session umgestiegen, Methoden aus Model Menu entfernt)![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Bestellstatus hinzugefügen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Bestellung kann stoniert werden![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)| <ul><li>BillController testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>MenuController testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>MenuService testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>OrderService doCalulationForBill und calculateDeliveryTime testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>MenuDao testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>JavaDocs korrigieren![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li></ul>
| Sandra Sporrer        | <ul><li>Website online durchklicken und überlegen welche Tests man alle schreiben könnte.![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Website online durchklicken und überlegen was Alles in die Bedienungsanleitung soll.![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Protokollierung der Meetings und Rollenverteilung festhalten.*![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Benutzerhandbuch Mitarbeiter-Teil![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)| <ul><li>Application Controller testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>MenuController testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>BadRequest im Controller testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Mit simulierten "Browser" testen, ob der Aufruf von den Application Methoden funktioniert![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>ScalaDocs![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>CodeStyle![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li></ul>
| Vanessa Becker       | <ul><li>Website online durchklicken und überlegen welche Tests man alle schreiben könnte.![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Website online durchklicken und überlegen was Alles in die Bedienungsanleitung soll.![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Protokollierung der Meetings und Rollenverteilung festhalten.*![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>Benutzerhandbuch Kunden-Teil![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)| <ul><li>Application Controller testen![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><<li>Mit simulierten "Browser" testen, ob der Aufruf von den Application Methoden funktioniert![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li><li>ScalaDocs![false](https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678134-sign-check-24.png)</li></ul>
Vanessa Becker
* *Website online durchklicken und überlegen welche Tests man alle schreiben könnte.*
* *Website online durchklicken und überlegen was Alles in die Bedienungsanleitung soll.*
* *Protokollierung der Meetings und Rollenverteilung festhalten.*
* *Benutzerhandbuch Kunden-Teil*
* *Application Controller testen*
* *Mit simulierten "Browser" testen, ob der Aufruf von den Application Methoden funktioniert*
* *ScalaDocs*

 Sandra Sporrer       
* *Website online durchklicken und überlegen welche Tests man alle schreiben könnte.*
* *Website online durchklicken und überlegen was Alles in die Bedienungsanleitung soll.*
* *Protokollierung der Meetings und Rollenverteilung festhalten.*
* *Benutzerhandbuch Mitarbeiter-Teil*
* *Application Controller testen*
* *BadRequest im Controller testen*
* *Mit simulierten "Browser" testen, ob der Aufruf von den Application Methoden funktioniert*
* *ScalaDocs*
* *CodeStyle*



Rene Karl Baral
* *Anpassung der Main.css von fixed zu relative Positions*
