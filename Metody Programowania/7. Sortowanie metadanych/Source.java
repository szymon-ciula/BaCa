//Ciula Szymon - gr. 4
import java.util.Scanner;

public class Source
{
    private static final Scanner sc = new Scanner(System.in);
    private static final Albumy albumy = new Albumy();

    public static void main (String [] args)
    {
        for(byte zestaw=Byte.parseByte(sc.nextLine()); zestaw>0; --zestaw)
        {
            albumy.wprowadzDane(sc);
            albumy.sortuj();
            albumy.wypisz();
        }
    }
}

class Albumy
{
    private Album[] albumy;
    private byte porzadek; // 1 rosnacy, inny - malejacy
    private int klucz; // Wedlug ktorej kolumny sortujemy.
    private int ileKolumn; // ile kolumn danych o piosenkach
    private boolean kluczLiczba; // czy klucz jest liczba czy napisem
    /** Funkcja do wprowadzania danych o albumach w formacie CSV. */
    public void wprowadzDane(Scanner sc)
    {
    // Wyciaganie danych o ilosci wierszy, kluczu i porzadku.
        String koordynaty = sc.nextLine();
        int beg, i=-1;

        while(koordynaty.charAt(++i) != ',') ;
        int liczbaAlbumow = Integer.parseInt(koordynaty.substring(0, beg=i))+1;
        while(koordynaty.charAt(++i) != ',') ;
        klucz = Integer.parseInt(koordynaty.substring(++beg, i)) - 1;
        porzadek = Byte.parseByte(koordynaty.substring(++i));
    // Wprowadzanie naglowkow i danych o albumach.
        albumy = new Album[liczbaAlbumow];
        albumy[0] = new Album(sc);
        for(int j=1; j<liczbaAlbumow; ++j)
            albumy[j] = new Album(sc.nextLine());
    // Sprawdzamy, czy klucz jest liczba, aby nie sprawdzac tego za kazdym razem.
        kluczLiczba = true;
        String string = albumy[1].dane[klucz];
        for(int j=string.length()-1; j>=0; --j)
        {
            if(string.charAt(j)>'9' || string.charAt(j)<'0')
            {
                kluczLiczba = false;
                break;
            }
        }
    }
    /** Funkcja wypisujaca albumy w formacie CSV (kazdy w nowej linii).
      * Pierwsza wypisywana kolumna jest kolumna klucza sortowania. */
    public void wypisz()
    {
        for(Album album : albumy)
            System.out.println(album);
        System.out.println();
    }
    /** Sortuje albumy wedlug klucza za pomoca sortowania szybkiego (zlozonosc czasowa: liniowo logarytmiczna).
      * Dla podzadan krotszych niz 6 elementow uzywa sortowania przez wstawianie. (zlozonosc: stala, bo wykonujemy dla maks 5 elementow) */
    public void sortuj()
    {
        int left = 1, right = albumy.length;
        int pivot = right, temp;
        while(left < albumy.length)
        {
            if(right-left>5)
            {
                temp = right;
                pivot = partition(left, right); // Szukamy elementu dzielacego.
                albumy[pivot].dane[klucz] += "\t" + temp; // W elemencie dzielacym zapisujemy informacje o prawym brzegu.
                right = pivot; // Zmniejszamy prawy kraniec.
            }
            else
            {
                SelectionSort(left, right);
                if( (left=right) != albumy.length)
                    right = getNext(right);
            }
        }
    }
    /** Funkcja wyluskujaca indeks kolejnego (prawego) zakresu z oznaczonego elementu.
      * Przywraca wartosc poczatkowa tego elementu. */
    private int getNext(int right)
    {
        String string = albumy[right].dane[klucz]; // zmienna pomocnicza
        int index = string.lastIndexOf('\t'); // indeks konca wlasciwego slowa
        albumy[right].dane[klucz] = string.substring(0, index); // Przywracamy wartosc poczatkowa.
        return Integer.parseInt( string.substring(index+1) ); // Wyluskujemy indeks konca kolejnego zakresu.
    }
    /** Funkcja dokonujaca podzialu elementow z zakresu [left,right) podczas wykonania quick-sorta. */
    private int partition(int left, int right) //wesja Hoare'a
    {
        int i = left-1, j = right-1;
        Album pivot = albumy[right-1]; // Ostatni element jest elementem dzielacym.
        while(true)
        {
        // Szukamy elementow, ktore sa w nieprawidlowej relacji wzgledem elementu dzielacego.
            if(porzadek == 1)
            {
                while( albumy[++i].mniejsze(pivot) ) ;
                while( j>left && albumy[--j].wieksze(pivot) ) ;
            }
            else
            {
                while( albumy[++i].wieksze(pivot) ) ;
                while( j>left && albumy[--j].mniejsze(pivot) ) ;
            }
            if(i < j) // Zamieniamy je, jesli zakresy sie nie nalozyly.
                swap(i, j);
            else // Konczymy petle w przeciwnym wypadku.
                break;
        }
        swap(i, right-1); // Ustawiamy element dzielacy na jego miejscu.
        return i; // Zwracamy polozenie elementu dzielacego.
    }
    /** Sortuje przez wybor albumy z zakresu [left, right). */
    private void SelectionSort(int left, int right)
    {
        for(int i=left; i<right-1; ++i)
        {
        // Wewnatrz petli bedziemy uzywac "left" jako indeksu najmniejszego obiektu sposrod nieposortowanych.
            left = i;
        // Znajdujemy najmniejszy element.
            if(porzadek==1)
            {
                for(int j=i+1; j<right; ++j)
                {
                    if( albumy[left].wieksze(albumy[j]) )
                        left = j;
                }
            }
            else
            {
                for(int j=i+1; j<right; ++j)
                {
                    if( albumy[left].mniejsze(albumy[j]) )
                        left = j;
                }
            }
        // Zamieniamy go z elementem na aktualnej pozycji.
            swap(left, i);
        }
    }
    /** Zamienia ze soba dane w albumach o indeksach a1 i a2. */
    private void swap(int a1, int a2)
    {
        String [] temp = albumy[a1].dane;
        albumy[a1].dane = albumy[a2].dane;
        albumy[a2].dane = temp;
    }

    class Album
    {
        public String [] dane;
        /** Konstruktor dla etykiet. Musi byc wywolany jako pierwszy! */
        Album(Scanner sc) { wprowadzEtykiety( sc.nextLine() ); }
        /** Konstruktor dla albumu. Najpierw trzeba wywolac konstruktor dla etykiet, aby poznac ilosc kolumn! */
        Album(String metadane)
        {
            dane = new String [ileKolumn];
            int indeks=0, beg=0, i=-1;

            // Przeszukujemy dane znak po znaku.
            while(indeks<ileKolumn-1)
            {
                if(metadane.charAt(++i) == ',')
                {
                    dane[indeks++] = metadane.substring(beg, i);
                    beg = i+1;
                }
            }
            dane[indeks] = metadane.substring(beg);
        }
        /** Funkcja przetwarzajaca dane w formacie CSV na "Album" z etykietami. */
        private void wprowadzEtykiety(String metadane)
        {
            Stos stos = new Stos();
            stos.push(-1);
            int i=-1; // Iterator do przemieszczania sie po "metadanych". Potem uzywany jako indeks tablicy.
        // Przeszukujemy dane znak po znaku.
            while(++i<metadane.length())
            {
                if(metadane.charAt(i) == ',')
                    stos.push(i);
            }
            dane = new String [ ileKolumn = i = stos.size() ];
            int end, beg; // Krance kolejnych etykiet w zmiennej "metadane".
        // Wprowadzamy etykiety sciagajac kolejne indeksy ze stosu.
            dane[--i] = metadane.substring( (end=stos.pop()) + 1 ); // +1, bo to indeks przecinka.
            while(--i >= 0)
            {
                dane[i] = metadane.substring((beg=stos.pop())+1, end);
                end = beg;
            }
        }
        private boolean wieksze(Album other)
        {
            return kluczLiczba ? Integer.parseInt(dane[klucz]) > Integer.parseInt(other.dane[klucz])
                                : dane[klucz].compareTo(other.dane[klucz]) > 0;
        }
        private boolean mniejsze(Album other)
        {
            return kluczLiczba ? Integer.parseInt(dane[klucz]) < Integer.parseInt(other.dane[klucz])
                                : dane[klucz].compareTo(other.dane[klucz]) < 0;
        }
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder(dane[klucz]);
            for(int i=0; i<klucz; ++i)
                sb.append(',').append(dane[i]);
            for(int i=klucz+1; i<ileKolumn; ++i)
                sb.append(',').append(dane[i]);
            return sb.toString();
        }
    }
}
/** Klasa implementujaca strukture stosu.*/
class Stos // Potrzebna podczas wpisywania naglowkow.
{
    private Element last;
    private int length;

    Stos()
    {
        last = null;
        length = 0;
    }

    public int size() { return length; }

    public void push(int value)
    {
        if(last != null)
        {
            Element temp = last;
            last = new Element(value);
            last.prev = temp;
        }
        else
        {
            last = new Element(value);
            last.prev = null;
        }
        ++length;
    }

    public int pop()
    {
        if(last != null)
        {
            --length;
            int value = last.value;
            last = last.prev;
            return value;
        }
        return -1;
    }

    static class Element
    {
        private Element prev;
        private final int value;

        Element(int value) { this.value = value; }
    }
}

