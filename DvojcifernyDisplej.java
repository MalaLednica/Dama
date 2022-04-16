/** 
 * Trieda, ktorá pracuje s displejom, zloženým z dvoch SSD, na ktorých je zobrazená hodnota desiatok a jednotiek.
 */
public class DvojcifernyDisplej {

    private SSD lavy;
    private SSD pravy;
    private int aktualnaHodnota;
    private int maximalnaHodnota;
    private int dlzka;
    private int sirka;
    /**
     * Konštruktor vytvorí dvojciferný displej na pozícií podľa parametrov.
     * Rozmery, aktuálna a maximálna hodnota pri vytvorení sú udávané parametrami.
     */
    public DvojcifernyDisplej(int poziciaDisplejaX, int poziciaDisplejaY, int dlzka, int sirka, int aktualnaHodnota, int maximalnaHodnota) {
        this.aktualnaHodnota = aktualnaHodnota;
        this.maximalnaHodnota = maximalnaHodnota;
        this.dlzka = dlzka;
        this.sirka = sirka;
        this.lavy = new SSD(dlzka, sirka, poziciaDisplejaX, poziciaDisplejaY);
        this.pravy = new SSD(dlzka, sirka, poziciaDisplejaX + dlzka + 3 * sirka, poziciaDisplejaY);                           
    }
    
    /**
     * Vráti aktuálnu hodnotu
     */
    public int getAktualnaHodnota() {
        return this.aktualnaHodnota;
    }
    
    /**
     * Nastaví aktuálnu hodnotu podľa parametra.
     */
    public void setAktualnaHodnota(int cislo) {
        this.aktualnaHodnota = cislo;
        
    }
    
    /**
     * Zobrazí aktuálny čas na displeji.
     */
    public void zobrazAktualnuHodnotu() {
        this.lavy.zobraz(this.aktualnaHodnota / 10);

        this.pravy.zobraz(this.aktualnaHodnota % 10);

    }
    

    /**
     * Zmení maximálnu hodnotu, ktorá sa môže na displeji zobraziť
     * @param Celé číslo medzi 1-99.
     */
    public void zmenMaximalnuHodnotu(int max) {
        if (0 < max && max < 100 ) {
            this.maximalnaHodnota = max;
        }
        
    }
    
    /**
     * Zmení farbu displeja podľa parametra.
     */
    public void zmenFarbu(String farba) {
        this.lavy.zmenFarbu(farba);   
        this.pravy.zmenFarbu(farba);
        
        
    }
}
