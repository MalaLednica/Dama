/** 
 * Trieda, ktorá pracuje so sedem segmentovým displejom, zloženým zo siedmych segmentov, ktoré tvoria číslo.
 */
public class SSD {

    private Segment a;
    private Segment b;
    private Segment c;
    private Segment d;
    private Segment e;
    private Segment f;
    private Segment g;

    private int hodnota;
    
    /**
     * Konštruktor vytvorí SSD zložený zo 7segmentov.
     * Pozícia a rozmery sú udávané parametrami.
     */
    public SSD(int dlzkaSegmentu, int hrubkaSegmentu, int poziciaDisplejaX, int poziciaDisplejaY) {
        this.hodnota = -1;
        this.a = new Segment(dlzkaSegmentu, hrubkaSegmentu,
            poziciaDisplejaX + hrubkaSegmentu,
            poziciaDisplejaY);

        this.b = new Segment(hrubkaSegmentu, dlzkaSegmentu, 
            poziciaDisplejaX + hrubkaSegmentu + dlzkaSegmentu,
            poziciaDisplejaY + hrubkaSegmentu);

        this.c = new Segment(hrubkaSegmentu, dlzkaSegmentu,
            poziciaDisplejaX + hrubkaSegmentu + dlzkaSegmentu,
            poziciaDisplejaY + 2 * hrubkaSegmentu + dlzkaSegmentu);

        this.d = new Segment(dlzkaSegmentu, hrubkaSegmentu,
            poziciaDisplejaX + hrubkaSegmentu,
            poziciaDisplejaY + 2 * hrubkaSegmentu + 2 * dlzkaSegmentu);   

        this.e = new Segment(hrubkaSegmentu, dlzkaSegmentu,
            poziciaDisplejaX,
            poziciaDisplejaY + 2 * hrubkaSegmentu + dlzkaSegmentu);

        this.f = new Segment(hrubkaSegmentu, dlzkaSegmentu,
            poziciaDisplejaX,
            poziciaDisplejaY + hrubkaSegmentu);

        this.g = new Segment(dlzkaSegmentu, hrubkaSegmentu,
            poziciaDisplejaX + hrubkaSegmentu,
            poziciaDisplejaY + hrubkaSegmentu + dlzkaSegmentu);
    }
    
    /**
     * Zobrazí číslo 0.
     */
    public void zobraz0() {
        
        if (this.hodnota != 0) {
            this.zhasniVsetko();
            this.a.rozsviet();
            this.b.rozsviet();
            this.c.rozsviet();
            this.d.rozsviet();
            this.e.rozsviet(); 
            this.f.rozsviet(); 
        }
        this.hodnota = 0;
    }
    
    /**
     * Zobrazí číslo 1.
     */
    public void zobraz1() {
        if (this.hodnota != 1) {
            this.zhasniVsetko();
            this.b.rozsviet();
            this.c.rozsviet();
        }
        this.hodnota = 1;
    }
    
    /**
     * Zobrazí číslo 2.
     */
    public void zobraz2() {
        if (this.hodnota != 2) {
            this.zhasniVsetko();
            this.a.rozsviet();
            this.e.rozsviet();
            this.b.rozsviet();
            this.d.rozsviet();
            this.g.rozsviet();  
        }
        this.hodnota = 2;
    }
    
    /**
     * Zobrazí číslo 3.
     */
    public void zobraz3() {
        if (this.hodnota != 3) {
            this.zobraz1();
            this.a.rozsviet();
            this.g.rozsviet();
            this.d.rozsviet();
        }
        this.hodnota = 3;
    }
    
    /**
     * Zobrazí číslo 4.
     */
    public void zobraz4() {
        if (this.hodnota != 4) {
            this.zobraz1();
            this.f.rozsviet();
            this.g.rozsviet();
        }
        this.hodnota = 4;
    }
    
    /**
     * Zobrazí číslo 5.
     */
    public void zobraz5() {
        if (this.hodnota != 5) {
            this.zhasniVsetko();
            this.a.rozsviet();
            this.c.rozsviet();
            this.d.rozsviet();
            this.f.rozsviet();
            this.g.rozsviet();  
        }
        this.hodnota = 5;
    }
    
    /**
     * Zobrazí číslo 6.
     */
    public void zobraz6() {
        if (this.hodnota != 6) {
            this.zobraz5();
            this.e.rozsviet();
        }
        this.hodnota = 6;
    }
    
    /**
     * Zobrazí číslo 7.
     */
    public void zobraz7() {
        if (this.hodnota != 7) {
            this.zobraz1();
            this.a.rozsviet();
        }
        this.hodnota = 7;
    }
    
    /**
     * Zobrazí číslo 8.
     */
    public void zobraz8() {
        if (this.hodnota != 8) {
            this.rozsvietVsetko();
        }
        this.hodnota = 8;
    }
    
    /**
     * Zobrazí číslo 9.
     */
    public void zobraz9() {
        if (this.hodnota != 9) {
            this.zhasniVsetko();
            this.a.rozsviet();
            this.b.rozsviet();
            this.c.rozsviet();
            this.f.rozsviet();
            this.g.rozsviet();   
        }
        this.hodnota = 9;
    }

    /**
     * Zobrazí číslo podľa zadaného parametra.
     * @param cifra Celé číslo medzi 0-9, pri inej hodnote sa SSD zhasne.
     */
    public void zobraz(int cifra) {
        switch (cifra) {
            case 0:
                this.zobraz0();
                break;
            case 1:
                this.zobraz1();
                break;
            case 2:
                this.zobraz2();
                break;
            case 3:
                this.zobraz3();
                break;
            case 4:
                this.zobraz4();
                break;
            case 5:
                this.zobraz5();
                break;
            case 6:
                this.zobraz6();
                break;
            case 7:
                this.zobraz7();
                break;
            case 8:
                this.zobraz8();
                break;
            case 9:
                this.zobraz9();
                break;
            default:
                this.zhasniVsetko();
                break;
        }
    }
    
    /**
     * Zobrazí všetky segmenty.
     */
    public void rozsvietVsetko() {
        this.a.rozsviet();
        this.b.rozsviet();
        this.c.rozsviet();
        this.d.rozsviet();
        this.e.rozsviet();
        this.f.rozsviet();
        this.g.rozsviet();   
    }
    
    /**
     * Skryje všetky segmenty.
     */
    public void zhasniVsetko() {
        this.a.zhasni();
        this.b.zhasni();
        this.c.zhasni();
        this.d.zhasni();
        this.e.zhasni();
        this.f.zhasni();
        this.g.zhasni();
    }


    /**
     * Zmení farbu všetkých segmentov podľa parametra.
     */
    public void zmenFarbu(String farba) {
        this.a.zmenFarbu(farba);
        this.b.zmenFarbu(farba);
        this.c.zmenFarbu(farba);
        this.d.zmenFarbu(farba);
        this.e.zmenFarbu(farba);
        this.f.zmenFarbu(farba);
        this.g.zmenFarbu(farba);
        
        
        
    }
}
