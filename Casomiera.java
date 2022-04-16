/**
 * Trieda, ktorá pracuje s časomierou, zloženou z dvoch dvojciferných displejov, na ktorých sú minúty a sekundy.
 * @author Jozef Vitko
 * @version 1.0
 */
public class Casomiera {
    
    
    private DvojcifernyDisplej minutovyDisplej;
    private DvojcifernyDisplej sekundovyDisplej;
    private int minuty;
    private int sekundy;
    private int dlzka;
    private int sirka;
    
    
    
    private boolean meraj;
    private int cas;
    
    /**
     * Konštruktor vytvorí časomieru zloženú z dvoch dvojciferných displejov na pozícií podľa parametrov.
     * Rozmery a čas pri vytvorení sú udávané parametrami.
     */
    public Casomiera(int poziciaDisplejaX, int poziciaDisplejaY, int dlzka, int sirka, int minuty, int sekundy) {
        this.minuty = minuty;
        this.sekundy = sekundy;
        this.dlzka = dlzka;
        this.sirka = sirka;
        
        this.minutovyDisplej = new DvojcifernyDisplej(poziciaDisplejaX, poziciaDisplejaY, this.dlzka, this.sirka, this.minuty, 99);
        this.sekundovyDisplej = new DvojcifernyDisplej(poziciaDisplejaX + 2 * this.dlzka + 7 * this.sirka , poziciaDisplejaY, this.dlzka, this.sirka, this.sekundy, 59);
                        
    }
    
    /**
     * Zobrazí na plátne časomieru.
     */
    public void zobrazAktualnuHodnotu() {
        this.minutovyDisplej.zobrazAktualnuHodnotu();
        this.sekundovyDisplej.zobrazAktualnuHodnotu();

    }
    
    /**
     * Zmení farbu časomiery podľa zadaného parametra.
     */
    public void zmenFarbu(String farba) {
        this.minutovyDisplej.zmenFarbu(farba);   
        this.sekundovyDisplej.zmenFarbu(farba);   
        
    }
    
    /**
     * Vráti čas na časomiere ako celé číslo v sekundách.
     */
    public int getCasVSekundach() {
        return this.minutovyDisplej.getAktualnaHodnota() * 60 + this.sekundovyDisplej.getAktualnaHodnota();
    }
    
    /**
     * Nastaví čas na časomiere podľa parametra.
     * @param posteSekund Čas na nastavenie v sekudnách.
     */
    public void setAktualnaHodnota(int pocetSekund) {
        this.minutovyDisplej.setAktualnaHodnota(pocetSekund / 60);
        this.sekundovyDisplej.setAktualnaHodnota(pocetSekund % 60);
    }

}
