//Szymon Ciula - gr. 4

import java.util.Scanner;

public class Source
{
    private static final Scanner sc = new Scanner(System.in);
    private static final Piosenki piosenki = new Piosenki();

    public static void main(String [] args)
    {
        for(byte zestaw=sc.nextByte(); zestaw-->0; )
        {
            piosenki.wprowadzPiosenki(sc);
            piosenki.potasuj();
            System.out.println(piosenki);
        }
    }
}

/** Klasa przechowujaca informacje o tytulach piosenek z dwoch plyt o tej samej liczbie piosenek (lub gdy pierwsza ma jedna piosenke wiecej).
 * Pozwala na tasowanie piosenek. Podczas tasowania znajdowany jest rowniez najdluzszy wspolny prefiks. */
class Piosenki
{
    public String [] piosenki; // tablica tytulow piosenek
    private final static Przedrostek przedrostek = new Przedrostek();

    Piosenki() { piosenki = null; }
    /** Pozwala wpisac utwory z obu plyt. */
    public void wprowadzPiosenki(Scanner sc)
    {
        int liczbaPiosenek = sc.nextInt();
        piosenki = new String [liczbaPiosenek];

        for(int i=0; i<liczbaPiosenek; ++i)
            piosenki[i] = sc.next();
    // Jesli liczba piosenek jest nieparzysta, zaoszczedzimy w ten sposob szukania przedrostka srodkowego elementu.
        przedrostek.setPrzedrostek(piosenki[liczbaPiosenek>>1]);
    }
    /** Tasuje piosenki w nastepujacy sposob: (p1 p2 p3 q1 q2  ->  p1 q1 p2 q2 p3).
     * Znajduje rowniez najdluzszy wspolny prefiks. */
    public void potasuj()
    {
        tasuj(0, piosenki.length);
    /* Aby zaoszczedzic czas na sprawdzaniu warunku nieparzystosci na koncu wykonania tasuj(),
     * robimy tego swapa to poza tamta funkcja, bo dwie ostatnie piosenki nie zostaja przetasowane.
     * jesli liczba piosenek jest nieparzysta (wtedy tez srodkowy element zawsze laduje na koncu.
     * Nie trzeba sprawdzac jego przedrostka, bo byl wpisany jako wartosc poczatkowa. */
        if(piosenki.length%2==1 && piosenki.length!=1)
            swap(piosenki.length-1, piosenki.length-2);
    }
    /** Rekurencyjna funkcja tasujaca piosenki z zakresu [left,right). */
    private void tasuj(int left, int right)
    {
        if(right-left > 3) // Jesli do potasowania sa wiecej niz 3 piosenki.
        {
            int ilosc = (right-left+1)>>1; // ilosc elementow ktore nalezy przestawic
            int leftMid = left + (ilosc>>1); // indeks pierwszego do przestawienia na lewo od srodka
            int rightMid = leftMid+ilosc; // indeks pierwszego nieprzestawianego na prawo od srodka

            ilosc >>= 1; // o ile elementow trzeba przesunac
            int mid = leftMid + ilosc; // indeks srodkowego elementu

            if((rightMid-leftMid)%2 == 0) // Czy ilosc elementow do przestawienia jest parzysta?
            {
                for(int i=leftMid; i<mid; ++i)
                    swap(i, i+ilosc);
            }
            else
            {
                String temp = piosenki[mid]; // Zapamietujemy srodkowy element.
                for(int i=leftMid; i<mid; ++i)
                {
                // zmodyfikowany swap
                    piosenki[i+ilosc] = piosenki[i];
                    piosenki[i] = piosenki[i+ilosc+1];
                }
            // rightMid musi w tym przypadku pokazywac srodkowy element prawego podzialu, a nie nastepny za nim.
                piosenki[--rightMid] = temp;
            }
        // Tasujemy rekurencyjnie lewa i prawa polowe.
            tasuj(left, mid); // Zawsze parzysta ilosc elementow, bo left poczatkowo jest zerem, a mid zawsze parzysty.
            tasuj(mid, right);
        }
        else // Przypadek koncowy (szukamy prefiksow).
        {
        /* Przypadek gdy zostana 3 elementy zawsze bedzie wykonywany raz jako ostatni.
         * Aby zaoszczedzic czas na sprawdzanie warunku (czy pozostaly 3 elementy),
         * szukamy prefiksu ostatniego elementu dopiero w funkcji potasuj(). */
            przedrostek.znajdzPrzedrostek(piosenki[left]);
            przedrostek.znajdzPrzedrostek(piosenki[right-1]);
        }
    }

    private void swap(int p1, int p2)
    {
        String temp = piosenki[p1];
        piosenki[p1] = piosenki[p2];
        piosenki[p2] = temp;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(piosenki[0]);
        for(int i=1; i<piosenki.length; ++i)
            sb.append(' ').append(piosenki[i]);
        return sb.append('\n').append(przedrostek).toString();
    }

    static class Przedrostek
    {
        private String przedrostek;

        Przedrostek() { przedrostek = null; }
        @Override
        public String toString() { return przedrostek; }
        public void setPrzedrostek(String przedrostek) { this.przedrostek = przedrostek; }
        /** Szuka wspolnego przedrostka z podanym Stringiem. */
        public void znajdzPrzedrostek(String tytul)
        {
            if( !przedrostek.isEmpty() )
            {
                int rozmiar = Math.min(przedrostek.length(), tytul.length());
            // Porownujemy wyrazy znak po znaku.
                for(int i=0; i<rozmiar; ++i)
                {
                    if(przedrostek.charAt(i) != tytul.charAt(i))
                    {
                        rozmiar = i;
                        break;
                    }
                }
            // aktualizacja przedrostka
                przedrostek = przedrostek.substring(0, rozmiar);
            }
        }
    }
}
