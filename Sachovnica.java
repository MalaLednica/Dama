/**
 * Jednoduchá trieda, ktorá slúži ako šachovnica vykreslená na plátne.
 * @author Jozef Vitko
 * @version 1.0
 */
public class Sachovnica {
    
    private Obdlznik[][] pole;
    private int lavyHornyX;
    private int lavyHornyY;
    private int strana;
    
    
    
    /**
     * Konštruktor vytvorí štandardnú šachovnicu 8x8 z obdĺžnikov, ktoré sú uložené v poli. Pri vytvorení sa šachovnica nevykreslí na plátno.
     * @param strana Dĺžka strany jedného políčka.
     */
    public Sachovnica(int lavyHornyX, int lavyHornyY, int strana) {
        this.lavyHornyX = lavyHornyX;
        this.lavyHornyY = lavyHornyY;
        this.strana = strana;
        this.pole = new Obdlznik[8][8];
        boolean cierna = true;
        for (int riadok = 0; riadok < 8; riadok++) {
            cierna = !cierna;
            
            for (int stlpec = 0; stlpec < 8; stlpec++) {
                
                this.pole[riadok][stlpec] = new Obdlznik();
                this.pole[riadok][stlpec].zmenStrany(this.strana, this.strana);
                
                this.pole[riadok][stlpec].zmenPolohu(this.lavyHornyX + this.strana * riadok, this.lavyHornyY + this.strana * stlpec);
                
                if (cierna) {
                    this.pole[riadok][stlpec].zmenFarbu("black");
                    cierna = !cierna;
                } else {
                    this.pole[riadok][stlpec].zmenFarbu("white");
                    cierna = !cierna;
                }
                
                
            }
            
            
            
            
            
        }
        
        
        
        
    }
    
    /**
     * Zobrazí šachovnicu.
     */
    public void zobraz() {
        for (int riadok = 0; riadok < 8; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {
                this.pole[riadok][stlpec].zobraz();
            }
        }
    }
    
    /**
     * Skryje šachovnicu.
     */
    public void skry() {
        for (int riadok = 0; riadok < 8; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {
                this.pole[riadok][stlpec].skry();
            }
        }
    }
    
    /**
     * Vráti farbu políčka na pozícií podľa parametrov riadok, stĺpec na šachovnicovom poli ako String. 
     */
    public String getFarba(int riadok, int stlpec) {
        return this.pole[riadok][stlpec].getFarba();
        
    }
    
    
    
    
    
    
    
    
    
}
