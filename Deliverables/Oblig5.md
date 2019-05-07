
# Obligatorisk øvelse 5

## Deloppgave 1: Prosjekt og prosjektstruktur
    
### Er det noen erfaringer enten team-messig eller mtp prosjektmetodikk som er verdt å nevne? Synes teamet at de valgene dere har tatt er gode? Hvis ikke, hva kan dere gjøre annerledes for å forbedre måten teamet fungerer på?
    - Vi kunne lest mer om LibGDX før vi begynte på projektet. 
	Mange av MVP-kravene ville nok vært lettere å implementere hvis vi fikk bedre tid til å starte.
    	
### Hvordan er gruppedynamikken?
    - Gruppedynamikken er god, vi hjelper hverandre ofte, og samarbeider godt både på møter og på slack.
	
### Hvordan fungerer kommunikasjonen for dere?
    - Bruk av slack har holdt kommunikasjonen i god flyt.
    - Bruken av Project Board er ikke optimal, siden det ikke er så lett å definere oppgavene tydelig og isolert, 
	dvs uten at flere issues går ut over hverandre. 
	Derfor har det hendt et par ganger at noen jobber på samme problem.
	
### Utfør et retrospektiv før leveranse med fokus på hele prosjektet:
	- Hva justerte dere underveis, og hvorfor? Ble det bedre?
		- Sluttet med pull request og gikk over til å bruke en branch "experimental" for å kombinere 
		flere endringer. Vi merger den inn i master kun når den er stabil.
		- Det ble betydlig bedre, altså mer effektivt og det førte til at vi fikk gjort mer raskere.
		- Byttet fra trello til github project board for å ha færre platformer å jobbe på. 
		Av samme grunn bruker vi bare slack som kommunikasjonsplatform.
		- I Koden
			- Byttet til en implementasjon av Kryonet, fra egen skrevet nettverkskode.
			- Separert spillogikken fra Host til Game. 
		
	- hva har fungert best, og hvorfor? (Hva er dere mest fornøyde med?)
		- Vi er fornøyd med
			- BoardGenerator klassen, fordi den bruker et "factory design pattern".
			- Kryonet koden funker veldig bra selv om den består av få linjer kode.
			- Klassestukturen på hele projektet, fordi den er oversiktelig og lett å forstå.
			- Game klassen gjør det lett å isolere logikken fra Board som burde funke som datastruktur 
			og Host-klassen som bare skal være knutepunkt for kommunikasjon mellom hele strukturen.
		
	- hvis dere skulle fortsatt med prosjektet, hva ville dere justert?
		- Vi ville 
			- Øke testdekning.
			- Restrukturert GUI klassen, ved å bruke LibGDX tiledmap.
			- Refaktorert Board klassen slik at den skal bare virke som en datastruktur.
			- Lagt til "nice to have" kravene.
			- Generalisert mest mulig.
		
	- hva er det viktigste dere har lært?
        - at ting tar lenger tid enn først antatt.
        - mye om git, "multithreading", "Kryonet" og "LibGDX".
        - at samarbeid i systemkonstruksjon krever veldig mye koordinering.
        - å lage spill.

## Deloppgave 2: krav
    - Alle MVP-krav med unntak av følgende er implementert:
		- Spille mot AI (single-player-mode)

		
## Deloppgave 3: kode

### Som en del av denne leveransen skal dere legge ved en liste (og kort beskrivelse?) over kjente feil og svakheter i produktet.
	- Spillet er ikke godt tilpasset høy oppløsning. Tekst kan bli vanskelig å lese.
	
### Dere må dokumentere hvordan prosjektet bygger, testes og kjøres, slik at det er lett å teste koden. Under vurdering kommer koden også til å brukertestes.
	- Bygger / kjøres
		- Se README.md, under "How to run"
	- Testes
		- Se README.md, under "Tests"

### Produktet skal fungere på Linux ( Ubuntu / Debian ), Windows og OSX.
    - Testet på Windows og OSX
    
### Dokumentér også hvordan testene skal kjøres.
    - Se README.md, under "Tests"
    
### Legg også ved et klassediagram som viser de viktige delene av koden. Tilpass klassediagrammet slik at det gir leseren mest mulig informasjon (feks Intellij kan tilpasse klassediagram som genereres). 
### Hvis dere gjør justeringer som feks å ta vekk ubetydelige klasser, skriv noen linjer om hvordan dere har tilpasset disse kommuniserer. Tenk at dere skal forklare arkitekturen i programmet deres til en ny utvikler.
    - Spillet er strukturert slik at Main starter en GUI tråd. Deretter avhengig av Spillerens valg blir enten 
	Host eller Client satt i gang.
	- En Host har en Client av sin egen, i tillegg til andre Clienter gjennom HostNetHandler I tillegg til at hver 
	client kjører en Game av sin egen, gjør host også det samme.
	- Både host og client har en Board objekt, men bare Client sin board blir vist på skjermen.
	- Det finnes andre klasser som blir brukt av disse hover klassene.

