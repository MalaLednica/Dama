
/**
 * Enum trieda využívaná v hre dáma a pri práci s jej súbormi.
 * Trieda robená pomocou hry Sokoban.
 * @author Jozef Vitko
 * @version 2.0
 */
public enum Typ {
    HRAC1_PESIAK('a'),
    HRAC1_DAMA('A'),
    HRAC2_PESIAK('b'),
    HRAC2_DAMA('B'),
    NEZARADENE('x');
    
    private char znak;
    /**
     * Konštruktor priradí Typu String atribút.
     */
    Typ(char znak) {
        this.znak = znak;
    }
    /**
     * Metóda na získanie String atribútu Typu.
     */
    public String toString() {
        return "" + this.znak;
    }
    /**
     * Metóda na získanie Typu podľa jeho znaku.
     */
    public static Typ vratTyp(char znak) {
        switch (znak) {
            case 'a':
                return Typ.HRAC1_PESIAK;
            
            case 'A':
                return Typ.HRAC1_DAMA;
            
            case 'b':
                return Typ.HRAC2_PESIAK;
            
            case 'B':
                return Typ.HRAC2_DAMA;
            
            case 'x':
                return null;
            

            
        
            
        }
        return null;    
    }
}

