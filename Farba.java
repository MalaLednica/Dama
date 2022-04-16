
/**
 * Enum trieda slúžiaca na prácu s farbami.
 * 
 * @author Jozef Vitko
 * @version 1.1
 */
public enum Farba {
    GREEN("green"),
    BLUE("blue"),
    RED("red"),
    YELLOW("yellow"),
    WHITE("white"),
    BLACK("black");
    private String farba;
    
    /**
     * Konštruktor priradí Farbe String atribút.
     */
    Farba(String farba) {
        this.farba = farba;
        
    }
    
    /**
     * Metóda na získanie String atribútu Farby.
     */
    public String getString() {
        return this.farba;
        
    }
    
}

    
    
    
