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

|                       | Sprint 1 | Sprint 2 |
| --------------------- | :-------------: | :-------------: |
| Maximilian Öttl        | <ul>Rolleneinteilung</li>Wechsel von H2 zu PostgreSQL</li>Heroku, Travis & Codacy Einrichtung</li>Implementierung Warenkorb</li>Bestellung im uneingeloggten Zustand speichern</li>Implementierung von Kategorien-Verwaltung (+ dynamische Anpassung der Benutzeroberfläche je nach Anzahl)</li>Implementierung von Bestellung mehrerer Produkte derselben Kategorie</li>Implementierung von Cookie bzw. Session Login</li>Implementierung von Cookie bzw. Session Data Handling für Registrierung/Warenkorb (Eliminierung von activeUser)</li>Implementierung von Cookie bzw. Session Data Handling für OrderHistory (Eliminierung von OrderProcess)</li>calculateDeliverytime in Service verschieben und setOrder vereinfachen</li>Codeoptimierung (setOrder, Trennung Produkt-/Kategorieverwaltung, OrderBill entfernt, Bestellablauf, Bestellstatus)</li>Fehlerbehebung (Login, Mitarbeiter fügt Nutzer hinzu, UpdateCategory aktualisiert nicht alle Einträge, Registrieren außerhalb Liefergebiet, nötige Änderungen an Zugriffsschutz nach Switch zu Sessions, ...)</li></ul> | <ul>Rolleneinteilung</li>UserController testen</li>OrderController testen</li>UserService testen</li>OrderService testen</li>UserDao testen</li>OrderDao testen</li>Checkstyle Fehler beheben (u.a. Wildcard Imports entfernt)</li>JavaDocs korrigiert + Notationsänderung zu ScalaDocs</li><Unnötige Dateien (Rest, JavaScript) und Leerzeilen entfernt</li>Play auf v2.3.10, Scala auf v2.11.11 und Dependencies aktualisieren</li>


Hasibullah Faroq
* *BillController testen*
* *MenuController testen*
* *MenuService testen*
* *OrderService doCalculationForBill & calculateDeliveryTime testen*
* *MenuDao testen*

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