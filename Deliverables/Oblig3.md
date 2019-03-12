
# Obligatorisk øvelse 3

## Deloppgave 1: Prosjekt og prosjektstruktur

### Hvordan funker rollene i teamet? 
    Rollene fungerer bra, men er ikke av stor betydning da vi har en ganske demokratisk tilnærming.
    Brukes i praksis ikke så veldig mye, men greit å ha.
    Initielt var ideen at "kundekontakt" skulle være den som har full kontroll på spillets regler,
	men dette har vi alle lest oss opp på. 
    
### Trenger dere å oppdatere hvem som er teamlead eller kundekontakt?
    Nei
    
    
### Trenger dere andre roller?
    Vi har opprettet en ny rolle "grafikksjef" (Henrik).
	Det har ikke vært tid for alle til å lese like mye om Libgdx, så Henrik har fått overordnet ansvar for GUI-klassen. 
    
### Skriv ned noen linjer om hva de ulike rollene faktisk innebærer for dere.
    - Kunde kontakt blir ikke så mye brukt, tar opp hvordan logikk skal fungere i plenum, og spør gruppe ledere etc.
    - Teamlead har tatt ansvar for ryddighet i repo, branches og project board.
    
    
### Er det noen erfaringer enten team-messig eller mtp prosjektmetodikk som er verdt å nevne? 
    - Parprogrammering: Tar tid å bli vant til det, og må jobbe med å ha en positiv innstilling til det, 
	forsøker å bytte på hvem som commiter fra sin git bruker, 
	men dette kan utgjøre en forskjell ved lange parprogrameringsøkter.
    - Experimental branch: Vi merker at vi begynner å jobbe med ting som er avhengig av arbeid andre jobber med samtidig 
	(mye kommunikasjon mellom klassene vi jobber med). 
	Derfor bestemte vi oss for å opprette en branch hvor folk kan pushe halvferdig arbeid som andre kan bentytte seg av. 
    Vi opplever at vi blir "pushet" til å implementere funksjonalitet for raskt med tanke på oblig'ene, 
	slik at vi ikke får skrevet dokumentasjon og tester i den grad vi egentlig vil.
    
### Synes teamet at de valgene dere har tatt er gode? Hvis ikke, hva kan dere gjøre annerledes for å forbedre måten teamet fungerer på?
    - Vi vurderte å bruke TiledMap, men konkluderte med at det var for mye arbeid å skrive om koden vi har.
    - Har ikke tatt "dårlige" valg, men lærer underveis, vi bruker nå f.eks. en "Experimental branch",
    og vi deler opp oppgaver i midre deler, som gjør det lettere for en enkelt person å angripe. 

### Hvordan er gruppedynamikken?
    Bra, god kommunikasjon, og jobber med å bli bedre.

### Hvordan fungerer kommunikasjonen for dere?
    Vi blir mer og mer vant til å burke Slack og Git, noe som gjør kommunikasjon lettere.
    Er også flink til å ta opp felles "problemer"/"knutepunkt" på møter.

### Gjør et kort retrospektiv hvor dere vurderer hva dere har klart til nå, og hva som kan forbedres. Dette skal handle om prosjektstruktur, ikke kode. Dere kan selvsagt diskutere kode, men dette handler ikke om feilretting, men om hvordan man jobber og kommuniserer.
    Går for det meste bra, som skrevet over, gjør forandringer nå fortløpende.
	Vi vurderer å planlegge med færre mål per sprint for å kunne ta oss bedre tid til implementasjon, 
	dokumentasjon og testing, 
	men det er da samtidig en mulighet for at vi ikke har nok tid til å komme i mål på lang sikt.

### Under vurdering vil det vektlegges at alle bidrar til kodebasen. Hvis det er stor forskjell i hvem som committer, må dere legge ved en kort forklaring for hvorfor det er sånn. husk å committe alt. (Også designfiler) 
	- Parprogrammering (forklart over).
	- Per dato 07.03.19, står det ingen ting om Henrik sine bidrag i "contributors" på github, 
	selv om han har en god del commits på master. Vi finner ikke ut hvorfor Henrik sine bidrag ikke vises i git 
	statistikken.

### Referat fra møter siden forrige leveranse skal legges ved.
	Referater legges i prosjekt root /Documents

### Bli enige om maks tre forbedringspunkter fra retrospektivet, som skal følges opp under neste sprint.
    - Testing
    - Forbedring av sammarbeid mellom branches (herav ny Experimental branch)
	- Dokumentasjon (spesielt skrive utfyllende Readme.md)

## Deloppgave 2: krav

### Presisering av krav som har kommet fra kunden. Hva blir de faktiske oppgavene?
    Kravet vi skal nå:
        - Få en brikke til å bevege seg basert på valg av kort. 
    Oppgavene vi setter mål om:
        - implementer Client og Host som styrer spillet mellom de to.
        - flytte brikke basert på valg av kort.
        - Tester til ICell objekter
    
        
### Teamets prioritering av oppgavene
	- Få en brikke til å bevege seg basert på valg av kort.
	- Skrive tester
	- Implementere hindringer og elementer som interagerer med robotene på brettet
	
### Hvis det er gjort endringer i rekkefølge utfra hva som er gitt fra kunde, hvorfor er dette gjort?
	Følger rekkefølgen som er gitt

### Hvordan vil dere verifisere at kravene er oppfylt? (Hva er akseptansekriteriene?)
    Brukertester og jUnit

### Oppdatere hvilke krav dere har prioritert, hvor langt dere har kommet og hva dere har gjort siden forrige gang
    - Vi har jobbet mest med første prioritet, og prøver å få til 3. punkt på listen før innlevering. 
	- Vi er delvis i mål med tanke på tester.

## Deloppgave 4: kode

### Dere må dokumentere hvordan prosjektet bygger, testes og kjøres, slik at det er lett å teste koden. Under vurdering kommer koden også til å brukertestes.
	Bygging, testing og kjøring vil dokumenteres i Readme.md i root.

### Dokumentér også hvordan testene skal kjøres. Kodekvalitet og testdekning vektlegges. Merk at testene dere skriver skal brukes i produktet.
	- Se Readme.md angående JUnit tester.
	- Forslag / instruks til brukertesting: Documents/UserTest.md 