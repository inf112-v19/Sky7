
# Obligatorisk øvelse 3

## Deloppgave 1: Prosjekt og prosjektstruktur

- Hvordan funker rollene i teamet? 
    Rollene fungerer bra, men er ikke av stor betydning da vi har en ganske demokratisk tilnærming.
    Brukes i praktis ikke så veldig mye, men greit å ha.
    Initielt var ideen at "kundekontakt" skulle være den som har full kontroll på spillets regler,
	men dette har vi alle lest oss opp på. 
    
- Trenger dere å oppdatere hvem som er teamlead eller kundekontakt?
    nei
    
    
- Trenger dere andre roller?
    Henrik er grafikk master
    
    
- Skriv ned noen linjer om hva de ulike rollene faktisk innebærer for dere.
    Kunde kontakt blir ikke så mye brukt, tar opp hvordan logikk skal fungere i plenum,
    og spør gruppe ledere etc.
    Teamlead har tatt ansvar for ryddighet i repo. 
    
    
-Er det noen erfaringer enten team-messig eller mtp prosjektmetodikk som er verdt å nevne? 
    Parprogrammering: Tar tid å bli vant til det, og må jobbe med å ha en positiv innstiling til det, pleiere å commite fra ca          annene hver sin git gbruker, men dette kan utgjøre en forskjell ved lange parprogramerings sprints.
    Experimental branch: Vi merker at vi begynner å jobbe med ting som er innviklet i hverandre nå (mye kommunikasjon mellom                            klassene vi jobber med). Derfor bestemte vi oss for å opprette en branch for folk kan pushe                                    halvferdig arbeid som andre kan bentytte seg av. 
    Bli flinker til å bruke tester
    
-Synes teamet at de valgene dere har tatt er gode? Hvis ikke, hva kan dere gjøre annerledes for å forbedre måten teamet
fungerer på?
    Vi vurderte å bruke TiledMap, men konkluderte med at det var for mye arbeid å skrive om koden vi har.
    har ikke tatt "dårlige" valg, men lærer underveis, skal f.eks. bruke en "Experimental branch",
    vi deler opp oppgaven i midre deler, som gjør det lettere for en enkelt person å angripe. 


- Hvordan er gruppedynamikken?
    Bra, god kommunikasjon, og jobber med å bli bedre.

-Hvordan fungerer kommunikasjonen for dere?
    Vi blir mer og mer vant til å burke Slack og Git Projects, noe som gjør komminikasjon lettere.
    Er også flink til å ta opp felles "probemer"/"knutepunkt" på møter.

- Gjør et kort retrospektiv hvor dere vurderer hva dere har klart til nå, og hva som kan forbedres. Dette skal
handle om prosjektstruktur, ikke kode. Dere kan selvsagt diskutere kode, men dette handler ikke om
feilretting, men om hvordan man jobber og kommuniserer.
    Går for det meste bra, som skrevet over, gjør forandringer nå fortløpende, men ut  
    

-Under vurdering vil det vektlegges at alle bidrar til kodebasen. Hvis det er stor forskjell i hvem som
committer, må dere legge ved en kort forklaring for hvorfor det er sånn. husk å committe alt. (Også
designfiler)
    parprogrammering (forklart over)


-Referat fra møter siden forrige leveranse skal legges ved.


-Bli enige om maks tre forbedringspunkter fra retrospektivet, som skal følges opp under neste sprint.
    
    - Tester
    - Forbedring av sammarbeid mellom branches (herav ny Experimental branch)


Deloppgave 2: krav

-Presisering av krav som har kommet fra kunden. Hva blir de faktiske oppgavene?
    Kravet vi skal nå:
        - Få en brikke til å bevege seg basert på valg av kort. 
    Oppgavene vi setter mål om:
        - implementer Clinet og Host som styrer spillet mellom de to.¨
        - flytte brikke ut fra kort
        - tester
    
        
-Teamets prioritering av oppgavene
    Få en brikke til å bevege seg basert på valg av kort. 


-Hvis det er gjort endringer i rekkefølge utfra hva som er gitt fra kunde, hvorfor er dette gjort?
    - Følger rekke følgen som er gitt, med untakk av at man kan få nye kort til hver runde, siden dette kommer 
        som en naturlig side effekt av måten vi løser problemet på

-Hvordan vil dere verifisere at kravene er oppfylt? (Hva er akseptansekriteriene?)
    - Brukertester og jUnit


-Oppdatere hvilke krav dere har prioritert, hvor langt dere har kommet og hva dere har gjort siden forrige
gang
    - Viser til punkt over hvor vi har svarte på prioritering og projects board.




Deloppgave 4: kode

-Dere må dokumentere hvordan prosjektet bygger, testes og kjøres, slik at det er lett å teste koden. Under
vurdering kommer koden også til å brukertestes.



-Dokumentér også hvordan testene skal kjøres.
Kodekvalitet og testdekning vektlegges. Merk at testene dere skriver skal brukes i produktet.
