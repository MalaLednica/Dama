/** 
 * Trieda, ktorá pracuje so segmentom, tvoreným obdĺžnikom.
 */
public class Segment {
  
    private Obdlznik segment;
    /**
     * Konštruktor vytvorí segment tvorený obdĺžnikom.
     * Rozmery a pozícia sú určené parametrami.
     */
    public Segment(int dlzkaSegmentu, int hrubkaSegmentu, int poziciaX, int poziciaY) {        
        this.segment = new Obdlznik();        
        this.segment.zmenStrany(dlzkaSegmentu, hrubkaSegmentu);
        this.segment.zmenPolohu(poziciaX, poziciaY);
    }
    
    /**
     * Zobrazí segment.
     */
    public void rozsviet() {
        this.segment.zobraz();
    }
    
    /**
     * Skryje segment.
     */
    public void zhasni() {
        this.segment.skry();
    }
    
    /**
     * Zmení farbu podľa zadaného parametra.
     */
    public void zmenFarbu(String farba) {
        this.segment.zmenFarbu(farba);   
        
        
    }
    
    
    
    
}
