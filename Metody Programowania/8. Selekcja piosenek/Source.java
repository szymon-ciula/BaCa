//Szymon Ciula - gr.4
import java.util.Scanner;

/** Program rozwiazuje problem szukania k-tego najmniejszego elementu w nieposortowanym zbiorze.
 * Zlozonosc czasowa algorytmu jest liniowa, a pamieciowa - stala (pomijajac naklad zwiazany z wywolaniami rekurencji).
 * Program opiera sie na algorytmie mediany median (magicznych piatek).
 * Mediana median rowniez wyszukiwana jest rekurencyjnie za pomoca tego samego algorytmu.
 * Gdy zbior sklada sie z 20 lub mniej elementow, zbior jest sortowany za pomoca sortowania przez wstawianie,
 * ktory dziala szybciej niz quick sort dla malych danych wstepnie posortowanych
 * (a wiec w tym przypadku, poniewaz dane sa czesciowo sortowane na biezaco).
 */

public class Source
{
    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args)
    {
        final MagicznePiatki mp = new MagicznePiatki();
        for(int zestaw=sc.nextByte(); zestaw-->0; )
        {
            mp.wprowadzDane(sc);
            mp.wyszukajElementy(sc);
        }
    }
}

class MagicznePiatki
{
    private int [] tablica; // Przechowuje pozycje piosenek w rankingu Listy Przebojow.
    /** Konstruktor */
    public MagicznePiatki() { tablica = null; }
    /** Pozwala wprowadzic elementy tablicy pozycji w rankingu Wieczornej Listy Przebojow. */
    public void wprowadzDane(Scanner sc)
    {
        tablica = new int[sc.nextInt()];
    // Wprowadzamy elementy do tablicy.
        for(int i=0; i<tablica.length; ++i)
            tablica[i] = sc.nextInt();
    }
    /** Obsluguje zapytania o k-te elementy (wyszukuje k-ty najmniejszy z wczesniej wprowadzonej tablicy).
      * Najpierw pobiera liczbe zapytan, nastepnie dla zadanej liczby k wypisuje ja oraz pozycje w rankingu Listy Przebojow. */
    public void wyszukajElementy(Scanner sc)
    {
        int szukana;
        for(int i=sc.nextByte(); i-->0; )
        {
            szukana = sc.nextInt();
            System.out.println( szukana+" "+ ( (szukana>0 && szukana<=tablica.length)  ?  tablica[wyszukaj(szukana,0,tablica.length-1)]  :  "brak") );
        }
    }
    /** Zwraca wartosc k-tego najmniejszego elementu.
     * Dziala na zakresie [left,right]. Zlozonosc O(n). */
    private int wyszukaj(int szukanaPozycja, int lewy, int prawy)
    {
        int pozycjaMediany;
        while(prawy-lewy > 20)
        {
            pozycjaMediany = podziel(lewy, prawy, znajdzMediane(lewy, prawy), szukanaPozycja);
        // Zmieniamy przeszukiwany zakres w zaleznosci od informacji, jaka otrzymamy z powyzszej funkcji.
            if(szukanaPozycja-1 > pozycjaMediany)  // Element wiekszy od mediany.
                lewy = pozycjaMediany+1;
            else if(szukanaPozycja-1 < pozycjaMediany) // Element mniejszy od mediany.
                prawy = pozycjaMediany-1;
            else    // Znalezlismy szukany element.
                return szukanaPozycja-1;
        }

        insertionSort(lewy, prawy+1);
        return szukanaPozycja-1;
    }
    /** Grupuje tablice w piatki i znajduje mediane median. */
    private int znajdzMediane(int lewy, int prawy)
    {
    // Przesuwamy mediany poszczegolnych piatek na poczatek zakresu.
        int i=lewy;
        while(i+5 < prawy)
        {
            swap(medianaPiatki(i, i+4), lewy + (i-lewy)/5);
            i += 5;
        }
        swap(medianaPiatki(i, prawy), lewy + (i-lewy)/5);
    // Obliczamy mediane median (robimy to rekurencyjnie za pomoca tego samego algorytmu magicznych piatek).
        return wyszukaj((prawy-lewy)/10 + lewy+1, lewy, lewy + (prawy-lewy)/5);
    }
    /** Dzieli tablice na trzy podzbiory (elementy mniejsze, rowne oraz wieksze od mediany).
     * Zwraca indeks:
     * - otwartego konca nowego zakresu, jesli element szukany jest mniejszy od mediany;
     * - otwartego poczatku nowego zakresu, jesli element szukany jest wiekszy od mediany;
     * - miediany, jesli element szukany jest jej rowny.
     * Dziala na przedziale [left,right]. Zlozonosc O(n). */
    private int podziel(int lewy, int prawy, int indeksMediany, int szukanaPozycja)
    {
        int mediana = tablica[indeksMediany];
        swap(indeksMediany, prawy);  // Przesuwamy mediane na koniec.
    // Przesuwamy wszystkie elementy mniejsze od mediany na lewo.
    // Bedziemy odtad uzywac zmiennej "lewy" do oznaczania konca zbioru elementow mniejszych od mediany.
        for(int i=lewy; i<prawy; ++i)
        {
            if(tablica[i] < mediana)
                swap(lewy++, i);
        }
    // Przesuwamy wszystkie elementy rowne medianie za elementy mniejsze od niej.
        int indeksyRownych = lewy;
        for(int i=lewy; i<prawy; ++i)
        {
            if(tablica[i] == mediana)
                swap(indeksyRownych++, i);
        }
        swap(prawy, indeksyRownych);  // Przesuwamy mediane na jej miejsce docelowe.
    // Zwracamy odpowiedni zakres, w zaleznosci od licznosci podzbiorow.
        if(szukanaPozycja <= lewy)  // Szukany element jest wsrod elementow mniejszych od mediany.
            return lewy;
    // Szukany element jest wsrod elementow wiekszych od mediany, jesli szukana pozycja jest wieksza od indeksu mediany
    // oraz rowny medianie w przeciwnym wypadku.
        return Math.min(szukanaPozycja-1, indeksyRownych);
    }
    /** Zwraca indeks mediany (lub tzw. mediany mniejszej) w ciagu krotszym niz 6 elementow. Zlozonosc O(10). */
    private int medianaPiatki(int left, int right)
    {
        insertionSort(left, right+1);
        return (left+right)/2;
    }
    /** Sortuje elementy tablicy z zakresu [left,right) metoda przez wstawianie. */
    private void insertionSort(int left, int right)
    {
        int tmp;
        for(int i=left+1, j; i<right; ++i)
        {
            tmp = tablica[i];
            j = i-1;
            while(j>=left && tmp<tablica[j])
            {
                tablica[j+1] = tablica[j];
                j--;
            }
            tablica[j+1] = tmp;
        }
    }
    /** Zamienia ze soba elementy o wybranych indeksach w tablicy. */
    private void swap(int first, int second)
    {
        int temp = tablica[first];
        tablica[first] = tablica[second];
        tablica[second] = temp;
    }
}

