//Szymon Ciula - gr. 4

/* Aplikacja dziala na kolejce priorytetowej zawierajacej elementy typu Person,
 * w ktorych priorytety sie nie powtarzaja i zrealizowanej jako drzewo BST.
 *
 * Aplikacja zawiera trzy moduly: edycji, kolejkowania oraz raportowania.
 *
 * Wszystkie komendy oprocz CREATE oraz HEIGHT sa zaimplementowane w wersji iteracyjnej.
 * Zadna operacja nie moze zostac wywolana na kolejce przed wykonaniem operacji CREATE.
 * Zadna komenda nie ma zlozonosci wiekszej niz liniowa.
 * Operacje PREV x oraz NEXT x nie korzystaja z listy INORDER. */

import java.util.Scanner;

public class Source
{
    public static final Scanner sc = new Scanner(System.in);
    public static final Funkcje fun = new Funkcje();

    public static void main(String[] args)
    {
        byte zestawy = sc.nextByte();

        for(byte zestaw=1; zestaw<=zestawy; ++zestaw)
        {
            System.out.println("ZESTAW " + zestaw);
            for(byte komendy=sc.nextByte(); komendy>0; --komendy)
            {
                switch(sc.next())
                {
                    case "CREATE":
                        switch(sc.next())
                        {
                            case "PREORDER": fun.create(true, sc); break;
                            case "POSTORDER": fun.create(false, sc); break;
                            default: break;
                        }
                        break;
                    case "DELETE": fun.delete(sc.nextInt()); break;
                    case "NEXT": fun.next(sc.nextInt()); break;
                    case "PREV": fun.prev(sc.nextInt()); break;
                    case "ENQUE": fun.enque(sc); break;
                    case "DEQUEMAX": fun.dequeMax(); break;
                    case "DEQUEMIN": fun.dequeMin(); break;
                    case "PREORDER": fun.preorder(); break;
                    case "INORDER": fun.inorder(); break;
                    case "POSTORDER": fun.postorder(); break;
                    case "HEIGHT": fun.height(); break;
                    default: break;
                }
            }
        }
    }
}

/** Klasa pomocnicza sterujaca funkcjami programu. */
class Funkcje
{
    private final DrzewoBTS tree;
    /** Konstruktor. */
    public Funkcje()
    {
        tree = new DrzewoBTS();
    }
    /** Rekurencyjnie tworzy drzewo BST na podstawie listy n elementow x1 do xn typu Person,
     * podanych w porzadku ich priorytetow wyznaczonym przez drugi argument (PREORDER albo POSTORDER).
     * Przy czym, jesli drzewo juz istnieje, powinno zostac zastapione nowym.
     * @param preorder true - preorder, false - postorder */
    public void create(boolean preorder, Scanner sc)
    {
        tree.clean();
        int ilosc = sc.nextInt();
        Person [] persons = new Person[ilosc];
        int priorytet;
        String imie, nazwisko;
    // Zapelniamy tablice osob.
        for(int i=0; i<ilosc; ++i)
        {
            priorytet = sc.nextInt();
            imie = sc.next();
            nazwisko = sc.next();
            persons[i] = new Person(priorytet, imie, nazwisko);
        }

        if(preorder)
        {
            tree.root = new DrzewoBTS.Node(persons[0]);
            tree.createPreorder(persons, 1, tree.root, Integer.MAX_VALUE, null);
        }
        else
        {
            tree.root = new DrzewoBTS.Node(persons[ilosc-1]);
            tree.createPostorder(persons, ilosc-2, tree.root, Integer.MIN_VALUE, null);
        }
    }
    /** Usuwa z drzewa osobe o priorytecie x.
     * W przypadku, gdy usuwany wezel ma dwoch potomkow, zamienia go z jego nastepnikiem. */
    public void delete(int x)
    {
        if( !tree.delete(x) )
            System.out.println("DELETE " + x + ": BRAK");
    }
    /** Zwraca najblizsza osobe o priorytecie wiekszym od x. */
    public void next(int x)
    {
        DrzewoBTS.Node node = tree.next(x);
        System.out.println("NEXT " + x + ": " + ( node!=null ? node.toString() : "BRAK" ));
    }
    /** Zwraca najblizsza osobe o priorytecie mniejszym od x. */
    public void prev(int x)
    {
        DrzewoBTS.Node node = tree.prev(x);
        System.out.println("PREV " + x + ": " + ( node!=null ? node.toString() : "BRAK" ));
    }
    /** Dodaje osobe o priorytecie x do kolejki. */
    public void enque(Scanner sc)
    {
        int priority = sc.nextInt();
        String name = sc.next();
        String surname = sc.next();
        tree.add( new Person(priority, name, surname) );
    }
    /** Wypisuje oraz usuwa osobe o najwyzszym priorytecie. */
    public void dequeMax()
    {
        DrzewoBTS.Node node = tree.removeMaximum();
        System.out.println("DEQUEMAX: " + node);
    }
    /** Zwraca oraz usuwa osobe o najnizszym priorytecie. */
    public void dequeMin()
    {
        DrzewoBTS.Node node = tree.removeMinimum();
        System.out.println("DEQUEMIN: " + node);
    }
    /** Wypisuje liste osob w porzadku preorder. */
    public void preorder()
    {
        System.out.println("PREORDER:" + tree.preorder());
    }
    /** Wypisuje liste osob w porzadku inorder. */
    public void inorder()
    {
        System.out.println("INORDER:" + tree.inorder());
    }
    /** Wypisuje liste osob w porzadku postorder. */
    public void postorder()
    {
        System.out.println("POSTORDER:" + tree.postorder());
    }
    /** Rekurencyjnie zwraca wysokosc drzewa. */
    public void height()
    {
        System.out.println("HEIGHT: " + tree.height(tree.root));
    }
}

/** Struktura drzewa binarnego. */
class DrzewoBTS
{
    public Node root;

    /** Konstruktor. */
    public DrzewoBTS()
    {
        root = null;
    }
    /** Zwraca wezel o minimalnej wartosci klucza. */
    public Node removeMinimum()
    {
        Node curr = root, prev = null;

        if(root != null)
        {
        // Szukamy liscia najbardziej na lewo.
            while(curr.left != null)
            {
                prev = curr;
                curr = curr.left;
            }
        // Usuwamy element.
            if(prev != null)
                prev.left = curr.right; //(curr.left != null) ? curr.left : null;
            else
                root = root.right; //(root.left != null) ? root.left : null;
        }

        return curr;
    }
    /** Zwraca i usuwa element maksymalny. */
    public Node removeMaximum()
    {
        Node curr = root, prev = null;

        if(root != null)
        {
        // Szukamy liscia najbardziej na prawo.
            while(curr.right != null)
            {
                prev = curr;
                curr = curr.right;
            }
        // Usuwamy element.
            if(prev != null)
                prev.right = curr.left; //(curr.left != null) ? curr.left : null;
            else
                root = root.left; //(root.left != null) ? root.left : null;
        }

        return curr;
    }
    /** Rekurencyjnie zwraca wysokosc drzewa. */
    public int height(Node node)
    {
        return (node != null)  ?  Math.max(height(node.left), height(node.right)) + 1  :  -1;
    }
    /** Zwraca drzewo w postaci listy inorder. */
    public String inorder()
    {
        StringBuilder sb = new StringBuilder();

        Stos stos = new Stos(); // tworzenie stosu pustego
        Node curr = root; // bierzaca referencja

        while( curr != null || stos.notEmpty() )
        {
            if( curr != null )
            {
                stos.push(curr);
                curr = curr.left; // przejscie do lewego poddrzewa
            }
            else
            {
                curr = stos.pop(); // wierzcholek stosu
                sb.append(" ").append(curr.info.toString()).append(",");
                curr = curr.right; // przejscie do prawego poddrzewa
            }
        }

        return sb.substring(0, sb.length()-1);
    }
    /** Zwraca drzewo w postaci listy preorder. */
    public String preorder()
    {
        StringBuilder sb = new StringBuilder();

        Stos stos = new Stos(); // tworzenie stosu pustego
        Node curr = root; // biezaca referencja

        while( curr != null || stos.notEmpty() )
        {
            if( curr != null )
            {
                sb.append(" ").append(curr.info.toString()).append(",");
                stos.push(curr);
                curr = curr.left; // przejscie do lewego poddrzewa
            }
            else
                curr = stos.pop().right; // przejscie do prawego poddrzewa
        }

        return sb.substring(0, sb.length()-1);
    }
    /** Zwraca drzewo w postaci listy postorder. */
    public String postorder()
    {
        if(root != null)
        {
            StringBuilder sb = new StringBuilder();
            Stos stos = new Stos(); // stos pomocniczy
            Stos wynik = new Stos();// stos wynikowy
            Node node;

            stos.push(root);
        /* W kazdej iteracji bedziemy wrzucac do wynikowego stosu wezel sciagniety ze stosu pomocniczego (rodzica).
        * Nastepnie do stosu pomocniczego wrzucamy prawego potomka, a potem lewego potomka.
        * Zachowamy w ten sposob na stosie wynikowym kolejnosc wypisywania postorderowego. */
            while(stos.notEmpty())
            {
                wynik.push( node = stos.pop() );
                if(node.left != null)
                    stos.push(node.left);
                if(node.right != null)
                    stos.push(node.right);
            }
        // Wypisujemy kolejne elementy stosu wynikowego.
            while(wynik.notEmpty())
                sb.append(" ").append(wynik.pop()).append(",");

            return sb.substring(0, sb.length()-1);
        }

        return "";
    }
    /** Usuwa wszystkie wezly drzewa. */
    public void clean()
    {
        if(root != null)
        {
            root.clean();
            root = null;
        }
    }
    /** Wyszukuje nastepny co do wielkosci element po elemencie 'x'. */
    public Node next(int x)
    {
        Node node = root;
        Node rightAncestor = null; // Rodzic ostatniego lewego przodka.

        while(node != null)
        {
        // Sprawdzamy, czy osoba o takim priorytecie istnieje.
            if(x < node.info.priority)
            {
                rightAncestor = node;
                node = node.left;
            }
            else if(x > node.info.priority)
                node = node.right;
        // Jesli znajdziemy, to szukamy wezla najbardziej na lewo.
            else
            {
                node = node.right;

                while(node != null)
                {
                    rightAncestor = node;
                    node = node.left;
                }

                return rightAncestor;
            }
        }
    // Nie ma osoby o takim priorytecie, wiec zwracamy null.
        return null;
    }
    /** Wyszukuje poprzedni co do wielkosci element po elemencie 'x'. */
    public Node prev(int x)
    {
        Node node = root;
        Node leftAncestor = null; // Rodzic ostatniego prawego przodka.

        while(node != null)
        {
        // Sprawdzamy, czy osoba o takim priorytecie istnieje.
            if(x > node.info.priority)
            {
                leftAncestor = node;
                node = node.right;
            }
            else if(x < node.info.priority)
                node = node.left;
        // Jesli znajdziemy, to szukamy wezla najbardziej na prawo.
            else
            {
                node = node.left;

                while(node != null)
                {
                    leftAncestor = node;
                    node = node.right;
                }

                return leftAncestor;
            }
        }
    // Nie ma osoby o takim priorytecie, wiec zwracamy null.
        return null;
    }
    /** Dodaje do drzewa kolejna osobe (jako korzen). */
    public void add(Person person)
    {
        Node node = root, prev = null;
        while(node != null)
        {
            prev = node;
            node = (person.priority < node.info.priority) ? node.left : node.right;
        }

        if(prev != null)
        {
            if(person.priority < prev.info.priority)
                prev.left = new Node(person);
            else
                prev.right = new Node(person);
        }
        else
            root = new Node(person);
    }
    /** Usuwa wezel o priorytecie 'x'. */
    public boolean delete(int x)
    {
    // Wyszukujemy wezel o priorytecie x.
        Node node = root, prev = null;
        while ( (node != null)  &&  (x != node.info.priority) )
        {
            prev = node;
            node = (x < node.info.priority) ? node.left : node.right;
        }
    // Gdy szukany element istnieje i nie jest korzeniem.
        if(node != null)
        {
            if(node != root)
            {
                if(node.left != null)
                {
                    if(node.right != null)
                    {
                        Node curr = node.right;
                    // Szukamy liscia najbardziej na lewo.
                        while(curr.left != null)
                            curr = curr.left;
                    // Dopinamy element do najbardziej lewego liscia potomka x.
                        curr.left = node.left;
                    // Usuwamy element x.
                        if(prev.right == node)
                            prev.right = node.right;
                        else
                            prev.left = node.right;
                    }
                    else
                    {
                        if(prev.right == node)
                            prev.right = node.left;
                        else
                            prev.left = node.left;
                    }
                }
                else
                {
                    if(prev.right == node)
                        prev.right = node.right;
                    else
                        prev.left = node.right;
                }
            }
        // Gdy szukany element jest korzeniem.
            else
            {
                if(root.left != null)
                {
                    if(root.right != null)
                    {
                        Node curr = root.right;
                    // Szukamy liscia najbardziej na lewo.
                        while(curr.left != null)
                            curr = curr.left;
                    // Dopinamy element do najbardziej lewego liscia potomka x.
                        curr.left = root.left;
                    // Usuwamy element x.
                        root = root.right;
                    }
                    else
                        root = root.left;
                }
                else
                    root = root.right;
            }

            return true;
        }
        else
            return false;
    }
    /** Tworzy rekurencyjnie drzewo na podstawie listy preoderowanej.
     * @return indeks w tablicy kolejnego elementu do wstawienia (jej rozmiar, jesli koniec).
     * @param persons tablica osob wpisywanych do drzewa.
     * @param indeks indeks w tablicy wstawianego elementu.
     * @param node wezel, na ktorym aktualnie pracujemy (juz utworzony).
     * @param top priorytet rodzica.
     * @param rightAncestor pierwszy przodek ustawiony na prawo wzgledem dziecka, ktorego potomkowe ida na prawo. */
    public int createPreorder(Person [] persons, int indeks, Node node, int top, Node rightAncestor)
    {
        if(indeks < persons.length)
        {
            if(node.info.priority >= persons[indeks].priority)
            {
                node.left = new Node(persons[indeks]);
                indeks = createPreorder(persons, indeks+1, node.left, node.info.priority, node);
                if(indeks < persons.length)
                {
                    if( (rightAncestor != null  &&  top == rightAncestor.info.priority  &&  top >= persons[indeks].priority)
                            || (rightAncestor == null  ||  rightAncestor.info.priority >= persons[indeks].priority) )
                    //if(top >= persons[indeks].priority  ||  rightAncestor==null  ||  top != rightAncestor.info.priority)
                    {
                        node.right = new Node(persons[indeks]);
                        return createPreorder(persons, indeks+1, node.right, node.info.priority, rightAncestor);
                    }
                    else
                        return indeks;
                }
                else
                    return persons.length;
            }
            else
            {
                if(rightAncestor == null  ||  rightAncestor.info.priority >= persons[indeks].priority)
                {
                    node.right = new Node(persons[indeks]);
                    return createPreorder(persons, indeks+1, node.right, node.info.priority, rightAncestor);
                }
                else
                    return indeks;
            }
        }
        return persons.length;
    }
    /** Tworzy rekurencyjnie drzewo na podstawie listy postoderowanej.
     * @return indeks w tablicy kolejnego elementu do wstawienia (jej rozmiar, jesli koniec).
     * @param persons tablica osob wpisywanych do drzewa.
     * @param indeks indeks w tablicy wstawianego elementu.
     * @param node wezel, na ktorym aktualnie pracujemy (juz utworzony).
     * @param top priorytet rodzica.
     * @param leftAncestor pierwszy przodek ustawiony na prawo wzgledem dziecka, ktorego potomkowe ida na lewo.  */
    public int createPostorder(Person [] persons, int indeks, Node node, int top, Node leftAncestor)
    {
        if(indeks >= 0)
        {
            if(node.info.priority <= persons[indeks].priority)
            {
                node.right = new Node(persons[indeks]);
                indeks = createPostorder(persons, indeks-1, node.right, node.info.priority, node);
                if(indeks >= 0)
                {
                    if( (leftAncestor != null  &&  top == leftAncestor.info.priority  &&  top <= persons[indeks].priority)
                        || (leftAncestor == null  ||  leftAncestor.info.priority <= persons[indeks].priority) )
                    {
                        node.left = new Node(persons[indeks]);
                        return createPostorder(persons, indeks-1, node.left, node.info.priority, leftAncestor);
                    }
                    else
                        return indeks;
                }
                else
                    return -1;
            }
            else
            {
                if(leftAncestor==null  ||  leftAncestor.info.priority <= persons[indeks].priority)
                {
                    node.left = new Node(persons[indeks]);
                    return createPostorder(persons, indeks-1, node.left, node.info.priority, leftAncestor);
                }
                else
                    return indeks;
            }
        }
        else
            return -1;
    }

    /** Klasa implementujaca wezel drzewa. */
    static class Node
    {
        public Person info; // element danych (klucz)
        public Node left;   // lewy potomek wezla
        public Node right;  // prawy lewy potomek wezla

        public Node(Person info)
        {
            this.info = info;
            left = right = null;
        }
        @Override
        public String toString()
        {
            return info.toString();
        }
        public void clean()
        {
            if(left != null)
            {
                left.clean();
                left = null;
            }
            if(right != null)
            {
                right.clean();
                right = null;
            }
        }
    }

}
/** Klasa reprezentujaca pojedyncza osobe. */
class Person
{
    public int priority; // priorytet osoby
    public String name;
    public String surname;

    public Person(int priority, String name, String surname)
    {
        this.priority = priority;
        this.name = name;
        this.surname = surname;
    }
    @Override
    public String toString()
    {
        return priority + " - " + name + " " + surname;
    }
}

/** Klasa implementujaca strukture stosu wiazanego. */
class Stos
{
    public Node top;

    /** Konstruktor. */
    public Stos()
    {
        top = null;
    }
    public DrzewoBTS.Node pop()
    {
        if(top != null)
        {
            DrzewoBTS.Node temp = top.value;
            top = top.next;
            return temp;
        }
        return null;
    }
    public void push(DrzewoBTS.Node value)
    {
        if(top != null)
        {
            Node temp = top;
            top = new Node(value);
            top.next = temp;
        }
        else
            top = new Node(value);
    }
    public boolean notEmpty()
    {
        return top != null;
    }

    static class Node
    {
        private final DrzewoBTS.Node value;
        private Node next;

        private Node(DrzewoBTS.Node value)
        {
            this.value = value;
            next = null;
        }
    }
}
