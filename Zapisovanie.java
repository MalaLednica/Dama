
/**
 * Trieda slúži na vytváranie a mazanie textových súborov, v ktorých sú uložené hracie plochy.
 * 
 * @author Jozef Vitko
 * @version 1.0
 */


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
public class Zapisovanie {
    private Typ[][] figurkyNaZapis;
    
    /**
     * Konštruktor vytvorý dvojrozmerné pole na ukladanie typov figúrok.
     */
    public Zapisovanie() {
        this.figurkyNaZapis = new Typ[8][8];
    }
    
    /**
     * Zmení prvok poľa Typov figurok podľa zadaných parametrov.
     */
    public void figurkyNapln(int riadok, int stlpec, Typ typFigurky) {
        this.figurkyNaZapis[riadok][stlpec] = typFigurky;
    }
    
    /**
     * Vytvorí textový súbor s 8x8 znakmi, ktoré reprezentujú typy figúrok.
     * @param String nazovSuboru Musí mať tvar ___.txt.
     */
    public void vytvor(String nazovSuboru) throws IOException {
        File plocha = new File(nazovSuboru);
        PrintWriter zapis = new PrintWriter(plocha);
        for (int riadok = 0; riadok < 8; riadok++) {
            for (int stlpec = 0; stlpec < 8; stlpec++) {
                zapis.print(this.figurkyNaZapis[riadok][stlpec].toString());
                
            }
            zapis.println();
        }
        zapis.close();
    }
    
    /**
     * Vymaže súbor podľa zadaného parametru.
     * 
     * Zdroj delete() metódy:
     * https://www.javatpoint.com/how-to-delete-a-file-in-java
     * @param String nazovSuboru Súbor musí existovať a mať tvar ___.txt.
     */
    public void vymaz(String nazovSuboru) {
        File subor = new File(nazovSuboru);
        subor.delete();
    }
}
