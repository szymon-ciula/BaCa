//Szymon Ciula - 4
/*Program ma za zadanie policzyc, na ile sposobow mozna ulozyc trojkaty z podanych na wejsciu zestawow bokow.
* Wypisuje rowniez do 10 pierwszych takich trojkatow w porzadku leksykograficznym.*/

//import java.util.Random;
import java.util.Scanner;

public class Source
{
    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args)
    {
        int l_zestawow;// = 200000;
        Tablica tablica = new Tablica();
        //int [] tab;

        l_zestawow = sc.nextInt();
        for(int zestaw=1; zestaw<=l_zestawow; ++zestaw)
        {
            tablica.wpiszDane(sc);
            tablica.insertSort();
            tablica.policzTrojkaty();
            tablica.wypisz(zestaw);
            tablica.clean();
        }
    }
}

class Tablica
{
    private int l_elementow;
    private int[] tablica;
    private final Trojkaty trojkaty;
//konstruktor
    public Tablica()    { trojkaty = new Trojkaty(); }
//Pozwala wprowadzic dane calego zestawu: liczbe elementow oraz te elementy.
    public void wpiszDane(Scanner sc)
    {
        //Random generator = new Random();
        l_elementow = /*generator.nextInt(98)+3;/*/sc.nextByte();
        tablica = new int [l_elementow];
        for(byte i=0; i<l_elementow; ++i)
            tablica[i] = /*generator.nextInt(1000)+1;/*/sc.nextInt();
        //return tablica;
    }
//Sortowanie przez wstawianie - zlozonosc O(n^2).
    public void insertSort()
    {
        int tmp;
        for(byte i=1, j; i<l_elementow; ++i)
        {
            tmp = tablica[i];
            j = (byte) (i-1);
            while(j>=0 && tmp<tablica[j])
            {
                tablica[j+1] = tablica[j];
                j--;
            }
            tablica[j+1] = tmp;
        }
    }
//Liczy liczbe trojkatow, ktore da sie utworzyc z podanych w zestawie bokow.
//Zlozonosc: O((n^2)*log n)
    public void policzTrojkaty()
    {
        long suma;
        byte ostatni_mniejszy;
    //Iterujemy po dwoch najkrotszych bokach.
        for(byte i=0; i<l_elementow-2; ++i)
        {
            for(byte j=(byte) (i+1); j<l_elementow-1; ++j)
            {
                suma = tablica[i]+tablica[j];
            //Szukamy wystapienia ostatniego elementu mniejszego badz rownego kluczowi.
                ostatni_mniejszy = binSearch(j+1, l_elementow-1, suma-1);
                trojkaty.dodajTrojkaty(ostatni_mniejszy-j);
            //Dodawanie informacji i indeksach pierwszych 10 trojkatow.
                if(!(trojkaty.full()))
                {
                    for(byte k = (byte) (j+1); k<=ostatni_mniejszy && !(trojkaty.full()); ++k)
                        trojkaty.addElement(i,j,k);
                }
            }
        }
    }

//Wyszukuje binarnie ostatnie wystapienie elementu mniejszego lub rownego kluczowi.
//Zlozonosc: O(log n)
    public byte binSearch(int left, int right, long key)
    {
    //Szukamy elementu rownego.
        int mid=left;
        while(left<=right)
        {
            mid = (left+right)/2;
            if(tablica[mid]==key)
                break;
            else if(tablica[mid]<key)
                left = mid+1;
            else
                right = mid-1;
        }
    //Jesli go znajdziemy - szukamy jego ostatniego wystapienia.
        if(tablica[mid] == key)
        {
            while(left<=right)
            {
                mid = (left+right)/2;
                if(tablica[mid]<=key)
                    left = mid+1;
                else
                    right = mid-1;
            }
        }
    //Wiemy, ze petla konczy sie albo na elemencie slabo mniejszym od klucza, albo na elemencie za nim.
    //Nalezy wiec rozpatrzyc oba przypadki.
        return (byte) (tablica[mid]>key ? mid-1 : mid);
    }
/*Wypisuje na standardowe wyjscie: nr zestawu i liczbe jego elementow,
posortowana tablice (po 25 elementow w wierszu),
do 10 trojek indeksow bokow w porzadku leksykograficznym oraz liczbe rozwiazan.*/
    public void wypisz(int zestaw)
    {
        System.out.print(zestaw+": n= "+l_elementow);
        for(byte i=0; i<l_elementow; ++i)
        {
            if(i%25==0)
                System.out.println();
            System.out.print(tablica[i] + " ");
        }
        System.out.println();

        trojkaty.wypisz();
    }
    public void clean()
    {   trojkaty.clean();   }
}

//klasa przechowujaca rozwiazanie zadania:
//l. mozliwych do ulozenia trojkatow oraz indeksy 10 pierwszych trojkatow.
class Trojkaty
{
    private final byte [][] wspolrzedne;    //Przechowuje indeksy bokow 10 pierwszych trojkotow mozliwych do ulozenia.
    private byte ilosc; //ilosc zapelnionych pol tablicy wspolrzedne
    private long l_trojkatow;   //liczba mozliwych do utworzenia trojkatow w zestawie
//konstruktor
    public Trojkaty()
    {
        wspolrzedne = new byte[10][3];
        l_trojkatow = ilosc = 0;
    }
//Dodaje trojke indeksow do tablicy wspolrzednych.
    public void addElement(byte a, byte b, byte c)
    {
        wspolrzedne[ilosc][0] = a;
        wspolrzedne[ilosc][1] = b;
        wspolrzedne[ilosc][2] = c;
        ++ilosc;
    }
//Dodaje do liczby trojkatow, ktore mozemy uzyskac.
    public void dodajTrojkaty(int a)
    {   l_trojkatow += a;   }
/*Wypisuje na standardowe wyjscie tablice wspolrzednych w formacie (i,j,k).
* Elementy sa oddzielone spacja. Strumien konczy sie znakiem nowej linii.
* Jesli tablica jest pusta, wypisywana jest stosowna informacja.
* W przeciwnym wypadku zostaje wypisana informacja o liczbie mozliwych do utworzenia trojkatow.*/
    public void wypisz()
    {
        //String wynik="";
        if(ilosc!=0)
        {
            for(byte i=0; i<ilosc; ++i)
                //wynik += "("+Integer.toString(wspolrzedne[i][0])+","+Integer.toString(wspolrzedne[i][1])+","+Integer.toString(wspolrzedne[i][2])+") ";
                System.out.print("("+wspolrzedne[i][0]+","+wspolrzedne[i][1]+","+wspolrzedne[i][2]+") ");
            System.out.println("\nNumber of triangles: "+l_trojkatow);
        }
        else
            System.out.println("Triangles cannot be built");
    }
//Zwraca true, jesli tablica wspolrzednych jest pelna oraz false w przeciwnym wypadku.
    public boolean full()
    {   return (ilosc<10 ? false : true);   }
//Czysci stan obiektu.
    public void clean()
    {
        ilosc = 0;
        l_trojkatow = 0;
    }
}

