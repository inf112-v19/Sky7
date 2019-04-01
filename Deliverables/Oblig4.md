
# Obligatorisk øvelse 4

## Deloppgave 1: Prosjekt og prosjektstruktur

### Innfør en testrolle.
	- Brigt har allerede hatt overordnet ansvar for testing og får herved tildelt rollen som test-sjef.
	
### Er det noen erfaringer enten team-messig eller mtp prosjektmetodikk som er verdt å nevne? Synes teamet at de valgene dere har tatt er gode? Hvis ikke, hva kan dere gjøre annerledes for å forbedre måten teamet fungerer på?
    - Teamet fungerer bra
	- Endringer underveis, spesielt innføring av experimental branch, har vært gode valg.
	- Vi oppdager stadig at det er mange hjelpemidler i libGDX som hadde gjort arbeidet vårt enormt 
	mye lettere hvis vi visste om de fra begynnelsen. Men nå er det for sent / ikke tid til å gå 
	tilbake og skrive om koden for å bruke disse hjelpemidlene. Dette tenker vi er på grunn av at 
	vi fikk raskt stilt krav til innlevering uten å få nok tid til å sette oss inn i hva libGDX 
	kan tilby av hjelpemidler. Det hadde vært bedre med et kurs, eller en introduksjon til libGDX 
	før oppstart. 
	
### Hvordan er gruppedynamikken?
    - Gruppedynamikken er god, vi hjelper hverandre ofte, og samarbeider godt både på møter og på slack.

### Hvordan fungerer kommunikasjonen for dere?
    - Vi kommuniserer godt over slack og møtes minst 1 gang i uken.
	- Kunnskapsoverføring: 
		- Vi jobber av og til i par.
		- Vi har hatt diskusjon angående nettverksprogrammering.
		- Vi har delt kunnskap om: flertrådet programmering, TestSuite.

### Gjør et kort retrospektiv hvor dere vurderer hva dere har klart til nå, og hva som kan forbedres. Dette skal handle om prosjektstruktur, ikke kode. Dere kan selvsagt diskutere kode, men dette handler ikke om feilretting, men om hvordan man jobber og kommuniserer.
    - I begynnelsen var vi enige om å jobbe på hver vår branch, og lage pull requests til master, 
	slik at noen andre kan godkjenne og merge. Dette gikk vi bort fra på grunn av at det gikk for sakte, 
	og vi trenger raskt tilgang på kode andre skriver for å jobbe med klasser som snakker sammen. 
	Per nå bruker vi experimental branch for samarbeid, og dette fungerer veldig bra for oss.
	- Vi har blitt flinkere til å følge prosjekttavlen på github, og å holde denne oppdatert.
	- Prosjektet har nå mye bedre test-dekning.
	
### Bli enige om maks tre forbedringspunkter fra retrospektivet, som skal følges opp under neste sprint.
	- Vi kan bli flinkere til å jobbe jevnt, og ikke så mye skippertak.
	- Mer ryddig kode, husk å fjerne kommentert-ut kode. Bedre abstraksjon.
	
### Referat fra møter siden forrige leveranse skal legges ved.
	Referater legges i prosjekt root /Documents

### Under vurdering vil det vektlegges at alle bidrar til kodebasen. 
	- Per dato 26.03.19, står det ingen ting om Henrik sine bidrag i "contributors" på github, 
	selv om han har en god del commits på master. Vi finner ikke ut hvorfor Henrik sine bidrag ikke vises i git 
	statistikken.


## Deloppgave 2: krav
  
### Hva vi har prioritert denne sprinten
	- Host testing
	- Client testing
	- BoardGenerator testing
	- Robotgrafikk redesign
	- Robot kan dytte andre roboter
	- Refaktorering av robot movement i Board
	- Redesign av dock i GUI
	- Refaktorering av Host (90%)
	- Redesign floor tiles (for bedre grafikk ved skalering)
	- Påbegynt nettverkskode for Host
	- Implementasjon av klasser: Pusher, Laser (Ikke på brett enda)
	- Spillet starter med en meny, slik at vi senere kan legge inn knapper for valg av board osv.

## Deloppgave 3: kode
	- Se README.md for informasjon om bygging og testing.
