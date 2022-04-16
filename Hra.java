/**
 * Hlavná trieda hry Dáma, kde sa odohrávajú hlavné zložité algoritmy.
 * Pravidlá hry(anglická verzia dámy):
 * Hra pre 2hráčov na šachovnici 8x8, ktorých cieľom je vyhodiť všetky súperove figúrky. 
 * Hráči začínajú s 3radmi pešiakov na čiernych políčkach, ktorí sa môžu pohybovať iba po diagonále a iba vpred.
 * Pešiak sa pohybuje iba o 1políčko alebo o 2 pri preskakovaní súperovej figúrky (vlastnú figúrku preskočiť nemôže).
 * Pri preskočení hráč súperovu figúrku vyhodí.
 * Pokiaľ má hráč platný ťah, ktorým vyhodí súperovu figúrku, musí ho uskutočniť(ak má viac takých ťahov, môže si vybrať, ktorý vykoná).
 * Keď hráč preskočí a figúrka na novej pozícií má znova vyhadzovací ťah, musí znova vyhadzovať s tou istou figúrkou.
 * Keď sa pešiak dostane na súperov prvý riadok šachovnice, zmení sa na dámu a môže sa pohybovať rovnakým spôsobom po všetkých 4 diagonálach.
 * Hráči začínajú s vlastnou časomierou a rovnakým časom. Po uplynutí svojho času hráč automaticky prehráva.
 * 
 * Výsledok hry sa vypíše do terminalu.
 * Je možné 1x použiť Undo a Redo metódy šípkami dole a hore.
 * Escape hru ukončí.
 * 
 * @author Jozef Vitko
 * @version 7.4.1
 */
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
public class Hra {
    private boolean meraj;
    private int cas;

    private boolean jeTah;

    private static Hra aplikacia;

    private Manazer manazer;
    private Sachovnica sachovnica;
    private int strana;
    private int x;
    private int y;
    private int aktualnyHrac;

    private Farba farba1;
    private Farba farba2;

    private StavHry stavHry;

    private Casomiera casomiera1;
    private Casomiera casomiera2;
    private Casomiera aktualnaCasomiera;

    private Figurka[][] figurky;
    private Kruh[][] tahy;

    private Zapisovanie zapisovac;
    private boolean undo;
    private boolean redo;
    
    private Figurka aktualnaFigurka;
    private int aktualnaFigurkaRiadok;
    private int aktualnaFigurkaStlpec;

    private boolean jeHra;

    private boolean vyhodil;
    private boolean musiVyhodit;
    
    
    /**
     * Konštruktor je označený ako private, keďže sa využíva návrhový model singleton.
     * Nastavia sa defaultné nastavenia atribútov hry.
     * Vytvorí sa pole ťahov a figúrok.
     * Vytvorí sa manažér, šachovnica a časomiery.
     * Konštruktor nič nevykreslí na plátno.
     * Pozície sú odvodené od parametrov x a y (ľavý horný roh šachovnice).
     * @param strana Udáva veľkosť jedného políčka šachovnice a sú od nej odvodené všetky veľkosti.
     */
    private Hra(int x, int y, int strana) {
        this.strana = strana;
        this.x = x;
        this.y = y;
        this.farba1 = Farba.GREEN;
        this.farba2 = Farba.BLUE;
        this.aktualnaCasomiera = null;
        this.manazer = new Manazer();

        this.zapisovac = new Zapisovanie();
        
        this.stavHry = StavHry.NEROZHODNUTE;

        this.aktualnaFigurka = null;
        this.aktualnaFigurkaRiadok = -1;
        this.aktualnaFigurkaStlpec = -1;

        this.aktualnyHrac = 0;

        this.figurky = new Figurka[8][8];
        this.tahy = new Kruh[8][8];

        this.sachovnica = new Sachovnica(this.x, this.y, this.strana);

        this.casomiera1 = new Casomiera(this.x - 9 * this.strana, this.y, this.strana, this.strana / 3, 3, 30);
        this.casomiera2 = new Casomiera(this.x + 9 * this.strana, this.y, this.strana, this.strana / 3, 3, 30);

    }    

    /**
     * Slúži na spustenie konštruktora(model singleton) s preddefinovanými hodnotami.
     */
    public static Hra vygeneruj() {
        return Hra.getInstance(400, 40, 40);

    }

    /**
     * Slúži ako hlavná metóda na spustenie hry(pri spustení vytvoreného jar súboru sa vykoná táto metóda).
     */
    public static void main(String[] args) {
        Hra.vygeneruj();
        aplikacia.start();
    }

    /**
     * Singleton konštruktor.
     */
    public static Hra getInstance(int x, int y, int strana) {
        if (Hra.aplikacia == null) {
            Hra.aplikacia = new Hra(x, y, strana);   
        }
        return Hra.aplikacia;

    }

    /**
     * Zobrazí šachovnicu a časomiery pre hráčov.
     * Vytvorí kruhy v poli ťahov na všetkých čiernych políčkach šachovnice.
     * Vytvorí pešiakov pre hráča1 a hráča2 v poli figúrok na začiatočných pozíciach a zobrazí ich. 
     */
    private void generator() {
        this.casomiera1.zmenFarbu(this.farba1.getString());
        this.casomiera2.zmenFarbu(this.farba2.getString());
        this.casomiera1.zobrazAktualnuHodnotu();
        this.casomiera2.zobrazAktualnuHodnotu();

        this.sachovnica.zobraz();
        for (int riadok = 0; riadok < 8; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {
                if (this.sachovnica.getFarba(riadok, stlpec).equals("black")) {
                    this.tahy[riadok][stlpec] = new Kruh();
                    this.tahy[riadok][stlpec].zmenPriemer(this.strana);
                    this.tahy[riadok][stlpec].zmenFarbu("white");
                    this.tahy[riadok][stlpec].zmenPolohu(this.x + stlpec * this.strana, this.y + riadok * this.strana);

                }
            }   
        } 

        for (int riadok = 0; riadok < 3; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {                
                if (this.sachovnica.getFarba(riadok, stlpec).equals("black")) {
                    this.figurky[riadok][stlpec] = new Figurka(this.x + stlpec * this.strana, this.y + riadok * this.strana, this.strana);                    
                    this.figurky[riadok][stlpec].zobraz();
                    this.figurky[riadok][stlpec].zmenFarbu(this.farba1);
                    this.figurky[riadok][stlpec].setTyp(Typ.HRAC1_PESIAK);
                }
            }
        }    
        for (int riadok = 5; riadok < 8; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {                
                if (this.sachovnica.getFarba(riadok, stlpec).equals("black")) {
                    this.figurky[riadok][stlpec] = new Figurka(this.x + stlpec * this.strana, this.y + riadok * this.strana, this.strana);                    
                    this.figurky[riadok][stlpec].zobraz();
                    this.figurky[riadok][stlpec].zmenFarbu(this.farba2);
                    this.figurky[riadok][stlpec].setTyp(Typ.HRAC2_PESIAK);
                }
            }
        }

    }
    /**
     * Spustenie hry.
     * Spustí generator, manažér začne spravovať hru a odštartuje hráča1.
     */
    private void start() {

        this.jeHra = true;
        this.generator();
        this.manazer.spravujObjekt(this);

        this.meraj = false;
        this.cas = 0;

        this.hrac1();

    }

    /**
     * Spustí ťah hráča1.
     * Nastaví aktuálneho hráča a spustí odpočítavanie času prvej časomiery.
     */
    private void hrac1() {
        this.jeTah = true;
        this.aktualnyHrac = 1;
        this.aktualnaCasomiera = this.casomiera1;

        this.startCas(this.aktualnaCasomiera.getCasVSekundach());

    }
    /**
     * Spustí ťah hráča2.
     * Nastaví aktuálneho hráča a spustí odpočítavanie času druhej časomiery.
     */
    private void hrac2() {
        this.jeTah = true;
        this.aktualnyHrac = 2;
        this.aktualnaCasomiera = this.casomiera2;

        this.startCas(this.aktualnaCasomiera.getCasVSekundach());

    }
    /**
     * Kontrola sa vykonáva pri zmene hráča.
     * V prvej časti sa testuje, či sa nejaká figúrka nestala dámou (dostala sa na súperov prvý riadok šachovnice), ak áno zmení sa typ figúrky na dámu.
     * V druhej časti sa resetne vyhadzovací atribút ťahov a kontroluje sa počet figúrok hráčov.
     * Ak má jeden hráč 0figúrok jeho súper vyhral a hra sa ukončí.
     * Ak majú stále obaja hráči figúrky zmení sa aktuálny hráč a hra pokračuje.
     */
    private void kontrola() { 
        this.musiVyhodit = false;

        for (int stlpec = 0; stlpec < 8; stlpec++) {
            if (this.figurky[0][stlpec] != null) {
                if (this.figurky[0][stlpec].getFarba() == this.farba2) {
                    this.figurky[0][stlpec].setDama(true);
                    this.figurky[0][stlpec].setTyp(Typ.HRAC2_DAMA);
                }            
            }
        }

        for (int stlpec = 0; stlpec < 8; stlpec++) {
            if (this.figurky[7][stlpec] != null) {
                if (this.figurky[7][stlpec].getFarba() == this.farba1) {
                    this.figurky[7][stlpec].setDama(true);
                    this.figurky[7][stlpec].setTyp(Typ.HRAC1_DAMA);
                }
            }            
        }

        int pocetFigurok1 = 0;
        int pocetFigurok2 = 0;

        for (int riadok = 0; riadok < 8; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {

                if (this.sachovnica.getFarba(riadok, stlpec).equals("black")) {
                    this.tahy[riadok][stlpec].setVyhod(false);
                    if (this.figurky[riadok][stlpec] != null && this.figurky[riadok][stlpec].getFarba() == this.farba1) {
                        pocetFigurok1++;
                    } else if (this.figurky[riadok][stlpec] != null && this.figurky[riadok][stlpec].getFarba() == this.farba2) {
                        pocetFigurok2++;   

                    }
                }
            }  
        }

        if (pocetFigurok1 == 0) {
            this.jeHra = false;
            this.stavHry = StavHry.VYHRAL_HRAC2;
        } else if (pocetFigurok2 == 0) {
            this.jeHra = false;
            this.stavHry = StavHry.VYHRAL_HRAC1;
        }

        
        if (this.jeHra) {
            this.zmenHraca();
        } else {            
            this.koniec();
        }       
    }

    /**
     * Metóda ukončí hru a manažér prestane spravovať objekt.
     * Vymažú sa textové súbory.
     * Výsledok hry sa vypíše do konzoli.
     */    
    private void koniec() {
        this.jeHra = false;
        this.manazer.prestanSpravovatObjekt(this);
        this.zapisovac.vymaz("undo.txt");
        if (this.redo) {
            this.zapisovac.vymaz("redo.txt"); 
        }
        System.out.println(this.stavHry.toString());
    }

    /**
     * Slúži na zmenu hráča, ktorý je na ťahu a zároveň kontroluje, či má nový hráč vyhadzovacie ťahy.
     * Ak má vyhadzovacie ťahy, automaticky mu pôjdu zobraziť len tie.
     */
    private void zmenHraca() {
        //System.out.println("AKTIVUJ");
        
        this.jeTah = !this.jeTah;
        if (this.aktualnyHrac == 1) {

            this.hrac2();
        } else if (this.aktualnyHrac == 2) {

            this.hrac1();
        }

        for (int riadok = 0; riadok < 8; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {
                if (this.figurky[riadok][stlpec] != null) {
                    if ((this.figurky[riadok][stlpec].getFarba() == this.farba1 && this.aktualnyHrac == 1) || (this.figurky[riadok][stlpec].getFarba() == this.farba2 && this.aktualnyHrac == 2) ) {
                        if (this.maVyhadzovacieTahy(riadok, stlpec)) {
                            this.musiVyhodit = true;
                        }
                    }

                }

            }
        }
    }

    /**
     * Pri kliknutí na plátno sa vykoná metóda so súradnicami na plátne ako parametrami.
     * Metóda prejde polom ťahov a polom figúrok a kontroluje, či niečo z toho obsahuje súradnice x a y.
     * 
     * Prvá časť metódy pracuje s figúrkami a pri kliknutí na figúrku kontroluje, či ide o figúrku hráča, ktorý je na rade.
     * Ďalej kontroluje, či má figúrka vyhadzovacie ťahy, ak áno vyhadzovacie ťahy sa zobrazia.
     * Ak nemá vyhadzovacie ťahy a aktuálny hráč práve nemusí vyhadzovať, zobrazia sa obyčajné ťahy.
     * 
     * Druhá časť metódy pracuje s ťahmi a pri kliknutí na ťah kontroluje, či je práve zobrazený(zobrazenými ťahmi sa dá hýbať).
     * Na začiatku sa zapisovaču pošle stav hracej plochy, aby bolo možné vykonať undo ťah.
     * Metódou getVyhod() zistí, či je daný ťah vyhadzovací, a ak áno vyhodí súperovú figúrku, ktorú preskakuje hráč pri tomto ťahu.
     * Ďalej metóda posunie aktuálnu figúrku hráča.
     * Pokiaľ hráč vyhadzoval a na novej pozícií má figúrka opäť vyhadzovacie ťahy, zobrazia sa a hráč musí hýbať s touto figúrkou.
     * Ak hráč nevyhadzoval, vykoná sa kontrola a zmení sa hráč.
     */    
    public void vyberSuradnice(int x, int y) throws IOException {

        for (int riadok = 0; riadok < 8; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {
                if (this.figurky[riadok][stlpec] != null ) {
                    if (this.figurky[riadok][stlpec].obsahujeSuradnice(x, y)) {
                        if (this.aktualnyHrac == 1 && this.figurky[riadok][stlpec].getFarba() == this.farba1 || this.aktualnyHrac == 2 && this.figurky[riadok][stlpec].getFarba() == this.farba2) {
                            if (this.maVyhadzovacieTahy(riadok, stlpec)) {
                                //System.out.println(x + " " + y);
                                this.aktualnaFigurka = this.figurky[riadok][stlpec];
                                this.aktualnaFigurkaRiadok = riadok;
                                this.aktualnaFigurkaStlpec = stlpec;

                                this.zobrazVyhadzovacieTahy(riadok, stlpec);
                                this.musiVyhodit = true;
                            } else if (!this.musiVyhodit) {
                                this.aktualnaFigurka = this.figurky[riadok][stlpec];
                                this.aktualnaFigurkaRiadok = riadok;
                                this.aktualnaFigurkaStlpec = stlpec;                                
                                this.zobrazTahy(riadok, stlpec);
                            }
                        }
                    }    

                }

                if (this.tahy[riadok][stlpec] != null ) {
                    if (this.tahy[riadok][stlpec].obsahujeSuradnice(x, y)) {                        
                        if (this.tahy[riadok][stlpec].getViditelnost()) {   
                            
                            this.undo = false;
                            this.posliFigurkyZapisovacu("undo.txt");
                            
                            
                            if (this.tahy[riadok][stlpec].getVyhod()) {

                                this.figurky[(riadok + this.aktualnaFigurkaRiadok) / 2][(stlpec + this.aktualnaFigurkaStlpec) / 2].skry();
                                this.figurky[(riadok + this.aktualnaFigurkaRiadok) / 2][(stlpec + this.aktualnaFigurkaStlpec) / 2] = null;

                                this.vyhodil = true;   
                                this.musiVyhodit = false;
                            }

                            
                            this.figurky[riadok][stlpec] = this.aktualnaFigurka;
                            this.figurky[riadok][stlpec].zmenPolohu(this.tahy[riadok][stlpec].getX(), this.tahy[riadok][stlpec].getY());

                            this.figurky[this.aktualnaFigurkaRiadok][this.aktualnaFigurkaStlpec].skry();
                            this.figurky[this.aktualnaFigurkaRiadok][this.aktualnaFigurkaStlpec] = null;

                            this.figurky[riadok][stlpec].zobraz();
                            this.skryTahy();

                            if (this.maVyhadzovacieTahy(riadok, stlpec) && this.vyhodil) {
                                
                                this.musiVyhodit = true;
                                this.vyhodil = false;
                                this.aktualnaFigurkaRiadok = riadok;
                                this.aktualnaFigurkaStlpec = stlpec;                                
                                this.zobrazVyhadzovacieTahy(riadok, stlpec);
                            } else {

                                this.vyhodil = false;
                                this.kontrola();
                            }

                        }
                    } 

                }

            }
        }

    }
    /**
     * Univerzálna metóda pre všetky typy figúrok, ktorá zobrazuje vyhadzovacie ťahy.
     * Všeobecný princíp fungovania metódy:
     * Pomocou parametrov riadok a stĺpec sa nájde typ figúrky, keďže každá figúrka má iné vyhadzovacie ťahy.
     * Po nájdení typu figúrky sa kontrolujú ťahy pre jej smery pohybu.
     * Prvá kontrola v smere spočíva v tom, či by pri skoku bola figúrka ešte stále na šachovnici(Bez tejto kontroli by nastávali chyby, keďže by sme skúšali načítať prvok mimo poľa).
     * Ďalej sa kontroluje v danom smere, či sa o jedno políčko ďalej nachádza súperová figúrka a o dve políčka prázdne miesto.
     * Pokiaľ sa všetky if-y z tohoto smeru vyhodnotia ako true, zobrazí sa ťah na mieste za súperovou figúrkou (prázdne miesto o dve políčka ďalej) a 
     * nastaví sa "vyhod" atribút tohoto ťahu na true (dôležité pri kontrole po kliknutí na ťah, aby sme zistili či je vyhadzovací). 
     */

    private void zobrazVyhadzovacieTahy(int riadok, int stlpec) {
        this.skryTahy();
        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC1_PESIAK) {            

            if (riadok + 2 <= 7 && stlpec - 2 >= 0  && this.figurky[riadok + 1][stlpec - 1] != null ) { 
                if (this.figurky[riadok + 1][stlpec - 1].getFarba() == this.farba2 &&
                    this.figurky[riadok + 2][stlpec - 2] == null) {
                    this.tahy[riadok + 2][stlpec - 2].zobraz();
                    this.tahy[riadok + 2][stlpec - 2].setVyhod(true);
                }

            }            
            if (riadok + 2 <= 7 && stlpec + 2 <= 7  && this.figurky[riadok + 1][stlpec + 1] != null ) { 
                if (this.figurky[riadok + 1][stlpec + 1].getFarba() == this.farba2 &&
                    this.figurky[riadok + 2][stlpec + 2] == null) {
                    this.tahy[riadok + 2][stlpec + 2].zobraz();
                    this.tahy[riadok + 2][stlpec + 2].setVyhod(true);
                }
            }
        }

        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC2_PESIAK) {

            if (riadok - 2 >= 0 && stlpec - 2 >= 0 && this.figurky[riadok - 1][stlpec - 1] != null ) {
                if (this.figurky[riadok - 1][stlpec - 1].getFarba() == this.farba1 &&
                    this.figurky[riadok - 2][stlpec - 2] == null) {
                    this.tahy[riadok - 2][stlpec - 2].zobraz();   
                    this.tahy[riadok - 2][stlpec - 2].setVyhod(true);
                }  
            }

                       
            if (riadok - 2 >= 0 && stlpec + 2 <= 7 && this.figurky[riadok - 1][stlpec + 1] != null ) { 
                if (this.figurky[riadok - 1][stlpec + 1].getFarba() == this.farba1 &&
                    this.figurky[riadok - 2][stlpec + 2] == null) {
                    this.tahy[riadok - 2][stlpec + 2].zobraz(); 
                    this.tahy[riadok - 2][stlpec + 2].setVyhod(true);
                }   
            }

        }     

            
        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC1_DAMA) {

            if (riadok + 2 <= 7 && stlpec - 2 >= 0 && this.figurky[riadok + 1][stlpec - 1] != null ) { 
                if (this.figurky[riadok + 1][stlpec - 1].getFarba() == this.farba2 &&
                    this.figurky[riadok + 2][stlpec - 2] == null) {
                    this.tahy[riadok + 2][stlpec - 2].zobraz();
                    this.tahy[riadok + 2][stlpec - 2].setVyhod(true);
                }
            }

            if (riadok + 2 <= 7 && stlpec + 2 <= 7  && this.figurky[riadok + 1][stlpec + 1] != null ) { 
                if (this.figurky[riadok + 1][stlpec + 1].getFarba() == this.farba2 &&
                    this.figurky[riadok + 2][stlpec + 2] == null ) {
                    this.tahy[riadok + 2][stlpec + 2].zobraz();
                    this.tahy[riadok + 2][stlpec + 2].setVyhod(true);
                }
            }

            if (riadok - 2 >= 0 && stlpec - 2 >= 0 && this.figurky[riadok - 1][stlpec - 1] != null ) {
                if (this.figurky[riadok - 1][stlpec - 1].getFarba() == this.farba2 &&
                    this.figurky[riadok - 2][stlpec - 2] == null) {
                    this.tahy[riadok - 2][stlpec - 2].zobraz(); 
                    this.tahy[riadok - 2][stlpec - 2].setVyhod(true);
                }  
            }

            if (riadok - 2 >= 0 && stlpec + 2 <= 7 && this.figurky[riadok - 1][stlpec + 1] != null ) { 
                if (this.figurky[riadok - 1][stlpec + 1].getFarba() == this.farba2 &&
                    this.figurky[riadok - 2][stlpec + 2] == null) {

                    this.tahy[riadok - 2][stlpec + 2].zobraz();   
                    this.tahy[riadok - 2][stlpec + 2].setVyhod(true);
                }   
            }

        }    
        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC2_DAMA) {
            if (riadok + 2 <= 7 && stlpec - 2 >= 0 && this.figurky[riadok + 1][stlpec - 1] != null ) { 
                if (this.figurky[riadok + 1][stlpec - 1].getFarba() == this.farba1 &&
                    this.figurky[riadok + 2][stlpec - 2] == null ) {
                    this.tahy[riadok + 2][stlpec - 2].zobraz();
                    this.tahy[riadok + 2][stlpec - 2].setVyhod(true);
                }
            }

            if (riadok + 2 <= 7 && stlpec + 2 <= 7 && this.figurky[riadok + 1][stlpec + 1] != null  ) { 
                if (this.figurky[riadok + 1][stlpec + 1].getFarba() == this.farba1 &&
                    this.figurky[riadok + 2][stlpec + 2] == null ) {
                    this.tahy[riadok + 2][stlpec + 2].zobraz();
                    this.tahy[riadok + 2][stlpec + 2].setVyhod(true);
                }
            }

            if (riadok - 2 >= 0 && stlpec - 2 >= 0 && this.figurky[riadok - 1][stlpec - 1] != null ) {
                if (this.figurky[riadok - 1][stlpec - 1].getFarba() == this.farba1 &&
                    this.figurky[riadok - 2][stlpec - 2] == null ) {
                    this.tahy[riadok - 2][stlpec - 2].zobraz();  
                    this.tahy[riadok - 2][stlpec - 2].setVyhod(true);
                }  
            }

            if (riadok - 2 >= 0 && stlpec + 2 <= 7 && this.figurky[riadok - 1][stlpec + 1] != null ) { 
                if (this.figurky[riadok - 1][stlpec + 1].getFarba() == this.farba1 &&
                    this.figurky[riadok - 2][stlpec + 2] == null) {
                    this.tahy[riadok - 2][stlpec + 2].zobraz();   
                    this.tahy[riadok - 2][stlpec + 2].setVyhod(true);
                }   
            }

        }            

    }        
    
    /**
     * Univerzálna metóda pre všetky typy figúrok, ktorá zobrazuje jednoduché ťahy o jedno políčko.
     * Všeobecný princíp fungovania metódy:
     * Pomocou parametrov riadok a stĺpec sa nájde typ figúrky, keďže každá figúrka má iné jednoduché ťahy(Okrem dám).
     * Po nájdení typu figúrky sa kontrolujú ťahy pre jej smery pohybu.
     * Prvá kontrola v smere spočíva v tom, či by pri posune bola figúrka ešte stále na šachovnici(Bez tejto kontroli by nastávali chyby, keďže by sme skúšali načítať prvok mimo poľa).
     * Ďalej sa kontroluje v danom smere, či sa o jedno políčko ďalej nachádza  prázdne miesto.
     * Pokiaľ sa všetky if-y z tohoto smeru vyhodnotia ako true, zobrazí sa ťah. 
     * 
     */    
    private void zobrazTahy(int riadok, int stlpec) {
        this.skryTahy();

        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC1_PESIAK) {            
            if (riadok + 1 <= 7) {
                if (stlpec - 1 >= 0) {
                    if (this.figurky[riadok + 1][stlpec - 1] == null) {
                        this.tahy[riadok + 1][stlpec - 1].zobraz();
                    } 
                }            
                if (stlpec + 1 <= 7) {
                    if (this.figurky[riadok + 1][stlpec + 1] == null) {
                        this.tahy[riadok + 1][stlpec + 1].zobraz();
                    } 
                }
            }
        }

        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC2_PESIAK) {           
            if (riadok - 1 >= 0) {
                if (stlpec - 1 >= 0) {
                    if (this.figurky[riadok - 1][stlpec - 1] == null) {
                        this.tahy[riadok - 1][stlpec - 1].zobraz();
                    } 
                }
                if (stlpec + 1 <= 7) {
                    if (this.figurky[riadok - 1][stlpec + 1] == null) {
                        this.tahy[riadok - 1][stlpec + 1].zobraz();
                    } 
                }            
            }
        } 

        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC1_DAMA || this.figurky[riadok][stlpec].getTyp() == Typ.HRAC2_DAMA) {
            if (riadok + 1 <= 7) {
                if (stlpec - 1 >= 0) {
                    if (this.figurky[riadok + 1][stlpec - 1] == null) {
                        this.tahy[riadok + 1][stlpec - 1].zobraz();
                    } 
                }            
                if (stlpec + 1 <= 7) {
                    if (this.figurky[riadok + 1][stlpec + 1] == null) {
                        this.tahy[riadok + 1][stlpec + 1].zobraz();
                    } 
                }
            }            
            if (riadok - 1 >= 0) {
                if (stlpec - 1 >= 0) {
                    if (this.figurky[riadok - 1][stlpec - 1] == null) {
                        this.tahy[riadok - 1][stlpec - 1].zobraz();
                    } 
                }
                if (stlpec + 1 <= 7) {
                    if (this.figurky[riadok - 1][stlpec + 1] == null) {
                        this.tahy[riadok - 1][stlpec + 1].zobraz();
                    } 
                }            
            }    
        }    

        
        
    }
    /**
     * Univerzálna metóda pre všetky typy figúrok, ktorá kontroluje, či má figúrka vyhadzovacie ťahy.
     * Všeobecný princíp fungovania metódy:
     * Pomocou parametrov riadok a stĺpec sa nájde typ figúrky, keďže každá figúrka má iné vyhadzovacie ťahy.
     * Po nájdení typu figúrky sa kontrolujú ťahy pre jej smery pohybu.
     * Prvá kontrola v smere spočíva v tom, či by pri skoku bola figúrka ešte stále na šachovnici(Bez tejto kontroli by nastávali chyby, keďže by sme skúšali načítať prvok mimo poľa).
     * Ďalej sa kontroluje v danom smere, či sa o jedno políčko ďalej nachádza súperová figúrka a o dve políčka prázdne miesto.
     * Pokiaľ sa všetky if-y z tohoto smeru vyhodnotia ako true, metóda vráti true.
     * @return true, ak má figúrka vyhadzovacie ťahy.
     */
    private boolean maVyhadzovacieTahy(int riadok, int stlpec) {

        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC1_PESIAK) {  

            if (riadok + 2 <= 7 && stlpec - 2 >= 0) { 
                if (this.figurky[riadok + 1][stlpec - 1] != null) {
                    if (this.figurky[riadok + 1][stlpec - 1].getFarba() == this.farba2 &&
                        this.figurky[riadok + 2][stlpec - 2] == null) {

                        return true;
                    }
                }        
            }

            if (riadok + 2 <= 7 && stlpec + 2 <= 7 ) { 
                if (this.figurky[riadok + 1][stlpec + 1] != null) {
                    if (this.figurky[riadok + 1][stlpec + 1].getFarba() == this.farba2 &&
                        this.figurky[riadok + 2][stlpec + 2] == null) {
                        return true;
                    }
                }
            }
        }

        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC2_PESIAK) {                
            if (riadok - 2 >= 0 && stlpec - 2 >= 0) {
                if (this.figurky[riadok - 1][stlpec - 1] != null) {
                    if (this.figurky[riadok - 1][stlpec - 1].getFarba() == this.farba1 &&
                        this.figurky[riadok - 2][stlpec - 2] == null) {
                        return true;   
                    }  
                }
            }

            if (riadok - 2 >= 0 && stlpec + 2 <= 7) { 
                if (this.figurky[riadok - 1][stlpec + 1] != null) {
                    if (this.figurky[riadok - 1][stlpec + 1].getFarba() == this.farba1 &&
                        this.figurky[riadok - 2][stlpec + 2] == null) {
                        return true;   
                    }   
                }
            }
        }     

        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC1_DAMA) {
            if (riadok + 2 <= 7 && stlpec - 2 >= 0) { 
                if (this.figurky[riadok + 1][stlpec - 1] != null) {
                    if (this.figurky[riadok + 1][stlpec - 1].getFarba() == this.farba2 &&
                        this.figurky[riadok + 2][stlpec - 2] == null) {
                        return true;
                    }
                }
            }
            if (riadok + 2 <= 7 && stlpec + 2 <= 7 ) { 
                if (this.figurky[riadok + 1][stlpec + 1] != null) {
                    if (this.figurky[riadok + 1][stlpec + 1].getFarba() == this.farba2 &&
                        this.figurky[riadok + 2][stlpec + 2] == null ) {
                        return true;
                    }
                }
            }
            if (riadok - 2 >= 0 && stlpec - 2 >= 0) {
                if (this.figurky[riadok - 1][stlpec - 1] != null) {
                    if (this.figurky[riadok - 1][stlpec - 1].getFarba() == this.farba2 &&
                        this.figurky[riadok - 2][stlpec - 2] == null) {
                        return true; 
                    }  
                }
            }
            if (riadok - 2 >= 0 && stlpec + 2 <= 7) { 
                if (this.figurky[riadok - 1][stlpec + 1] != null) {
                    if (this.figurky[riadok - 1][stlpec + 1].getFarba() == this.farba2 &&
                        this.figurky[riadok - 2][stlpec + 2] == null) {
                        return true;   
                    }   
                }
            }
        }    
        if (this.figurky[riadok][stlpec].getTyp() == Typ.HRAC2_DAMA) {
            if (riadok + 2 <= 7 && stlpec - 2 >= 0) { 
                if (this.figurky[riadok + 1][stlpec - 1] != null) {
                    if (this.figurky[riadok + 1][stlpec - 1].getFarba() == this.farba1 &&
                        this.figurky[riadok + 2][stlpec - 2] == null ) {
                        return true;
                    }
                }
            }   
            if (riadok + 2 <= 7 && stlpec + 2 <= 7 ) { 
                if (this.figurky[riadok + 1][stlpec + 1] != null) {
                    if (this.figurky[riadok + 1][stlpec + 1].getFarba() == this.farba1 &&
                        this.figurky[riadok + 2][stlpec + 2] == null ) {
                        return true;
                    }
                }
            }

            if (riadok - 2 >= 0 && stlpec - 2 >= 0) {
                if (this.figurky[riadok - 1][stlpec - 1] != null) {
                    if (this.figurky[riadok - 1][stlpec - 1].getFarba() == this.farba1 &&
                        this.figurky[riadok - 2][stlpec - 2] == null ) {
                        return true;   
                    }  
                }
            }
            if (riadok - 2 >= 0 && stlpec + 2 <= 7) { 
                if (this.figurky[riadok - 1][stlpec + 1] != null) {
                    if (this.figurky[riadok - 1][stlpec + 1].getFarba() == this.farba1 &&
                        this.figurky[riadok - 2][stlpec + 2] == null) {
                        return true;   

                    }   
                }
            }
        }            
        return false;
    }

    /**
     * Skryje všetky ťahy na plátne.
     */
    private void skryTahy() {
        for (int riadok = 0; riadok < 8; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {
                if (this.sachovnica.getFarba(riadok, stlpec).equals("black")) {
                    this.tahy[riadok][stlpec].skry();
                }
            }   
        }         

    }


    /**
     * Spustí odpočet času na aktuálnej časomiere (vďaka metóde tik()) a nastaví čas podľa zadaného parametra.
     */
    private void startCas(int sekundy) {
        this.meraj = true;
        this.cas = sekundy * 4;
    } 

    /**
     * Manažér posiela správu tik() každých 0,25s.
     * Pokiaľ je atribút meraj nastavený ako true, každú sekundu sa odráta čas na aktuálnej časomiere.
     * Pokiaľ čas dosiahne hodnotu 0, odpočet sa zastaví, hráč ktorému došiel čas prehral, druhý sa vyhodnotí ako víťaž a ukončí sa hra metódou koniec().
     */
    public void tik() {

        if (this.meraj) {
            this.cas--;

            if (this.cas % 4 == 0 ) {
                this.aktualnaCasomiera.setAktualnaHodnota(this.cas / 4);
                this.aktualnaCasomiera.zobrazAktualnuHodnotu();   

            }
            if (this.cas == 0) {
                this.meraj = false;
                this.aktualnyHrac = 0;
                if (this.aktualnaCasomiera == this.casomiera1) {
                    this.stavHry = StavHry.VYHRAL_HRAC2;   
                } else if (this.aktualnaCasomiera == this.casomiera2) {
                    this.stavHry = StavHry.VYHRAL_HRAC1;   
                }
                this.koniec();

            }
        }
    }
    
    /**
     * Pošle Zapisovaču stav hracej plochy (údaj o stave každého políčka) a 
     * vytvorí z neho textový súbor.
     * 
     * 
     * @param String nazovSuboru Názov súboru, do ktorého uloží zapisovač stav hracej plochy.
     *                           Musí byť v tvare ___.txt
     */
    private void posliFigurkyZapisovacu(String nazovSuboru) throws IOException {
        for (int riadok = 0; riadok < 8; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {
                if (this.figurky[riadok][stlpec] == null) {
                    this.zapisovac.figurkyNapln(riadok, stlpec, Typ.NEZARADENE);
                } else {
                    this.zapisovac.figurkyNapln(riadok, stlpec, this.figurky[riadok][stlpec].getTyp());
                }
                
            }
            
        }
        this.zapisovac.vytvor(nazovSuboru);
    }
    
    /**
     * Undo metóda, ktorá je ovládaná šípkou nadol (vďaka manažérovi).
     * Pri spustení obnoví posledný stav hracej plochy a vytvorí redo súbor.
     */
    public void posunDole() throws IOException {
        if (!this.undo) {
            this.posliFigurkyZapisovacu("redo.txt");
            this.nacitajHru("undo.txt");
            this.skryTahy();
            this.musiVyhodit = false;
            this.zmenHraca();
            this.undo = true;
        }
        
    }
    
    
    /**
     * Redo metóda, ktorá je ovládaná šípkou nahor (vďaka manažérovi).
     * Pri spustení obnoví posledný stav hracej plochy pred spustením undo metódy.
     */
    public void posunHore() throws IOException {
        if (this.undo) {
            this.nacitajHru("redo.txt");
            this.skryTahy();
            this.musiVyhodit = false;
            this.zmenHraca();
            this.undo = false;
            this.redo = true;
        }
        
    }
    
    
    /**
     * Metóda slúži na načítanie hry zo súboru.
     * Metóda otvorí súbor, postupne vďaka cyklu načíta každý riadok súboru a vyberie z neho postupne každý znak.
     * V tele cyklu sa kontroluje znak a podľa každého znaku sa vytvorí figúrka na konkrétnej pozícií.
     * 
     * Zdroj charAt() metódy:
     * https://stackoverflow.com/questions/7853502/how-to-convert-parse-from-string-to-char-in-java
     * @param String nazovSuboru Názov súboru, z ktorého sa načíta hra. Musí byť v tvare ___.txt.
     */
    public void nacitajHru(String nazovSuboru) throws IOException {
        File subor = new File(nazovSuboru);
        Scanner citac = new Scanner(subor);
        
        for (int riadok = 0; riadok < 8; riadok++) {
            String figurkaNaVytvorenie = citac.next();
            for (int stlpec = 0; stlpec < 8; stlpec++) {
                if (this.figurky[riadok][stlpec] != null) {
                    this.figurky[riadok][stlpec].skry();
                    this.figurky[riadok][stlpec] = null;
                }
                
                char figurkaNaVytvorenieChar = figurkaNaVytvorenie.charAt(stlpec);
                Typ typFigurky = Typ.vratTyp(figurkaNaVytvorenieChar);
                if (typFigurky != null) {
                    
                    this.figurky[riadok][stlpec] = new Figurka(this.x + stlpec * this.strana, this.y + riadok * this.strana, this.strana);
                    this.figurky[riadok][stlpec].setTyp(typFigurky);
                    if (figurkaNaVytvorenieChar == 'a' || figurkaNaVytvorenieChar == 'A') {
                        this.figurky[riadok][stlpec].zmenFarbu(this.farba1);
                    } else {
                        this.figurky[riadok][stlpec].zmenFarbu(this.farba2);
                    }
                    if (figurkaNaVytvorenieChar == 'A' || figurkaNaVytvorenieChar == 'B') {
                        this.figurky[riadok][stlpec].setDama(true);
                    } 
                
                    this.figurky[riadok][stlpec].zobraz();
                }
            }
            
        }
        citac.close();
    }
    
    public void zrus() {
        this.koniec();
    }

}
