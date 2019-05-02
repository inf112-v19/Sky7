
# Obligatorisk øvelse 5

## Deloppgave 1: Prosjekt og prosjektstruktur
    
### Er det noen erfaringer enten team-messig eller mtp prosjektmetodikk som er verdt å nevne? Synes teamet at de valgene dere har tatt er gode? Hvis ikke, hva kan dere gjøre annerledes for å forbedre måten teamet fungerer på?
    - Teamet fungerer bra
    - Vi kunne lest mer opp på LibGDX før vi begynte på projektet. Mye ville vært lettere å fokusere på å implementere MVP kravene.
    	
### Hvordan er gruppedynamikken?
    - Gruppedynamikken er god, vi hjelper hverandre ofte, og samarbeider godt både på møter og på slack.
    - 
### Hvordan fungerer kommunikasjonen for dere?
    - Bruk av slack har holdt kommunikasjonen i god flyt.
    - Bruk av projekt-board er ikke optimalt, siden det ikke er så lett å definere oppgavene tydeligt og isolert, dvs uten at flere issues går ut over hverandre. Derfor ender det par ganger med at flere jobber på samme problem.
### Utfør et retrospektiv før leveranse med fokus på hele prosjektet:
####Hva justerte dere underveis, og hvorfor? Ble det bedre?
    - Sluttet med pull request og gikk over til å bruke en branch "experimental" som brukes for kombinere flere endringer. Vi merger den inn i master, kun når den er stabil.
    - Det ble betydlig bedre, altså mer effektivt og det førte til at vi fikk gjort mer raskere.
    - Sluttet gikk fra trello til github project-board for å ha færre platform å jobbe på. Av samme grunn bruker vi bare slack som kommunikasjonsplatform.
    - I Koden
        - Byttet til Kryonet fra egen skrevet kode for som nethandler.
        - Fjernet ut spill logikken fra Host til Game. 
####hva har fungert best, og hvorfor? (Hva er dere mest fornøyde med?)
    - Vi er fornøyd med
        - BoardGenerator klassen, fordi den bruker en factory design pattern.
        - Kryonet koden funker veldig bra selv om den består av få kode linjer.
        - Klasse stukturen på hele projektet, fordi den er oversiktelig og lett å forstå.
        - Game klassen gjør det lett å isolere logikken fra board som burde funke som datastruktur og host klassen som burde bare hoste  
####hvis dere skulle fortsatt med prosjektet, hva ville dere justert?
    - Vi ville 
        - Større test dekning.
        - restrukturert GUI klassen, ved bruke LibGDX tiledmap.
        - Refakturert Board klassen slik at den skal bare virke som en datastruktur.
        - Lagt til "nice to have" kravene.
        - Generalisert mest mulig.
####hva er det viktigste dere har lært? 
    - Vi har lært 
        - at ting tar lenger tid enn først antatt.
        - om "multithreading", "Kryonet" og "LibGDX".
        - at samarbeid i systemkonstruksjon krever veldig stort koordinering.
        - mye om git.
        - å lage spill.

## Deloppgave 2: krav
    - FERDIG:
        - Man må kunne spille en komplett runde
        - Man må kunne vinne spillet spillet ved å besøke siste flagg (fullføre et spill)
        - Det skal være lasere på brettet
        - Det skal være hull på brettet
        - Skademekanismer (spilleren får færre kort ved skade)
        - Spillmekanismer for å skyte andre spillere innen rekkevidde med laser som peker rett frem
        - fungerende samlebånd på brettet som flytter robotene
        - fungerende gyroer på brettet som flytter robotene
        - multiplayer over LAN eller Internet (trenger ikke gjøre noe fancy her, men må kunne spille på ulike maskinermot hverandre)
        - Feilhåndtering ved disconnect. (Spillet skal i hvertfall ikke kræsje)
        - power down
        - samlebånd som går i dobbelt tempo
  
### Som en del av denne leveransen skal dere legge ved en liste (og kort beskrivelse?) over kjente feil ogsvakheter i produktet.
	- TODO
	
### Dere må dokumentere hvordan prosjektet bygger, testes og kjøres, slik at det er lett å teste koden. Undervurdering kommer koden også til å brukertestes.
	- TODO

### Produktet skal fungerer på Linux ( Ubuntu / Debian ), Windows og OSX.
    - TODO
    
### Dokumentér også hvordan testene skal kjøres.
    - TODO
    
### Legg også ved et klassediagram som viser de viktige delene av koden. Tilpass klassediagrammet slik at det gir leseren mest mulig informasjon (feks Intellij kan tilpasse klassediagram som genereres). 
### Hvis dere gjør justeringer som feks å ta vekk ubetydelige klasser, skriv noen linjer om hvordan dere har tilpasset disse kommuniserer. Tenk at dere skal forklare arkitekturen i programmet deres til en ny utvikler.
    - TODO
    
## Deloppgave 3: kode
	- 
