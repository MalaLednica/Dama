/**
 * Trieda, ktorá pracuje s figúrkami zloženými z dvoch kruhov, vykreslených na plátne.
 * @author Jozef Vitko
 * @version 3.0
 */
public class Figurka {
    private Typ typ;
    private boolean jeDama;
    private Kruh pesiak;
    private Kruh dama;
    private boolean jeViditelny;
    private int lavyHornyX;
    private int lavyHornyY;
    private int velkost;
    private Farba farba;
    
    /**
     * Konštruktor vytvorí figúrku na pozícií podľa parametrov.
     * Nastavia sa defaultné nastavenia atribútov figúrky.
     * Vytvoria sa 2kruhy, z ktorých je figúrka zložená.
     * @param velkost Udáva veľkosť figúrky (pešiaka)
     */
    public Figurka(int lavyHornyX, int lavyHornyY, int velkost) {
        this.lavyHornyX = lavyHornyX;
        this.lavyHornyY = lavyHornyY;
        this.velkost = velkost;
        this.jeDama = false;
        this.jeViditelny = false;
        this.pesiak = new Kruh();
        this.dama = new Kruh();
        this.typ = Typ.NEZARADENE;
        
        
        this.dama.zmenFarbu("red");
        
        this.pesiak.zmenPriemer(this.velkost);
        this.pesiak.zmenPolohu(this.lavyHornyX, this.lavyHornyY);
        
        this.dama.zmenPriemer(this.velkost / 2);
        this.dama.zmenPolohu(this.lavyHornyX + this.velkost / 2 - this.velkost / 4, this.lavyHornyY + this.velkost / 2 - this.velkost / 4);
    }
    
    /**
     * Zmení farbu figúrky podľa zadaného parametra.
     */
    public void zmenFarbu(Farba farba) {
        this.farba = farba;
        this.pesiak.zmenFarbu(this.farba.getString());
    }
    
    /**
     * Vráti farbu figúrky ako enum triedy Farba.
     */
    public Farba getFarba() {
        return this.farba;
        
    }
    
    /**
     * Zobrazí figúrku.
     */
    public void zobraz() {
        this.pesiak.zobraz();
        if (this.jeDama) {
            this.dama.zobraz();
        } else {
            this.dama.skry();
        }
            
        this.jeViditelny = true;
    }
    
    /**
     * Skryje figúrku.
     */
    public void skry() {
        this.pesiak.skry();
        this.dama.skry();
        this.jeViditelny = false;
    }
    
    /**
     * Vráti true pokiaľ je figúrka dáma.
     */
    public boolean getDama() {
        return this.jeDama;   
    }
    
    /**
     * Zmení figúrku na dámu (true) alebo pešiaka (false) a aktualizuje atribút o jej stave.
     */
    public void setDama(boolean nastavDamu) {
        if (this.jeDama != nastavDamu) {
            this.jeDama = nastavDamu;
            
            if (this.jeViditelny) {
                this.zobraz();
            }
        }
    }
    
    /**
     * Zmení polohu figúrky a aktualizuje atribúty o jej polohe.
     */
    public void zmenPolohu(int x, int y) {
        this.lavyHornyX = x;
        this.lavyHornyY = y;
        
        this.pesiak.zmenPolohu(this.lavyHornyX, this.lavyHornyY);
        this.dama.zmenPolohu(this.lavyHornyX + this.velkost / 2 - this.velkost / 4, this.lavyHornyY + this.velkost / 2 - this.velkost / 4);
        
    
    
    }
    
    /**
     * Vráti figúrku, ak figúrka obsahuje súradnice z parametra x a y.
     */    
    public boolean obsahujeSuradnice(int x, int y) {
        if ((x >= this.lavyHornyX && x <= this.lavyHornyX + this.velkost)
             && (y >= this.lavyHornyY && y <= this.lavyHornyY + this.velkost)) {
            return true;
        }
        return false;
        
    }
    
    /**
     * Vráti figúrku, ak figúrka obsahuje súradnice z parametra x a y.
     */
    public Figurka vyberFigurku(int x, int y) {
        if (this.obsahujeSuradnice(x, y)) {
            return this;
        } else {
            return null;
        }
    }
    
    /**
     * Vráti x-ovú súradnicu ľavého horného rohu figúrky.
     */
    public int getX() {
        return this.lavyHornyX;
    }
    
    /**
     * Vráti y-ovú súradnicu ľavého horného rohu figúrky.
     */
    public int getY() {
        return this.lavyHornyY;
    }
    
    /**
     * Vráti hodnotu atribútu typ.
     */
    public Typ getTyp() {
        return this.typ;
    }
    
    /**
     * Nastaví hodnotu atribútu typ podľa zadaného parametru.
     */
    public void setTyp(Typ typ) {
        this.typ = typ;
    }
    

    
    
    
}