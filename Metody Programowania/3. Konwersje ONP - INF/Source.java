//Szymon Ciula - 4
/* Program konwertujacy wyrazenia z Odwroconej Notacji Polskiej na notacje infiksowa i na odwrot.
1. Wejscie
Na wejsciu najpierw wprowadzana zostaje liczba wyrazen do konwersji (z zakresu int).
Nastepnie wprowadzane sa wyrazenia zakonczone znakiem nowej linii.
Wyrazenie w ONP powinno rozpoczynac sie znakami "ONP: ", zas wyrazenie w postaci infoksowej znakami "INF: ".
Wyrazenie powinno miescic sie w zakresie [1, 256].

2. Dzialanie
W trakcie konwersji wykonywane sa na biezaco nastepujace czynnosci:
    a) usuwanie z wyrazenia wszystkich znakow innych niz male litery alfabetu angielskiego oraz spoza zestawu: "()!~^*%/+-<>?&|=".
       Dodatkowo, jesli wejscie jest formatu ONP, usuwane zostaja takze nawiasy.
    b) sprawdzanie poprawnosci wyrazenia pod wzgledem skladniowym.

3. Wyjscie
Na wyjsciu program wypisuje przekonwertowane wyrazenie (lub "error" w przypadku nieudanej konwersji)
rozpoczynajace sie od znakow "INF: " albo "ONP: " w zaleznosci od notacji oraz zakonczone znakiem nowej linii.
Kazdy znak wyrazenia jest poprzedzany spacja.
Dodatkowo w konwersji ONP->INF do wyjscia dokladane sa nawiasy gwarantujace kolejnosc obliczen jak w ONP.
 */

import java.util.Scanner;

public class Source
{
    public static final Scanner sc = new Scanner(System.in);
//Pomocniczy string z operatorow.
    public final static String operatory = "!~^*/%+-<>?&|=";
//Stos do przechowywania operatorow, wspomagajacy konwersje INF->ONP.
    public static Stos stos_op = new Stos();

    public static void main(String[] args)
    {
        int zestawy = sc.nextInt();
        sc.nextLine();
        String linia;
        for(int z=zestawy; z>0; --z)
        {
            linia = sc.nextLine();

            if(linia.charAt(0) == 'O')
                ONP_2_INF(linia);
            else
                INF_2_ONP(linia);
        }
    }
//Funkcja obslugujaca konwersje i wypisujaca wyrazenie ONP->INF.
    public static void ONP_2_INF(String linia)
    {
        Koordynaty koordynaty = new Koordynaty();
    //Konwersja wyrazenia.
        String wynik = konwersjaO2I(linia, linia.length()-1, ' ', false, koordynaty);
    //Koncowe sprawdzanie poprawnosci wyrazenia za pomoca koordynatow z obiektu Zmienne.
        if(koordynaty.poprawne())
        {
        //Jesli funkcja konwertujaca nie byla w stanie przekonwertowac calego wyrazenia, sprawdzane sa pozostale znaki.
            if(koordynaty.getIterator() != 0)
            {
                char c;
                for(int i=koordynaty.getIterator()-1; i>=0; --i)
                {
                    c = linia.charAt(i);
                //Jesli zostanie napotkany znak mogacy wchodzic w sklad wyrazenia, rezultat jest zamieniany na "error".
                    if(operatory.indexOf(c) != -1  ||  (c>='a' && c<='z'))
                    {
                        wynik = "error";
                        break;
                    }
                }
            }
            System.out.println("INF:" + (!wynik.contains("error") ? wynik : " error"));
        }
        else
            System.out.println("INF: error");
    }
/* Rekurencyjna funkcja konwertujaca wyrazenie z ONP na INF. Argumenty:
 * linia - wyrazenie do konwersji;
 * pozycja - indeks znaku do czytania z obiektu "linia" (poczatkowo: linia.length()-1);
 * operator - glowny operator nadrzednego wyrazenia (poczatkowo: dowolny spoza dozwolonych operatorow i nawiasow);
 * na_lewo - jesli obliczane wyrazenie stoi na lewo od operatora: true (poczatkowo: false);
 * koordynaty - obiekt koordynatow funkcji (poczatkowo: domyslny).
 */
    public static String konwersjaO2I(String linia, int pozycja, char operator, boolean na_lewo, Koordynaty koordynaty)
    {
        char c;
    //Petla przebiegajaca po obiekcie "linia".
        for(int i=pozycja; i>=0; --i)
        {
            c = linia.charAt(i);
            if(c>='a' && c<='z')
            {
                koordynaty.zwieksz_zmienne();
            //Ustawiamy iterator, aby wyzsza instancja wiedziala, odkad wznowic czytanie wyrazenia.
                koordynaty.setIterator(i);
                return " "+c;
            }
            else if(operatory.indexOf(c) != -1)
            {
                if(c != '=')
                    koordynaty.przypisane();
                else if(koordynaty.bylo_przypisane())
                    return "error";
            //Jesli funkcja znajdzie operator unarny, szuka kolejnego.
                if(c=='!' || c=='~')
                    return " "+c+konwersjaO2I(linia, i-1, c, false, koordynaty);
                else
                {
                    koordynaty.zwieksz_operatory();
                //Zapisujemy prawa strone wyrazenia.
                    String wynik = konwersjaO2I(linia, i-1, c, false, koordynaty);
                    i = koordynaty.getIterator();
                /* Sprawdzamy, czy wyrazenie musi byc oblozone nawiasami, zgodnie z nasza specyfikacja.
                 * Operatory '=' i '^' nie implikuja nawiasow ze wzgledu na lacznosc lewostronna.
                 * Jesli priorytet jest taki sam, ale wyrazenie jest z lewej strony, nawiasy nie sa konieczne.  */
                    if( (priorytet(operator) < priorytet(c))  ||  (priorytet(operator)==priorytet(c) & (na_lewo || c=='=' || c=='^')) )
                        return konwersjaO2I(linia, i-1, c, true, koordynaty) + ' ' + c + wynik;
                    else
                        return " (" + konwersjaO2I(linia, i-1, c, true, koordynaty) + ' ' + c + wynik + " )";
                }
            }
        }
    //Wyjscie poza petle oznacza bledne wyrazenie.
        return "error";
    }
//Funkcja obslugujaca konwersje i wypisujaca wyrazenie INF->ONP.
    public static void INF_2_ONP(String linia)
    {
        String wynik;

        wynik = konwersjaI2O(linia);
        System.out.println("ONP:" + wynik);
        stos_op.clean();
    }
//Iteracyjna funkcja wykonujaca konwersje INF->ONP.
    public static String konwersjaI2O(String linia)
    {
        char c;
        Koordynaty koordynaty = new Koordynaty();
    //wynik - rezultat konwersji.
    //kolejka - pomocnicza zmienna do przechowywania mniejszych czesci wyniku na poszczegolnych etapach konwersji.
        String wynik = "", kolejka = "";
    //Zmienna przechowujaca informacje, ile nawiasow jest aktualnie otwartych.
        int nawiasy_otw = 0;
    /* niepusty - przechowuje informacje, czy otwarty nawiast zawiera jakies wyrazenia (zapobiega wystapieniu "()").
     * bez_zmiennej_obok - przechowuje informacje, czy po lewej stronie nie wystepuja zmienna nieoddzielona operatorem binarnym.
     *                     Zapobiega wystepowaniu dwoch zmiennych obok siebie, np.: "*ab", "(a)b" itd. */
        boolean niepusty = false, bez_zmiennej_obok = true;

        for(int i=0; i<linia.length(); ++i)
        {
            c = linia.charAt(i);
            if(c>='a' && c<='z')
            {
                if(bez_zmiennej_obok)
                {
                    bez_zmiennej_obok = false;
                    niepusty = true;
                    koordynaty.zwieksz_zmienne();
                    kolejka += " "+c;
                //Dokladanie operatorow unarnych.
                    while(stos_op.notEmpty())
                    {
                        if(stos_op.top() != '~'  &&  stos_op.top() != '!')
                            break;
                        else
                            kolejka += " "+stos_op.pop();
                    }
                //Jesli to poczatek podwyrazenia, doklejamy "kolejke" do rezultatu.
                    if(!stos_op.notEmpty() || stos_op.top()=='(')
                    {
                        wynik += kolejka;
                        kolejka = "";
                    }
                }
                else
                    return " error";
            }
        //Napotkanie operatora.
            else if(operatory.indexOf(c) != -1)
            {
                if(c=='~' || c=='!')
                {
                //Zapobiega sytuacji "a~".
                    if(bez_zmiennej_obok)
                        stos_op.push(c);
                    else
                        return " error";
                }
                else
                {
                    bez_zmiennej_obok = true;
                    koordynaty.zwieksz_operatory();
                /* Dokladamy do kolejki operatory ze stosu, jesli mozemy to zrobic,
                 * aby zachowac odpowiednia kolejnosc wykonania.
                 * Operatory '=' i '^' zachowuja sie inaczej ze wzgledu na lacznosc. */
                    if(kolejka.length() > 0  ||  stos_op.notEmpty())
                    {
                        while(  stos_op.notEmpty() && stos_op.top()!='(' &&
                                ( priorytet(c)<priorytet(stos_op.top()) ||
                                (priorytet(c)==priorytet(stos_op.top()) && c!='=' && c!='^') )  )
                         kolejka += " "+stos_op.pop();
                    }
                    stos_op.push(c);
                }
            }
            else if(c == '(')
            {
                wynik += kolejka;
                kolejka = "";
                niepusty = false;
                ++nawiasy_otw;
            //Dokladamy do stosu, aby wiedziec, gdzie skonczyc czytanie operatorow, gdy napotkamy nawias zamykajacy.
                stos_op.push('(');
            }
            else if(c == ')')
            {
                wynik += kolejka;
                kolejka = "";
                --nawiasy_otw;
            //Sprawdzanie, czy nawiasy sa dobrze postawione.
                if(nawiasy_otw >= 0  &&  niepusty)
                {
                //Nasza petla konczy sie, gdy napotka '(', ktory na pewno tam jest, bo inaczej blok nie zostalby wykonany.
                    while(stos_op.top() != '(')
                        wynik += " "+stos_op.pop();
                //Sciagamy nawias otwierajacy.
                    stos_op.pop();
                    if(stos_op.notEmpty())
                    {
                    //Dodajemy wszystkie operatory unarne
                        if(stos_op.top()=='~' || stos_op.top()=='!')
                        {
                            while(stos_op.notEmpty() && (stos_op.top()=='~' || stos_op.top()=='!'))
                                wynik += " "+stos_op.pop();
                        }
                    }
                }
                else
                    return " error";
            }
        }
    //Sprawdzanie koordynatow poprawnosci.
        if(koordynaty.poprawne() && nawiasy_otw==0)
        {
            wynik += kolejka;
        //Oproznianie stosu operatorow.
            while(stos_op.notEmpty())
                wynik += " "+stos_op.pop();
            return wynik;
        }
        else
            return " error";
    }
//Funkcja zwracajaca priorytet podanego znaku (wiekszy priorytet -> wieksza wartosc).
    public static short priorytet(char c)
    {
        switch(c)
        {
            case '(':
            case ')': return 9;
            case '!':
            case '~': return 8;
            case '^': return 7;
            case '*':
            case '/':
            case '%': return 6;
            case '+':
            case '-': return 5;
            case '<':
            case '>': return 4;
            case '?': return 3;
            case '&': return 2;
            case '|': return 1;
            case '=': return 0;
            default: return -1;
        }
    }
}
//Klasa pomocnicza wspierajaca funkcje ONP_2_INF().
class Koordynaty
{
    private int zmienne;
    private int zmienne_od_operatorow;
    private int iterator;
    private boolean przypisane;

    public Koordynaty()
    {
        zmienne = 0;
        zmienne_od_operatorow = 0;
        iterator = 0;
        przypisane = false;
    }

    public void zwieksz_zmienne()   { ++zmienne; }
    public void zwieksz_operatory() { ++zmienne_od_operatorow; }
    public void przypisane()        { przypisane = true; }
    public void setIterator(int it) { iterator = it; }
    public int getIterator()        { return iterator; }
//Funkcja sprawdzajaca, czy liczba operatorow zgadza sie z liczba zmiennych.
    public boolean poprawne()       { return (zmienne == zmienne_od_operatorow+1); }
//Czy na koncu sa same operatory '='
    public boolean bylo_przypisane(){ return przypisane; }
}
class Stos
{
    public Stos()
    {
        stos = new char[MAX_SIZE];
        currSize = 0;
    }

    public void push(char c)
    {
        if(currSize<MAX_SIZE-1)
            stos[currSize++] = c;
        else
            System.out.println("Brak miejsca!");
    }
    public char pop()
    {
        if(currSize>0)
            return stos[--currSize];
        else
        {
            System.out.println("Brak elementow!");
            return '\0';
        }
    }
    public char top()   { return (currSize>0 ? stos[currSize-1] : '\0'); }
    public boolean notEmpty() { return currSize!=0; }
    public void clean() { currSize = 0; }

    private static final short MAX_SIZE = 256;
    private final char[] stos;
    private short currSize;
}
