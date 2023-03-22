//Szymon Ciula - 4
import java.util.Scanner;

public class Source
{
    public static final Scanner sc = new Scanner(System.in);
    public static final Funkcje fun = new Funkcje();

    public static void main(String[] args)
    {
        String operacja, parametr1, parametr2;

        for(int zestawy=sc.nextInt(); zestawy>0; --zestawy)
        {
            for(int operacje=sc.nextInt(); operacje>0; --operacje)
            {
                operacja = sc.next();
                switch(operacja)
                {
                    case "New":
                    case "InsertFirst":
                    case "InsertLast":
                    case "Union":
                    case "DelFirst":
                    case "DelLast":
                        parametr1 = sc.next();
                        parametr2 = sc.next();
                        switch(operacja)
                        {
                            case "New": fun.New(parametr1, parametr2); break;
                            case "InsertFirst": fun.InsertFirst(parametr1, parametr2); break;
                            case "InsertLast": fun.InsertLast(parametr1, parametr2); break;
                            case "Union":     fun.Union(parametr1, parametr2); break;
                            case "DelFirst": fun.DelFirst(parametr1, parametr2); break;
                            case "DelLast": fun.DelLast(parametr1, parametr2); break;
                        }
                        break;
                    case "Display":
                    case "Reverse":
                        parametr1 = sc.next();
                        switch(operacja)
                        {
                            case "Display": fun.Display(parametr1); break;
                            case "Reverse": fun.Reverse(parametr1); break;
                        }
                        break;
                    case "Trains": fun.Trains(); break;
                }
            }
            fun.clean();
        }
    }
}

class Funkcje
{
    private final ListaPociagow pociagi;

    public Funkcje() { pociagi = new ListaPociagow(); }
    public void clean() { pociagi.clean(); }
    /** Tworzy nowy pociag "nazwaPociagu" z jednym wagonem "nazwaWagonu" i wstawia go do listy pociagow. */
    public void New(String nazwaPociagu, String nazwaWagonu)
    {
        if(pociagi.find(nazwaPociagu) == null)
            pociagi.push_front(new Pociag(nazwaPociagu, nazwaWagonu));
        else
            System.out.println("Train "+nazwaPociagu+" already exists");
    }
    /** Wstawia wagon "nazwaWagonu" na poczatek pociagu "nazwaPociagu". */
    public void InsertFirst(String nazwaPociagu, String nazwaWagonu)
    {
        Pociag pociag = pociagi.find(nazwaPociagu);
        if(pociag != null)
            pociag.dodajPierwszyWagon(new Wagon(nazwaWagonu));
        else
            System.out.println("Train "+nazwaPociagu+" does not exist");
    }
    /** Wstawia wagon "nazwaWagonu" na koniec pociagu "nazwaPociagu". */
    public void InsertLast(String nazwaPociagu, String nazwaWagonu)
    {
        Pociag pociag = pociagi.find(nazwaPociagu);
        if(pociag != null)
            pociag.dodajOstatniWagon(new Wagon(nazwaWagonu));
        else
            System.out.println("Train "+nazwaPociagu+" does not exist");
    }
    /** Wypisuje liste wagonow pociagu "nazwaPociagu" poczawszy od pierwszego wagonu. */
    public void Display(String nazwaPociagu)
    {
        Pociag pociag = pociagi.find(nazwaPociagu);
        if(pociag != null)
            pociag.display();
        else
            System.out.println("Train "+nazwaPociagu+" does not exist");
    }
    /** Wypisuje aktualna liste pociagow. */
    public void Trains() { System.out.println("Trains:"+pociagi); }
    /** Odwraca kolejnosc wagonow w pociagu "nazwaPociagu". */
    public void Reverse(String nazwaPociagu)
    {
        Pociag pociag = pociagi.find(nazwaPociagu);
        if(pociag != null)
            pociag.reverse();
        else
            System.out.println("Train "+nazwaPociagu+" does not exist");
    }
    /** Dolacza pociag o nazwie T2 na koniec pociagu o nazwie T1.
     * Usuwa pociag T2 z listy pociagow. */
    public void Union(String T1, String T2)
    {
        Pociag pociag = pociagi.find(T1);
        if(pociag != null)
            pociagi.merge(pociag, T2);
        else
            System.out.println("Train "+T1+" does not exist");
    }
    /** Usuwa pierwszy wagon z pociagu "nazwaPociagu" i tworzy z niego nowy pociag "nowaNazwa".
     * Jesli byl to jedyny wagon, to pociag jest usuwany z listy pociagow. */
    public void DelFirst(String nazwaPociagu, String nowaNazwa)
    {
        pociagi.delCarriage(nazwaPociagu, nowaNazwa, 'f');
    }
    /** Usuwa ostatni wagon z pociagu "nazwaPociagu" i tworzy z niego nowy pociag "nowaNazwa".
     * Jesli byl to jedyny wagon, to pociag jest usuwany z listy pociagow. */
    public void DelLast(String nazwaPociagu, String nowaNazwa)
    {
        pociagi.delCarriage(nazwaPociagu, nowaNazwa, 'l');
    }
}

class Pociag
{
    public Pociag(String nazwaPociagu, String nazwaWagonu)
    {
        nazwa = nazwaPociagu;
        wagony = new ListaWagonow(new Wagon(nazwaWagonu));
    }
    public void dodajPierwszyWagon(Wagon wagon) { wagony.pushFront(wagon); }
    public void dodajOstatniWagon(Wagon wagon)  { wagony.pushBack(wagon); }
    public void display() { System.out.println(nazwa+':'+wagony); }
    public String delCarriage(char FirstLast) { return (FirstLast=='f' ? wagony.popFront() : wagony.popBack()); }
    public int size() { return wagony.size(); }
    public void reverse() { wagony.reverse(); }
    public String getNazwa() { return nazwa; }
    public void dolacz(Pociag pociag) { wagony.dolacz(pociag.wagony); }

    private final String nazwa;
    private final ListaWagonow wagony;
}

class Wagon
{
    public Wagon(String nazwaWagonu) { nazwa = nazwaWagonu; }
    public String getNazwa() { return nazwa; }

    private final String nazwa;
}

/** Jednokierunkowa lista niecykliczna. */
class ListaPociagow
{
    public ElementListyPociagow first, last;

    public ListaPociagow() { last = first = null; }
    public void push_front(Pociag pociag)
    {
        if(first != null)
        {
            ElementListyPociagow temp = first;
            first = new ElementListyPociagow(pociag);
            first.next = temp;
        }
        else
            last = first = new ElementListyPociagow(pociag);
    }
    /** FirstLast wskazuje, ktory wagon ma byc usuniety: 'f' -> pierwszy, 'l' -> ostatni. */
    public void delCarriage(String nazwaPociagu, String nazwaNowego, char FirstLast)
    {
        ElementListyPociagow temp = first, prev = null;
        while( (temp != null)  &&  !(temp.pociag.getNazwa().equals(nazwaPociagu)) )
        {
            prev = temp;
            temp = temp.next;
        }

        if(temp != null)
        {
            if(find(nazwaNowego) == null)
            {
                if(temp == first)
                {
                    first = new ElementListyPociagow( new Pociag(nazwaNowego, temp.pociag.delCarriage(FirstLast)) );
                    if(temp.pociag.size() != 0)
                        first.next = temp;
                    else
                        first.next = temp.next;
                }
                else
                {
                    ElementListyPociagow temp2 = first;
                    first = new ElementListyPociagow( new Pociag(nazwaNowego, temp.pociag.delCarriage(FirstLast)) );
                    first.next = temp2;
                    if(temp.pociag.size() == 0) {
                        assert prev != null;
                        prev.next = temp.next;
                    }
                }
            }
            else
                System.out.println("Train "+nazwaNowego+" already exists");
        }
        else
            System.out.println("Train "+nazwaPociagu+" does not exist");
    }
    public void merge(Pociag pociag, String nazwaPociagu)
    {
        ElementListyPociagow temp = first, prev = null;

        while( (temp != null)  &&  !(temp.pociag.getNazwa().equals(nazwaPociagu)) )
        {
            prev = temp;
            temp = temp.next;
        }

        if(temp != null)
        {
            pociag.dolacz(temp.pociag);
            if(temp == last)
                last = prev;
            if(temp != first) {
                assert prev != null;
                prev.next = temp.next;
            }
            else
                first = temp.next;
        }
        else
            System.out.println("Train "+nazwaPociagu+" does not exist");
    }
    public Pociag find(String nazwaPociagu)
    {
        ElementListyPociagow temp = first;
        while( (temp != null)  &&  !(temp.pociag.getNazwa().equals(nazwaPociagu)) )
            temp = temp.next;
        return (temp!=null ? temp.pociag : null);
    }
    public String toString()
    {
        StringBuilder pociagi = new StringBuilder();
        ElementListyPociagow temp = first;

        while(temp != null)
        {
            pociagi.append(' ').append(temp.pociag.getNazwa());
            temp = temp.next;
        }

        return pociagi.toString();
    }
    public void clean() { last = first = null; }

    static class ElementListyPociagow
    {
        public ElementListyPociagow(Pociag p) { pociag = p; }

        public Pociag pociag;
        public ElementListyPociagow next;
    }
}

/** Dwukierunkowa lista cykliczna. */
class ListaWagonow
{
//Lista wagonow zawsze zawiera przynajmniej jeden element, wiec w funkcjach nie trzeba sprawdzac pustosci.
    public ListaWagonow(Wagon wagon)
    {
        last = first = new ElementListyWagonow(wagon);
        first.prev = last.next = last;
        length = 1;
    }
    public void pushBack(Wagon wagon)
    {
        if(unreversed())
        {
        //Standardowe przepinanie wskaznikow.
            if(last.next == first)
            {
                last.next = new ElementListyWagonow(wagon);
                last.next.prev = last;
                last.next.next = first;
                last = first.prev = last.next;
            }
            else
            {
                last.prev = new ElementListyWagonow(wagon);
                last.prev.next = last;
                last.prev.prev = first;
                last = first.prev = last.prev;
            }
        }
        else
        {
        //Jak wyzej, ale next zamieniamy na prev i na odwrot oraz nie modyfikujemy first.
            if(last.next == first)
            {
                last.next = new ElementListyWagonow(wagon);
                last.next.prev = last;
                last.next.next = first;
                last = last.next;
            }
            else
            {
                last.prev = new ElementListyWagonow(wagon);
                last.prev.next = last;
                last.prev.prev = first;
                last = last.prev;
            }
        }
        ++length;
    }
    public void pushFront(Wagon wagon)
    {
        if(unreversed())
        {
        //Standardowe przepinanie wskaznikow.
            first.prev = new ElementListyWagonow(wagon);
            first.prev.next = first;
            first.prev.prev = last;
            if(last.next == first)
                first = last.next = first.prev;
            else
                first = last.prev = first.prev;
        }
        else
        {
        //Jak wyzej, ale next zamieniamy na prev i na odwrot.
            first.next = new ElementListyWagonow(wagon);
        //Wskazniki pierwszego maja wskazywac na to samo, bo odwrocony.
            first.next.next = first.next.prev = first;
            if(last.next == first)
                first = last.next = first.next;
            else
                first = last.prev = first.next;
        }
        ++length;
    }
    /** Zwraca nazwe pierwszego wagonu. */
    public String popFront()
    {
        String wagon = first.wagon.getNazwa();
    //Jesli jest tylko jeden wagon, to pociag i tak zostanie usuniety.
        if(first != last)
        {
            if(unreversed())
            {
            //Standardowe przepinanie wskaznikow.
                if(last.next == first)
                    last.next = first.next;
                else
                    last.prev = first.next;
                first = first.next;
                first.prev = last;
            }
            else
            {
                if(last.next == first)
                    last.next = first.prev;
                else
                    last.prev = first.prev;
                first = first.prev;
                first.next = first.prev;
            }
        }

        --length;
        return wagon;
    }
    /** Zwraca nazwe ostatniego wagonu. */
    public String popBack()
    {
        String wagon = last.wagon.getNazwa();
    //Jesli jest tylko jeden wagon, to pociag i tak zostanie usuniety.
        if(first != last)
        {
            ElementListyWagonow temp = last;
        //Czy ostatni jest odwrocony
            last = (last.next==first ? last.prev : last.next);
        //Czy przedostatni byl odwrocony wzgledem ostatniego
            if(last.next == temp)
                last.next = first;
            else
                last.prev = first;
        //Jesli pociag nie jest odwrocony, to trzeba zmienic referencje first.prev.
            if(unreversed())
                first.prev = last;
        }

        --length;
        return wagon;
    }
    public String toString()
    {
        StringBuilder wagony = new StringBuilder();
        ElementListyWagonow temp = first;
    //Jedyna roznica to kolejnosc wykonywania sie wewnetrznych petli.
        if(unreversed())
        {
            while(temp != last)
            {
            //Szukamy po referencjach next.
                while(temp.next.prev==temp && temp!=last)
                {
                    wagony.append(temp);
                    temp = temp.next;
                }
            //Jesli petla zerwala sie z powodu: temp.next.prev==temp,
            //trzeba wypisac kolejny wagon, przestawic wskaznik i dopiero zmienic zwrot poszukiwan.
                if(temp != last)
                {
                    wagony.append(temp);
                    temp = temp.next;
                }
            //Szukamy po referencjach prev.
                while(temp.prev.next == temp && temp != last)
                {
                    wagony.append(temp);
                    temp = temp.prev;
                }
                if(temp != last)
                {
                    wagony.append(temp);
                    temp = temp.prev;
                }
            }
        }
    //Wariacja powyzszej petli (kolejnosc wykonywania).
        else
        {
            while(temp != last)
            {
            //Szukamy po referencjach prev.
                while(temp.prev.next == temp && temp != last)
                {
                    wagony.append(temp);
                    temp = temp.prev;
                }
                if(temp != last)
                {
                    wagony.append(temp);
                    temp = temp.prev;
                }
            //Szukamy po referencjach next.
                while(temp.next.prev==temp && temp!=last)
                {
                    wagony.append(temp);
                    temp = temp.next;
                }
                if(temp != last)
                {
                    wagony.append(temp);
                    temp = temp.next;
                }
            }
        }

        return (wagony.toString() + ' ' + last.wagon.getNazwa());
    }
    public void dolacz(ListaWagonow wagony)
    {
    //Przepinanie wskaznikow doczepianego pociagu.
        if(wagony.unreversed())
            wagony.first.prev = last;
        else
            wagony.first.next = last;
    //Sprawdzamy, czy zwrot ostatnich jest inny.
        if(wagony.last.next == wagony.first)
            wagony.last.next = first;
        else
            wagony.last.prev = first;

    //Przepinanie wskaznikow pociagu, do ktorego doczepiamy.
    //Sprawdzamy, czy zwrot ostatnich jest inny.
        if(last.next == first)
            last.next = wagony.first;
        else
            last.prev = wagony.first;

        last = wagony.last;
    //Jesli jest odwrocony, nie trzeba zmieniac.
        if(unreversed())
            first.prev = last;

        length += wagony.length;
    }
    public void reverse()
    {
    /* Zeby odwrocic pociag wystarczy zrobic swap(first,last), a potem w funkcjach sprawdzac warunek first.prev==last.
     * Sposob ten jednak nie dziala, gdy zostana polaczone dwa pociagi, z ktorych jeden jest odwrocony.
     * Dlatego dodatkowo przepinamy wskazniki obecnie ostatniego pociagu tak, by wskazywaly na to samo. */
        if(!unreversed())
        //Przywracamy stan poczatkowy wskaznika.
            first.next = last;
    //Jesli ostatnie wagony sa odwrocone, to nie trzeba nic zmieniac, aby wypisywanie odbywalo sie po nastepnikach.
        if(last.next == first)
            last.next = last.prev;
    //Zamiana referencji na pierwszy i ostatni element.
        ElementListyWagonow temp = first;
        first = last;
        last = temp;
    }
    public boolean unreversed() { return (first.next!=first.prev || size()<3); }
    public int size() { return length; }

    private int length;
    private ElementListyWagonow first, last;

    static class ElementListyWagonow
    {
        public ElementListyWagonow(Wagon w) { wagon = w; }
        public String toString() { return ' '+wagon.getNazwa(); }

        public Wagon wagon;
        public ElementListyWagonow next, prev;
    }
}
