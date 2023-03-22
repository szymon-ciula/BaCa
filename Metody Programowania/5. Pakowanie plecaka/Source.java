//Szymon Ciula - gr. 4
import java.util.Scanner;

public class Source
{
    public static final Scanner sc = new Scanner(System.in);

    public static void main(String [] args)
    {
        int pojemnosc, licz_elementow;
        int [] elementy; //elementy do spakowania
        String wynik;

        for(int zestaw=sc.nextInt(); zestaw>0; --zestaw)
        {
            pojemnosc = sc.nextInt();
            licz_elementow = sc.nextInt();
            elementy = new int[licz_elementow];

        //Wprowadzenie elementow do tablicy.
            for(int i=0; i<licz_elementow; ++i)
                elementy[i] = sc.nextInt();

            wynik = rec_pakuj(pojemnosc, licz_elementow, elementy, 0);
        //Jesli nie znajdzie rekurencyjnie, to nie znajdzie tez iteracyjnie.
            if(!wynik.equals("BRAK"))
            {
                System.out.println("REC:  "+pojemnosc+" ="+wynik);
                System.out.println("ITER: "+pojemnosc+" ="+iter_pakuj(pojemnosc, licz_elementow, elementy));
            }
            else
                System.out.println("BRAK");
        }
    }
    /** Rekurencyjna funkcja znajdujaca pierwszy zestaw elementow, ktore wypelnia plecak po brzegi.
      * Jesli znajdzie taki zestaw, zwroci jego zawartosc, rozdzielajac elementy spacjami.
      * W przeciwnym wypadku zwroci string "BRAK".
      * Indeks poczatkowo nalezy ustawic na 0, bo to od niego rozpoczynamy wyszukiwanie. */
    public static String rec_pakuj(int pojemnosc, int licz_elementow, int[] elementy, int indeks)
    {
    //Nie ma rozwiazania, wiec konczymy algorytm.
        if(indeks < licz_elementow)
        {
            int element = elementy[indeks]; //zmienna pomocnicza
            if(element < pojemnosc)
            {
                String wynik = rec_pakuj(pojemnosc-element, licz_elementow, elementy, indeks+1);
            //Jesli nie ma rozwiazania z podanym elementem, wykonujemy algorytm dla kolejnego elementu.
                if(wynik.contains("BRAK"))
                    return rec_pakuj(pojemnosc, licz_elementow, elementy, indeks+1);
            //W przeciwnym wypadku konczymy algorytm.
                else
                    return " " + element + wynik;
            }
        //Jesli element sie nie miesci, wykonujemy algorytm od kolejnego elementu.
            else if(element > pojemnosc)
                return rec_pakuj(pojemnosc, licz_elementow, elementy, indeks+1);
        //Znalezlismy rozwiazanie, wiec konczymy algorytm.
            else //element == pojemnosc
                return " " + element;
        }
        else
            return "BRAK";
    }
    /** Iteracyjna implementacja funkcji rec_pakuj, wykorzystujaca stos. */
    public static String iter_pakuj(int pojemnosc, int licz_elementow, int [] elementy)
    {
        Stos stos = new Stos();

        for(int i=0; i<licz_elementow; ++i)
        {
        // Jesli jest rowne, przechodzimy do bloku else i przerywamy petle.
            if(elementy[i] != pojemnosc)
            {
                if(i != licz_elementow-1)
                {
                // Jesli jestesmy w stanie zapakowac element, dokladamy indeks do stosu.
                // Nastepnie przechodzimy do kolejnego kroku petli.
                    if(elementy[i] < pojemnosc)
                    {
                        stos.push(i);
                        pojemnosc -= elementy[i];
                    }
                }
            // Jesli to ostatni element tablicy, rozwiazanie nie istnieje dla indeksow bedacych na stosie,
            // wiec sciagamy ostatni ze stosu i wznawiamy poszukiwanie od jego nastepnika.
                else
                {
                // Jesli stos jest pusty, rozwiazanie nie istnieje.
                    if(stos.notEmpty())
                        pojemnosc += elementy[(i = stos.pop())];
                    else
                        return "BRAK";
                }
            }
            else
            {
                stos.push(i);
                break;
            }
        }

    // Jesli nie zostanie znaleziony, to petla zwroci "BRAK" i funkcja nie dojdzie do tego miejsca.
        StringBuilder wynik = new StringBuilder(" ");
    // Tworzymy wynikowy string. Nalezy pamietac, ze kolejnosc elementow na stosie jest odwrocona.
        while(stos.notEmpty())
            wynik.insert(0, " " + elementy[stos.pop()]);

        return wynik.toString();
    }
}

//Klasa potrzebna do iteracyjnej implementacji funkcji pakujacej plecak.
class Stos
{
    public Stos()
    {
        MAX_SIZE = 512;
        stos = new int[MAX_SIZE];
        currSize = 0;
    }
    public void push(int element)
    {
        if(currSize==MAX_SIZE)
        {
            int [] temp = stos;
            stos = new int[MAX_SIZE*2];
            System.arraycopy(temp, 0, stos, 0, MAX_SIZE);
            MAX_SIZE <<= 1;
        }

        stos[currSize++] = element;
    }
    public int pop()
    {
        if(currSize>0)
            return stos[--currSize];
        else
        {
            System.out.println("Brak elementow!");
            return '\0';
        }
    }
    public boolean notEmpty() { return currSize!=0; }

    private int MAX_SIZE;
    private int[] stos;
    private short currSize;
}
